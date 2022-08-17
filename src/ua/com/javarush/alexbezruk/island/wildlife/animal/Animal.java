package ua.com.javarush.alexbezruk.island.wildlife.animal;

import ua.com.javarush.alexbezruk.island.interfaces.Movable;
import ua.com.javarush.alexbezruk.island.interfaces.Multipliable;
import ua.com.javarush.alexbezruk.island.logic.DirectionsOfMovement;
import ua.com.javarush.alexbezruk.island.logic.NumberGenerator;
import ua.com.javarush.alexbezruk.island.terrain.Island;
import ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores.*;
import ua.com.javarush.alexbezruk.island.wildlife.animal.predator.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Animal implements Cloneable, Movable, Multipliable {
    protected int x;
    protected int y;

    protected double weight;
    protected int speed;
    protected double saturation;
    protected double maxSaturation;
    protected int maxPopulation;

    public Animal(int x, int y, double weight, int speed, double saturation, double maxSaturation, int maxPopulation) {
        this.x = x;
        this.y = y;
        this.weight = weight;
        this.speed = speed;
        this.saturation = saturation;
        this.maxSaturation = maxSaturation;
        this.maxPopulation = maxPopulation;
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

    @Override
    public String toString() {
        return  getClass().getSimpleName() + "{" +
                "saturation=" + saturation +
                '}';
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getWeight() {
        return weight;
    }

    public int getMaxPopulation() {
        return maxPopulation;
    }

    public int getSpeed() {
        return speed;
    }

    public double getMaxSaturation() {
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

    @Override
    public void move() {
        for (int i = 0; i < getSpeed(); i++) {
            List<DirectionsOfMovement> possibleDirectionsOfMovement = calculationOfPossibleDirectionsOfMovement();
            int randomNumber = NumberGenerator.randomNumber(possibleDirectionsOfMovement.size() - 1);
            switch (possibleDirectionsOfMovement.get(randomNumber)) {
                case LEFT -> moveLeft();
                case UP -> moveUp();
                case RIGHT -> moveRight();
                case DOWN -> moveDown();
            }
        }
    }

    @Override
    public Animal multiply() {
        Animal animal = null;
        try {
            animal = (Animal) this.clone();
            animal.saturation = animal.maxSaturation;
        } catch (CloneNotSupportedException e) {
            System.err.println("Ошибка при размножении (клонировании) животного" + e.getMessage());
        }
        return animal;
    }

    private List<DirectionsOfMovement> calculationOfPossibleDirectionsOfMovement() {
        List<DirectionsOfMovement> possibleDirectionsOfMovement = new ArrayList<>();
        if (this.x != 0) {
            possibleDirectionsOfMovement.add(DirectionsOfMovement.LEFT);
        }
        if (this.y != 0) {
            possibleDirectionsOfMovement.add(DirectionsOfMovement.UP);
        }
        if (this.x != Island.getWidth() - 1) {
            possibleDirectionsOfMovement.add(DirectionsOfMovement.RIGHT);
        }
        if (this.y != Island.getLength() - 1) {
            possibleDirectionsOfMovement.add(DirectionsOfMovement.DOWN);
        }
        return possibleDirectionsOfMovement;
    }

    private void moveLeft() {
        this.x--;
    }

    private void moveUp() {
        this.y--;
    }

    private void moveRight() {
        this.x++;
    }

    private void moveDown() {
        this.y++;
    }
}
