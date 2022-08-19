package ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores;

public class Buffalo extends Herbivores {
    protected static double weight = 700;
    protected static int speed = 3;
    protected static double maxSaturation = 100;
    protected static int maxPopulation = 10;

    public Buffalo(int x, int y) {
        super(x, y, weight, speed, maxSaturation, 0.75 * maxSaturation, maxPopulation);
    }
}
