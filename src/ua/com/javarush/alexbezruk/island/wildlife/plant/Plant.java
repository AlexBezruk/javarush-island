package ua.com.javarush.alexbezruk.island.wildlife.plant;

public class Plant {
    protected static double weight = 1;
    protected static int maxPopulation = 200;

    public double getWeight() {
        return weight;
    }

    public static int getMaxPopulation() {
        return maxPopulation;
    }

    @Override
    public String toString() {
        return "Plant{}";
    }


}
