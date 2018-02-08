package pentominoes;

import java.util.*;

public class Node {

    private int[][] grid;
    private Node godParent;
    private ArrayList<Node> children = new ArrayList<Node>();
    private BoardTile[][] boardState;
    private ArrayList<PentominoShape> remainingShapes;
    private PentominoShape pentomino;

    public Node (Node godParent, int[][] board, BoardTile[][] state, ArrayList<PentominoShape> remaining, PentominoShape prev) {
        this.grid = board;
        this.godParent = godParent;
        this.boardState = state;
        this.remainingShapes = remaining;
        this.pentomino = prev;
    }

    public int[][] getGrid() {
        return this.grid;
    }

    public BoardTile[][] getState(){
        return this.boardState;
    }

    public ArrayList<Node> getChildren(){
        return this.children;
    }

    public ArrayList<PentominoShape> getRemainingShapes() {
        return remainingShapes;
    }

    public Node getGodParent() {
        return godParent;
    }

    public PentominoShape getPentomino() {
        return pentomino;
    }

    public String toString () {
        if (pentomino == null) {
            return "root";
        }
        return pentomino.toString();
    }

    public void printBoardState () {
        for (int x = 0; x < boardState.length; x++) {
            for (int y = 0; y < boardState[x].length; y++) {
                if (boardState[x][y] == null) {
                    System.out.print(".");
                } else {
                    System.out.print(boardState[x][y]);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public void makeChildren(){
        ArrayList<PentominoShape> usedPentominos = new ArrayList<PentominoShape>();
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[i].length; j++){
                if (boardState[i][j] != null) {
                    if(!usedPentominos.contains(boardState[i][j].getPentomino())){
                        usedPentominos.add(boardState[i][j].getPentomino());
                    }
                }
            }
        }
        for(PentominoShape pentominoShape : remainingShapes){
            if(!usedPentominos.contains(pentominoShape)){
                for(int x = 0; x < grid.length; x++){
                    for(int y = 0; y < grid[x].length; y++){
                        for(Pentomino p : PuzzleSolver.getUniqueForms(pentominoShape)){
                            Puzzle clonePuzzle = new Puzzle(getGridClone(), getBoardClone());
                            ArrayList<PentominoShape> newRemaining = new ArrayList<PentominoShape>(remainingShapes);
                            newRemaining.remove(pentominoShape);
                            if(clonePuzzle.addPiece(p, x, y, 1)){
                                Node newChild = new Node(this.godParent, clonePuzzle.getGridClone(), clonePuzzle.getBoardTiles(), newRemaining, pentominoShape);
                                children.add(newChild);
                                //System.out.println("Resulting child:");
                                //newChild.printBoardState();
                            }
                        }
                    }
                }
            }
        }
        //System.out.println("CHILDREN BOARD STATES: ");
        //for (Node child : children) {
        //    child.printBoardState();
        //}
        
        return;
    }

    public int[][] getGridClone () {
        int[][] gridClone = new int[grid.length][grid[0].length];
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                gridClone[x][y] = grid[x][y];
            }
        }
        return gridClone;
    }

    public BoardTile[][] getBoardClone () {
        BoardTile[][] boardClone = new BoardTile[boardState.length][boardState[0].length];
        for (int x = 0; x < boardState.length; x++) {
            for (int y = 0; y < boardState[0].length; y++) {
                boardClone[x][y] = boardState[x][y];
            }
        }
        return boardClone;
    }
                                             
                    

    
    
    /*
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
    */
    
}
