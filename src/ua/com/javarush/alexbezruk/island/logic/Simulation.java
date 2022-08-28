package ua.com.javarush.alexbezruk.island.logic;

import ua.com.javarush.alexbezruk.island.statistics.Statistics;
import ua.com.javarush.alexbezruk.island.terrain.Island;
import ua.com.javarush.alexbezruk.island.terrain.Location;
import ua.com.javarush.alexbezruk.island.wildlife.WildLife;
import ua.com.javarush.alexbezruk.island.wildlife.animal.Animal;
import ua.com.javarush.alexbezruk.island.wildlife.plant.Plant;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Simulation {
    private static final int PARA = 2;
    public static final int PROBABILITY_OF_BIRTH_OF_ANIMAL = 25;
    private static int day = 0;
    private static final String PATH_TO_DATA = "resources/data.properties";
    private static Properties initialData;

    static {
        initialData = getProperties();
    }

    public static Properties getInitialData() {
        return initialData;
    }

    public static void start() {
        Simulation simulation = new Simulation();
        Island island = new Island();
        simulation.settlement(island);
        System.out.println("Остров создан и заселен животными");

        Statistics initialStatistics = new Statistics(island);
        initialStatistics.outputGeneral();
        initialStatistics.saveInitialStateOfIsland();

        System.out.println("\nЗапускаем симуляцию жизни острова...\n");

        while (true) {
            simulation.oneDay(island);
            day++;
            System.out.printf("Прошел день №%d", day);
            Menu.output(island);
        }
    }

    private static Properties getProperties() {
        File file = new File(PATH_TO_DATA);
        Properties properties = new Properties();
        try (FileReader reader = new FileReader(file)) {
            properties.load(reader);
        } catch (IOException e) {
            System.err.println("Ошибка при чтении данных из properties " + e.getMessage());
        }
        return properties;
    }

    private void settlement(Island island) {
        for (int y = 0; y < Island.getWidth(); y++) {
            for (int x = 0; x < Island.getLength(); x++) {
                Location location = island.getLocation(x, y);

                for (int i = 0; i < Animal.getListOfAnimals().size(); i++) {
                    Class<?> clazz = Animal.getListOfAnimals().get(i);
                    int maxPopulation = Integer.parseInt(initialData.getProperty(clazz.getSimpleName() + ".maxPopulation"));

                    for (int j = 0; j < NumberGenerator.randomNumber(maxPopulation); j++) {
                        try {
                            Animal animal = (Animal) clazz.getConstructor(int.class, int.class).newInstance(x, y);
                            location.getAnimals().add(animal);
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            System.err.println("Ошибка при создании объекта" + e.getMessage());
                        }
                    }
                }

                Collections.shuffle(location.getAnimals());

                int maxPopulation = Integer.parseInt(initialData.getProperty("Plant.maxPopulation"));
                for (int i = 0; i < NumberGenerator.randomNumber(maxPopulation); i++) {
                    location.getPlants().add(new Plant());
                }
            }
        }
    }

    private void oneDay(Island island) {
        Statistics.zeroingNumberOfDeadAndBornAnimals();

        for (int y = 0; y < Island.getWidth(); y++) {
            for (int x = 0; x < Island.getLength(); x++) {
                List<Animal> animals = island.getLocations()[x][y].getAnimals();
                List<Plant> plants = island.getLocations()[x][y].getPlants();
                for (int i = 0; i < animals.size(); i++) {
                    Animal animal = animals.get(i);

                    if (animal.isMoved) {
                        continue;
                    }

                    List<Integer> operations = new ArrayList<>();
                    operations.add(0);
                    operations.add(1);
                    operations.add(2);

                    for (int j = 0; j < operations.size(); j++) {
                        int randomNumber = NumberGenerator.randomNumber(operations.size() - 1);
                        switch (operations.get(randomNumber)) {
                            case 0 -> animalMove(animal, animals, island);
                            case 1 -> animalMultiply(animal, animals, island);
                            case 2 -> animalEat(animal, animals, plants, island);
                        }
                        operations.remove(operations.get(randomNumber));
                    }
                    animal.isMoved = true;
                }
            }
        }

        reducingSaturation(island);
        removeDeadAnimals(island);
        reducingSaturation(island);
        plantGrowth(island);
    }

    private void animalMove(Animal animal, List<Animal> animals, Island island) {
        if (animal.isAlive) {
            animals.remove(animal);
            animal.move();
            island.getLocation(animal.getX(), animal.getY()).getAnimals().add(animal);
        }
    }

    private void animalMultiply(Animal animal, List<Animal> animals, Island island) {
        if (animal.isAlive && !animal.isMultiplied) {
            Location location = island.getLocation(animal.getX(), animal.getY());
            int numberOfAnimalsOfCertainType = location.numberOfAnimalsOfCertainType(animal.getClass());
            if (isCheckingBirthOfAnimal(animal, numberOfAnimalsOfCertainType)) {
                animals.add(animal.multiply());
            }
        }
    }

    private boolean isCheckingBirthOfAnimal(Animal animal, int numberOfAnimalsOfCertainType) {
        return NumberGenerator.randomNumber(100 / PROBABILITY_OF_BIRTH_OF_ANIMAL - 1) == 0
                && numberOfAnimalsOfCertainType >= PARA
                && numberOfAnimalsOfCertainType < animal.getMaxPopulation();
    }

    private void animalEat(Animal animal, List<Animal> animals, List<Plant> plants, Island island) {
        if (animal.isAlive) {
            int randomNumber = NumberGenerator.randomNumber(100);
            int key = Animal.getListOfAnimals().indexOf(animal.getClass());

            List<Class<? extends WildLife>> list = new ArrayList<>();
            for (int j = 0; j < Animal.getProbabilityOfBeingEaten()[key].length; j++) {
                if (randomNumber < Animal.getProbabilityOfBeingEaten()[key][j]) {
                    if (j == Animal.getProbabilityOfBeingEaten()[key].length - 1) {
                        list.add(Plant.class);
                    } else {
                        list.add(Animal.getListOfAnimals().get(j));
                    }
                }
            }

            if (list.isEmpty()) {
                return;
            }

            randomNumber = NumberGenerator.randomNumber(list.size() - 1);
            Class<? extends WildLife> clazz = list.get(randomNumber);

            if (clazz.equals(Plant.class)) {
                for (int k = 0; k < 3; k++) {
                    if (animal.getSaturation() < animal.getMaxSaturation() && !plants.isEmpty()) {
                        double newSaturation = animal.getSaturation() + plants.remove(0).getWeight();
                        if (newSaturation > animal.getMaxSaturation()) {
                            newSaturation = animal.getMaxSaturation();
                        }
                        animal.setSaturation(newSaturation);
                    }
                }
            } else {
                for (int j = 0; j < animals.size(); j++) {
                    if (animals.get(j).isAlive && animals.get(j).getClass().equals(clazz)) {
                        double newSaturation = animal.getSaturation() + animals.get(j).getWeight();
                        if (newSaturation > animal.getMaxSaturation()) {
                            newSaturation = animal.getMaxSaturation();
                        }
                        animal.setSaturation(newSaturation);
                        animal.eat(animals.get(j));
                        break;
                    }
                }
            }
        }
    }

    private void reducingSaturation(Island island) {
        for (int y = 0; y < Island.getWidth(); y++) {
            for (int x = 0; x < Island.getLength(); x++) {
                List<Animal> animals = island.getLocation(x, y).getAnimals();
                for (int i = 0; i < animals.size(); i++) {
                    Animal animal = animals.get(i);
                    animal.reducingSaturation();
                    if (animal.getSaturation() < 0) {
                        animal.isAlive = false;

                    }
                }
            }
        }
    }

    private void removeDeadAnimals(Island island) {
        for (int y = 0; y < Island.getWidth(); y++) {
            for (int x = 0; x < Island.getLength(); x++) {
                List<Animal> animals = island.getLocation(x, y).getAnimals();
                for (int i = 0; i < animals.size(); i++) {
                    Animal animal = animals.get(i);
                    if (!animal.isAlive) {
                        animals.remove(i);
                        Statistics.incrementNumberOfDeadAnimals();
                        i--;
                    }
                }
            }
        }
    }

    private void resetLabels(Island island) {
        for (int y = 0; y < Island.getWidth(); y++) {
            for (int x = 0; x < Island.getLength(); x++) {
                List<Animal> animals = island.getLocation(x, y).getAnimals();
                for (int i = 0; i < animals.size(); i++) {
                    Animal animal = animals.get(i);
                    animal.isMoved = false;
                }
            }
        }
    }

    private void plantGrowth(Island island) {
        for (int y = 0; y < Island.getWidth(); y++) {
            for (int x = 0; x < Island.getLength(); x++) {
                Location pieceOfLand = island.getLocations()[x][y];

                int necessaryGrowth = NumberGenerator.randomNumber(Plant.getMaxPopulation() - pieceOfLand.getPlants().size());
                for (int i = 0; i < necessaryGrowth; i++) {
                    pieceOfLand.getPlants().add(new Plant());
                }
            }
        }
    }
}
