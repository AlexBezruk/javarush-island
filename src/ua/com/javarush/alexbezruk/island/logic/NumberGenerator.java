package ua.com.javarush.alexbezruk.island.logic;

import java.util.Random;

public class NumberGenerator {
    public static int randomNumber(int max) {
        Random random = new Random();
        return random.nextInt(max + 1);
    }
}
