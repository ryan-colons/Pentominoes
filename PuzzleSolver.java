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
        
        System.out.println("PUZZLES:");
        for (Puzzle puzz : puzzles) {
            System.out.println(puzz);
        }
    }

    public BoardTile[][] solvePuzzle (Puzzle puzzle) {
        //BoardTile[][] solution = puzzle.getBoardTiles();

        availableShapes.clear();
        for (PentominoShape shape : PentominoShape.values()) {
            availableShapes.add(shape);
        }

        int numLayers = puzzle.getNumLayers();
        boolean solved = solveLayer(puzzle, numLayers);

        
        
        return null;
    }

    public boolean solveLayer (Puzzle puzzle, int layer) {
        if (layer == 0) {
            return true;
        }

        ArrayList<PentominoShape> shapesClone = availableShapes.clone();
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
                }*/
        }
            
        
        return solvedPuzzle;
    }

    public ArrayList<BoardTile[][]> getLayouts (Puzzle puzzle, int layer) {
        ArrayList<BoardTile[][]> layouts = new ArrayList<BoardTile[][]>();
        ArrayList<BoardTile[][]> stack = new ArrayList<BoardTile[][]>();
   
        ArrayList<ArrayList<PentominoShape>> permutations = getPermutations(new ArrayList<PentominoShape>());
        for(ArrayList<PentominoShape> permutation : permutations){
            Puzzle clonePuzzle = puzzle.getClone();
            cloneGrid = puzzle.getCloneGrid();
            BoardTiles[][] board = new BoardTiles[cloneGrid.length][cloneGrid[0].length];
            boolean lastCheck = false;
                
                for(PentominoShape shape : permutation){
                    if(shape == permutation.get(0)){
                        int a = layouts.size() / cloneGrid.length;
                        int b = layouts.size() % cloneGrid[0].length;
                    }
                    for(int x = 0; x < cloneGrid.length; x++){
                        for(int y = 0; y < cloneGrid[x].length; y++){
                            clonePuzzle.addPiece(shape, x, y, layer);
                            
                        }
                    }
                }
                layouts.add(clonePuzzle.getBoardTiles());
            }
        }
        
        return layouts;
    }

    public ArrayList<ArrayList<PentominoShape>> getPermutations(ArrayList<PentominoShape> initial){
        ArrayList<PentominoShape> cloneShapes = availableShapes.clone();
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
