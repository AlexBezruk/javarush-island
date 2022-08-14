package ua.com.javarush.alexbezruk.island.wildlife.plant;

public class Plant {
    private final double weight = 1;
    private final static int maxPopulation = 200;

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
