package ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores;

public class Buffalo extends Herbivores {
    private final double weight = 700;
    private final static int maxPopulation = 10;
    private final int speed = 3;
    private final static double maxSaturation = 10;

    public Buffalo() {
        super(maxSaturation);
    }
}
