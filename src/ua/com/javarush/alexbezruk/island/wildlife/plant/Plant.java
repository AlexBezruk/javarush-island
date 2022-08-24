package ua.com.javarush.alexbezruk.island.wildlife.plant;

import ua.com.javarush.alexbezruk.island.logic.IslandSimulation;
import ua.com.javarush.alexbezruk.island.wildlife.WildLife;

import java.util.Properties;

public class Plant extends WildLife {
    private double weight;
    private static int maxPopulation;

    public Plant() {
        Properties properties = IslandSimulation.getInitialData();
        try {
            weight = Double.parseDouble(properties.getProperty("Plant.weight"));
            maxPopulation = Integer.parseInt(properties.getProperty("Plant.maxPopulation"));
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
