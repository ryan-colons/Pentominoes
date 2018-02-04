package pentominoes;

import java.util.*;

/**
 * Application class for printing pentominoes.
 * @author Aaron Anderson 8649682
 * @author James Strathern 5028791
 * @author Josh Whitney 4442561
 * @author Ryan Collins 5955140
 */
public class MainPentomino {

    /** Main method. Prints all unique pentominoes that can anchor
     *  at a given point on a 10x10 grid.
     */
    public static void main(String[] args) {
        ArrayList<Pentomino> uniquePentominoes = new ArrayList<Pentomino>();
        for (PentominoShape shape : PentominoShape.values()) {
            System.out.println(shape);
            uniquePentominoes.addAll(getUniqueForms(shape));
        }

        for (Pentomino unique : uniquePentominoes) {
            printPentomino(unique, 5, 5);
        }
       
    }

    /** Returns a list of all unique rotations/mirrors of a given pentomino shape. */
    public static ArrayList<Pentomino> getUniqueForms (PentominoShape shape) {
        ArrayList<Pentomino> forms = new ArrayList<Pentomino>();
        ArrayList<Pentomino> candidates = new ArrayList<Pentomino>();

        Pentomino pentomino = new Pentomino(shape);
        Pentomino clone = pentomino.copyOf();
        
        for (int i = 0; i < 4; i++) {
            clone = clone.getRotated();
            Pentomino to_add = clone.copyOf();
            candidates.add(to_add);
        }
        
        pentomino.determineBasicFlippedOffsets();
        clone = pentomino.copyOf();
        for (int i = 0; i < 4; i++) {
            clone = clone.getRotated();
            candidates.add(clone.copyOf());
        }
        
        for (Pentomino candidate : candidates) {
            boolean foundIdentical = false;
            for (Pentomino other : forms) {
                if (candidate.identicalTo(other)) {
                    foundIdentical = true;
                    break;
                }
            }
            if (!foundIdentical) {
                forms.add(candidate);
            }
        }
        
        return forms;
    }

    /** Prints a specified pentomino on a 10x10 grid, anchored at (posX, posY). */
    public static void printPentomino (Pentomino pentomino, int posX, int posY) {
        int[][] grid = new int[10][10];
        for (Coordinate coord : pentomino.determineBlocks(posX, posY)) {
            int x = coord.getX();
            int y = coord.getY();
            if (x >= 0 && x < grid.length && y >= 0 && y < grid[0].length) {
                grid[coord.getX()][coord.getY()] = 1;
            }
        }

        for (int x = grid.length - 1; x >= 0; x--) {
            for (int y = 0; y < grid[0].length; y++) {
                if (grid[y][x] == 1) {
                    System.out.print(pentomino.getShape() + " ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
        pentomino.printOffsets();
        System.out.println();  
    }
}
