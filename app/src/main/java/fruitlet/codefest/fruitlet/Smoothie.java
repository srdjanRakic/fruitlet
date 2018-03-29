package fruitlet.codefest.fruitlet;

import java.io.Serializable;
import java.util.ArrayList;

public class Smoothie extends Meal implements Serializable {
    public Smoothie(String name, Nutrition nutrition, String prepTime, String imgUrl, String recipe, ArrayList<String> ingredients, ArrayList<String> otherFruits) {
        super(name, nutrition, prepTime, imgUrl, recipe, ingredients, otherFruits);
    }
}
