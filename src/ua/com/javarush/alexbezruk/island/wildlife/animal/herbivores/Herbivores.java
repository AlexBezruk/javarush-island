package ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores;

import ua.com.javarush.alexbezruk.island.wildlife.animal.Animal;

public abstract class Herbivores extends Animal {
    public Herbivores(int x, int y, double weight, int speed, double saturation, double maxSaturation, int maxPopulation) {
        super(x, y, weight, speed, saturation, maxSaturation, maxPopulation);
    }
}
