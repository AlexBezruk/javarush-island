package ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores;

public class Rabbit extends Herbivores {
    private final static double weight = 2;
    private final static int maxPopulation = 150;
    private final static int speed = 2;
    private final static double maxSaturation = 0.45;

    public Rabbit() {
        super(maxSaturation);
    }
}
