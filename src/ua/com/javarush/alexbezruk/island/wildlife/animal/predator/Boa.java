package ua.com.javarush.alexbezruk.island.wildlife.animal.predator;

public class Boa extends Predator {
    protected static double weight = 15;
    protected static int speed = 1;
    protected static double maxSaturation = 3;
    protected static int maxPopulation = 30;

    public Boa(int x, int y) {
        super(x, y, weight, speed, maxSaturation, maxSaturation, maxPopulation);
    }
}
