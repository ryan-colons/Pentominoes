package pentominoes;

/**
 * Class for representing a spot on a pentomino board.
 * @author Aaron Anderson 8649682
 * @author James Strathern 5028791
 * @author Josh Whitney 4442561
 * @author Ryan Collins 5955140
 */
public class BoardTile {

    private PentominoShape occupyingPentomino = null;
    private Coordinate position;
    private int layer;

    public BoardTile (int x, int y, int l, PentominoShape shape) {
        position = new Coordinate(x, y);
        layer = l;
        occupyingPentomino = shape; 
    }

    public BoardTile copy () {
        return new BoardTile(position.getX(),
                             position.getY(),
                             layer,
                             occupyingPentomino);
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

    public Coordinate getPosition () {
        return position;
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
