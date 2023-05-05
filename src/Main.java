import controller.GameController;
import model.Chessboard;
import view.FirstFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FirstFrame mainFrame = new FirstFrame();
            GameController gameController = new GameController(mainFrame.getChessBoardFrame().getChessboardComponent(), new Chessboard());
            mainFrame.setVisible(true);

        });
    }
}
