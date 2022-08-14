package ua.com.javarush.alexbezruk.island.wildlife.animal.predator;

public class Wolf extends Predator {
    private final static double weight = 50;
    private final static int maxPopulation = 30;
    private final static int speed = 3;
    private final static double maxSaturation = 80;

    private double saturation;

    public Wolf() {
        super(maxSaturation);
    }
}
