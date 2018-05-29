# Pentominoes
Pentomino Puzzle Solver

A program for solving pentomino puzzles, made for an assessment at Otago Uni (COSC326).
Puzzles can be input through stdin, as grids of 0s and 1s (indicating spaces that must be empty/filled respectively). The solver will find a way to use each pentomino exactly once to fill the required spaces.
The program can also solve multilayer puzzles, where a number N in the input indicates that a space must be filled N times, over M overlapping puzzle grids (where M is the highest value of N).
