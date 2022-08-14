package ua.com.javarush.alexbezruk.island.wildlife.animal.predator;

public class Bear extends Predator {
    private final static double weight = 500;
    private final static int maxPopulation = 5;
    private final static int speed = 2;
    private final static double maxSaturation = 80;

    public Bear() {
        super(maxSaturation);
    }
}
