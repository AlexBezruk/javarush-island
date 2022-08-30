package ua.com.javarush.alexbezruk.island.terrain;

import ua.com.javarush.alexbezruk.island.wildlife.animal.Animal;
import ua.com.javarush.alexbezruk.island.wildlife.plant.Plant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Location {
    private List<Animal> animals;
    private List<Plant> plants;

    public Location() {
        animals = new CopyOnWriteArrayList<>();
        plants = new CopyOnWriteArrayList<>();
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public List<Plant> getPlants() {
        return plants;
    }

    public int numberOfAnimalsOfCertainType (Class<? extends Animal> clazz) {
        int count = 0;

        for (Animal animal : getAnimals()) {
            if (animal.getClass().equals(clazz)) {
                count++;
            }
        }

        return count;
    }
}
