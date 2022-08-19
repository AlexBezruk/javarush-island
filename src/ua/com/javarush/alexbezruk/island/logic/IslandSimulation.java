package ua.com.javarush.alexbezruk.island.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.com.javarush.alexbezruk.island.statistics.Statistics;
import ua.com.javarush.alexbezruk.island.terrain.Island;
import ua.com.javarush.alexbezruk.island.terrain.PieceOfLand;
import ua.com.javarush.alexbezruk.island.wildlife.animal.Animal;
import ua.com.javarush.alexbezruk.island.wildlife.plant.Plant;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

public class IslandSimulation {
    public static void start() {
        Island island = new Island();
        settlement(island);

        Statistics initialStatistics = new Statistics(island);
        initialStatistics.generalStatisticsOutput();
        initialStatistics.preservingInitialStateOfIsland();

        initialStatistics.outputOfStatisticsOnGrowthOfAnimals();

        System.out.println("\nЗапускаем симуляцию жизни острова...");
    }

    private static void plantGrowth(Island island) {
        for (int y = 0; y < Island.getWidth(); y++) {
            for (int x = 0; x < Island.getLength(); x++) {
                PieceOfLand pieceOfLand = island.get()[x][y];

                int necessaryGrowth = NumberGenerator.randomNumber(Plant.getMaxPopulation() - pieceOfLand.getPlants().size());
                for (int i = 0; i < necessaryGrowth; i++) {
                    pieceOfLand.getPlants().add(new Plant());
                }
            }
        }
    }

    private static void settlement(Island island) {
        for (int y = 0; y < Island.getWidth(); y++) {
            for (int x = 0; x < Island.getLength(); x++) {
                PieceOfLand pieceOfLand = island.get()[x][y];

                for (int i = 0; i < Animal.getMapOfAnimals().size(); i++) {
                    Class<?> clazz = Animal.getMapOfAnimals().get(i);
                    int maxPopulation = 0;
                    try {
                        Field field = clazz.getDeclaredField("maxPopulation");
                        field.setAccessible(true);
                        maxPopulation = (int) field.get(clazz);
                        field.setAccessible(false);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        System.err.println("Ошибка при получении поля класса" + e.getMessage());
                    }

                    for (int j = 0; j < NumberGenerator.randomNumber(maxPopulation); j++) {
                        try {
                            Animal animal = (Animal) clazz.getConstructor(int.class, int.class).newInstance(x, y);
                            pieceOfLand.getAnimals().add(animal);
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            System.err.println("Ошибка при создании объекта" + e.getMessage());
                        }
                    }
                }

                Collections.shuffle(pieceOfLand.getAnimals());

                for (int i = 0; i < NumberGenerator.randomNumber(Plant.getMaxPopulation()); i++) {
                    pieceOfLand.getPlants().add(new Plant());
                }
            }
        }
    }
}
