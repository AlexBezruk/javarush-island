package ua.com.javarush.alexbezruk.island.wildlife.animal.predator;

public class Wolf extends Predator {
    protected static int maxPopulation = 30;
    protected static double weight = 50;
    protected static int speed = 3;
    protected static double maxSaturation = 80;

    public Wolf(int x, int y) {
        super(x, y, weight, speed, 0.75 * maxSaturation, maxSaturation, maxPopulation);
    }
}
