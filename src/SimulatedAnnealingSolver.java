import java.util.*;

public class SimulatedAnnealingSolver {
    int[][] solution = new int[9][9];

    public static void printSudoku(int[][] sudokuP){
        System.out.println();
        for(int i = 0; i < 9; i++){
            String line = "";
            if(i==3||i==6){
                line += ("----------------------\n");
            }
            for(int j = 0; j <= 8; j++){
                if(j==3||j==6){
                    line += (" | ");
                }
                line += sudokuP[i][j]+" ";
            }
            System.out.println(line);
        }
    }

    private static void markFixedValues(int[][] fixedSudoku) {
        for (int i = 0; i < fixedSudoku.length; i++) {
            for (int j = 0; j < fixedSudoku[0].length; j++) {
                // If the value in the cell is not 0, set it to 1 in fixedSudoku
                if (fixedSudoku[i][j] != 0) {
                    fixedSudoku[i][j] = 1;
                }
            }
        }
    }

    public static int calculateErrorNumber(int[][] sudoku){
        int numberOfErrors = 0;
        for(int i = 0; i < 9; i++){
            numberOfErrors += calculateErrorRowCol(i,i,sudoku);
        }
        return numberOfErrors;
    }

    public static int calculateErrorRowCol(int row, int column, int[][] sudoku) {
        int numberOfErrorsInColumn = 9 - countUniqueCol(sudoku, column);
        int numberOfErrorsInRow = 9 - countUniqueRow(sudoku, row);

        return numberOfErrorsInColumn + numberOfErrorsInRow;
    }

    public static int countUniqueCol(int[][] sudoku, int column) {
        Set<Integer> uniqueValues = new HashSet<>();
        for (int i = 0; i < 9; i++) {
            uniqueValues.add(sudoku[i][column]);
        }
        return uniqueValues.size();
    }

    public static int countUniqueRow(int[][] sudoku, int row) {
        Set<Integer> uniqueValues = new HashSet<>();
        for (int i = 0; i < 9; i++) {
            uniqueValues.add(sudoku[row][i]);
        }
        return uniqueValues.size();
    }

    public static List<List<int[]>> createList3x3Blocks() {
        List<List<int[]>> listOfBlocks = new ArrayList<>();

        for (int r = 0; r < 9; r++) {
            List<int[]> block = new ArrayList<>();

            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {
                    int row = 3 * (r / 3) + x;
                    int col = 3 * (r % 3) + y;
                    block.add(new int[]{row, col});
                }
            }

            listOfBlocks.add(block);
        }

