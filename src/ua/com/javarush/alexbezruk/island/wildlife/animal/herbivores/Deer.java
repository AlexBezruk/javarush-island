package ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores;

public class Deer extends Herbivores {
    protected static double weight = 300;
    protected static int speed = 4;
    protected static double maxSaturation = 50;
    protected static int maxPopulation = 20;

    public Deer(int x, int y) {
        super(x, y, weight, speed, 0.75 * maxSaturation, maxSaturation, maxPopulation);
    }
}
