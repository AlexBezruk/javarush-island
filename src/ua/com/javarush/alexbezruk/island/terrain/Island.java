package ua.com.javarush.alexbezruk.island.terrain;

public class Island {
    private static final int LENGTH = 100;
    private static final int WIDTH = 20;

    private final Location[][] locations;

    public Island() {
        locations = new Location[LENGTH][WIDTH];
        for (int y = 0; y < locations.length; y++) {
            for (int x = 0; x < locations[y].length; x++) {
                locations[y][x] = new Location();
            }
        }
    }

    public static int getWidth() {
        return WIDTH;
    }

    public static int getLength() {
        return LENGTH;
    }

    public Location[][] getLocations() {
        return locations;
    }

    public Location getLocation(int y, int x) {
        return locations[y][x];
    }
}
