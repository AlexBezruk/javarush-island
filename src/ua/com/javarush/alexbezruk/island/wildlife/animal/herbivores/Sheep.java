package ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores;

public class Sheep extends Herbivores {
    protected static double weight = 70;
    protected static int speed = 3;
    protected static double maxSaturation = 15;
    protected static int maxPopulation = 140;

    public Sheep(int x, int y) {
        super(x, y, weight, speed, maxSaturation, maxSaturation, maxPopulation);
    }
}
