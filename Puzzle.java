package pentominoes;

import java.util.*;

public class Puzzle {

    int[][] grid;
    BoardTile[][] boardTiles;

    public Puzzle (String[][] strGrid) {
        int[][] intGrid = new int[strGrid.length][];
        for (int i = 0; i < strGrid.length; i++) {
            intGrid[i] = new int[strGrid[i].length];
            for (int j = 0; j < intGrid[i].length; j++) {
                String str = strGrid[i][j];
                if (str.equals("*")) {
                    intGrid[i][j] = 0;
                } else if (str.equals(".")) {
                    intGrid[i][j] = 1;
                } else {
                    intGrid[i][j] = Integer.parseInt(str);
                }
            }
        }
        grid = intGrid;
        setDisplayBoard();
    }

    public Puzzle(int[][] cloneGrid, BoardTile[][] cloneTiles) {
        grid = cloneGrid;
        boardTiles = cloneTiles;
    }

    public int[][] getGridClone () {
        return grid.clone();
    }

    public Puzzle getClone () {
        Puzzle clonePuzzle = new Puzzle(grid, boardTiles);
        return clonePuzzle;
    }

    public boolean finishedAtLayer (int layer) {
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                if (grid[x][y] == layer) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean finishedAtLayer (int layer, int[][] testGrid) {
        for (int x = 0; x < testGrid.length; x++) {
            for (int y = 0; y < testGrid[x].length; y++) {
                if (testGrid[x][y] == layer) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getNumLayers () {
        int maxValue = grid[0][0];
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                if (grid[x][y] >= maxValue) {
                    maxValue = grid[x][y];
                }
            }
        }
        return maxValue;
    }

    // return true iff successful
    public boolean addPiece (Pentomino pentomino, int x, int y, int layer) {
        Coordinate[] blocks = pentomino.determineBlocks(x, y);
        boolean violation = isViolation(blocks, layer);
        if (!violation) {
            // add piece
            for (Coordinate coord : blocks) {
                grid[coord.getX()][coord.getY()]--; 
            }
            PuzzleSolver.availableShapes.remove(pentomino.getShape());
            return true;
        } else {
            return false;
        }
    }

    public boolean isViolation (Coordinate[] blocks, int layer) {
        for (Coordinate coord : blocks) {
            int x = coord.getX();
            int y = coord.getY();
            if (x < 0 || y < 0 || x >= grid.length || y >= grid[0].length) {
                System.out.println("Out of grid!");
                return true;
            }
            if (boardTiles[x][y].getPentomino() != null) {
                if (boardTiles[x][y].getLayer() == layer) {
                    System.out.println("Space is already occupied!");
                    return true;
                }
            }
        }
        int[][] gridClone = grid.clone();
        for (Coordinate coord : blocks) {
            gridClone[coord.getX()][coord.getY()]--; 
        }
        if (!checkAllSpaces(gridClone)) {
            return true;
        }
        return false;
    }

    public boolean outOfBounds (Coordinate coord) {
        int x = coord.getX();
        int y = coord.getY();
        if (x < 0 || y < 0 || x >= grid.length || y >= grid[0].length) {
            return true;
        }
        return false;
    }

    // return false if a space cannot be filled
    // i.e. if there are less than 5 transitively contiguous neighbour spaces
    public boolean checkAllSpaces (int[][] testGrid) {
        ArrayList<Coordinate> validated = new ArrayList<Coordinate>();
        for (int x = 0; x < testGrid.length; x++) {
            int count = 0;
            for (int y = 0; y < testGrid[x].length; y++) {
                boolean foundEnough = false;
                Coordinate newCoord = new Coordinate(x, y);
                if (!validated.contains(newCoord) && testGrid[x][y] != 0) {
                    ArrayList<Coordinate> queue = new ArrayList<Coordinate>();
                    ArrayList<Coordinate> visited = new ArrayList<Coordinate>();
                    queue.add(new Coordinate(x,y));
                    
                    while (queue.size() > 0) {
                        count += 1;
                        if (count >= 5) {
                            validated.addAll(visited);
                            foundEnough = true;
                            break;
                        }
                        Coordinate coord = queue.remove(0);
                        visited.add(coord);
                        // add each neighbour to the queue
                        Coordinate[] neighbours = new Coordinate[]{
                            new Coordinate(x + 1, y),
                            new Coordinate(x - 1, y),
                            new Coordinate(x, y + 1),
                            new Coordinate(x, y - 1)
                        };
                        for (Coordinate neighbour : neighbours) {
                            if (!outOfBounds(neighbour) && !visited.contains(neighbour)) {
                                if (testGrid[neighbour.getX()][neighbour.getY()] != 0) {
                                    queue.add(neighbour);
                                }
                            }
                        }
                    }

                    if (!foundEnough) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // this does not work properly
    public void setDisplayBoard () {
        if (grid == null || grid.length == 0) {
            System.out.println("Nothing to display!");
            return;
        }
        boardTiles = new BoardTile[grid.length][];
        for (int i = 0; i < boardTiles.length; i++) {
            boardTiles[i] = new BoardTile[grid[i].length];
            for (int j = 0; j < boardTiles[i].length; j++) {
                boardTiles[i][j] = new BoardTile(i, j, 0);
            }
        }
    }

    public BoardTile[][] getBoardTiles () {
        return boardTiles;
    }

    public String toString () {
        String returnString = "";
        for (int x = 0; x < boardTiles.length; x++) {
            for (int y = 0; y < boardTiles[x].length; y++) {
                returnString += boardTiles[x][y] + " ";
            }
            returnString += "\n";
        }
        return returnString;
    }
    
}
