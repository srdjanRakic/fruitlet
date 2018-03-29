package fruitlet.codefest.fruitlet;

import java.io.Serializable;
import java.util.ArrayList;

public class Fruit implements Serializable {
    String name;
    Nutrition nutrition;
    ArrayList<Smoothie> smoothies;
    ArrayList<Salad> salads;

    public Fruit(String name, Nutrition nutrition, ArrayList<Smoothie> smoothies, ArrayList<Salad> salads) {
        this.name = name;
        this.nutrition = nutrition;
        this.smoothies = smoothies;
        this.salads = salads;
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

    public ArrayList<Smoothie> getSmoothies() {
        return smoothies;
    }

    public void setSmoothies(ArrayList<Smoothie> smoothies) {
        this.smoothies = smoothies;
    }

    public ArrayList<Salad> getSalads() {
        return salads;
    }

    public void setSalads(ArrayList<Salad> salads) {
        this.salads = salads;
    }

    @Override
    public String toString() {
        return name + ", " + nutrition;
    }
}
