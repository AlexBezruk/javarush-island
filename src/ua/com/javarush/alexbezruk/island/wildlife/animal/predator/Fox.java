package ua.com.javarush.alexbezruk.island.wildlife.animal.predator;

public class Fox extends Predator {
    private final static double weight = 8;
    private final static int maxPopulation = 30;
    private final static int speed = 2;
    private final static double maxSaturation = 2;

    public Fox() {
        super(maxSaturation);
    }
}
