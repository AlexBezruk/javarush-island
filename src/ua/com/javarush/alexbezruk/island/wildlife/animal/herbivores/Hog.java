package ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores;

public class Hog extends Herbivores {
    private final static double weight = 400;
    private final static int maxPopulation = 50;
    private final static int speed = 2;
    private final static double maxSaturation = 50;

    public Hog() {
        super(maxSaturation);
    }
}
