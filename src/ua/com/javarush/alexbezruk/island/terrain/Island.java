package ua.com.javarush.alexbezruk.island.terrain;

import ua.com.javarush.alexbezruk.island.logic.NumberGenerator;
import ua.com.javarush.alexbezruk.island.wildlife.animal.Animal;
import ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores.*;
import ua.com.javarush.alexbezruk.island.wildlife.plant.Plant;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;

public class Island {
    private static final int LENGTH = 100;
    public static final int WIDTH = 20;

    private PieceOfLand[][] island;

    public Island() {
        island = new PieceOfLand[LENGTH][WIDTH];
        for (int y = 0; y < island.length; y++) {
            for (int x = 0; x < island[y].length; x++) {
                island[y][x] = new PieceOfLand();
            }
        }
    }

    public PieceOfLand[][] getIsland() {
        return island;
    }

    @Override
    public String toString() {
        return "Island{" +
                "island=" + Arrays.deepToString(island) +
                '}';
    }

    public void settlement() {
        for (int y = 0; y < WIDTH; y++) {
            for (int x = 0; x < LENGTH; x++) {
                PieceOfLand pieceOfLand = island[x][y];

                for (int i = 0; i < Animal.getMapOfAnimals().size(); i++) {
                    Class<?> clazz = Animal.getMapOfAnimals().get(i);
                    int maxPopulation = 0;
                    try {
                        Field field = clazz.getDeclaredField("maxPopulation");
                        field.setAccessible(true);
                        maxPopulation = (int) field.get(clazz);
                        field.setAccessible(false);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        System.err.println(e.getMessage());
                    }

                    for (int j = 0; j < NumberGenerator.randomNumber(0, maxPopulation); j++) {
                        try {
                            Animal animal = (Animal) clazz.getConstructor().newInstance();
                            pieceOfLand.animals.add(animal);
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            System.err.println(e.getMessage());
                        }
                    }
                }

                Collections.shuffle(pieceOfLand.animals);

                for (int i = 0; i < NumberGenerator.randomNumber(0, Plant.getMaxPopulation()); i++) {
                    pieceOfLand.plants.add(new Plant());
                }
            }
        }
    }
}
