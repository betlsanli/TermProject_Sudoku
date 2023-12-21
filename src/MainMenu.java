import javax.swing.*;

public class MainMenu extends JFrame {

    private JPanel mainPanel;
    private JButton easyButton;
    private JButton mediumButton;
    private JButton hardButton;

    public MainMenu(){
        setSize(400,250);
        setTitle("Sudoku Solver");
        setResizable(false);
        add(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public JButton getEasyButton() {
        return easyButton;
    }

    public JButton getMediumButton() {
        return mediumButton;
    }

    public JButton getHardButton() {
        return hardButton;
    }
}
