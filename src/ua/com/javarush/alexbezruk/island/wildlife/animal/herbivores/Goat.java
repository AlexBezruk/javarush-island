package ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores;

public class Goat extends Herbivores {
    private final static double weight = 60;
    private final static int maxPopulation = 140;
    private final static int speed = 3;
    private final static double maxSaturation = 10;

    public Goat() {
        super(maxSaturation);
    }
}
