package view;

import javax.swing.*;

public class VictoryDialog extends JFrame {

    public VictoryDialog() {
        super("Victory");


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        ImageIcon icon = new ImageIcon("resource/Victory/victory.jfif");
        JOptionPane.showMessageDialog(this, "GAME\nOVER", "The Player A/B have win the game!!! ", JOptionPane.INFORMATION_MESSAGE, icon);
    }

    public static void displayWinning() {
        SwingUtilities.invokeLater(() -> {
            new VictoryDialog().setVisible(true);
        });
    }
}