public class Main {
    public static void main(String[] args) {
        int[][] sudoku = SudokuGenerator.generateSudoku(Level.HARD);
        System.out.println("Sudoku");
        printSudoku(sudoku);
        BruteForceSolver.solveSudoku(sudoku);
        System.out.println("Solution");
        printSudoku(sudoku);
    }
    private static void printSudoku(int[][] sudoku) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(sudoku[i][j] + " ");
            }
            System.out.println();
        }
    }
}