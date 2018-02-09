package pentominoes;

public class BoardTile {

    private PentominoShape occupyingPentomino = null;
    private Coordinate position;
    private int layer;

    public BoardTile (int x, int y, int l, PentominoShape shape) {
        position = new Coordinate(x, y);
        layer = l;
        occupyingPentomino = shape; 
    }

    public boolean isAvailable (int[][] grid) {
        if (occupyingPentomino != null) {
            return false;
        }
        if (grid[position.getX()][position.getY()] <= 0) {
            return false;
        }
        return true;
    }

    public int getLayer() {
        return layer;
    }
    
    public void setPentomino (Pentomino pentomino) {
        occupyingPentomino = pentomino.getShape();
    }

    public void clearPentomino () {
        occupyingPentomino = null;
    }

    public PentominoShape getPentomino () {
        return occupyingPentomino;
    }

    public String toString () {
        return occupyingPentomino.toString();
    }

    public static String arrayToString (BoardTile[][] array) {
        String result = "";
        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array[x].length; y++) {
                if (array[x][y] != null) {
                    result += array[x][y].toString();
                } else {
                    result += ".";
                }
            }
        }
        return result;
    }
    
}
