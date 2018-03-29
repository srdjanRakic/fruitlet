package fruitlet.codefest.fruitlet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FruitResultActivity extends AppCompatActivity {
    static final String CLOUD_VISION_API_KEY = "AIzaSyDbQ5neiTLka_Pl5c3upn6w6G7WKhIPlEg";
    static final String TAG = MainActivity.class.getSimpleName();
    String switcher;
    static LoadingDialog dialog;
    static Context context;
    FruitManager fruitManager;
    private TextView mImageDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit_result);

        dialog = new LoadingDialog(this);
        dialog.getWindow().getCurrentFocus();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_screen);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setOwnerActivity(this);
        dialog.show();

        Intent intent = getIntent();

        switcher = intent.getStringExtra("switcher");
        fruitManager = (FruitManager) intent.getSerializableExtra("FruitManager");
        context = this;

        mImageDetails = (TextView) findViewById(R.id.lblResult);

        if(switcher.equals("image")){
            Uri uri = Uri.parse(intent.getStringExtra("Uri"));

            uploadImage(uri);
        } else {
            String input = intent.getStringExtra("input");
            findFruitByInputString(input);
        }
    }

    public void uploadImage(Uri uri){
        if(uri != null){
            try{
                Bitmap bitmap = scaleBitmapDown(MediaStore.Images.Media.getBitmap(getContentResolver(), uri), 1200);
                Log.e("Test", "Before Vision API");
                callCloudVision(bitmap);
            }catch(FileNotFoundException e){
                Log.e("Bitmap error", "Image not found.");
            }catch(IOException e){
                Log.e("Bitmap error", e.getMessage());
            }
        } else {
            Log.e("Upload Image", "URI is null");
        }
    }

    private void callCloudVision(final Bitmap bitmap) throws IOException {
        // Switch text to loading

        // Do the real work in an async task, because we need to use the network anyway
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(new
                            VisionRequestInitializer(CLOUD_VISION_API_KEY));
                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                            new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                        AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

                        // Add the image
                        Image base64EncodedImage = new Image();
                        // Convert the bitmap to a JPEG
                        // Just in case it's a format that Android understands but Cloud Vision
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();

                        // Base64 encode the JPEG
                        base64EncodedImage.encodeContent(imageBytes);
                        annotateImageRequest.setImage(base64EncodedImage);

                        // add the features we want
                        annotateImageRequest.setFeatures(new ArrayList<Feature>() {
                            {
                                Feature labelDetection = new Feature();
                                labelDetection.setType("LABEL_DETECTION");
                                labelDetection.setMaxResults(10);
                                add(labelDetection);
                            }
                        });

                        // Add the list of one thing to the request
                        add(annotateImageRequest);
                    }});

                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchAnnotateImagesRequest);
                    // Due to a bug: requests to Vision API containing large images fail when GZipped.
                    annotateRequest.setDisableGZipContent(true);
                    Log.e(TAG, "created Cloud Vision request object, sending request");

                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return convertResponseToString(response);

                } catch (GoogleJsonResponseException e) {
                    Log.e(TAG, "failed to make API request because " + e.getContent());
                } catch (IOException e) {
                    Log.e(TAG, "failed to make API request because of other IOException " +
                            e.getMessage());
                }
                return "Cloud Vision API request failed. Check logs for details.";
            }

            protected void onPostExecute(String result) {
                String displayText = "";
                String[] results = result.split(", ");
                for(int i = 0; i < results.length; i++){
                    Log.e("found: ", results[i]);
                    Fruit current = fruitManager.getFruitByName(results[i].trim());
                    if (current != null){
                        displayText = current.toString();
                        break;
                    }
                }
                dialog.dismiss();
                mImageDetails.setText(displayText);
            }
        }.execute();
    }

    private String convertResponseToString(BatchAnnotateImagesResponse response) {
        String message = "";

        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        if (labels != null) {
            for (EntityAnnotation label : labels) {
                message += String.format("%s, ", label.getDescription());
            }
        } else {
            message += "nothing";
        }

        return message.substring(0, message.length() - 2);
    }

    public Bitmap scaleBitmapDown(Bitmap bitmap, int maxSize){
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();

        int maxWidth = maxSize;
        int maxHeight = maxSize;

        if (originalHeight > originalWidth){
            maxHeight = maxSize;
            maxWidth = (int) (maxSize * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            maxWidth = maxSize;
            maxHeight = (int) (maxSize * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            maxHeight = maxSize;
            maxWidth = maxSize;
        }

        return Bitmap.createScaledBitmap(bitmap, maxWidth, maxHeight, false);

    }

    private void findFruitByInputString(String input){
        String displayText = "nothing";
        Fruit current = fruitManager.getFruitByName(input);
        if (current != null){
            Log.e("Results: ", current.getName());
            mImageDetails.setText(current.toString());
        }
        dialog.dismiss();

    }
}
