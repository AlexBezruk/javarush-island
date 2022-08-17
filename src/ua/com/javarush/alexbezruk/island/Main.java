package ua.com.javarush.alexbezruk.island;

import ua.com.javarush.alexbezruk.island.terrain.Island;
import ua.com.javarush.alexbezruk.island.wildlife.animal.predator.Bear;

public class Main {
    public static void main(String[] args) {
        Bear bear = new Bear(1, 1);
        System.out.println(bear.getSpeed());
        System.out.println(bear.getSaturation());
        System.out.println(bear.getWeight());
        System.out.println(bear.getMaxPopulation());
        System.out.println(bear.getMaxSaturation());

        bear.move();
        System.out.println(bear.getX());
        System.out.println(bear.getY());

        Island island = new Island();
        island.settlement();
    }
}