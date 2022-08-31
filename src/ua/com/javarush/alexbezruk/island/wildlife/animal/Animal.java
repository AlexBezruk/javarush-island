package ua.com.javarush.alexbezruk.island.wildlife.animal;

import ua.com.javarush.alexbezruk.island.interfaces.Eatable;
import ua.com.javarush.alexbezruk.island.interfaces.Movable;
import ua.com.javarush.alexbezruk.island.interfaces.Multipliable;
import ua.com.javarush.alexbezruk.island.logic.DirectionsOfMovement;
import ua.com.javarush.alexbezruk.island.logic.Simulation;
import ua.com.javarush.alexbezruk.island.logic.NumberGenerator;
import ua.com.javarush.alexbezruk.island.statistics.Statistics;
import ua.com.javarush.alexbezruk.island.terrain.Island;
import ua.com.javarush.alexbezruk.island.wildlife.WildLife;
import ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores.*;
import ua.com.javarush.alexbezruk.island.wildlife.animal.predator.*;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Animal extends WildLife implements Cloneable, Movable, Multipliable<Animal>, Eatable {
    private static final int PERCENTAGE_DECREASE_IN_SATURATION_PER_DAY = 25;
    public Lock lock = new ReentrantLock();

    protected int x;
    protected int y;

    protected double weight;
    protected int speed;
    protected double saturation;
    protected double maxSaturation;
    protected int maxPopulation;

    private boolean isAlive;
    private boolean isMoved;
    private boolean isMultiplied;

    protected Animal(int x, int y) {
        this.x = x;
        this.y = y;
        Properties properties = Simulation.getInitialData();
        String simpleName = getClass().getSimpleName();
        try {
            weight = Double.parseDouble(properties.getProperty(simpleName + ".weight"));
            speed = Integer.parseInt(properties.getProperty(simpleName + ".speed"));
            maxSaturation = Double.parseDouble(properties.getProperty(simpleName + ".maxSaturation"));
            maxPopulation = Integer.parseInt(properties.getProperty(simpleName + ".maxPopulation"));
        } catch (NumberFormatException e) {
            System.err.println("Неверный формат данных в файле properties " + e.getMessage());
        }
        saturation = maxSaturation * (100 - PERCENTAGE_DECREASE_IN_SATURATION_PER_DAY) / 100;
        isAlive = true;
        isMoved = false;
        isMultiplied = false;
    }

    private static final List<Class<? extends Animal>> listOfAnimals = new ArrayList<>(Arrays.asList(Wolf.class, Boa.class,
            Fox.class, Bear.class, Eagle.class, Horse.class, Deer.class, Rabbit.class, Mouse.class, Goat.class,
            Sheep.class, Hog.class, Buffalo.class, Duck.class, Caterpillar.class));

    private static final int[][] probabilityOfBeingEaten = {
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
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 100},
    };

    public void setX(int x) {
        this.x = x;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean isMoved() {
        return isMoved;
    }

    public void setMoved(boolean moved) {
        isMoved = moved;
    }

    public boolean isMultiplied() {
        return isMultiplied;
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

    public void setSaturation(double saturation) {
        this.saturation = saturation;
    }

    public void reducingSaturation() {
        saturation -= maxSaturation * PERCENTAGE_DECREASE_IN_SATURATION_PER_DAY / 100;
    }

    public static List<Class<? extends Animal>> getListOfAnimals() {
        return listOfAnimals;
    }

    public static int[][] getProbabilityOfBeingEaten() {
        return probabilityOfBeingEaten;
    }

    @Override
    public void move() {
        for (int i = 0; i < getSpeed(); i++) {
            List<DirectionsOfMovement> possibleDirectionsOfMovement = calculationOfPossibleDirectionsOfMovement();
            if (possibleDirectionsOfMovement.isEmpty()) {
                return;
            }
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
            animal = (Animal) clone();
            Statistics.incrementNumberOfAnimalsBorn();
            animal.saturation = animal.maxSaturation;
            isMultiplied = true;
            animal.isMoved = true;
        } catch (CloneNotSupportedException e) {
            System.err.println("Ошибка при размножении (клонировании) животного" + e.getMessage());
        }
        return animal;
    }

    @Override
    public void eat(Object o) {
        Animal animal = (Animal) o;
        animal.isAlive = false;
    }

    private List<DirectionsOfMovement> calculationOfPossibleDirectionsOfMovement() {
        List<DirectionsOfMovement> possibleDirectionsOfMovement = new ArrayList<>();
        if (x != 0) {
            possibleDirectionsOfMovement.add(DirectionsOfMovement.LEFT);
        }
        if (y != 0) {
            possibleDirectionsOfMovement.add(DirectionsOfMovement.UP);
        }
        if (x != Island.getLength() - 1) {
            possibleDirectionsOfMovement.add(DirectionsOfMovement.RIGHT);
        }
        if (y != Island.getWidth() - 1) {
            possibleDirectionsOfMovement.add(DirectionsOfMovement.DOWN);
        }
        return possibleDirectionsOfMovement;
    }

    private void moveLeft() {
        x--;
    }

    private void moveUp() {
        y--;
    }

    private void moveRight() {
        x++;
    }

    private void moveDown() {
        y++;
    }
}
