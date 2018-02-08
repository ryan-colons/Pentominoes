package pentominoes;

import java.util.*;

public class PuzzleSolver {

    public static ArrayList<PentominoShape> availableShapes = new ArrayList<PentominoShape>();

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        ArrayList<Puzzle> puzzles = new ArrayList<Puzzle>();
        ArrayList<String[]> lines = new ArrayList<String[]>();
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            if (line.equals("")) {
                puzzles.add(makePuzzle(lines));
                lines.clear();           
            } else {
                line = line.replaceAll("\\s", "");
                String[] chars = new String[line.length()];
                for (int i = 0; i < line.length(); i++) {
                    chars[i] = Character.toString(line.charAt(i));
                }
                lines.add(chars);
            }
        }

        puzzles.add(makePuzzle(lines));
        
        for (Puzzle puzz : puzzles) {
            availableShapes = new ArrayList<PentominoShape>(Arrays.asList(PentominoShape.values()));
            Node solution = solveLayer(puzz, puzz.getNumLayers(), null);
            if (solution == null) {
                System.out.println("IMPOSSIBLE!");
            } else {
                ArrayList<Node> solutionNodes = new ArrayList<Node>();
                Node current = solution;
                while (current != null) {
                    solutionNodes.add(current);
                    current = current.getGodParent();
                }
                for (Node solutionNode : solutionNodes) {
                    BoardTile[][] boardTiles = solutionNode.getState();
                    for (int x = 0; x < boardTiles.length; x++) {
                        for (int y = 0; y < boardTiles[x].length; y++) {
                            if (boardTiles[x][y] == null) {
                                System.out.print(".");
                            } else {
                                System.out.print(boardTiles[x][y]);
                            }
                        }
                        System.out.println();
                    }
                    System.out.println();
                    System.out.println();
                }
                
            }
            // remove this later pls
            // it means we only look at the first puzzle
            return;
        }
    }

    public static Node solveLayer (Puzzle puzzle, int layer, Node godParent) {
        if (layer == 0) {
            return godParent;
        }
        System.out.println("Attempting to solve layer " + layer);
        Node root = new Node(null, puzzle.getGridClone(), puzzle.getBoardTiles(), availableShapes, null);
        root.makeChildren();
        ArrayList<Node> children = root.getChildren();
        ArrayList<Node> stack = new ArrayList<Node>();
        stack.addAll(children);
        while (stack.size() > 0) {
            System.out.println("Stack:            " + stack.size());
            Node current = stack.get(stack.size() - 1);
            System.out.println("Remaining shapes: " + current.getRemainingShapes().size());
            availableShapes = current.getRemainingShapes();

            
            Puzzle currentPuzzle = new Puzzle(current.getGrid(), current.getState());
            current.printBoardState();
            if (currentPuzzle.finishedAtLayer(layer)) {
                System.out.println(currentPuzzle);
                Node solution = solveLayer(currentPuzzle, layer - 1, current);
                if (solution != null) {
                    System.out.println("FULLY SOLVED!");
                    return solution;
                } else {
                    System.out.println("PARTIALLY SOLVED");
                }
            }

            current.makeChildren();
            stack.addAll(current.getChildren());
            stack.remove(current);
        }
        System.out.println("NO SOLUTION FOUND AT LAYER " + layer);
        return null;
    }
        


        /* // set available shapes
        availableShapes.clear();
        for (PentominoShape shape : PentominoShape.values()) {
            availableShapes.add(shape);
        }

        int numLayers = puzzle.getNumLayers();
        Node[] solutions = new Node[numLayers];
        for (int i = numLayers; i > 0; i--) {
            System.out.println("layer: " + i);
            Node solutionRoot = solveLayer(puzzle, i);
            if (solutionRoot == null) {
                // go back to previous layer
                if (i == numLayers) {
                    return null;
                }
                i += 2;
            } else {
                // set solution, continue to next layer
                solutions[i] = solveLayer(puzzle, i);
                }
        }
            
        

        ArrayList<ArrayList<Node>> to_return = new ArrayList<ArrayList<Node>>();
        for (int i = 0; i < solutions.length; i++) {
            ArrayList<Node> successfulNodes = new ArrayList<Node>();
            Node current = solutions[i].successfulNode();
            successfulNodes.add(current);
            while (current.getParent() != null) {
                current = current.getParent();
                successfulNodes.add(current);
            }
            to_return.add(successfulNodes);
        }
        return to_return;
    }

    /* public static Node solveLayer (Puzzle puzzle, int layer) {
        Puzzle puzzleClone = puzzle.getClone();
        ArrayList<Pentomino> allPentominoes = new ArrayList<Pentomino>();
        for (PentominoShape shape : PentominoShape.values()) {
            allPentominoes.addAll(getUniqueForms(shape));
        }
        ArrayList<PentominoShape> usedShapes = new ArrayList<PentominoShape>();
        
        Node initial = new Node(allPentominoes);
        ArrayList<Node> stack = new ArrayList<Node>();
        stack.addAll(initial.getChildren());

        int[][] grid = puzzleClone.getGridClone();
        boolean totalBreak = false;
        
        while (stack.size() > 0) {
            totalBreak = false;
            System.out.println("STACK SIZE: " + stack.size() + ", LAYER: " + layer);
            
            int x = 0;
            int y = 0;
            Node current = stack.get(stack.size() - 1);
            /*
            Coordinate memoCoord = getMemo(layer, current.getShape());
            if (memoCoord != null) {
                if (x != grid.length - 1) {
                    x = memoCoord.getX() + 1;
                    y = memoCoord.getY();
                } else {
                    x = 0;
                    y = memoCoord.getY() + 1;
                }
            }
            

            if (usedShapes.contains(current.getShape())) {
                System.out.println("Shape already used! " + current.getShape());
                stack.remove(current);
                totalBreak = true;
            }
            
            for (; x < grid.length; x++) {
                
                for (; y < grid[x].length && !totalBreak; y++) {
 
                    current.setCoord(new Coordinate(x, y));

                    System.out.println(current + ": " + x + ", " + y);
                    
                    Pentomino pentomino = current.getPentomino();
                    boolean noViolation = puzzleClone.addPiece(pentomino, x, y, layer);
                    if (!noViolation) {
                        System.out.println("Violation!");
                        break;
                    }

                    boolean layerSolved = puzzleClone.finishedAtLayer(layer);
                    if (layerSolved) {
                        // return list of nodes
                        initial.setEnd(current);
                        System.out.println("Success!");
                        return initial;
                    }
                    System.out.println("Shape placed.");
                    stack.remove(current);
                    current.generateChildren();
                    usedShapes.add(current.getShape());
                    stack.addAll(current.getChildren());
                    addMemo(layer, new Coordinate(x, y), current.getShape());
                    totalBreak = true;
                    break;
                }
                if (totalBreak == true) {
                    break;
                }
            }
        }
        // no solution for this layer
        System.out.println("No solution for layer " + layer);
        return null;
    }*/
    
    /*
    public boolean solveLayer (Puzzle puzzle, int layer) {
        if (layer == 0) {
            return true;
        }

        ArrayList<PentominoShape> shapesClone = (ArrayList<PentominoShape>)availableShapes.clone();
        boolean solvedPuzzle = false;
        
        ArrayList<BoardTile[][]> layouts = getLayouts(puzzle, layer);
        for (BoardTile[][] layout : layouts) {
            availableShapes = shapesClone;
            int[][] grid = puzzle.getGridClone();
            for (int x = 0; x < layout.length; x++) {
                for (int y = 0; y < layout[x].length; y++) {
                    if (layout[x][y] != null) {
                        grid[x][y]--;
                    }
                }
            }
            /* if (finishedAtLayer(layer, grid)) {
                for (int x = 0; x < layout.length; x++) {
                    for (int y = 0; y < layout[x].length; y++) {
                        availableShapes.remove(layout[x][y].getPentomino());
                    }
                }
                solvedPuzzle = solveLayer(puzzle, layer - 1);
                }
        }
            
        
        return solvedPuzzle;
    }

    public ArrayList<BoardTile[][]> getLayouts (Puzzle puzzle, int layer) {
        ArrayList<BoardTile[][]> layouts = new ArrayList<BoardTile[][]>();
        ArrayList<BoardTile[][]> stack = new ArrayList<BoardTile[][]>();
   
        ArrayList<ArrayList<PentominoShape>> permutations = getPermutations(new ArrayList<PentominoShape>());
        for(ArrayList<PentominoShape> permutation : permutations){
            Puzzle clonePuzzle = puzzle.getClone();
            int[][] cloneGrid = puzzle.getGridClone();
            BoardTile[][] board = new BoardTile[cloneGrid.length][cloneGrid[0].length];
            boolean lastCheck = false;
                
            for(PentominoShape shape : permutation){
                if(shape == permutation.get(0)){
                    int a = layouts.size() / cloneGrid.length;
                    int b = layouts.size() % cloneGrid[0].length;
                }
                for(int x = 0; x < cloneGrid.length; x++){
                    for(int y = 0; y < cloneGrid[x].length; y++){
                        //clonePuzzle.addPiece(shape, x, y, layer);
                            
                    }
                }
            }
            layouts.add(clonePuzzle.getBoardTiles());
        }
        return layouts;
    }
*/
    
    /*

    public ArrayList<ArrayList<PentominoShape>> getPermutations(ArrayList<PentominoShape> initial){
        ArrayList<PentominoShape> cloneShapes = (ArrayList<PentominoShape>)availableShapes.clone();
        ArrayList<ArrayList<PentominoShape>> toReturn = new ArrayList<ArrayList<PentominoShape>>();
        toReturn.add(initial);
        for(PentominoShape shape : cloneShapes){
            availableShapes.remove(shape);
            ArrayList<PentominoShape> toAdd = initial.clone();
            toAdd.add(shape);
            toReturn.addAll(getPermutations(toAdd));
            availableShapes = cloneShapes;
        }
    }
    */

    public static Puzzle makePuzzle (ArrayList<String[]> lines) {
        if (lines.size() == 0) {
            return null;
        } 
        String[][] grid = new String[lines.size()][];
        for (int i = 0; i < grid.length; i++) {
            String[] row = lines.get(i);
            grid[i] = new String[row.length];
            for (int j = 0; j < row.length; j++) {
                grid[i][j] = row[j];
            }
        }
        return new Puzzle(grid);
    }


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

}
