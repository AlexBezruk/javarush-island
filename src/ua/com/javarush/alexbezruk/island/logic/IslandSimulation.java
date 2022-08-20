package ua.com.javarush.alexbezruk.island.logic;

import ua.com.javarush.alexbezruk.island.statistics.Statistics;
import ua.com.javarush.alexbezruk.island.terrain.Island;
import ua.com.javarush.alexbezruk.island.terrain.PieceOfLand;
import ua.com.javarush.alexbezruk.island.wildlife.WildLife;
import ua.com.javarush.alexbezruk.island.wildlife.animal.Animal;
import ua.com.javarush.alexbezruk.island.wildlife.plant.Plant;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class IslandSimulation {
    public static void start() {
        Island island = new Island();
        settlement(island);

        Statistics initialStatistics = new Statistics(island);
        initialStatistics.generalStatisticsOutput();
        initialStatistics.preservingInitialStateOfIsland();

        System.out.println("\nЗапускаем симуляцию жизни острова...\n");

        Statistics.zeroingNumberOfDeadAndBornAnimals();

        animalMove(island);
        animalMultiply(island);
        animalEat(island);
        reducingSaturationAndremoveDeadAnimals(island);
        plantGrowth(island);

        Statistics statistics = new Statistics(island);
        statistics.generalStatisticsOutput();
    }

    private static void animalMove(Island island) {
        for (int y = 0; y < Island.getWidth(); y++) {
            for (int x = 0; x < Island.getLength(); x++) {
                List<Animal> animals = island.get()[x][y].getAnimals();
                for (int i = 0; i < animals.size(); i++) {
                    Animal animal = animals.get(i);
                    if (animal.isAlive && !animal.isMoved) {
                        animals.remove(i--);
                        animal.move();
                        animal.isMoved = true;
                        island.get()[animal.getX()][animal.getY()].getAnimals().add(animal);
                    }
                }
            }
        }
    }

    private static void animalMultiply(Island island) {
        for (int y = 0; y < Island.getWidth(); y++) {
            for (int x = 0; x < Island.getLength(); x++) {
                List<Animal> animals = island.get()[x][y].getAnimals();
                for (int i = 0; i < animals.size(); i++) {
                    Animal animal = animals.get(i);
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
            }
        }
    }

    private static void animalEat(Island island) {
        for (int y = 0; y < Island.getWidth(); y++) {
            for (int x = 0; x < Island.getLength(); x++) {
                List<Animal> animals = island.get()[x][y].getAnimals();
                for (int i = 0; i < animals.size(); i++) {
                    Animal animal = animals.get(i);
                    if (animal.isAlive && !animal.isEated) {
                        int randomNumber = NumberGenerator.randomNumber(100);
                        int key = -1;
                        for (Map.Entry<Integer, Class<? extends Animal>> entry: Animal.getMapOfAnimals().entrySet()) {
                            if (animal.getClass().equals(entry.getValue())) {
                                key = entry.getKey();
                            }
                        }

                        List<Class<? extends WildLife>> list = new ArrayList<>();
                        for (int j = 0; j < Animal.getProbabilityOfBeingEaten()[key].length; j++) {
                            if (randomNumber < Animal.getProbabilityOfBeingEaten()[key][j]) {
                                if (j == Animal.getProbabilityOfBeingEaten()[key].length - 1) {
                                    list.add(Plant.class);
                                } else {
                                    list.add(Animal.getMapOfAnimals().get(j));
                                }
                            }
                        }

                        if (list.isEmpty()) {
                            animal.isEated = true;
                            break;
                        }

                        randomNumber = NumberGenerator.randomNumber(list.size() - 1);
                        Class<? extends WildLife> clazz = list.get(randomNumber);

                        for (int j = 0; j < animals.size(); j++) {
                            if (clazz == null) {

                            }
                            if (clazz.equals(Plant.class)) {
                                List<Plant> plants = island.get()[x][y].getPlants();
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
            }
        }
    }

    private static void reducingSaturationAndremoveDeadAnimals(Island island) {
        for (int y = 0; y < Island.getWidth(); y++) {
            for (int x = 0; x < Island.getLength(); x++) {
                List<Animal> animals = island.get()[x][y].getAnimals();
                for (int i = 0; i < animals.size(); i++) {
                    animals.get(i).reducingSaturation();
                    if (!animals.get(i).isAlive || animals.get(i).getSaturation() < 0) {
                        animals.remove(i);
                        i--;
                    }
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
