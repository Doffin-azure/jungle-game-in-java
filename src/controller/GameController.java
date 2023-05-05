package controller;

import listener.GameListener;
import model.*;
import view.CellComponent;
import view.AnimalChessComponent;
import view.ChessboardComponent;
import view.VictoryDialog;

import java.io.*;
import java.util.regex.Pattern;

/**
 * Controller is the connection between model and view,
 * when a Controller receive a request from a view, the Controller
 * analyzes and then hands over to the model for processing
 * [in this demo the request methods are onPlayerClickCell() and
 * onPlayerClickChessPiece()]
 *
 */
public class GameController implements GameListener {

    private PlayerColor winner = null;

    private int count = 1;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private Chessboard model;
    private ChessboardComponent view;
    private PlayerColor currentPlayer=PlayerColor.BLUE;

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
    public void loading(){
        try (BufferedReader br = new BufferedReader(new FileReader("save.txt"))) {
            String line;
            int num=0;
            while ((line = br.readLine()) != null) {
                Pattern pattern = Pattern.compile("\\d+");
                java.util.regex.Matcher matcher = pattern.matcher(line);
                int counts = 0;
                int[] arr = new int[5];
                while (matcher.find() && counts < 4) {
                    arr[counts] = Integer.parseInt(matcher.group());
                    counts++;
                }
                num++;
                arr[4]=num;
                ChessboardPoint src = new ChessboardPoint(arr[0], arr[1]);
                ChessboardPoint dest = new ChessboardPoint(arr[2], arr[3]);
                int turn = arr[4];
                Step step = new Step(src, dest, null, null, turn,null);
                doStep(step);
                swapColor();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        view.repaint();
    }

    public static void Save() {

        File file = new File("save.txt");
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for (Step step : SharedData.stepList) {
                bw.write(step.toString());
                bw.newLine();
            }
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doStep(Step step) {
        ChessboardPoint src = step.getFrom();
        ChessboardPoint dest = step.getTo();
        if (model.isNull(dest)) {

            model.recordStep(src,dest, count, null);
            count++;
            model.moveChessPiece(src, dest);
            view.setChessComponentAtGrid(dest, view.removeChessComponentAtGrid(src));
            view.repaint();
        } else if (model.isValidCapture(src, dest)) {
            AnimalChessComponent chessComponent = (AnimalChessComponent) view.getGridComponentAt(dest).getComponents()[0];
            model.recordStep(src,dest, count, chessComponent);
            model.captureChessPiece(src, dest);
            view.removeChessComponentAtGrid(dest);
            view.repaint();
            view.setChessComponentAtGrid(dest, view.removeChessComponentAtGrid(src));
            view.repaint();
            view.repaint();
            count++;
        } else {
            throw new IllegalArgumentException("Illegal chess move!");
        }
    }

    public void undo() {
        if(count <=2){
            restart();
        }
        for (int i=0;i<2;i++) {
            Step step = SharedData.stepList.get(count - 2);
            ChessboardPoint src = step.getFrom();
            ChessboardPoint dest = step.getTo();
            if (step.getCapturedChessPiece() == null) {
                model.moveChessPiece(dest, src);
                view.setChessComponentAtGrid(src, view.removeChessComponentAtGrid(dest));
                view.repaint();
            } else if (step.getCapturedChessPiece() != null) {
                model.moveChessPiece(dest, src);
                view.setChessComponentAtGrid(src, view.removeChessComponentAtGrid(dest));
                view.repaint();
                model.setChessPiece(dest, step.getCapturedChessPiece());
                view.getGridComponentAt(dest).add(step.getAcc());
                view.repaint();
            } else {
                throw new IllegalArgumentException("Illegal chess move!");
            }
            count--;
            swapColor();
            view.repaint();
            SharedData.stepList.remove(count - 1);

        }
    }

    public void restart(){
        model.restart();
        view.initiateChessComponent(model);
        view.repaint();
        this.currentPlayer = PlayerColor.BLUE;
        this.selectedPoint = null;
        this.count = 1;
        SharedData.stepList.clear();
    }

    @Override
    public void onPlayerClickCell(ChessboardPoint point, CellComponent component) {
        if (selectedPoint != null && model.isValidMove(selectedPoint, point) || model.isNull(point)) {
            if (point.getName().equals("Den") &&
                    ((this.currentPlayer.equals(PlayerColor.BLUE) && point.getRow() < 3)
                            || (this.currentPlayer.equals(PlayerColor.RED) && point.getRow() > 6))) {
            } else {
                model.recordStep(selectedPoint,point,count,null);
               count++;
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
                    winner = currentPlayer;
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
                model.findPossibleStep(point);
                component.setSelected(true);
                component.repaint();
            }
        } else if (selectedPoint.equals(point)) {
            selectedPoint = null;
            component.setSelected(false);
            component.repaint();
        } else if (!model.isNull(point)) {
            AnimalChessComponent chessComponent = (AnimalChessComponent) view.getGridComponentAt(point).getComponents()[0];
            model.recordStep(selectedPoint, point, count, chessComponent);
            count++;
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
