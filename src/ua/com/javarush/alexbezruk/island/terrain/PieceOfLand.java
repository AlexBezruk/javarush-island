package ua.com.javarush.alexbezruk.island.terrain;

import ua.com.javarush.alexbezruk.island.wildlife.animal.Animal;
import ua.com.javarush.alexbezruk.island.wildlife.plant.Plant;

import java.util.ArrayList;
import java.util.List;

public class PieceOfLand {
    List<Animal> animals;
    List<Plant> plants;

    public PieceOfLand() {
        animals = new ArrayList<>();
        plants = new ArrayList<>();
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public List<Plant> getPlants() {
        return plants;
    }

    @Override
    public String toString() {
        return "PieceOfLand{" +
                "animals=" + animals +
                ", plants=" + plants.size() +
                '}';
    }
}
