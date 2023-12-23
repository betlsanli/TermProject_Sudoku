import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameManager {
    private static MainMenu mainMenu;
    private static SudokuGameGUI game;

    public static void setupGame(){
        mainMenu = new MainMenu();
        game = new SudokuGameGUI();
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Menu buttons
                if(e.getSource() == mainMenu.getEasyButton() || e.getSource() == mainMenu.getMediumButton() || e.getSource() == mainMenu.getHardButton()){
                    int[][] sudoku;
                    if(e.getSource() == mainMenu.getEasyButton()){ //Easy level
                        sudoku = SudokuGenerator.generateSudoku(Level.EASY);
                    }
                    else if(e.getSource() == mainMenu.getMediumButton()){ //Medium level
                        sudoku = SudokuGenerator.generateSudoku(Level.MEDIUM);
                    }
                    else{ //Hard leve
                        sudoku = SudokuGenerator.generateSudoku(Level.HARD);
                    }
                    initGame(sudoku);
                }
                else{ // Game buttons
                    if(e.getSource() == game.getBacktrackButton()){ //Backtrack solver
                        game.getBacktrackButton().setEnabled(false);
                        game.getSimulatedButton().setEnabled(false);
                        game.getAc3Button().setEnabled(false);
                        int[][] sudoku = BacktrackSolver.solve(game.getPuzzle());
                        game.setupTable(sudoku);
                    }
                    else if(e.getSource() == game.getSimulatedButton()){ //Simulated annealing solver
                        game.getBacktrackButton().setEnabled(false);
                        game.getSimulatedButton().setEnabled(false);
                        game.getAc3Button().setEnabled(false);
                        int[][] sudoku = SimulatedAnnealingSolver.solveSudoku(game.getPuzzle());
                        game.setupTable(sudoku);
                    }
                    else if(e.getSource() == game.getAc3Button()){ //AC3 Solver
                        game.getBacktrackButton().setEnabled(false);
                        game.getSimulatedButton().setEnabled(false);
                        game.getAc3Button().setEnabled(false);
                        int[][] sudoku = AC3Solver.solve(game.getPuzzle());
                        game.setupTable(sudoku);
                    }
                    else if(e.getSource() == game.getRefreshButton()){ //Refresh puzzle
                        game.getBacktrackButton().setEnabled(true);
                        game.getSimulatedButton().setEnabled(true);
                        game.getAc3Button().setEnabled(true);
                        game.setupTable(game.getPuzzle());
                    }
                    else{ //Back
                        game.getBacktrackButton().setEnabled(true);
                        game.getSimulatedButton().setEnabled(true);
                        game.getAc3Button().setEnabled(true);
                        game.dispose();
                        mainMenu.setVisible(true);
                    }
                }
            }
        };

        mainMenu.getEasyButton().addActionListener(actionListener);
        mainMenu.getMediumButton().addActionListener(actionListener);
        mainMenu.getHardButton().addActionListener(actionListener);
        game.getBacktrackButton().addActionListener(actionListener);
        game.getBackButton().addActionListener(actionListener);
        game.getSimulatedButton().addActionListener(actionListener);
        game.getRefreshButton().addActionListener(actionListener);
        game.getAc3Button().addActionListener(actionListener);
        mainMenu.setVisible(true);
    }

    private static void initGame(int[][] sudoku){ //Initializes game with the given puzzle
        mainMenu.setVisible(false);
        game.setPuzzle(sudoku);
        game.setupTable(sudoku);
        game.setVisible(true);
    }
}
