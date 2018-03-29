package fruitlet.codefest.fruitlet;

import java.io.Serializable;
import java.util.ArrayList;

public class Meal implements Serializable {
    String name;
    Nutrition nutrition;
    String prepTime;
    String imgUrl;
    String recipe;
    ArrayList<String> ingredients;
    ArrayList<String> otherFruits;

    public Meal(String name, Nutrition nutrition, String prepTime, String imgUrl, String recipe, ArrayList<String> ingredients, ArrayList<String> otherFruits) {
        this.name = name;
        this.nutrition = nutrition;
        this.prepTime = prepTime;
        this.imgUrl = imgUrl;
        this.recipe = recipe;
        this.ingredients = ingredients;
        this.otherFruits = otherFruits;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Nutrition getNutrition() {
        return nutrition;
    }

    public void setNutrition(Nutrition nutrition) {
        this.nutrition = nutrition;
    }

    public String getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(String prepTime) {
        this.prepTime = prepTime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getOtherFruits() {
        return otherFruits;
    }

    public void setOtherFruits(ArrayList<String> otherFruits) {
        this.otherFruits = otherFruits;
    }
}
