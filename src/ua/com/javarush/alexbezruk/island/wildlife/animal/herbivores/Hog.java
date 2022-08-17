package ua.com.javarush.alexbezruk.island.wildlife.animal.herbivores;

public class Hog extends Herbivores {
    protected static double weight = 400;
    protected static int speed = 2;
    protected static double maxSaturation = 50;
    protected static int maxPopulation = 50;

    public Hog(int x, int y) {
        super(x, y, weight, speed, maxSaturation, maxSaturation, maxPopulation);
    }
}
