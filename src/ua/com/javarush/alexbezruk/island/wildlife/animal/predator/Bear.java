package ua.com.javarush.alexbezruk.island.wildlife.animal.predator;

public class Bear extends Predator {
    protected static double weight = 500;
    protected static int speed = 2;
    protected static double maxSaturation = 80;
    protected static int maxPopulation = 5;

    public Bear(int x, int y) {
        super(x, y, weight, speed, maxSaturation, 0.75 * maxSaturation, maxPopulation);
    }
}
