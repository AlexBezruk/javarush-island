package ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores;

public class Horse extends Herbivores {
    private final static double weight = 400;
    private final static int maxPopulation = 20;
    private final static int speed = 4;
    private final static double maxSaturation = 60;

    public Horse() {
        super(maxSaturation);
    }
}
