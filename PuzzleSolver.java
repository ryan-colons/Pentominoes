package pentominoes;

import java.util.*;

/**
 * Class for solving pentomino puzzles.
 * @author Aaron Anderson 8649682
 * @author James Strathern 5028791
 * @author Josh Whitney 4442561
 * @author Ryan Collins 5955140
 */
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
            Puzzle solution = null;
            boolean rotate = false;
            if (!isUnsolvable(puzz)) {
                int[][] puzzGrid = puzz.getGrid();
                if (puzzGrid.length < puzzGrid[0].length) {
                    puzz = rotate(puzz);
                    rotate = true;
                }
                puzz.setShapes(new ArrayList<PentominoShape>(Arrays.asList(PentominoShape.values())));
                solution = Puzzle.findSolution(puzz);
            }
            if (solution == null) {
                System.out.println("IMPOSSIBLE!");
            } else {
                if (rotate) {
                    solution = rotate(solution);
                }
                solution.printBoard();

            }
        }
        
    }

    // return true if puzzle grid doesn't sum to 60
    public static boolean isUnsolvable (Puzzle puzzle) {
        int count = 0;
        int[][] grid = puzzle.getGrid();
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                count += grid[x][y];
            }
        }
        return count != 60;
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
