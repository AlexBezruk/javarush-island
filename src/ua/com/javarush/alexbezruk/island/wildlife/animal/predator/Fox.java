package ua.com.javarush.alexbezruk.island.wildlife.animal.predator;

public class Fox extends Predator {
    protected static double weight = 8;
    protected static int speed = 2;
    protected static double maxSaturation = 2;
    protected static int maxPopulation = 30;

    public Fox(int x, int y) {
        super(x, y, weight, speed, maxSaturation, maxSaturation, maxPopulation);
    }
}
