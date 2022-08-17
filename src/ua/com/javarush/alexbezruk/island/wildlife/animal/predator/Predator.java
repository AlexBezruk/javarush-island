package ua.com.javarush.alexbezruk.island.wildlife.animal.predator;

import ua.com.javarush.alexbezruk.island.wildlife.animal.Animal;

public abstract class Predator extends Animal {
    public Predator(int x, int y, double weight, int speed, double saturation, double maxSaturation, int maxPopulation) {
        super(x, y, weight, speed, saturation, maxSaturation, maxPopulation);
    }
}
