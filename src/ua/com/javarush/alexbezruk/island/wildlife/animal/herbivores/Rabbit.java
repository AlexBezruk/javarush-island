package ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores;

public class Rabbit extends Herbivores {
    protected static double weight = 2;
    protected static int speed = 2;
    protected static double maxSaturation = 0.45;
    protected static int maxPopulation = 150;

    public Rabbit(int x, int y) {
        super(x, y, weight, speed, maxSaturation, maxSaturation, maxPopulation);
    }
}
