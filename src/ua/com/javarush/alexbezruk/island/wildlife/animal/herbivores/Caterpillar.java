package ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores;

public class Caterpillar extends Herbivores {
    private final static double weight = 0.01;
    private final static int maxPopulation = 1000;
    private final static int speed = 0;
    private final static double maxSaturation = 0;

    private double saturation;

    public Caterpillar() {
        super(maxSaturation);
    }
}
