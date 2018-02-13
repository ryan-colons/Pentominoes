package pentominoes;

import java.util.*;

public class Node {

    private static HashMap<String, Node> memo = new HashMap<String, Node>(); 
    
    private int[][] grid;
    private Node godParent;
    //private ArrayList<Node> children = new ArrayList<Node>();
    private BoardTile[][] boardState;
    private ArrayList<PentominoShape> remainingShapes;
    private PentominoShape pentomino;
    private int layer;

    public Node (Node godParent, int[][] board, BoardTile[][] state, ArrayList<PentominoShape> remaining, PentominoShape prev, int layer) {
        this.grid = board;
        this.godParent = godParent;
        this.boardState = state;
        this.remainingShapes = remaining;
        this.pentomino = prev;
        this.layer = layer;
    }

    public int[][] getGrid() {
        return this.grid;
    }

    public BoardTile[][] getState(){
        return this.boardState;
    }

    //public ArrayList<Node> getChildren(){
    //    return this.children;
    //}

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

    public void printGrid () {
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                System.out.print(grid[x][y]);
            }
            System.out.println();
        }
        System.out.println();
    }
    
    public void printBoardState () {
        for (int x = 0; x < boardState.length; x++) {
            for (int y = 0; y < boardState[x].length; y++) {
                if (boardState[x][y] == null) {
                    System.out.print(grid[x][y]);
                } else {
                    System.out.print(boardState[x][y]);
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    
    public Node findSolution(){
        Puzzle clonePuzzle = new Puzzle(getGridClone(), getBoardClone());
        //System.out.println(clonePuzzle);
        printBoardState();
        if (clonePuzzle.finishedAtLayer(layer)) {
            return this;
        }

        for(int x = 0; x < grid.length; x++){
            for(int y = 0; y < grid[x].length; y++){
                if (boardState[x][y] == null) {
                    boolean pieceAdded = false;
                    for(PentominoShape pentominoShape : remainingShapes){
                        for(Pentomino p : PuzzleSolver.getUniqueForms(pentominoShape)){
                            clonePuzzle = new Puzzle(getGridClone(), getBoardClone());
                            ArrayList<PentominoShape> newRemaining = new ArrayList<PentominoShape>(remainingShapes);
                            newRemaining.remove(pentominoShape);
                            if(clonePuzzle.addPiece(p, x, y, layer, remainingShapes)){
                                pieceAdded = true;
                                String boardString = BoardTile.arrayToString(clonePuzzle.getBoardTiles());
                                Node newChild = new Node(this.godParent, clonePuzzle.getGridClone(), clonePuzzle.getBoardTiles(), newRemaining, pentominoShape, layer);
                                if (memo.containsKey(boardString)) {
                                    if (memo.get(boardString) != null) {
                                        return memo.get(boardString);
                                    }
                                } else {
                                    //System.out.println("STORING AT " + boardString);
                                    memo.put(boardString, newChild.findSolution());
                                    if(memo.get(boardString) != null){
                                        Node childNode = memo.get(boardString);
                                        Puzzle nextLayerPuzzle = new Puzzle(childNode.getGridClone(), childNode.getBoardClone());
                                        Node nextLayerSolution = PuzzleSolver.solveLayer(nextLayerPuzzle, layer - 1, childNode);
                                        if (nextLayerSolution != null) {
                                            return nextLayerSolution;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (!pieceAdded && layer == 1) {
                        System.out.println("Couldn't place at " + x + ", " + y);
                        return null;
                    }
                    
                }
                //System.out.println("Skipped over " + x + ", " + y);
            }   
        }
        //System.out.println("Processed all children of " + BoardTile.arrayToString(this.boardState));
        return null;
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
