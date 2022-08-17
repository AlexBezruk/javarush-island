package ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores;

public class Goat extends Herbivores {
    protected static double weight = 60;
    protected static int speed = 3;
    protected static double maxSaturation = 10;
    protected static int maxPopulation = 140;

    public Goat(int x, int y) {
        super(x, y, weight, speed, maxSaturation, maxSaturation, maxPopulation);
    }
}
