package fruitlet.codefest.fruitlet;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.provider.MediaStore.Images.Media.getBitmap;

import java.io.File;


public class MainActivity extends Activity {
    static final String FILE_NAME = "fruitlet.jpg";
    static final int GALLERY_IMAGE_REQUEST = 1;
    static final int CAMERA_PERMISSION_REQUEST = 2;
    static final int CAMERA_IMAGE_REQUEST = 3;


    final String JSON_DATA_URL = "http://pastebin.com/raw/04C0sGAg";
    static FruitManager fruitManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fruitManager = new FruitManager();

        //Reading JSON
        Intent intent = new Intent(this, RetrieveJSONData.class);
        intent.putExtra("url", JSON_DATA_URL);
        startService(intent);

    }

    public void searchByNameActivityLaunch(View v){
        Intent intent = new Intent(this, SearchByNameActivity.class);
        intent.putExtra("FruitManager", fruitManager);
        startActivity(intent);
    }

    public void showFruitResultActivityLaunch(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder
                .setMessage(R.string.dialog_select_prompt)
                .setPositiveButton(R.string.dialog_select_gallery, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startGalleryChooser();
                    }
                })
                .setNegativeButton(R.string.dialog_select_camera, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startCamera();
                    }
                });
        builder.create().show();
    }

    public void startCamera() {
        if (PermissionUtils.requestPermission(this, CAMERA_PERMISSION_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getCameraFile()));
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }

    public File getCameraFile() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    public void startGalleryChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Photo"), GALLERY_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null){
            Intent intent = new Intent(this, FruitResultActivity.class);
            intent.putExtra("Uri", data.getData().toString());
            intent.putExtra("switcher", "image");
            intent.putExtra("FruitManager", fruitManager);
            startActivity(intent);
        } else if(requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            //Start new activity here
            Intent intent = new Intent(this, FruitResultActivity.class);
            intent.putExtra("Uri", Uri.fromFile(getCameraFile()).toString());
            intent.putExtra("switcher", "image");
            intent.putExtra("FruitManager", fruitManager);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSION_REQUEST, grantResults)){
            startCamera();
        }
    }

    public static void InitializeFruitManager(String data){
        fruitManager.initializeFruits(data);
    }
}
