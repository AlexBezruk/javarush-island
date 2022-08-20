package ua.com.javarush.alexbezruk.island.wildlife.plant;

import ua.com.javarush.alexbezruk.island.wildlife.WildLife;

public class Plant extends WildLife {
    protected static double weight = 1;
    protected static int maxPopulation = 200;

    public double getWeight() {
        return weight;
    }

    public static int getMaxPopulation() {
        return maxPopulation;
    }
}
