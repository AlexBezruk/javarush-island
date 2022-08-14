package ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores;

public class Duck extends Herbivores {
    private final static double weight = 1;
    private final static int maxPopulation = 200;
    private final static int speed = 4;
    private final static double maxSaturation = 0.15;

    public Duck() {
        super(maxSaturation);
    }
}
