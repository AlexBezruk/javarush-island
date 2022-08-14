package ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores;

public class Deer extends Herbivores {
    private final static double weight = 300;
    private final static int maxPopulation = 20;
    private final static int speed = 4;
    private final static double maxSaturation = 50;

    public Deer() {
        super(maxSaturation);
    }
}
