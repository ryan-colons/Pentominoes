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
            ArrayList<ArrayList<Node>> solution = solvePuzzle(puzz);
            if (solution == null) {
                System.out.println("IMPOSSIBLE!");
            } else {
                int[][] grid = puzz.getGridClone();
                for (ArrayList<Node> layer : solution) {
                    String[][] to_print = new String[grid.length][grid[0].length];
                    for (Node node : layer) {
                        Pentomino pentomino = node.getPentomino();
                        Coordinate origin = node.getCoord();
                        for (Coordinate coord : pentomino.determineBlocks(origin.getX(), origin.getY())) {
                            to_print[coord.getX()][coord.getY()] = node.getShape().toString();
                        }
                    }
                    for (int x = 0; x < to_print.length; x++) {
                        for (int y = 0; y < to_print[x].length; y++) {
                            if (to_print == null) {
                                System.out.print(".");
                            } else {
                                System.out.print(to_print[x][y]);
                            }
                        }
                        System.out.println();
                    }
                }
            }
        }
    }

    public static ArrayList<ArrayList<Node>> solvePuzzle (Puzzle puzzle) {
        // set available shapes
        availableShapes.clear();
        for (PentominoShape shape : PentominoShape.values()) {
            availableShapes.add(shape);
        }

        int numLayers = puzzle.getNumLayers();
        Node[] solutions = new Node[numLayers];
        for (int i = numLayers; i > 0; i--) {
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

    public static Node solveLayer (Puzzle puzzle, int layer) {
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
        
        while (stack.size() > 0) {
            System.out.println("STACK SIZE: " + stack.size());
            
            for (int x = 0; x < grid.length; x++) {
                for (int y = 0; y < grid[x].length; y++) {
 
                    Node current = stack.get(stack.size() - 1);
                    current.setCoord(new Coordinate(x, y));

                    if (usedShapes.contains(current.getShape())) {
                        break;
                    }
                    usedShapes.add(current.getShape());

                    Pentomino pentomino = current.getPentomino();
                    boolean noViolation = puzzleClone.addPiece(pentomino, x, y, layer);
                    if (!noViolation) {
                        break;
                    }

                    boolean layerSolved = puzzleClone.finishedAtLayer(layer);
                    if (layerSolved) {
                        // return list of nodes
                        initial.setEnd(current);
                        return initial;
                    }

                    stack.remove(current);
                    current.generateChildren();
                    stack.addAll(current.getChildren());
                }
            }
        }
        // no solution for this layer
        return null;
    }
    
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
