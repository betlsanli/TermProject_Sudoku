public class BruteForceSolver {

    public static int[][] solve(int[][] sudoku){ // Creates a copy, does not change the original puzzle
        int[][] solution = new int[sudoku.length][sudoku[0].length];
        for(int i = 0; i < solution.length; i++){
            for(int j = 0; j < solution[0].length; j++){
                solution[i][j] = sudoku[i][j];
            }
        }
        solveSudoku(solution);
        return solution;
    }

    private static boolean solveSudoku(int[][] sudoku) {
        int[] emptyCell = findEmptyCell(sudoku);

        if (emptyCell == null) {
            return true; // Sudoku solved
        }

        int row = emptyCell[0];
        int col = emptyCell[1];

        for (int num = 1; num <= 9; num++) {
            if (isValidMove(sudoku, row, col, num)) {
                sudoku[row][col] = num;

                if (solveSudoku(sudoku)) {
                    return true; // Continue with the next empty cell
                }

                sudoku[row][col] = 0; // Backtrack if the current configuration doesn't lead to a solution
            }
        }

        return false; // No valid number found for the current cell
    }

    private static int[] findEmptyCell(int[][] sudoku) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return null; // No empty cell found
    }

    private static boolean isValidMove(int[][] sudoku, int row, int col, int num) {
        return !usedInRow(sudoku, row, num) &&
                !usedInColumn(sudoku, col, num) &&
                !usedInBox(sudoku, row - row % 3, col - col % 3, num);
    }

    private static boolean usedInRow(int[][] sudoku, int row, int num) {
        for (int col = 0; col < 9; col++) {
            if (sudoku[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    private static boolean usedInColumn(int[][] sudoku, int col, int num) {
        for (int row = 0; row < 9; row++) {
            if (sudoku[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    private static boolean usedInBox(int[][] sudoku, int boxStartRow, int boxStartCol, int num) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (sudoku[boxStartRow + i][boxStartCol + j] == num) {
                    return true;
                }
            }
        }
        return false;
    }
}
