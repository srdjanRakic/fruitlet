package fruitlet.codefest.fruitlet;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class FruitManager implements Serializable {
    ArrayList<Fruit> fruits;

    public FruitManager() {
        fruits = new ArrayList<Fruit>();
    }

    public Fruit getFruitByName(String name){
        for(Fruit fruit : fruits){
            if (fruit.name.toLowerCase().trim().equals(name.toLowerCase().trim())){
                return fruit;
            }
        }
        return null;
    }

    public void initializeFruits(String data){
        try{
            JSONObject jsonObject = new JSONObject(data);

            int n = jsonObject.getJSONObject("fruit").getInt("count");
            for(int i = 0; i < n; i++){
                JSONObject current = jsonObject.getJSONObject("fruit")
                        .getJSONArray("fruitList")
                        .getJSONObject(i);
                Nutrition nutrition = new Nutrition(
                        current.getJSONObject("nutrition").getInt("kcal"),
                        current.getJSONObject("nutrition").getString("fullDesc"),
                        current.getJSONObject("nutrition").getString("inAmount")
                );
                ArrayList<Smoothie> smoothies = new ArrayList<Smoothie>();
                for(int j = 0; j < current.getJSONObject("meals").getJSONArray("smoothies").length(); j++){
                    JSONObject smoothie = current.getJSONObject("meals").getJSONArray("smoothies").getJSONObject(j);
                    Nutrition smoothieNutrition = new Nutrition(
                            smoothie.getJSONObject("nutrition").getInt("kcal"),
                            smoothie.getJSONObject("nutrition").getString("fullDesc")
                    );
                    ArrayList<String> otherFruits = new ArrayList<String>();
                    for(int k = 0; k < smoothie.getJSONArray("otherFruits").length(); k++){
                        otherFruits.add(smoothie.getJSONArray("otherFruits").getString(k));
                    }
                    ArrayList<String> ingredients = new ArrayList<String>();
                    for(int k = 0; k < smoothie.getJSONArray("ingredients").length(); k++){
                        otherFruits.add(smoothie.getJSONArray("ingredients").getString(k));
                    }
                    smoothies.add(
                            new Smoothie(
                                    smoothie.getString("name"),
                                    smoothieNutrition,
                                    smoothie.getString("prepTime"),
                                    smoothie.getString("img-url"),
                                    smoothie.getString("recipe"),
                                    ingredients,
                                    otherFruits
                            )
                    );
                }
                ArrayList<Salad> salads = new ArrayList<Salad>();
                for(int j = 0; j < current.getJSONObject("meals").getJSONArray("salads").length(); j++){
                    JSONObject salad = current.getJSONObject("meals").getJSONArray("salads").getJSONObject(j);
                    Nutrition saladNutrition = new Nutrition(
                            salad.getJSONObject("nutrition").getInt("kcal"),
                            salad.getJSONObject("nutrition").getString("fullDesc")
                    );
                    ArrayList<String> otherFruits = new ArrayList<String>();
                    for(int k = 0; k < salad.getJSONArray("otherFruits").length(); k++){
                        otherFruits.add(salad.getJSONArray("otherFruits").getString(k));
                    }
                    ArrayList<String> ingredients = new ArrayList<String>();
                    for(int k = 0; k < salad.getJSONArray("ingredients").length(); k++){
                        otherFruits.add(salad.getJSONArray("ingredients").getString(k));
                    }
                    salads.add(
                            new Salad(
                                    salad.getString("name"),
                                    saladNutrition,
                                    salad.getString("prepTime"),
                                    salad.getString("img-url"),
                                    salad.getString("recipe"),
                                    ingredients,
                                    otherFruits
                            )
                    );
                }
                Fruit fruitToAdd = new Fruit(
                        current.getString("name"),
                        nutrition,
                        smoothies,
                        salads
                );
                fruits.add(fruitToAdd);
            }
        }catch (JSONException e){
            Log.e("Error in JSON.", e.getMessage());
        }
    }
}
