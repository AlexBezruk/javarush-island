package ua.com.javarush.alexbezruk.island.wildlife.animal.predator;

public class Eagle extends Predator {
    private final static double weight = 6;
    private final static int maxPopulation = 20;
    private final static int speed = 3;
    private final static double maxSaturation = 1;

    public Eagle() {
        super(maxSaturation);
    }
}
