package pentominoes;

import java.util.*;

public class PuzzleSolver {

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
            System.out.println(puzz);
            boolean rotate  = false;
            int[][] puzzGrid = puzz.getGrid();
            if (puzzGrid.length < puzzGrid[0].length) {
                //rotate
                puzz = rotate(puzz);
                rotate = true;
            }
            puzz.setShapes(new ArrayList<PentominoShape>(Arrays.asList(PentominoShape.values())));
            ArrayList<Puzzle> solution = Puzzle.findSolution(puzz);
            if (solution == null) {
                System.out.println("IMPOSSIBLE!");
            } else {
                for (Puzzle s : solution) {
                    if (rotate) {
                        s = rotate(s);
                    }
                    BoardTile[][][] boardTiles = s.getBoard();
                    for (int l = 0; l < boardTiles.length; l++) {
                        for (int x = 0; x < boardTiles[l].length; x++) {
                            for (int y = 0; y < boardTiles[l][x].length; y++) {
                                if (boardTiles[l][x][y] == null) {
                                    System.out.print(".");
                                } else {
                                    System.out.print(boardTiles[l][x][y]);
                                }
                            }
                            System.out.println();
                        }
                        System.out.println();
                    }
                }
                System.out.println();
                System.out.println();
                
            }
            // remove this later pls
            // it means we only look at the first puzzle
            //return;
            }
        
    }

    public static Puzzle rotate (Puzzle puzzle) {
        int[][] originalGrid = puzzle.getGrid();
        int[][] rotatedGrid = new int[originalGrid[0].length][originalGrid.length];
        for (int x = 0; x < rotatedGrid.length; x++) {
            for (int y = 0; y < rotatedGrid[x].length; y++) {
                rotatedGrid[x][y] = originalGrid[y][x];
            }
        }
        BoardTile[][][] originalBoard = puzzle.getBoard();
        BoardTile[][][] rotatedBoard = new BoardTile[originalBoard.length]
            [originalBoard[0][0].length]
            [originalBoard[0].length];
        for (int l = 0; l < rotatedBoard.length; l++) {
            for (int x = 0; x < rotatedBoard[l].length; x++) {
                for (int y = 0; y < rotatedBoard[l][x].length; y++) {
                    rotatedBoard[l][x][y] = originalBoard[l][y][x];
                }
            }
        }
        Puzzle rotatedPuzzle = new Puzzle(rotatedGrid, rotatedBoard, new ArrayList<PentominoShape>());
        return rotatedPuzzle;
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
                forms.addAll(candidate.getShiftedPositions());
            }
        }
        return forms;
    }

}
