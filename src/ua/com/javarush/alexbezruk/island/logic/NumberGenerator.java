package ua.com.javarush.alexbezruk.island.logic;

import java.util.concurrent.ThreadLocalRandom;

public class NumberGenerator {
    public static int randomNumber(int max) {
        return ThreadLocalRandom.current().nextInt(max + 1);
    }
}
