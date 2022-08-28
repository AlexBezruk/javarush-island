package ua.com.javarush.alexbezruk.island.statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.com.javarush.alexbezruk.island.terrain.Island;
import ua.com.javarush.alexbezruk.island.terrain.Location;
import ua.com.javarush.alexbezruk.island.wildlife.animal.Animal;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Statistics {
    public static final String PATH_TO_INITIAL_STATISTICS = "resources/initialStatistics.json";
    private static int numberOfDeadAnimals;
    private static int numberOfAnimalsBorn;
    private Map<Class<? extends Animal>, Integer> animalsBySpecies;
    ObjectMapper mapper = new ObjectMapper();
    File file = new File(PATH_TO_INITIAL_STATISTICS);

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

    public void outputGeneral() {
        int numberOfAnimals = countingTotalNumberOfAnimals();
        System.out.printf("На острове %d животных\n", numberOfAnimals);
        System.out.println("Animals: ");
        for (Map.Entry<Class<? extends Animal>, Integer> entry : animalsBySpecies.entrySet()) {
            System.out.printf("%s - %d\n", entry.getKey().getSimpleName(), entry.getValue());
        }
    }

    public void outputNumberOfBornAndDeadAnimals() {
        System.out.printf("За сутки родилось %d животных\n", numberOfAnimalsBorn);
        System.out.printf("За сутки умерло %d животных\n", numberOfDeadAnimals);
    }

    public void outputAnimalsGrowth() {
        Map<Class<? extends Animal>, Integer> growthOfAnimals = loadInitialStateOfIsland();

        System.out.printf("Прирост животных составил:\n");
        for (Map.Entry<Class<? extends Animal>, Integer> entry : getAnimalsBySpecies().entrySet()) {
            System.out.printf("%s: %d\n", entry.getKey().getSimpleName(), entry.getValue() - growthOfAnimals.get(entry.getKey()));
        }
    }

    public void saveInitialStateOfIsland() {
        try {
            mapper.writeValue(file, this);
        } catch (IOException e) {
            System.err.println("Ошибка при записи данных в Json-файл " + e.getMessage());
        }
    }

    private Map<Class<? extends Animal>, Integer> loadInitialStateOfIsland() {
        try {
            return mapper.readValue(file, Statistics.class).getAnimalsBySpecies();
        } catch (IOException e) {
            System.err.println("Ошибка при записи данных в Json-файл " + e.getMessage());
            return Collections.EMPTY_MAP;
        }
    }

    private Map<Class<? extends Animal>, Integer> countingNumberOfAnimalsBySpecies(Island island) {
        Map<Class<? extends Animal>, Integer> map = new HashMap<>();
        for (Class<? extends Animal> clazz : Animal.getListOfAnimals()) {
            map.put(clazz, 0);
        }

        for (int y = 0; y < island.getLocations().length; y++) {
            for (int x = 0; x < island.getLocations()[y].length; x++) {
                Location pieceOfLand = island.getLocations()[y][x];
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
