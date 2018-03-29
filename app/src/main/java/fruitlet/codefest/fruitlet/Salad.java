package fruitlet.codefest.fruitlet;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Salad extends Meal implements Serializable {
    public Salad(String name, Nutrition nutrition, String prepTime, String imgUrl, String recipe, ArrayList<String> ingredients, ArrayList<String> otherFruits) {
        super(name, nutrition, prepTime, imgUrl, recipe, ingredients, otherFruits);
    }
}
