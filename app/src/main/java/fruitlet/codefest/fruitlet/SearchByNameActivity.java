package fruitlet.codefest.fruitlet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SearchByNameActivity extends Activity {
    FruitManager fruitManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_name);

        fruitManager = (FruitManager) getIntent().getSerializableExtra("FruitManager");
    }

    public void showFruitResultActivityLaunch(View v){
        Intent intent = new Intent(this, SearchByNameActivity.class);


        String param = ((EditText) findViewById(R.id.txtSearchParameters)).getText().toString().trim();
        Fruit fruit = fruitManager.getFruitByName(param);
        if (fruit != null) Toast.makeText(this, fruit.getName(), Toast.LENGTH_LONG).show();
        else Toast.makeText(this, "Fruit is null.", Toast.LENGTH_LONG).show();
        //TODO: Launch Fruit Result Activity
    }
}
