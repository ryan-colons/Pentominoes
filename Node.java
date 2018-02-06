package pentominoes;

import java.util.*;

public class Node {

    private Coordinate coord;
    private Node parent;
    private ArrayList<Node> children = new ArrayList<Node>();
    private Pentomino pentomino;
    private Node endOfSuccessfulPath;

    // constructor for initial node
    public Node (ArrayList<Pentomino> allShapes) {
        for (Pentomino p : allShapes) {
            children.add(new Node(this, p));
        }
    }

    public String toString() {
        return pentomino.getShape().toString();
    }

    public void setEnd (Node node) {
        endOfSuccessfulPath = node;
    }
    public Node successfulNode () {
        return endOfSuccessfulPath;
    }
    
    public Node (Node parent, Pentomino pentomino) {
        this.pentomino = pentomino;
        this.parent = parent;
    }

    public void generateChildren() {
        for (Node node : parent.getChildren()) {
            if (node != this) {
                children.add(node);
            }
        }
    }

    public void setCoord (Coordinate pos) {
        this.coord = pos;
    }
    public Coordinate getCoord () {
        return coord;
    }
    
    public PentominoShape getShape() {
        return pentomino.getShape();
    }

    public Pentomino getPentomino () {
        return pentomino;
    }

    public ArrayList<Node> getChildren () {
        return children;
    }

    public Node getParent () {
        return parent;
    }
    
}
