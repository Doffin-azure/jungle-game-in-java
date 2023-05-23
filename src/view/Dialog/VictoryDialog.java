package view.Dialog;

import model.PlayerColor;

import javax.swing.*;

public class VictoryDialog extends JFrame {
    public static ImageIcon icon;

    public VictoryDialog() {
        super("Victory");



        setLocationRelativeTo(null);
        setResizable(false);
        icon = new ImageIcon("resource/Victory/victory.jfif");
//        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    public static void displayWinning(PlayerColor winnerColor,VictoryDialog victoryDialog) {SwingUtilities.invokeLater(() -> {
            if(winnerColor == PlayerColor.RED)
                JOptionPane.showMessageDialog(victoryDialog, "GAME\nOVER", "The Player RED have win the game!!! ", JOptionPane.INFORMATION_MESSAGE, icon);
            else
                JOptionPane.showMessageDialog(victoryDialog, "GAME\nOVER", "The Player BLUE have win the game!!! ", JOptionPane.INFORMATION_MESSAGE, icon);
            new VictoryDialog().setVisible(true);
        });
    }
}