package ua.com.javarush.alexbezruk.island.logic;

import ua.com.javarush.alexbezruk.island.statistics.Statistics;
import ua.com.javarush.alexbezruk.island.terrain.Island;
import ua.com.javarush.alexbezruk.island.terrain.PieceOfLand;
import ua.com.javarush.alexbezruk.island.wildlife.WildLife;
import ua.com.javarush.alexbezruk.island.wildlife.animal.Animal;
import ua.com.javarush.alexbezruk.island.wildlife.plant.Plant;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class IslandSimulation {
    private static int DAY = 0;
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static final String PATH_TO_DATA = "src/ua/com/javarush/alexbezruk/island/resources/data.properties";
    private static Properties initialData;

    public IslandSimulation() {
        initialData = getProperties();
    }

    public static Properties getInitialData() {
        return initialData;
    }

    public static void start() {
        IslandSimulation islandSimulation = new IslandSimulation();
        Island island = new Island();
        islandSimulation.settlement(island);
        System.out.println("Остров создан и заселен животными");

        Statistics initialStatistics = new Statistics(island);
        initialStatistics.outputGeneral();
        initialStatistics.saveInitialStateOfIsland();

        System.out.println("\nЗапускаем симуляцию жизни острова...\n");

        while (true) {
            simulationOfOneDay(island);
            DAY++;
            System.out.printf("Прошел день №%d", DAY);
            menuOutput(island);
        }
    }

    private Properties getProperties() {
        File file = new File(PATH_TO_DATA);
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(file));
        } catch (IOException e) {
            System.err.println("Ошибка при чтении данных из properties " + e.getMessage());
        }
        return properties;
    }

    private void settlement(Island island) {
        for (int y = 0; y < Island.getWidth(); y++) {
            for (int x = 0; x < Island.getLength(); x++) {
                PieceOfLand pieceOfLand = island.get()[x][y];
                Properties properties = IslandSimulation.getInitialData();

                for (int i = 0; i < Animal.getListOfAnimals().size(); i++) {
                    Class<?> clazz = Animal.getListOfAnimals().get(i);
                    int maxPopulation = Integer.parseInt(properties.getProperty(clazz.getSimpleName() + ".maxPopulation"));

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

                int maxPopulation = Integer.parseInt(properties.getProperty("Plant.maxPopulation"));
                for (int i = 0; i < NumberGenerator.randomNumber(maxPopulation); i++) {
                    pieceOfLand.getPlants().add(new Plant());
                }
            }
        }
    }

    private static void simulationOfOneDay(Island island) {
        Statistics.zeroingNumberOfDeadAndBornAnimals();

        for (int y = 0; y < Island.getWidth(); y++) {
            for (int x = 0; x < Island.getLength(); x++) {
                List<Animal> animals = island.get()[x][y].getAnimals();
                List<Plant> plants = island.get()[x][y].getPlants();
                for (int i = 0; i < animals.size(); i++) {
                    Animal animal = animals.get(i);

                    List<Integer> operations = new ArrayList<>();
                    operations.add(0);
                    operations.add(1);
                    operations.add(2);

                    for (int j = 0; j < operations.size(); j++) {
                        int randomNumber = NumberGenerator.randomNumber(operations.size() - 1);
                        switch (operations.get(randomNumber)) {
                            case 0 -> animalMove(animal, animals, island);
                            case 1 -> animalMultiply(animal, animals);
                            case 2 -> animalEat(animal, animals, plants, island);
                        }
                        operations.remove(operations.get(randomNumber));
                    }
                }
            }
        }

        reducingSaturationAndremoveDeadAnimals(island);
        plantGrowth(island);
    }

    private static void menuOutput(Island island) {
        int number = 0;
        System.out.println("\nЕсли требуется симуляция еще одного дня жизни острова, введи 1");
        System.out.println("Если требуется вывести статистику по острову, введи 2");
        System.out.println("Если требуется закончить симуляцию жизни острова, введи 3");
        try {
            number = Integer.parseInt(reader.readLine());
            if (number < 1 || number > 3) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.out.println("Введена информация неверного формата, повторите ввод");
            menuOutput(island);
        } catch (IOException e) {
            System.err.println("Ошибка при чтении данных с консоли: " + e.getMessage());
            menuOutput(island);
        }
        switch (number) {
            case 2 -> statisticsOutput(island);
            case 3 -> System.exit(0);
        }
    }

    private static void statisticsOutput(Island island) {
        int number = 0;
        Statistics statistics = new Statistics(island);
        System.out.println("\nЕсли требуется вывести общую статистику по острову, введи 1");
        System.out.println("Если требуется вывести статистику по количеству умерших и рожденных животных за сутки, введи 2");
        System.out.println("Если требуется вывести статистику прироста животных с дня основания острова, введи 3");
        try {
            number = Integer.parseInt(reader.readLine());
            if (number < 1 || number > 3) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.out.println("Введена информация неверного формата, повторите ввод");
            statisticsOutput(island);
        } catch (IOException e) {
            System.err.println("Ошибка при чтении данных с консоли: " + e.getMessage());
            statisticsOutput(island);
        }
        switch (number) {
            case 1 -> statistics.outputGeneral();
            case 2 -> statistics.outputNumberOfBornAndDeadAnimals();
            case 3 -> statistics.outputAnimalsGrowth();
        }
        menuOutput(island);
    }

    private static void animalMove(Animal animal, List<Animal> animals, Island island) {
        if (animal.isAlive && !animal.isMoved) {
            animals.remove(animal);
            animal.move();
            animal.isMoved = true;
            island.get()[animal.getX()][animal.getY()].getAnimals().add(animal);
        }
    }

    private static void animalMultiply(Animal animal, List<Animal> animals) {
        if (animal.isAlive && !animal.isMultiplied) {
            int count = 0;
            for (int j = 0; j < animals.size(); j++) {
                if (animal.getClass().equals(animals.get(j).getClass())) {
                    count++;
                }
                if (count == 2) {
                    if (NumberGenerator.randomNumber(3) == 0) {
                        int countAnimal = 0;
                        for (Animal animal1 : animals) {
                            if (animal.getClass().equals(animal1.getClass())) {
                                countAnimal++;
                            }
                        }
                        if (countAnimal < animal.getMaxPopulation()) {
                            animals.add(animal.multiply());
                        }
                    }
                    break;
                }
            }
        }
    }

    private static void animalEat(Animal animal, List<Animal> animals, List<Plant> plants, Island island) {
        if (animal.isAlive && !animal.isEated) {
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
                animal.isEated = true;
                return;
            }

            randomNumber = NumberGenerator.randomNumber(list.size() - 1);
            Class<? extends WildLife> clazz = list.get(randomNumber);

            for (int j = 0; j < animals.size(); j++) {
                if (clazz == null) {

                }
                if (clazz.equals(Plant.class)) {
                    for (int k = 0; k < 3; k++) {
                        if (animal.getSaturation() < animal.getMaxSaturation() && !plants.isEmpty()) {
                            double newSaturation = animal.getSaturation() + plants.get(0).getWeight();
                            if (newSaturation > animal.getMaxSaturation()) {
                                newSaturation = animal.getMaxSaturation();
                            }
                            animal.setSaturation(newSaturation);
                            plants.remove(0);
                            break;
                        }
                    }
                } else if (animals.get(j).isAlive && animals.get(j).getClass().equals(clazz)) {
                    double newSaturation = animal.getSaturation() + animals.get(j).getWeight();
                    if (newSaturation > animal.getMaxSaturation()) {
                        newSaturation = animal.getMaxSaturation();
                    }
                    animal.setSaturation(newSaturation);
                    animal.isEated = true;
                    animal.eat(animals.get(j));
                    break;
                }
            }
        }
    }

    private static void reducingSaturationAndremoveDeadAnimals(Island island) {
        for (int y = 0; y < Island.getWidth(); y++) {
            for (int x = 0; x < Island.getLength(); x++) {
                List<Animal> animals = island.get()[x][y].getAnimals();
                for (int i = 0; i < animals.size(); i++) {
                    Animal animal = animals.get(i);
                    animal.reducingSaturation();
                    if (!animal.isAlive || animal.getSaturation() < 0) {
                        animals.remove(i);
                        i--;
                    } else {
                        animal.isMoved = false;
                        animal.isMultiplied = false;
                        animal.isEated = false;
                    }
                }
            }
        }
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
}
