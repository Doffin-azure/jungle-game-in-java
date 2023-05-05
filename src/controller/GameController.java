package controller;

import listener.GameListener;
import model.Constant;
import model.PlayerColor;
import model.Chessboard;
import model.ChessboardPoint;
import view.CellComponent;
import view.AnimalChessComponent;
import view.ChessboardComponent;
import view.VictoryDialog;

/**
 * Controller is the connection between model and view,
 * when a Controller receive a request from a view, the Controller
 * analyzes and then hands over to the model for processing
 * [in this demo the request methods are onPlayerClickCell() and
 * onPlayerClickChessPiece()]
 *
 */
public class GameController implements GameListener {

    private Chessboard model;
    private ChessboardComponent view;
    private PlayerColor currentPlayer;

    // Record whether there is a selected piece before
    private ChessboardPoint selectedPoint;

    public GameController(ChessboardComponent view, Chessboard model) {
        this.view = view;
        this.model = model;
        this.currentPlayer = PlayerColor.BLUE;

        view.registerController(this);
        initialize();
        view.initiateChessComponent(model);
        view.repaint();
    }

    private void initialize() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {

            }
        }
    }

    // after a valid move swap the player
    private void swapColor() {
        currentPlayer = currentPlayer == PlayerColor.BLUE ? PlayerColor.RED : PlayerColor.BLUE;
    }

    private boolean win() {
        // TODO: Check the board if there is a winner
        if (model.getChessPieceAt(new ChessboardPoint(0, 3)).getOwner().equals(PlayerColor.RED)
                || model.getChessPieceAt(new ChessboardPoint(8, 3)).getOwner().equals(PlayerColor.BLUE)) {
            return true;
        }
        return false;
    }

    // click an empty cell
    @Override
    public void onPlayerClickCell(ChessboardPoint point, CellComponent component) {
        if (selectedPoint != null && model.isValidMove(selectedPoint, point) || model.isNull(point)) {
            if (point.getName().equals("Den") &&
                    ((this.currentPlayer.equals(PlayerColor.BLUE) && point.getRow() < 3)
                            || (this.currentPlayer.equals(PlayerColor.RED) && point.getRow() > 6))) {
            } else {
                model.moveChessPiece(selectedPoint, point);
                view.setChessComponentAtGrid(point, view.removeChessComponentAtGrid(selectedPoint));
                selectedPoint = null;
                swapColor();
                view.repaint();
                // TODO: if the chess enter Dens or Traps and so on
                if (point.getName().equals("Trap")
                        && ((this.currentPlayer.equals(PlayerColor.BLUE) && point.getRow() < 3)
                                || (this.currentPlayer.equals(PlayerColor.RED) && point.getRow() > 6))) {
                    this.model.getChessPieceAt(point).setRank(0);
                }
                if (point.getName().equals("Den")) {
                    win();
                    VictoryDialog.displayWinning();
                }// finish the game if the chess enter the Den
            } // finish the game
        }
    }

    // click a cell with a chess
    @Override
    public void onPlayerClickChessPiece(ChessboardPoint point, AnimalChessComponent component) {
        if (selectedPoint == null) {
            if (model.getChessPieceOwner(point).equals(currentPlayer)) {
                selectedPoint = point;
                component.setSelected(true);
                component.repaint();
            }
        } else if (selectedPoint.equals(point)) {
            selectedPoint = null;
            component.setSelected(false);
            component.repaint();
        } else if (!model.isNull(point)) {
            model.captureChessPiece(selectedPoint, point);

            view.removeChessComponentAtGrid(point);

            view.setChessComponentAtGrid(point, view.removeChessComponentAtGrid(selectedPoint));
            selectedPoint = null;
            swapColor();
            view.repaint();
            if (point.getName().equals("Trap") && ((this.currentPlayer.equals(PlayerColor.BLUE) && point.getRow() < 3)
                    || (this.currentPlayer.equals(PlayerColor.RED) && point.getRow() > 6))) {
                this.model.getChessPieceAt(point).setRank(0);
            }
        }
    }
}
