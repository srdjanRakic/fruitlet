package fruitlet.codefest.fruitlet;

import java.io.Serializable;

public class Nutrition implements Serializable {
    int kcal;
    String fullDescription;
    String inAmount;

    public Nutrition(int kcal, String fullDescription) {
        this.kcal = kcal;
        this.fullDescription = fullDescription;
        this.inAmount = "N/A";
    }

    public Nutrition(int kcal, String fullDescription, String inAmount) {
        this.kcal = kcal;
        this.fullDescription = fullDescription;
        this.inAmount = inAmount;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public String getInAmount() {
        return inAmount;
    }

    public void setInAmount(String inAmount) {
        this.inAmount = inAmount;
    }

    @Override
    public String toString() {
        return fullDescription;
    }
}
