package ua.com.javarush.alexbezruk.island.terrain;

public class Island {

    private static final int LENGTH = 100;
    private static final int WIDTH = 20;

    private PieceOfLand[][] island;

    public Island() {
        island = new PieceOfLand[LENGTH][WIDTH];
        for (int y = 0; y < island.length; y++) {
            for (int x = 0; x < island[y].length; x++) {
                island[y][x] = new PieceOfLand();
            }
        }
    }

    public static int getWidth() {
        return WIDTH;
    }

    public static int getLength() {
        return LENGTH;
    }

    public PieceOfLand[][] get() {
        return island;
    }
}
