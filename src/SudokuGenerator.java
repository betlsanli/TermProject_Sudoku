import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SudokuGenerator {

    public static int[][] generateSudoku(Level level) {
        int[][] sudoku = new int[9][9];

        // Fill the diagonal blocks
        fillDiagonalBlocks(sudoku);

        // Solve the Sudoku
        BruteForceSolver.solveSudoku(sudoku);

        // Remove some numbers to create the puzzle
        removeNumbers(sudoku, level.level); // You can adjust the difficulty level by changing the second parameter

        return sudoku;
    }
    private static void fillDiagonalBlocks(int[][] sudoku) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Collections.shuffle(numbers);

        for (int i = 0; i < 9; i += 3) {
            fillBlock(sudoku, i, i, numbers);
        }
    }
    private static void fillBlock(int[][] sudoku, int startRow, int startCol, List<Integer> numbers) {
        int n = 0;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                sudoku[i][j] = numbers.get(n++);
            }
        }
    }
    private static void removeNumbers(int[][] sudoku, int numberOfCellsToRemove) {

        Random rand = new Random();
        while(numberOfCellsToRemove != 0){
            int row = rand.nextInt(0,9);
            int col = rand.nextInt(0,9);
            if(sudoku[row][col] != 0){
                sudoku[row][col] = 0;
                numberOfCellsToRemove--;
            }
        }
    }
}
