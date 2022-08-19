package ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores;

public class Caterpillar extends Herbivores {
    protected static double weight = 0.01;
    protected static int speed = 0;
    protected static double maxSaturation = 0;
    protected static int maxPopulation = 1000;

    public Caterpillar(int x, int y) {
        super(x, y, weight, speed, 0.75 * maxSaturation, maxSaturation, maxPopulation);
    }
}
