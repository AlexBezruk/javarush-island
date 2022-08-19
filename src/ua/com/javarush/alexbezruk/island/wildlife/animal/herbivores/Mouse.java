package ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores;

public class Mouse extends Herbivores {
    protected static double weight = 0.05;
    protected static int speed = 1;
    protected static double maxSaturation = 0.01;
    protected static int maxPopulation = 500;

    public Mouse(int x, int y) {
        super(x, y, weight, speed, 0.75 * maxSaturation, maxSaturation, maxPopulation);
    }
}
