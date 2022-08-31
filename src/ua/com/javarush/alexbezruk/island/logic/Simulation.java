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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Simulation {
    private static final int PARA = 2;
    private static final int PROBABILITY_OF_BIRTH_OF_ANIMAL = 25;
    private static final int MAXIMUM_AMOUNT_OF_GRASS_EATEN_PER_DAY = 3;
    private static int day = 0;
    private static final String PATH_TO_DATA = "resources/data.properties";
    private static final Properties initialData;

    private final Island island;
    private final Menu menu;

    static {
        initialData = getProperties();
    }

    public Simulation() {
        island = new Island();
        menu = new Menu(island);
    }

    public static Properties getInitialData() {
        return initialData;
    }

    public static void start() {
        Simulation simulation = new Simulation();
        simulation.settlement();
        System.out.println("Остров создан и заселен животными");

        Statistics initialStatistics = new Statistics(simulation.island);
        initialStatistics.outputGeneral();
        initialStatistics.saveInitialStateOfIsland();

        System.out.println("\nЗапускаем симуляцию жизни острова...\n");

        while (true) {
            simulation.oneDay();
            day++;
            System.out.printf("Прошел день №%d", day);
            simulation.menu.output();
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

    private void settlement() {
        for (int y = 0; y < Island.getWidth(); y++) {
            for (int x = 0; x < Island.getLength(); x++) {
                Location location = island.getLocation(x, y);

                for (int i = 0; i < Animal.getListOfAnimals().size(); i++) {
                    Class<?> clazz = Animal.getListOfAnimals().get(i);

                    int maxPopulation = 0;
                    try {
                        maxPopulation = Integer.parseInt(initialData.getProperty(clazz.getSimpleName() + ".maxPopulation"));
                    } catch (NumberFormatException e) {
                        System.err.println("Неверный формат данных в файле properties " + e.getMessage());
                    }

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

                int maxPopulation = 0;
                try {
                    maxPopulation = Integer.parseInt(initialData.getProperty("Plant.maxPopulation"));
                } catch (NumberFormatException e) {
                    System.err.println("Неверный формат данных в файле properties " + e.getMessage());
                }

                for (int i = 0; i < NumberGenerator.randomNumber(maxPopulation); i++) {
                    location.getPlants().add(new Plant());
                }
            }
        }
    }

    private void oneDay() {
        Statistics.reset();
        ExecutorService executor = Executors.newFixedThreadPool(Island.getLength() * Island.getWidth());
        for (int y = 0; y < Island.getWidth(); y++) {
            for (int x = 0; x < Island.getLength(); x++) {
                Location location = island.getLocation(x, y);

                executor.submit(() -> {
                    List<Animal> animals = location.getAnimals();
                    List<Plant> plants = location.getPlants();
                    for (int i = 0; i < animals.size(); i++) {
                        Animal animal = animals.get(i);

                        if (animal.isMoved()) {
                            continue;
                        }

                        List<Integer> operations = new ArrayList<>();
                        operations.add(0);
                        operations.add(1);
                        operations.add(2);

                        animal.lock.lock();
                        while (!operations.isEmpty()) {
                            int randomNumber = NumberGenerator.randomNumber(operations.size() - 1);
                            switch (operations.get(randomNumber)) {
                                case 0 -> animalMove(animal, animals);
                                case 1 -> animalMultiply(animal, animals);
                                case 2 -> animalEat(animal, animals, plants);
                            }
                            operations.remove(operations.get(randomNumber));
                        }
                        animal.setMoved(true);
                        animal.lock.unlock();
                    }
                });
            }
        }

        executor.shutdown();
        try {
            executor.awaitTermination(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            System.err.println("Поток прерван " + e.getMessage());
        }

        reducingSaturation();
        removeDeadAnimals();
        resetLabels();
        plantGrowth();
    }

    private void animalMove(Animal animal, List<Animal> animals) {
        if (animal.isAlive()) {
            animals.remove(animal);
            animal.move();
            island.getLocation(animal.getX(), animal.getY()).getAnimals().add(animal);
        }
    }

    private void animalMultiply(Animal animal, List<Animal> animals) {
        if (animal.isAlive() && !animal.isMultiplied()) {
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

    private void animalEat(Animal animal, List<Animal> animals, List<Plant> plants) {
        if (animal.isAlive()) {
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
                for (int k = 0; k < NumberGenerator.randomNumber(MAXIMUM_AMOUNT_OF_GRASS_EATEN_PER_DAY); k++) {
                    if (animal.getSaturation() < animal.getMaxSaturation() && !plants.isEmpty()) {
                        double newSaturation = animal.getSaturation() + plants.remove(0).getWeight();
                        if (newSaturation > animal.getMaxSaturation()) {
                            newSaturation = animal.getMaxSaturation();
                        }
                        animal.setSaturation(newSaturation);
                    }
                }
            } else {
                for (Animal animalVictim : animals) {
                    if (animalVictim.isAlive() && animalVictim.getClass().equals(clazz) && animalVictim.lock.tryLock()) {
                        double newSaturation = animal.getSaturation() + animalVictim.getWeight();
                        if (newSaturation > animal.getMaxSaturation()) {
                            newSaturation = animal.getMaxSaturation();
                        }
                        animal.setSaturation(newSaturation);
                        animal.eat(animalVictim);
                        animalVictim.lock.unlock();
                        break;
                    }
                }
            }
        }
    }

    private void reducingSaturation() {
        for (int y = 0; y < Island.getWidth(); y++) {
            for (int x = 0; x < Island.getLength(); x++) {
                List<Animal> animals = island.getLocation(x, y).getAnimals();
                for (Animal animal : animals) {
                    animal.reducingSaturation();
                    if (animal.getSaturation() < 0) {
                        animal.setAlive(false);
                    }
                }
            }
        }
    }

    private void removeDeadAnimals() {
        for (int y = 0; y < Island.getWidth(); y++) {
            for (int x = 0; x < Island.getLength(); x++) {
                List<Animal> animals = island.getLocation(x, y).getAnimals();
                for (Animal animal : new ArrayList<>(animals)) {
                    if (!animal.isAlive()) {
                        animals.remove(animal);
                        Statistics.incrementNumberOfDeadAnimals();
                    }
                }
            }
        }
    }

    private void resetLabels() {
        for (int y = 0; y < Island.getWidth(); y++) {
            for (int x = 0; x < Island.getLength(); x++) {
                List<Animal> animals = island.getLocation(x, y).getAnimals();
                for (Animal animal : animals) {
                    animal.setMoved(false);
                }
            }
        }
    }

    private void plantGrowth() {
        for (int y = 0; y < Island.getWidth(); y++) {
            for (int x = 0; x < Island.getLength(); x++) {
                Location location = island.getLocations()[x][y];

                int necessaryGrowth = NumberGenerator.randomNumber(Plant.getMaxPopulation()) - location.getPlants().size();
                for (int i = 0; i < necessaryGrowth; i++) {
                    location.getPlants().add(new Plant());
                }
            }
        }
    }
}
