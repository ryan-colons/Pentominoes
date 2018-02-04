package pentominoes;

import java.util.*;

/**
 * Class for representing a pentomino.
 * @author Aaron Anderson 8649682
 * @author James Strathern 5028791
 * @author Josh Whitney 4442561
 * @author Ryan Collins 5955140
 */
public class Pentomino {
    
    private PentominoShape shape;
    /** Offset coordinates for each of the 5 blocks. */
    private Coordinate[] offsets = null;
    
    public Pentomino (PentominoShape encoding) {
        shape = encoding;
        determineBasicOffsets();
    }

    public Pentomino copyOf () {
        Pentomino clone = new Pentomino(shape);
        clone.setOffsets(this.getOffsets());
        return clone;
    }

    public Coordinate[] getOffsets () {
        return offsets;
    }

    public void setOffsets (Coordinate[] coords) {
        this.offsets = new Coordinate[coords.length];
        for (int i = 0; i < coords.length; i++) {
            int x = coords[i].getX();
            int y = coords[i].getY();
            this.offsets[i] = new Coordinate(x, y);
        }
    }

    /** Returns a copy of this pentomino, rotated. */
    public Pentomino getRotated () {
        Pentomino clone = copyOf();
        clone.rotate();
        return clone;
    }

    /** Rotate this pentomino 90 degrees. */
    public void rotate () {
        for (int i = 0; i < offsets.length; i++) {
            int x = offsets[i].getX();
            int y = offsets[i].getY();
            offsets[i] = new Coordinate(y, -1 * x);
        }
    }

    /**
     * Determine coordinates of each block.
     * @param x X coordinate of the pentomino on a grid.
     * @param y Y coordinate of the pentomino on a grid.
     * @return Coordinate[] Coordinates for position of each block.
     */
    public Coordinate[] determineBlocks (int x, int y) {
        Coordinate[] newBlocks = new Coordinate[offsets.length];
        for (int i = 0; i < offsets.length; i++) {
            newBlocks[i] = new Coordinate(0, 0);
            int offsetX = offsets[i].getX();
            int offsetY = offsets[i].getY();
            newBlocks[i].setX(x + offsetX);
            newBlocks[i].setY(y + offsetY);
        }
        return newBlocks;
    }

    /** Gets offsets from determineBasicOffsets, then flips them. */
    public void determineBasicFlippedOffsets () {
        determineBasicOffsets ();
        for (int i = 0; i < offsets.length; i++) {
            int x = offsets[i].getX();
            int y = offsets[i].getY();
            offsets[i] = new Coordinate(-1 * x, y);
        }
    }

    /** Sets offsets according to the 'shape' data field.
     */
    public void determineBasicOffsets () {
        offsets = new Coordinate[5];
        offsets[0] = new Coordinate(0, 0);
        offsets[1] = new Coordinate(0, 1);
        switch(shape) {
            case O:
                offsets[2] = new Coordinate(0, 2);
                offsets[3] = new Coordinate(0, 3);
                offsets[4] = new Coordinate(0, 4);
                break;
            case P:
                offsets[2] = new Coordinate(0, 2);
                offsets[3] = new Coordinate(1, 1);
                offsets[4] = new Coordinate(1, 2);
                break;
            case Q:
                offsets[2] = new Coordinate(1, 1);
                offsets[3] = new Coordinate(2, 1);
                offsets[4] = new Coordinate(3, 1);
                break;
            case R:
                offsets[2] = new Coordinate(1, 1);
                offsets[3] = new Coordinate(1, 2);
                offsets[4] = new Coordinate(2, 1);
                break;
            case S:
                offsets[2] = new Coordinate(1, 1);
                offsets[3] = new Coordinate(1, 2);
                offsets[4] = new Coordinate(1, 3);
                break;
            case T:
                offsets[2] = new Coordinate(0, 2);
                offsets[3] = new Coordinate(-1, 2);
                offsets[4] = new Coordinate(1, 2);
                break;
            case U:
                offsets[1] = new Coordinate(-1, 0);
                offsets[2] = new Coordinate(-1, 1);
                offsets[3] = new Coordinate(1, 0);
                offsets[4] = new Coordinate(1, 1);
                break;
            case V:
                offsets[2] = new Coordinate(0, 2);
                offsets[3] = new Coordinate(1, 0);
                offsets[4] = new Coordinate(2, 0);
                break;
            case W:
                offsets[2] = new Coordinate(1, 1);
                offsets[3] = new Coordinate(1, 2);
                offsets[4] = new Coordinate(2, 2);
                break;
            case X:
                offsets[2] = new Coordinate(-1, 1);
                offsets[3] = new Coordinate(1, 1);
                offsets[4] = new Coordinate(0, 2);
                break;
            case Y:
                offsets[2] = new Coordinate(0, 2);
                offsets[3] = new Coordinate(1, 2);
                offsets[4] = new Coordinate(0, 3);
                break;
            case Z:
                offsets[2] = new Coordinate(1, 1);
                offsets[3] = new Coordinate(2, 1);
                offsets[4] = new Coordinate(2, 2);
                break;
            default:
                System.out.println("Bad encoding!");
                offsets = null;    
        }
    }
    
    public void printOffsets () {
        for (Coordinate coord : offsets) {
            System.out.print(coord + " ");
        }
        System.out.println();
    }

    /** Checks if this pentomino has all the same offsets
     *  as another pentomino. */
    public boolean identicalTo (Pentomino other) {
        Coordinate[] otherOffsets = other.getOffsets();
        if (offsets == null || otherOffsets == null ||
            offsets.length != otherOffsets.length) {
            System.out.println("BAD COMPARISON");
            return false;
        }

        Coordinate[] copy = offsets.clone();
        Coordinate[] otherCopy = otherOffsets.clone();
        Arrays.sort(copy);
        Arrays.sort(otherCopy);
        
        for (int i = 0; i < copy.length; i++) {
            if (!copy[i].equals(otherCopy[i])) {
                return false;
            }
        }
        return true;
    }

    public PentominoShape getShape() {
        return this.shape;
    }
}
