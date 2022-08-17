package ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores;

public class Duck extends Herbivores {
    protected static double weight = 1;
    protected static int speed = 4;
    protected static double maxSaturation = 0.15;
    protected static int maxPopulation = 200;

    public Duck(int x, int y) {
        super(x, y, weight, speed, maxSaturation, maxSaturation, maxPopulation);
    }
}
