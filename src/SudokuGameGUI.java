import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SudokuGameGUI extends JFrame{

    private class CustomRenderer extends DefaultTableCellRenderer{
        private List<Point> emptyCells = new ArrayList<>();

        private CustomRenderer(){
            setHorizontalAlignment(JLabel.CENTER);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            Point p = new Point(row, column);

            if(value.equals("") || emptyCells.contains(p)){
                comp.setForeground(Color.RED);
                if(!emptyCells.contains(p)){
                    emptyCells.add(p);
                }
            }
            else
                comp.setForeground(Color.BLACK);
            return comp;
        }
    }

    private class customCellEditor extends DefaultCellEditor{

        public customCellEditor(JTextField textField) {
            super(textField);
        }
        private List<Point> editableCells = new ArrayList<>();
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            JTextField textField = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
            Point p = new Point(row, column);

            if (value == null || value.equals("") || editableCells.contains(p)) {
                textField.setEditable(true);
                textField.setForeground(Color.RED);
                if (!editableCells.contains(p)) {
                    editableCells.add(p);
                }
            } else {
                textField.setEditable(false);
            }

            // Center-align the text
            textField.setHorizontalAlignment(JLabel.CENTER);

            return textField;
        }
    }
    private int[][] puzzle;
    private JPanel gamePanel;
    private JTable block1;
    private JButton backtrackButton;
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

    public SudokuGameGUI() throws NoSuchMethodException {
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
        Border border = new LineBorder(Color.BLACK, 1, true);
        for (JTable block : blocks) {
            block.setBorder(border);
            block.setRowHeight(40);
            block.setGridColor(Color.BLACK);
            TableColumnModel collumModel = block.getColumnModel();
            for (int j = 0; j < 3; j++) {
                collumModel.getColumn(j).setPreferredWidth(40);
                collumModel.getColumn(j).setCellRenderer(new CustomRenderer());
                collumModel.getColumn(j).setCellEditor(new customCellEditor(new JTextField()));
            }
        }
    }
    private void fillTable(int[][] sudoku){
        for(int i = 0; i < blocks.length; i++){
            int blockRow = (i / 3) * 3;
            String[][] temp = new String[3][3];
            for(int j = 0; j < 3; j++){

                int blockCol = (i % 3) * 3;
                for(int k = 0; k < 3; k++){
                    if(sudoku[blockRow][blockCol] == 0)
                        temp[j][k] = "";
                    else
                        temp[j][k] = (sudoku[blockRow][blockCol] + "");
                    blockCol++;
                }
                blockRow++;
            }
            blocks[i].setModel(new DefaultTableModel(temp,temp[0]));
        }
    }
    public void enableInput(boolean inputAllowed){
        for(JTable block : blocks){
            block.setEnabled(inputAllowed);
            block.setCellSelectionEnabled(inputAllowed);
        }
    }
    public JButton getRefreshButton(){
        return refreshButton;
    }

    public JButton getBacktrackButton() {
        return backtrackButton;
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
