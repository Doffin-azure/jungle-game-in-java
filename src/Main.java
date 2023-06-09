import controller.*;
import model.Chessboard;
import view.FirstFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FirstFrame mainFrame = new FirstFrame();
            GameController gameController = new GameController(mainFrame.getChessBoardFrame().getChessboardComponent(), new Chessboard());
            mainFrame.setVisible(true);
            mainFrame.getChessBoardFrame().setGameController(gameController);
            gameController.setGameFrame(mainFrame.getChessBoardFrame());
            AI ai = new AI(gameController, gameController.getModel(), mainFrame.getChessBoardFrame().getChessboardComponent());
            gameController.setAi(ai);
        });
    }
}
