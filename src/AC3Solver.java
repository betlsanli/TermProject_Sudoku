import java.util.LinkedList;
import java.util.Queue;

public class AC3Solver {
        private static final int SIZE = 9;

        public static int[][] solve(int[][] sudoku){
            int[][] solution = new int[sudoku.length][sudoku[0].length];
            for(int i = 0; i < solution.length; i++){
                for(int j = 0; j < solution[0].length; j++){
                    solution[i][j] = sudoku[i][j];
                }
            }
            solveSudoku(solution);
            return solution;
        }

        private static boolean solveSudoku(int[][] board) {
            if (!isConsistent(board)) {
                return false;
            }

            int[] emptyCell = findEmptyCell(board);
            if (emptyCell == null) {
                return true; // Puzzle is solved
            }

            int row = emptyCell[0];
            int col = emptyCell[1];

            for (int num = 1; num <= SIZE; num++) {
                if (isSafe(board, row, col, num)) {
                    board[row][col] = num;

                    if (solveSudoku(board)) {
                        return true; // Puzzle is solved
                    }

                    // If placing num at (row, col) doesn't lead to a solution, backtrack
                    board[row][col] = 0;
                }
            }

            return false; // No number can be placed at (row, col)
        }

    private static boolean isConsistent(int[][] board) {
        Queue<int[]> queue = new LinkedList<>();

        // Enqueue all arcs (cell pairs) initially
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != 0) {
                    enqueueArcs(queue, i, j);
                }
            }
        }

        while (!queue.isEmpty()) {
            int[] arc = queue.poll();
            int arcRow = arc[0];
            int arcCol = arc[1];

            // Try to revise the domain of the current cell
            if (revise(board, arcRow, arcCol)) {
                // If the domain is revised, enqueue arcs involving the current cell
                enqueueArcs(queue, arcRow, arcCol);

                // If any domain becomes empty, the puzzle is inconsistent
                if (isEmptyDomain(board, arcRow, arcCol)) {
                    return false;
                }
            }
        }

        return true;
    }

    private static void enqueueArcs(Queue<int[]> queue, int row, int col) {
        // Enqueue arcs related to the current cell (row, col)
        enqueueRowArcs(queue, row, col);
        enqueueColumnArcs(queue, row, col);
        enqueueBoxArcs(queue, row, col);
    }

    private static void enqueueRowArcs(Queue<int[]> queue, int row, int col) {
        // Enqueue all arcs involving the cells in the same row
        for (int c = 0; c < SIZE; c++) {
            if (c != col) {
                queue.add(new int[]{row, c});
            }
        }
    }

    private static void enqueueColumnArcs(Queue<int[]> queue, int row, int col) {
        // Enqueue all arcs involving the cells in the same column
        for (int r = 0; r < SIZE; r++) {
            if (r != row) {
                queue.add(new int[]{r, col});
            }
        }
    }

    private static void enqueueBoxArcs(Queue<int[]> queue, int row, int col) {
        // Enqueue all arcs involving the cells in the same 3x3 box
        int boxStartRow = row - row % 3;
        int boxStartCol = col - col % 3;
        for (int r = boxStartRow; r < boxStartRow + 3; r++) {
            for (int c = boxStartCol; c < boxStartCol + 3; c++) {
                if (r != row || c != col) {
                    queue.add(new int[]{r, c});
                }
            }
        }
    }

    private static boolean revise(int[][] board, int row, int col) {
        boolean revised = false;
        int domain = board[row][col];

        for (int c = 0; c < SIZE; c++) {
            if (c != col && board[row][c] == 0) {
                revised |= removeValueFromDomain(board, row, c, domain);
            }
        }

        for (int r = 0; r < SIZE; r++) {
            if (r != row && board[r][col] == 0) {
                revised |= removeValueFromDomain(board, r, col, domain);
            }
        }

        int boxStartRow = row - row % 3;
        int boxStartCol = col - col % 3;
        for (int r = boxStartRow; r < boxStartRow + 3; r++) {
            for (int c = boxStartCol; c < boxStartCol + 3; c++) {
                if ((r != row || c != col) && board[r][c] == 0) {
                    revised |= removeValueFromDomain(board, r, c, domain);
                }
            }
        }

        return revised;
    }


    private static boolean removeValueFromDomain(int[][] board, int row, int col, int value) {
        if (board[row][col] == value) {
            return false; // The value is already in the cell, cannot be removed
        }

        int oldValue = board[row][col];
        board[row][col] = 0; // Temporarily clear the cell

        if (!isSafe(board, row, col, oldValue)) {
            board[row][col] = oldValue; // Restore the original value if the removal makes the board inconsistent
            return false;
        }

        // Restore the original value if the removal doesn't violate the constraints
        board[row][col] = oldValue;
        return true;
    }

    private static boolean isEmptyDomain(int[][] board, int row, int col) {
        return board[row][col] == 0;
    }


    private static int[] findEmptyCell(int[][] board) {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == 0) {
                        return new int[]{i, j};
                    }
                }
            }
            return null; // No empty cell found
        }

        private static boolean isSafe(int[][] board, int row, int col, int num) {
            return !usedInRow(board, row, num) &&
                    !usedInColumn(board, col, num) &&
                    !usedInBox(board, row - row % 3, col - col % 3, num);
        }

        private static boolean usedInRow(int[][] board, int row, int num) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == num) {
                    return true;
                }
            }
            return false;
        }

        private static boolean usedInColumn(int[][] board, int col, int num) {
            for (int row = 0; row < SIZE; row++) {
                if (board[row][col] == num) {
                    return true;
                }
            }
            return false;
        }

        private static boolean usedInBox(int[][] board, int boxStartRow, int boxStartCol, int num) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[boxStartRow + i][boxStartCol + j] == num) {
                        return true;
                    }
                }
            }
            return false;
        }
}
