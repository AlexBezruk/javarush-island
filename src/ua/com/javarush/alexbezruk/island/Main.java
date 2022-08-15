package ua.com.javarush.alexbezruk.island;

import ua.com.javarush.alexbezruk.island.statistics.Statistics;
import ua.com.javarush.alexbezruk.island.terrain.Island;

public class Main {
    public static void main(String[] args) {
        Island island = new Island();
        island.settlement();

        Statistics.outputOfDetailedStatistics(island);
    }
}
