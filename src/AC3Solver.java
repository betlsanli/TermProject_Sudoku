import java.util.*;

public class AC3Solver {

    private static class Variable{
        private final int row;
        private final int col;
        ArrayList<Integer> domain;

        private Variable(int[][] sudoku,int row, int col){
            this.row = row;
            this.col = col;

            setDomain(sudoku);
        }
        private void setDomain(int[][] sudoku){
            ArrayList<Integer> domain = new ArrayList<>();
            for(int i = 1; i <= sudoku.length; i++){
                if(isValid(sudoku,row,col,i)){
                    domain.add(i);
                }
            }
            this.domain = domain;
        }
        private ArrayList<Integer> getDomain(){
            return domain;
        }
        private boolean removeFromDomain(int num){
            return domain.remove(Integer.valueOf(num));
        }
    } //End of Variable class

    public static int[][] solve(int[][] sudoku){

        int[][] solution = new int[sudoku.length][sudoku[0].length];
        for(int i = 0; i < solution.length; i++){
            for(int j = 0; j < solution[0].length; j++){
                solution[i][j] = sudoku[i][j];
            }
        }

        ArrayList<Variable> variables = new ArrayList<>();
        for(int i = 0; i < sudoku.length; i++){
            for(int j = 0; j < sudoku[0].length; j++){
                if(sudoku[i][j] == 0){
                    variables.add(new Variable(sudoku,i,j));
                }
            }
        }
        if(variables.isEmpty()) //No empty box in the sudoku
            return sudoku;

        if(!AC3(variables)){ //Sudoku is unsolvable
            return sudoku;
        }
        if(solveSudoku(solution,variables,0)){
            return solution;
        }
        return sudoku;
    }
    private static boolean solveSudoku(int[][] sudoku, ArrayList<Variable> variables, int index){
        if(index == variables.size())
            return true;
        Variable variable = variables.get(index);
        for(int value : variable.getDomain()){
            if(isValid(sudoku,variable.row,variable.col,value)){
                sudoku[variable.row][variable.col] = value;
                if(solveSudoku(sudoku,variables,index+1))
                    return true;
                sudoku[variable.row][variable.col] = 0;
            }
        }
        return false;
    }
    private static boolean AC3(ArrayList<Variable> variables){

        List<Variable[]> queue = new LinkedList<>();

        for(Variable from : variables){ //Create arcs between variables
            for(Variable to : variables){
                if(areNeighbours(from, to))
                    queue.add(new Variable[]{from,to});
            }
        }

        while(!queue.isEmpty()){
            Variable[] arc = queue.remove(0);
            if(arc[0].getDomain().size() == 0)
                return false;
            if(revise(arc[0], arc[1])){
                if(arc[0].getDomain().size() == 0)
                    return false;
                for(Variable var : variables){
                    if(areNeighbours(arc[0], var))
                        queue.add(new Variable[]{var,arc[0]});
                }
            }
        }
        return true;
    }
    private static boolean areNeighbours(Variable from, Variable to){
        if(from != to){
            if(from.row == to.row)
                return true;
            if(from.col == to.col)
                return true;
            if( ((from.row/3) * 3) == ((to.row/3) * 3) && ((from.col/3) * 3) == ((to.col/3) * 3) )
                return true;
        }
        return false;
    }
    private static boolean revise(Variable x, Variable y){
        boolean revised = false;
        boolean flag = false;
        for(int i = 0; i < x.domain.size(); i++){
            for(int j : y.domain){
                if(x.domain.get(i) != j){
                    flag = true;
                    break;
                }
            }
            if(!flag){
                x.removeFromDomain(x.domain.get(i));
                revised = true;
            }
        }
        return revised;
    }
    private static boolean isValid(int[][] sudoku, int row, int col, int num) {
        return !usedInRow(sudoku, row, num) &&
                !usedInColumn(sudoku, col, num) &&
                !usedInBox(sudoku, row - row % 3, col - col % 3, num);
    }
    private static boolean usedInRow(int[][] sudoku, int row, int num) {
        for (int col = 0; col < sudoku.length; col++) {
            if (sudoku[row][col] == num) {
                return true;
            }
        }
        return false;
    }
    private static boolean usedInColumn(int[][] sudoku, int col, int num) {
        for (int row = 0; row < sudoku[0].length; row++) {
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
