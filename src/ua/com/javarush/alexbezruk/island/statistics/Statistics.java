package ua.com.javarush.alexbezruk.island.statistics;

import ua.com.javarush.alexbezruk.island.terrain.Island;
import ua.com.javarush.alexbezruk.island.terrain.PieceOfLand;
import ua.com.javarush.alexbezruk.island.wildlife.animal.Animal;
import ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores.*;
import ua.com.javarush.alexbezruk.island.wildlife.animal.predator.*;

import java.util.HashMap;
import java.util.Map;

public class Statistics {
    public static void generalStatisticsOutput(Island island) {
        int numberOfAnimals = countingTotalNumberOfAnimals(countingNumberOfAnimalsBySpecies(island));
        int numberOfPlants = countingNumberOfPlants(island);

        System.out.printf("В настоящее время на острове %d животных и %d растений\n", numberOfAnimals, numberOfPlants);
    }

    public static void outputOfDetailedStatistics(Island island) {
        System.out.println("В настоящее время на острове:\n");
        System.out.println("Animals: ");
        for (Map.Entry<Class<? extends Animal>, Integer> entry : countingNumberOfAnimalsBySpecies(island).entrySet()) {
            System.out.printf("%s - %d\n", entry.getKey().getSimpleName(), entry.getValue());
        }
        System.out.println();
        System.out.printf("Plants - %d\n", countingNumberOfPlants(island));
    }

    private static Map<Class<? extends Animal>, Integer> countingNumberOfAnimalsBySpecies(Island island) {
        Map<Class<? extends Animal>, Integer> map = new HashMap<>();
        map.put(Wolf.class, 0);
        map.put(Boa.class, 0);
        map.put(Fox.class, 0);
        map.put(Bear.class, 0);
        map.put(Eagle.class, 0);
        map.put(Horse.class, 0);
        map.put(Deer.class, 0);
        map.put(Rabbit.class, 0);
        map.put(Mouse.class, 0);
        map.put(Goat.class, 0);
        map.put(Sheep.class, 0);
        map.put(Hog.class, 0);
        map.put(Buffalo.class, 0);
        map.put(Duck.class, 0);
        map.put(Caterpillar.class, 0);

        for (int y = 0; y < island.get().length; y++) {
            for (int x = 0; x < island.get()[y].length; x++) {
                PieceOfLand pieceOfLand = island.get()[y][x];
                for (Animal animal : pieceOfLand.getAnimals()) {
                    for (Class<? extends Animal> clazz : map.keySet()) {
                        if (clazz.equals(animal.getClass())) {
                            map.put(clazz, map.get(clazz) + 1);
                        }
                    }
                }
            }
        }
        
        return map;
    }

    private static int countingTotalNumberOfAnimals(Map<Class<? extends Animal>, Integer> map) {
        int sum = 0;
        for (Integer count : map.values()) {
            sum += count;
        }
        return sum;
    }

    private static int countingNumberOfPlants(Island island) {
        int sum = 0;

        for (int y = 0; y < island.get().length; y++) {
            for (int x = 0; x < island.get()[y].length; x++) {
                PieceOfLand pieceOfLand = island.get()[y][x];
                sum += pieceOfLand.getPlants().size();
            }
        }

        return sum;
    }
}
