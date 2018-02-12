package pentominoes;
import java.util.*;

/**
 * Class for representing 2D coordinates.
 * @author Aaron Anderson 8649682
 * @author James Strathern 5028791
 * @author Josh Whitney 4442561
 * @author Ryan Collins 5955140
 */
public class Coordinate implements Comparable<Coordinate>{
    private int xPos = 0, yPos = 0;

    public Coordinate (int x, int y) {
        xPos = x;
        yPos = y;
    }

    public int getX() {
        return xPos;
    }
    public int getY() {
        return yPos;
    }

    public void setX (int n) {
        xPos = n;
    }
    public void setY (int n) {
        yPos = n;
    }    

    public String toString () {
        return "(" + xPos + ", " + yPos + ")";
    }

    @Override
    public int compareTo (Coordinate otherCoord) {
        int otherX = (otherCoord.getX());
        int otherY = (otherCoord.getY());

        if (xPos != otherX) {
            return xPos - otherX;
        }
        if (yPos != otherY) {
            return yPos - otherY;
        }
        return 0;
    }

    @Override
    public boolean equals (Object other) {
        Coordinate otherCoord = (Coordinate)other;
        int otherX = (otherCoord.getX());
        int otherY = (otherCoord.getY());

        if (otherX == (xPos) && otherY == (yPos)) {
            return true;
        }
        return false;
    }

    public static boolean compareArrangement(ArrayList<Coordinate> c1, Coordinate[] c2) {
        ArrayList<Coordinate> masterArrangement = new ArrayList<Coordinate>(Arrays.asList(c2));
        for (int i = 0; i < c1.size(); i++) {
            boolean foundOffsets = true;
            int xOffset = c1.get(i).getX();
            int yOffset = c1.get(i).getY();
            for (int j = 0; j < c1.size() && foundOffsets; j++) {
                if (j != i) {
                    Coordinate testCoord = new Coordinate(c1.get(j).getX() - xOffset, c1.get(j).getY() - yOffset);
                    //System.out.println("Test coord: " + testCoord);
                    if (!masterArrangement.contains(testCoord)) {
                        foundOffsets = false;
                    }
                }
                //System.out.println("");
            }
            if(foundOffsets){
                return true;
            }
        }
        return false;
    }

    
}
