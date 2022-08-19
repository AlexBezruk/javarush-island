package ua.com.javarush.alexbezruk.island.interfaces;

import ua.com.javarush.alexbezruk.island.wildlife.animal.Animal;

public interface Eatable<T> {
    void eat(T t);
}