        return listOfBlocks;
    }

    public static int[][] fill3x3Blocks(int[][] sudoku) {
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(sudoku[i][j] == 0){
                    int boxStartRow = (i/3) * 3;
                    int boxStartCol = (j/3) * 3;
                    for(int k = 1; k < 10; k++){
                        if(!usedInBox(sudoku, boxStartRow,boxStartCol,k)){
                            sudoku[i][j] = k;
                            break;
                        }
                    }
                }
            }
        }
        return sudoku;
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

    public static int sumOfOneBlock(int[][] sudoku, List<int[]> oneBlock) {
        int sum = 0;

        for (int[] cell : oneBlock) {
            int row = cell[0];
            int col = cell[1];
            sum += sudoku[row][col];
        }

        return sum;
    }
    public static int[][] twoRandomBoxesWithinBlock(int[][] fixedSudoku, List<int[]> block) {
        Random random = new Random();

        int[][] boxesToFlip = new int[2][2];

        while (true) {
            int[] firstBox = block.get(random.nextInt(block.size()));
            int[] secondBox = block.get(random.nextInt(block.size()));

            if(firstBox == secondBox){
                continue;
            }

            if (fixedSudoku[firstBox[0]][firstBox[1]] != 1 && fixedSudoku[secondBox[0]][secondBox[1]] != 1) {
                boxesToFlip[0] = firstBox;
                boxesToFlip[1] = secondBox;
                break;
            }
        }

        return boxesToFlip;
    }
    public static int[][] flipBoxes(int[][] sudoku, int[][] boxesToFlip) {
        // Create a deep copy of the current Sudoku grid
        int[][] proposedSudoku = new int[sudoku.length][sudoku[0].length];
        for (int i = 0; i < sudoku.length; i++) {
            System.arraycopy(sudoku[i], 0, proposedSudoku[i], 0, sudoku[i].length);
        }

        // Swap the values of the two specified boxes
        int row1 = boxesToFlip[0][0];
        int col1 = boxesToFlip[0][1];
        int row2 = boxesToFlip[1][0];
        int col2 = boxesToFlip[1][1];

        int temp = proposedSudoku[row1][col1];
        proposedSudoku[row1][col1] = proposedSudoku[row2][col2];
        proposedSudoku[row2][col2] = temp;

        return proposedSudoku;
    }
    public static List<Object> proposedState(int[][] sudoku, int[][] fixedSudoku, List<List<int[]>> listOfBlocks) {
        Random random = new Random();
        // Randomly select a 3x3 block
        List<int[]> randomBlock = listOfBlocks.get(random.nextInt(0,9));

        // Check the sum of values in the selected block
        int sumInBlock = sumOfOneBlock(fixedSudoku, randomBlock);

        // If the sum is greater than 6, return the current state with a cost difference of 1
        if (sumInBlock > 6) {
            return Arrays.asList(sudoku, new int[2][2], 0);
        }

        // If the sum is 6 or less, get two random boxes within the block
        int[][] boxesToFlip = twoRandomBoxesWithinBlock(fixedSudoku, randomBlock);

        // Create a new state by flipping the values of the two selected boxes
        int[][] proposedSudoku = flipBoxes(sudoku, boxesToFlip);

        return Arrays.asList(proposedSudoku, boxesToFlip, 1);
    }
    public static List<Object> chooseNewState(int[][] currentSudoku, int[][] fixedSudoku, List<List<int[]>> listOfBlocks, double sigma) {
        // Get the proposed state and information about the boxes to check
        List<Object> proposal = proposedState(currentSudoku, fixedSudoku, listOfBlocks);
        int[][] newSudoku = (int[][]) proposal.get(0);
        int[][] boxesToCheck = (int[][]) proposal.get(1);

        int currentCost = calculateErrorRowCol(boxesToCheck[0][0], boxesToCheck[0][1], currentSudoku)
                + calculateErrorRowCol(boxesToCheck[1][0], boxesToCheck[1][1], currentSudoku);

        int newCost = calculateErrorRowCol(boxesToCheck[0][0], boxesToCheck[0][1], newSudoku)
                + calculateErrorRowCol(boxesToCheck[1][0], boxesToCheck[1][1], newSudoku);

        int costDifference = newCost - currentCost;

        // Calculate the acceptance probability
        double rho = Math.exp(-costDifference / sigma);

        // If the proposed state is accepted
        if (Math.random() < rho) {
            return Arrays.asList(newSudoku, boxesToCheck, costDifference);
        } else {
            // If not accepted, return the current state
            return Arrays.asList(currentSudoku, new int[2][2], 0);
        }
    }
    public static int chooseNumberOfIterations(int[][] fixedSudoku) {
        int numberOfIterations = 0;

        // Iterate through each cell in the Sudoku grid
        for (int i = 0; i < fixedSudoku.length; i++) {
            for (int j = 0; j < fixedSudoku[0].length; j++) {
                // If the value in the cell is not 0, increment numberOfIterations
                if (fixedSudoku[i][j] != 0) {
                    numberOfIterations++;
                }
            }
        }

        return numberOfIterations;
    }
    public static double calculateInitialSigma(int[][] sudoku, int[][] fixedSudoku, List<List<int[]>> listOfBlocks) {
        List<Integer> listOfDifferences = new ArrayList<>();

        // Iterate a specified number of times to collect differences in cost
        for (int i = 0; i < 10; i++) {
            int[][] proposedSudoku = (int[][]) proposedState(sudoku, fixedSudoku, listOfBlocks).get(0);
            listOfDifferences.add(calculateErrorNumber(proposedSudoku));
        }

        // Calculate the standard deviation of differences in cost
        return pstdev(listOfDifferences);
    }

    // Helper function to calculate the standard deviation of a list of integers
    public static double pstdev(List<Integer> values) {
        int n = values.size();
        if (n < 2) {
            throw new ArithmeticException("Population standard deviation requires at least two data points");
        }

        // Calculate the mean
        double mean = values.stream().mapToDouble(Integer::doubleValue).average().orElse(0);

        // Calculate the sum of squared differences from the mean
        double sumSquaredDifferences = values.stream().mapToDouble(value -> Math.pow(value - mean, 2)).sum();

        // Calculate the variance (average of squared differences)
        double variance = sumSquaredDifferences / n;

        // Return the square root of the variance (standard deviation)
        return Math.sqrt(variance);
    }

    public static int[][] solveSudoku(int[][] initialSudoku) {
        int[][] sudoku = new int[initialSudoku.length][initialSudoku[0].length];
        for(int i = 0; i < sudoku.length; i++){
            for(int j = 0; j < sudoku[0].length; j++){
                sudoku[i][j] = initialSudoku[i][j];
            }
        }
        // Initialize variables
        double decreaseFactor = 0.99;
        int stuckCount = 0;
        int[][] fixedSudoku = new int[sudoku.length][sudoku[0].length];
        for(int i = 0; i < fixedSudoku.length; i++){
            for(int j = 0; j < fixedSudoku[0].length; j++){
                fixedSudoku[i][j] = initialSudoku[i][j];
            }
        }

        // Mark fixed values in the Sudoku grid
        markFixedValues(fixedSudoku);

        // Create a list of 3x3 blocks
        List<List<int[]>> listOfBlocks = createList3x3Blocks();

        // Randomly fill 3x3 blocks with values in the Sudoku grid
        sudoku = fill3x3Blocks(sudoku);

        // Calculate the initial value of sigma
        double sigma = calculateInitialSigma(sudoku, fixedSudoku, listOfBlocks);

        // Calculate the initial number of errors (cost)
        int score = calculateErrorNumber(sudoku);

        // Determine the total number of iterations based on fixed values
        int iterations = chooseNumberOfIterations(fixedSudoku);

        // If the initial number of errors is 0, the Sudoku is already solved
        if (score <= 0) {
            return sudoku;
        }

        int limit = 1000;
        // Main simulated annealing loop
        while (limit > 0) {
            int previousScore = score;

            // For each iteration, attempt to choose a new state
            for (int i = 0; i < iterations; i++) {
                List<Object> newState = chooseNewState(sudoku, fixedSudoku, listOfBlocks, sigma);
                sudoku = (int[][]) newState.get(0);
                int scoreDiff = (int) newState.get(2);
                score += scoreDiff;

                /*System.out.println("Score: " + score);*/

                // If the number of errors becomes 0, the Sudoku is solved
                if (score <= 0) {
                    return sudoku;
                }
            }

            sigma *= decreaseFactor;

            if (score >= previousScore) {
                stuckCount += 1;
            } else {
                stuckCount = 0;
            }

            if (stuckCount > 80) {
                sigma += 2;
            }

            // If the current state has no errors, print and break out of the loop
            if (calculateErrorNumber(sudoku) == 0) {
                break;
            }
            limit--;
        }
        if(limit <= 0){
            return initialSudoku;
        }
        return sudoku;
    }

    public static void main(String[] args) {
        String startingSudoku =
                "024007000" +
                        "600000000" +
                        "003680415" +
                        "431005000" +
                        "500000032" +
                        "790000060" +
                        "209710800" +
                        "040093000" +
                        "310004750";

        int[][] sudoku = new int[9][9];
        int index = 0;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sudoku[i][j] = Character.getNumericValue(startingSudoku.charAt(index));
                index++;
            }
        }

        System.out.println("Sudoku Problem:");
        printSudoku(sudoku);

        int[][] solution = solveSudoku(sudoku);

        System.out.println("\nSolved Sudoku:");
        printSudoku(solution);
    }
}