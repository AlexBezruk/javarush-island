package ua.com.javarush.alexbezruk.island.wildlife.animal.predator;

public class Boa extends Predator {
    private final static double weight = 15;
    private final static int maxPopulation = 30;
    private final static int speed = 1;
    private final static double maxSaturation = 3;

    public Boa() {
        super(maxSaturation);
    }
}
