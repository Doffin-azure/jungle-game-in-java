import controller.GameController;
import model.ChessPiece;
import model.Chessboard;
import model.ChessboardPoint;
import model.PlayerColor;
import model.SharedData;
import view.AnimalChessComponent;
import view.ChessboardComponent;

import javax.swing.JButton;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class JungleSmoke {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(JungleSmoke::playFirstMove);
    }

    private static void playFirstMove() {
        Chessboard board = new Chessboard();
        ChessboardComponent view = new ChessboardComponent(60, new JButton(), new JButton());
        GameController controller = new GameController(view, board);

        ChessboardPoint source = new ChessboardPoint(2, 0);
        ChessboardPoint destination = new ChessboardPoint(3, 0);
        ChessPiece piece = board.getChessPieceAt(source);
        AnimalChessComponent token = (AnimalChessComponent) view.gridComponents[2][0].getComponent(0);

        BufferedImage image = new BufferedImage(60, 60, BufferedImage.TYPE_INT_RGB);
        view.gridComponents[2][0].doLayout();
        Graphics2D graphics = image.createGraphics();
        token.paint(graphics);
        graphics.dispose();
        if (image.getRGB(10, 10) != Color.BLUE.getRGB()) {
            throw new AssertionError("The blue piece did not render");
        }

        controller.onPlayerClickCell(destination, view.gridComponents[3][0]);
        if (board.getChessPieceAt(source) != piece || controller.getCount() != 1
                || controller.getCurrentPlayer() != PlayerColor.BLUE || !SharedData.stepList.isEmpty()) {
            throw new AssertionError("An empty click changed the game before a piece was selected");
        }

        controller.onPlayerClickChessPiece(source, token);
        controller.onPlayerClickCell(destination, view.gridComponents[3][0]);
        if (board.getChessPieceAt(source) != null || board.getChessPieceAt(destination) != piece
                || controller.getCount() != 2 || controller.getCurrentPlayer() != PlayerColor.RED
                || SharedData.stepList.size() != 1
                || view.gridComponents[3][0].getComponent(0) != token) {
            throw new AssertionError("The first move did not update the board, view, and turn");
        }
        System.out.println("Jungle smoke check passed");
    }
}
