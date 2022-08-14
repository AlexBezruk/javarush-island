package ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores;

public class Sheep extends Herbivores {
    private final static double weight = 70;
    private final static int maxPopulation = 140;
    private final static int speed = 3;
    private final static double maxSaturation = 15;

    public Sheep() {
        super(maxSaturation);
    }
}
