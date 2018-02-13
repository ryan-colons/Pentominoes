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
        boardTiles = new BoardTile[grid.length][grid[0].length];
        System.out.println(this);
    }

    public Puzzle(int[][] cloneGrid, BoardTile[][] cloneTiles) {
        grid = cloneGrid;
        boardTiles = cloneTiles;
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

    public Puzzle getClone () {
        Puzzle clonePuzzle = new Puzzle(grid, boardTiles);
        return clonePuzzle;
    }

    public boolean finishedAtLayer (int layer) {
        //System.out.println("Check if finished:");
        //System.out.println(this);
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                if (grid[x][y] == layer) {
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
    public boolean addPiece (Pentomino pentomino, int x, int y, int layer, ArrayList<PentominoShape> remainingShapes) {
        Coordinate[] blocks = pentomino.determineBlocks(x, y);
        boolean violation = isViolation(blocks, layer, remainingShapes);
        if (!violation) {
            // add piece
            //System.out.println("We will now add a piece...");
            //System.out.println(this);
            for (Coordinate coord : blocks) {
                grid[coord.getX()][coord.getY()]--;
                boardTiles[coord.getX()][coord.getY()] = new BoardTile(x, y, layer, pentomino.getShape());
            }
            //System.out.println("We have added a piece!");
            //System.out.println(this);
            return true;
        } else {
            return false;
        }
    }

    public boolean isViolation (Coordinate[] blocks, int layer, ArrayList<PentominoShape> remainingShapes) {
        for (Coordinate coord : blocks) {
            int x = coord.getX();
            int y = coord.getY();
            if (x < 0 || y < 0 || x >= grid.length || y >= grid[0].length) {
                //System.out.println("Out of grid!");
                return true;
            }
            if (grid[x][y] == 0) {
                //System.out.println("Nothing should be placed here!");
                return true;
            }
            if (boardTiles[x][y] != null) {
                if (boardTiles[x][y].getPentomino() != null) {
                    if (boardTiles[x][y].getLayer() == layer) {
                        //System.out.println("Space is already occupied!");
                        return true;
                    }
                }
            }
        }
        /*if (!touchesRelevantSquare(blocks, layer)) {
            System.out.println("Doesn't touch a relevant square!");
            return true;
        }*/
        int[][] gridClone = getGridClone();
        for (Coordinate coord : blocks) {
            gridClone[coord.getX()][coord.getY()]--; 
        }
        if (!checkLayer1(gridClone, remainingShapes)) {
            //System.out.println("Not enough (or too many) contiguous spaces!");
            return true;
        }
        System.out.println("No violations!");
        return false;
    }

    public boolean touchesRelevantSquare (Coordinate[] blocks, int layer) {
        for (Coordinate coord : blocks) {
            //System.out.println("Coord: " + coord + ", Value: " + grid[coord.getX()][coord.getY()]);
            //System.out.println("Layer: " + layer);
            if (grid[coord.getX()][coord.getY()] == layer) {
                //System.out.println("TOUCHES RELEVANT SQUARE");
                return true;
            }
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

    // return true if layer 1 is still solvable
    public boolean checkLayer1 (int[][] testGrid, ArrayList<PentominoShape> remainingShapes) {
        ArrayList<Coordinate> validated = new ArrayList<Coordinate>();
        for (int x = 0; x < testGrid.length; x++) {
            for (int y = 0; y < testGrid[x].length; y++) {
                int count = 0;
                Coordinate newCoord = new Coordinate(x, y);
                if (!validated.contains(newCoord) && testGrid[x][y] != 0) {
                    ArrayList<Coordinate> queue = new ArrayList<Coordinate>();
                    ArrayList<Coordinate> visited = new ArrayList<Coordinate>();
                    queue.add(new Coordinate(x,y));
                    
                    while (queue.size() > 0) {
                        
                        count += 1;
                        
                        Coordinate coord = queue.remove(0);
                        
                        visited.add(coord);
                        Coordinate[] neighbours = new Coordinate[]{
                            new Coordinate(coord.getX() + 1, coord.getY()),
                            new Coordinate(coord.getX() - 1, coord.getY()),
                            new Coordinate(coord.getX(), coord.getY() + 1),
                            new Coordinate(coord.getX(), coord.getY() - 1)
                        };
                        for (Coordinate neighbour : neighbours) {
                            if (!outOfBounds(neighbour) && !visited.contains(neighbour) && !queue.contains(neighbour)) {
                                if (testGrid[neighbour.getX()][neighbour.getY()] != 0) {
                                    queue.add(neighbour);
                                }
                            }
                        }
                    }
                    if (count % 5 == 0) {
                        validated.addAll(visited);
                        if (count == 5) {
                            boolean foundShape = false;
                            for (PentominoShape shape : remainingShapes) {
                                for (Pentomino pentomino : PuzzleSolver.getUniqueForms(shape)) {
                                    if (Coordinate.compareArrangement(visited, pentomino.getOffsets())) {
                                        foundShape = true;
                                    }
                                }
                            }
                            if (!foundShape) {
                                System.out.println("Couldn't find a pentomino to fill the spaces!");
                                return false;
                            }
                        }
                    }else{
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    // return false if a space cannot be filled
    // i.e. if there are less than 5 transitively contiguous neighbour spaces
    public boolean checkAllSpaces (int[][] testGrid) {
        ArrayList<Coordinate> validated = new ArrayList<Coordinate>();
        for (int x = 0; x < testGrid.length; x++) {
            for (int y = 0; y < testGrid[x].length; y++) {
                int count = 0;
                boolean foundEnough = false;
                Coordinate newCoord = new Coordinate(x, y);
                if (!validated.contains(newCoord) && testGrid[x][y] != 0) {
                    ArrayList<Coordinate> queue = new ArrayList<Coordinate>();
                    ArrayList<Coordinate> visited = new ArrayList<Coordinate>();
                    queue.add(new Coordinate(x,y));
                    
                    while (queue.size() > 0) {
                        
                        count += 1;
                        
                        if (count >= 5) {
                            //System.out.println("Found 5 ... count = " + count);
                            validated.addAll(visited);
                            foundEnough = true;
                            break;
                        }
                        
                        Coordinate coord = queue.remove(0);
                        
                        //System.out.println(coord + "| " + queue);
                        //System.out.println("     Visited: " + visited);
                        
                        visited.add(coord);
                        Coordinate[] neighbours = new Coordinate[]{
                            new Coordinate(coord.getX() + 1, coord.getY()),
                            new Coordinate(coord.getX() - 1, coord.getY()),
                            new Coordinate(coord.getX(), coord.getY() + 1),
                            new Coordinate(coord.getX(), coord.getY() - 1)
                        };
                        for (Coordinate neighbour : neighbours) {
                            if (!outOfBounds(neighbour) && !visited.contains(neighbour) && !queue.contains(neighbour)) {
                                if (testGrid[neighbour.getX()][neighbour.getY()] != 0) {
                                    queue.add(neighbour);
                                    //System.out.println("     Adding: " + neighbour);
                                }
                            }
                        }
                    }
                    if (!foundEnough) {
                        //System.out.println("All spaces checked: FAILED! Bad board below:");
                        //Puzzle to_print = new Puzzle(testGrid, new BoardTile[0][0]);
                        //System.out.println(to_print);
                        return false;
                    }
                }
            }
        }
        //System.out.println("All spaces checked: PASSED! Good board below:");
        //Puzzle to_print = new Puzzle(testGrid, new BoardTile[0][0]);
        //System.out.println(to_print);
        return true;
    }

    public BoardTile[][] getBoardTiles () {
        return boardTiles;
    }

    public String toString () {
        String returnString = "";
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                returnString += grid[x][y] + " ";
            }
            returnString += "\n";
        }
        return returnString;
    }
    
}
