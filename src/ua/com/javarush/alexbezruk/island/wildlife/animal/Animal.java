package ua.com.javarush.alexbezruk.island.wildlife.animal;

import ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores.*;
import ua.com.javarush.alexbezruk.island.wildlife.animal.predator.*;

import java.util.HashMap;
import java.util.Map;

public abstract class Animal {
    private static int maxPopulation;
    private static double maxSaturation;

    private static double weight;
    private static int speed;
    private double saturation;

    public Animal(double saturation) {
        this.saturation = saturation;
    }

    @Override
    public String toString() {
        return  getClass().getSimpleName() + "{" +
                "saturation=" + saturation +
                '}';
    }

    private static Map<Integer, Class<?>> mapOfAnimals = new HashMap<>() {{
        put(0, Wolf.class);
        put(1, Boa.class);
        put(2, Fox.class);
        put(3, Bear.class);
        put(4, Eagle.class);
        put(5, Horse.class);
        put(6, Deer.class);
        put(7, Rabbit.class);
        put(8, Mouse.class);
        put(9, Goat.class);
        put(10, Sheep.class);
        put(11, Hog.class);
        put(12, Buffalo.class);
        put(13, Duck.class);
        put(14, Caterpillar.class);
    }};

    private static int[][] probabilityOfBeingEaten = {
            {-1, 0, 0, 0, 0, 10, 15, 60, 80, 60, 70, 15, 10, 40, 0, 0},
            {0, -1, 15, 0, 0, 0, 0, 20, 40, 0, 0, 0, 0, 10, 0, 0},
            {0, 0, -1, 0, 0, 0, 0, 70, 90, 0, 0, 0, 0, 60, 40, 0},
            {0, 80, 0, -1, 0, 40, 80, 80, 90, 70, 70, 50, 20, 10, 0, 0},
            {0, 0, 10, 0, -1, 0, 0, 90, 90, 0, 0, 0, 0, 80, 0, 0},
            {0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100},
            {0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 100},
            {0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 100},
            {0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 90, 100},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 100},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 100},
            {0, 0, 0, 0, 0, 0, 0, 0, 50, 0, 0, -1, 0, 0, 90, 100},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 100},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 90, 100},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 100}
    };

    public double getWeight() {
        return weight;
    }

    public static int getMaxPopulation() {
        return maxPopulation;
    }

    public int getSpeed() {
        return speed;
    }

    public static double getMaxSaturation() {
        return maxSaturation;
    }

    public double getSaturation() {
        return saturation;
    }

    public double reducingSaturation() {
        return saturation - maxSaturation / 4;
    }

    public static Map<Integer, Class<?>> getMapOfAnimals() {
        return mapOfAnimals;
    }

    public static int[][] getProbabilityOfBeingEaten() {
        return probabilityOfBeingEaten;
    }
}
