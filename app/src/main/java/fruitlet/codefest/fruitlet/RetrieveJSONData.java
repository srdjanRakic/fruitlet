package fruitlet.codefest.fruitlet;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class RetrieveJSONData extends IntentService {

    public RetrieveJSONData() {
        super("RetrieveJSONData");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String target = intent.getStringExtra("url").toString().trim();
        String result = downloadJSON(target);
        MainActivity.InitializeFruitManager(result);
    }

    protected String downloadJSON(String target){
        String result = "";
        InputStream inputStream;
        try{
            URL url = new URL(target);
            inputStream = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            reader.close();
            inputStream.close();
            result = sb.toString();
        } catch(IOException e){
            Log.e("Cannot read data", e.getMessage());
        }
        return result;
    }
}
