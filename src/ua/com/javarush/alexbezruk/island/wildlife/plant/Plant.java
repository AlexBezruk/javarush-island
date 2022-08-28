package ua.com.javarush.alexbezruk.island.wildlife.plant;

import ua.com.javarush.alexbezruk.island.logic.Simulation;
import ua.com.javarush.alexbezruk.island.wildlife.WildLife;

import java.util.Properties;

public class Plant extends WildLife {
    private double weight;
    private static int maxPopulation;

    static {
        Properties properties = Simulation.getInitialData();
        maxPopulation = Integer.parseInt(properties.getProperty("Plant.maxPopulation"));
    }

    public Plant() {
        Properties properties = Simulation.getInitialData();
        try {
            weight = Double.parseDouble(properties.getProperty("Plant.weight"));

        } catch (NumberFormatException e) {
            System.err.println("Неверный формат данных в файле properties " + e.getMessage());
        }
    }

    public double getWeight() {
        return weight;
    }

    public static int getMaxPopulation() {
        return maxPopulation;
    }
}
