package ua.com.javarush.alexbezruk.island.wildlife.animal.predator;

public class Eagle extends Predator {
    protected static double weight = 6;
    protected static int speed = 3;
    protected static double maxSaturation = 1;
    protected static int maxPopulation = 20;

    public Eagle(int x, int y) {
        super(x, y, weight, speed, 0.75 * maxSaturation, maxSaturation, maxPopulation);
    }
}
