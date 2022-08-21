package ua.com.javarush.alexbezruk.island.statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.com.javarush.alexbezruk.island.logic.IslandSimulation;
import ua.com.javarush.alexbezruk.island.terrain.Island;
import ua.com.javarush.alexbezruk.island.terrain.PieceOfLand;
import ua.com.javarush.alexbezruk.island.wildlife.animal.Animal;
import ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores.*;
import ua.com.javarush.alexbezruk.island.wildlife.animal.predator.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Statistics {
    public static final String PATH_TO_INITIAL_DATA = "src/ua/com/javarush/alexbezruk/island/resources/initialStatistics.json";
    private static int numberOfDeadAnimals;
    private static int numberOfAnimalsBorn;
    private Map<Class<? extends Animal>, Integer> animalsBySpecies;

    public Statistics() {
    }

    public Statistics(Island island) {
        animalsBySpecies = countingNumberOfAnimalsBySpecies(island);
    }

    public Map<Class<? extends Animal>, Integer> getAnimalsBySpecies() {
        return animalsBySpecies;
    }

    public static void incrementNumberOfDeadAnimals() {
        numberOfDeadAnimals++;
    }

    public static void incrementNumberOfAnimalsBorn() {
        numberOfAnimalsBorn++;
    }

    public static void zeroingNumberOfDeadAndBornAnimals() {
        numberOfAnimalsBorn = 0;
        numberOfDeadAnimals = 0;
    }

    public void generalStatisticsOutput() {
        int numberOfAnimals = countingTotalNumberOfAnimals();
        System.out.printf("В настоящее время на острове %d животных\n", numberOfAnimals);
        System.out.println("Animals: ");
        for (Map.Entry<Class<? extends Animal>, Integer> entry : animalsBySpecies.entrySet()) {
            System.out.printf("%s - %d\n", entry.getKey().getSimpleName(), entry.getValue());
        }
    }

    public void conclusionOfStatisticsOnNumberOfDeadAndBornAnimals() {
        System.out.printf("За сутки родилось %d животных\n", numberOfAnimalsBorn);
        System.out.printf("За сутки умерло %d животных\n", numberOfDeadAnimals);
    }

    public void outputOfStatisticsOnGrowthOfAnimals() {
        Map<Class<? extends Animal>, Integer> growthOfAnimals = readingInitialStateOfIsland();

        System.out.printf("Прирост животных составил:\n");
        for (Map.Entry<Class<? extends Animal>, Integer> entry : this.getAnimalsBySpecies().entrySet()) {
            System.out.printf("%s: %d\n", entry.getKey().getSimpleName(), entry.getValue() - growthOfAnimals.get(entry.getKey()));
        }
    }

    public void preservingInitialStateOfIsland() {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(PATH_TO_INITIAL_DATA);
        try {
            mapper.writeValue(file, this);
        } catch (IOException e) {
            System.err.println("Ошибка при записи данных в Json-файл " + e.getMessage());
        }
    }

    private Map<Class<? extends Animal>, Integer> readingInitialStateOfIsland() {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(PATH_TO_INITIAL_DATA);
        try {
            return mapper.readValue(file, Statistics.class).getAnimalsBySpecies();
        } catch (IOException e) {
            System.err.println("Ошибка при записи данных в Json-файл " + e.getMessage());
            return null;
        }
    }

    private Map<Class<? extends Animal>, Integer> countingNumberOfAnimalsBySpecies(Island island) {
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

    private int countingTotalNumberOfAnimals() {
        int sum = 0;
        for (Integer count : animalsBySpecies.values()) {
            sum += count;
        }
        return sum;
    }
}
