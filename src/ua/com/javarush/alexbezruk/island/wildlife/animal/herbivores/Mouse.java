package ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores;

public class Mouse extends Herbivores {
    private final static double weight = 0.05;
    private final static int maxPopulation = 500;
    private final static int speed = 1;
    private final static double maxSaturation = 0.01;

    public Mouse() {
        super(maxSaturation);
    }
}
