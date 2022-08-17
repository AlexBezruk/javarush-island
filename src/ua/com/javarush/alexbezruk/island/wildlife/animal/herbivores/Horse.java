package ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores;

public class Horse extends Herbivores {
    protected static double weight = 400;
    protected static int speed = 4;
    protected static double maxSaturation = 60;
    protected static int maxPopulation = 20;

    public Horse(int x, int y) {
        super(x, y, weight, speed, maxSaturation, maxSaturation, maxPopulation);
    }
}
