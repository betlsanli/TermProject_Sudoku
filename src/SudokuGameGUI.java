import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class SudokuGameGUI extends JFrame{
    private int[][] puzzle;
    private JPanel gamePanel;
    private JTable block1;
    private JButton bruteButton;
    private JButton simulatedButton;
    private JButton backButton;
    private JTable block2;
    private JTable block3;
    private JPanel sudokuBlocks;
    private JTable block4;
    private JTable block5;
    private JTable block6;
    private JTable block7;
    private JTable block8;
    private JTable block9;
    private JButton refreshButton;
    private JButton ac3Button;
    JTable[] blocks = {block1,block2,block3,block4,block5,block6,block7,block8,block9};

    public SudokuGameGUI(){
        setSize(600,600);
        setTitle("Sudoku Solver");
        setResizable(false);
        add(gamePanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public int[][] getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(int[][] puzzle) {
        this.puzzle = puzzle;
    }

    public void setupTable(int[][] sudoku){
        fillTable(sudoku);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        Border border = new LineBorder(Color.BLACK, 1, true);
        for (JTable block : blocks) {
            block.setBorder(border);
            block.setRowHeight(40);
            block.setGridColor(Color.BLACK);
            TableColumnModel collumModel = block.getColumnModel();
            for (int j = 0; j < 3; j++) {
                collumModel.getColumn(j).setPreferredWidth(40);
                collumModel.getColumn(j).setCellRenderer(centerRenderer);
            }
        }
    }

    private void fillTable(int[][] sudoku){
        for(int i = 0; i < blocks.length; i++){
            int blockRow = (i / 3) * 3;
            Character[][] temp = new Character[3][3];
            for(int j = 0; j < 3; j++){

                int blockCol = (i % 3) * 3;
                for(int k = 0; k < 3; k++){
                    if(sudoku[blockRow][blockCol] == 0)
                        temp[j][k] = ' ';
                    else
                        temp[j][k] = (char)(sudoku[blockRow][blockCol] + '0');
                    blockCol++;
                }
                blockRow++;
            }
            blocks[i].setModel(new DefaultTableModel(temp,temp[0]));
        }
    }

    public JButton getRefreshButton(){
        return refreshButton;
    }

    public JButton getBruteButton() {
        return bruteButton;
    }

    public JButton getSimulatedButton() {
        return simulatedButton;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public JButton getAc3Button() {
        return ac3Button;
    }
}
