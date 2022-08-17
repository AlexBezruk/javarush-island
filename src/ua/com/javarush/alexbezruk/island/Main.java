package ua.com.javarush.alexbezruk.island;

import ua.com.javarush.alexbezruk.island.terrain.Island;
import ua.com.javarush.alexbezruk.island.wildlife.animal.Animal;
import ua.com.javarush.alexbezruk.island.wildlife.animal.predator.Bear;

public class Main {
    public static void main(String[] args) {
        Bear bear = new Bear(1, 1);
        System.out.println(bear.getSpeed());
        System.out.println(bear.getSaturation());
        System.out.println(bear.getWeight());
        System.out.println(bear.getMaxPopulation());
        System.out.println(bear.getMaxSaturation());

        Animal bear1 = bear.multiply();
        System.out.println(bear1.equals(bear));
        System.out.println(bear1.getClass());

        Island island = new Island();
        island.settlement();
    }
}