package controller;

import listener.GameListener;
import model.Constant;
import model.PlayerColor;
import model.Chessboard;
import model.ChessboardPoint;
import view.CellComponent;
import view.AnimalChessComponent;
import view.ChessboardComponent;

/**
 * Controller is the connection between model and view,
 * when a Controller receive a request from a view, the Controller
 * analyzes and then hands over to the model for processing
 * [in this demo the request methods are onPlayerClickCell() and
 * onPlayerClickChessPiece()]
 *
 */
public class GameController implements GameListener {
    private int count = 1;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

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

    public void loading(){
        SaveLoad saveload =new SaveLoad(model);
        saveload.Load();
    }

    public void restart(){
        model.restart();
        view.initiateChessComponent(model);
        view.repaint();
        this.currentPlayer = PlayerColor.BLUE;
        this.selectedPoint = null;
        this.count = 1;
    }

    // click an empty cell
    @Override
    public void onPlayerClickCell(ChessboardPoint point, CellComponent component) {
        if (selectedPoint != null && model.isValidMove(selectedPoint, point) || model.isNull(point)) {
            if (point.getName().equals("Den") &&
                    ((this.currentPlayer.equals(PlayerColor.BLUE) && point.getRow() < 3)
                            || (this.currentPlayer.equals(PlayerColor.RED) && point.getRow() > 6))) {
            } else {
                model.recordStep(selectedPoint, point, count);
                count++;
                model.moveChessPiece(selectedPoint, point);
                view.setChessComponentAtGrid(point, view.removeChessComponentAtGrid(selectedPoint));
                selectedPoint = null;
                swapColor();
                view.repaint();
                SaveLoad.Save();
                // TODO: if the chess enter Dens or Traps and so on
                if (point.getName().equals("Trap")
                        && ((this.currentPlayer.equals(PlayerColor.BLUE) && point.getRow() < 3)
                                || (this.currentPlayer.equals(PlayerColor.RED) && point.getRow() > 6))) {
                    this.model.getChessPieceAt(point).setRank(0);
                }
                if (point.getName().equals("Den")) {
                    win();
                }
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
        } else if (!model.isNull(point) && model.isValidCapture(selectedPoint, point)) {
            this.restart();
            model.recordStep(selectedPoint, point, count);
            count++;
            model.captureChessPiece(selectedPoint, point);

            view.removeChessComponentAtGrid(point);

            view.setChessComponentAtGrid(point, view.removeChessComponentAtGrid(selectedPoint));
            selectedPoint = null;
            swapColor();
            view.repaint();
            SaveLoad.Save();
            if (point.getName().equals("Trap") && ((this.currentPlayer.equals(PlayerColor.BLUE) && point.getRow() < 3)
                    || (this.currentPlayer.equals(PlayerColor.RED) && point.getRow() > 6))) {
                this.model.getChessPieceAt(point).setRank(0);
            }
        } // the color wait to be changed in gui.
    }
}
