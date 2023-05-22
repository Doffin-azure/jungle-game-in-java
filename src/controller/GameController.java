package controller;

import controller.NetWork.Client;
import controller.NetWork.Server;
import listener.GameListener;
import model.*;
import view.AnimalChessComponent;
import view.ChessboardComponent;
import view.Dialog.VictoryDialog;
import view.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;
import javax.swing.*;
import java.util.ArrayList;



import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * Controller is the connection between model and view,
 * when a Controller receive a request from a view, the Controller
 * analyzes and then hands over to the model for processing
 * [in this demo the request methods are onPlayerClickCell() and
 * onPlayerClickChessPiece()]
 */
public class GameController implements GameListener {


    public void setAi(AI ai) {
        this.ai = ai;
    }

    private AI ai;

    public Chessboard getModel() {
        return model;
    }

    private Chessboard model;
    public ChessboardComponent view;
    public JButton TimerCounterButton;
    public TimerCounter timerCounter;
    public ArrayList<ChessboardPoint> possibleMovePoints;
    // Record whether there is a selected piece before
    private ChessboardPoint selectedPoint;
    private PlayerColor winner = null;

    private int count = 1;
    ChessGameFrame gameFrame;

    //5.17晚上局域网搭建

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public PlayerColor getCurrentPlayer() {
        return currentPlayer;
    }

    public PlayerColor currentPlayer = PlayerColor.BLUE;
    private int aiStatus =3;//0_ai0, 1_ai1, 2_ai2, 3_human, 4_netWork_server, 5_netWork_server_client

    private Server server;
    private Client client;


    public GameController(ChessboardComponent view, Chessboard model) {
        this.view = view;
        this.model = model;
        this.currentPlayer = PlayerColor.BLUE;
        this.TimerCounterButton = view.TimerCounterButton;
        view.registerController(this);
        initialize();
        view.initiateChessComponent(model);
        view.repaint();
    }

    public void beginNetwork(int aiStatus,String host) {
        if (aiStatus == 4) {
            this.server = new Server();
            server.startServer(8888);
            server.receivingDataAndUpdate(this);
        } else {
            this.client = new Client();
            client.connectToServer(host, 8888);
            client.receivingDataAndUpdate(this);
        }
    }
    private void initialize() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
            }
        }
    }

    // after a valid move swap the player
    public void swapColor() {
        currentPlayer = currentPlayer == PlayerColor.BLUE ? PlayerColor.RED : PlayerColor.BLUE;
        if (currentPlayer == PlayerColor.BLUE) {
            view.TurnStatusButton.setText(String.format("Turn %d : Player Blue", count));
            TimerCounter.timeOfThisTurn = 30;
            view.TimerCounterButton.setText(String.format("Time Remained: %d ", TimerCounter.getTimeOfThisTurn()));
        } else
            view.TurnStatusButton.setText(String.format("Turn %d : Player Red", count));
        TimerCounter.timeOfThisTurn = 30;
        view.TimerCounterButton.setText(String.format("Time Remained: %d ", TimerCounter.getTimeOfThisTurn()));
    }


    public Boolean loading(String name) {
        String[] title = name.split("\\.");
        if (!title[1].equals("txt")) {
            System.out.println("错误编码: 101");
            return false;
        }
        String PathName = "resource\\save\\" + name;
        try (BufferedReader br = new BufferedReader(new FileReader(PathName))) {
            String line;
            int num = 0;
            while ((line = br.readLine()) != null) {

                Pattern pattern = Pattern.compile("\\d+");
                Pattern pattern1 = Pattern.compile("(?<==')[^']+(?=')");
                java.util.regex.Matcher matcher = pattern.matcher(line);
                java.util.regex.Matcher matcher1 = pattern1.matcher(line);
                int counts = 0;
                int[] arr = new int[5];
                String[] arr1 = new String[2];
                while (matcher.find() && counts < 4) {
                    arr[counts] = Integer.parseInt(matcher.group());
                    counts++;
                }
                counts = 0;
                while (matcher1.find() && counts < 2) {
                    arr1[counts] = matcher1.group();
                    counts++;
                }
                num++;
                arr[4] = num;
                if (arr[0] > 6 || arr[0] < 0 || arr[1] > 8 || arr[1] < 0 || arr[2] > 6 || arr[2] < 0 || arr[3] > 8 || arr[3] < 0) {
                    JOptionPane.showMessageDialog(null, "棋盘错误，错误编码:102", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                }
                ChessboardPoint src = new ChessboardPoint(arr[0], arr[1]);
                ChessboardPoint dest = new ChessboardPoint(arr[2], arr[3]);
                int turn = arr[4];

                if (model.getChessPieceAt(src).getName().equals(arr1[0]) && (model.getChessPieceAt(dest) == null || model.getChessPieceAt(dest).getName().equals(arr1[1]))) {
                    if (!model.isNull(dest)) {
                        if (model.isValidCapture(src, dest) == false) {
                            JOptionPane.showMessageDialog(null, "行棋步骤错误，错误编码:105", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                    }
                    if (model.isValidMove(src, dest) == false) {
                        JOptionPane.showMessageDialog(null, "行棋步骤错误，错误编码:105", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                    Step step = new Step(src, dest, null, null, turn, null);
                    doStep(step);
                    swapColor();
                    view.paintImmediately(0, 0, view.getHeight(), view.getWidth());


                } else {
                    JOptionPane.showMessageDialog(null, "棋子错误，错误编码:103", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        view.repaint();
        return true;
    }

    public void Save(String Path) {
        String PathName = "resource\\save\\" + Path;
        File file = new File(PathName);
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
            model.recordStep(src, dest, count, null);
            count++;
            model.moveChessPiece(src, dest);
            if (view != null) {
                view.setChessComponentAtGrid(dest, view.removeChessComponentAtGrid(src));
                try {
                    Thread.sleep(250);
                    view.paintImmediately(0, 0, view.getWidth(), view.getHeight());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (dest.getName().equals("Trap")) {
                if (dest.getPlayerColor() != model.getChessPieceOwner(dest)) {
                    model.getChessPieceAt(dest).setRank(0);
                }
            }
        } else if (model.isValidCapture(src, dest)) {
            AnimalChessComponent chessComponent = (AnimalChessComponent) view.getGridComponentAt(dest).getComponents()[0];
            model.recordStep(src, dest, count, chessComponent);
            model.captureChessPiece(src, dest);
            if (view != null) {
                view.removeChessComponentAtGrid(dest);
                view.repaint();
                view.setChessComponentAtGrid(dest, view.removeChessComponentAtGrid(src));
                try {
                    Thread.sleep(250);
                    view.paintImmediately(0, 0, view.getWidth(), view.getHeight());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            count++;
            if (dest.getName().equals("Trap")) {
                if (dest.getPlayerColor() != model.getChessPieceOwner(dest)) {
                    model.getChessPieceAt(dest).setRank(0);
                }
            }
        } else if (model.isValidCapture(src, dest)) {
            AnimalChessComponent chessComponent = (AnimalChessComponent) view.getGridComponentAt(dest).getComponents()[0];
            model.recordStep(src, dest, count, chessComponent);
            model.captureChessPiece(src, dest);
            if (view != null) {
                view.removeChessComponentAtGrid(dest);
                view.repaint();
                view.setChessComponentAtGrid(dest, view.removeChessComponentAtGrid(src));
                try {
                    Thread.sleep(250);
                    view.paintImmediately(0, 0, view.getWidth(), view.getHeight());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            count++;
            if (dest.getName().equals("Trap")) {
                if (dest.getPlayerColor() != model.getChessPieceOwner(dest)) {
                    model.getChessPieceAt(dest).setRank(0);
                }
            }
        } else {
            throw new IllegalArgumentException("Illegal chess move!");
        }
    }


    public void undo() {
        if (count <= 2) {
            restart();
        }
        for (int i = 0; i < 2; i++) {
            Step step = SharedData.stepList.get(count - 2);
            ChessboardPoint src = step.getFrom();
            ChessboardPoint dest = step.getTo();
            if (step.getCapturedChessPiece() == null) {
                model.moveChessPiece(dest, src);
                if (view != null) {
                    view.setChessComponentAtGrid(src, view.removeChessComponentAtGrid(dest));
                    view.repaint();
                }
            } else if (step.getCapturedChessPiece() != null) {
                model.moveChessPiece(dest, src);
                if (view != null) {
                    view.setChessComponentAtGrid(src, view.removeChessComponentAtGrid(dest));
                    view.repaint();
                }
                model.setChessPiece(dest, step.getCapturedChessPiece());
                if (view != null) {
                    view.getGridComponentAt(dest).add(step.getAcc());
                    view.repaint();
                }
            } else {
                throw new IllegalArgumentException("Illegal chess move!");
            }
            count--;
            swapColor();
            if (view != null) {
                view.repaint();
            }
            SharedData.stepList.remove(count - 1);

        }
    }

    public void restart() {
        model.restart();
        TimerCounter.timeOfThisTurn = 30;
        view.initiateChessComponent(model);
        view.repaint();
        this.currentPlayer = PlayerColor.BLUE;
        this.selectedPoint = null;
        this.count = 1;
        view.TimerCounterButton.setText(String.format("Time Remained: %d ", TimerCounter.getTimeOfThisTurn()));

        view.TurnStatusButton.setText(String.format("Turn 1 : Player Blue"));
//        view.TurnStatusButton.setText(String.format("Turn %d : Player Blue", count));
        SharedData.stepList.clear();
    }

    public boolean JudgeWin(Chessboard model) {
        // TODO: Check the board if there is a winner
        if (model.getChessPieceAt(new ChessboardPoint(0, 3)) != null) {
            if (model.getChessPieceAt(new ChessboardPoint(0, 3)).getOwner().equals(PlayerColor.RED)
            ) {
                return true;
            }
        }
        if (model.getChessPieceAt(new ChessboardPoint(8, 3)) != null) {
            if (model.getChessPieceAt(new ChessboardPoint(8, 3)).getOwner().equals(PlayerColor.BLUE)) {
                return true;
            }
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                if (model.getChessPieceAt(new ChessboardPoint(i, j)) != null) {
                    if (model.getChessPieceOwner(new ChessboardPoint(i, j)).equals(currentPlayer)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void AIPlayIntegrated(int aiStatus) {//整合所有ai方法
        switch (aiStatus) {
            case 0:
                ai.AIPlay_0();
                swapColor();
                break;
            case 1:
                ai.AIPlay_1();
                swapColor();
                break;
            case 2:
                ai.AIPlay_2();
                swapColor();
                break;
            case 3:    //啥也不做
                break;
        }
    }

    // click an empty cell
    @Override
    public void onPlayerClickCell(ChessboardPoint point, CellColorView component) {
        possibleMovePoints = null;
        setCanStepFalse();
        if (selectedPoint != null && model.isValidMove(selectedPoint, point) || model.isNull(point)) {//如果刚刚选有棋子且（空cell可以移动）或者（point是空的）
            if (!model.isValidMove(selectedPoint, point)) {
                component.revalidate();
                component.repaint();
                view.repaint();
                view.revalidate();
                selectedPoint = null;
                JOptionPane.showMessageDialog(null, "Invalid Move!");
            } else if (model.isValidMove(selectedPoint, point) && selectedPoint != null) {//如果是合法移动 else if (model.isValidMove(selectedPoint, point) && selectedPoint != null) {
                if (model.isValidMove(selectedPoint, point) == false) {
                    JOptionPane.showMessageDialog(null, "行棋步骤错误，错误编码:105", "Error", JOptionPane.ERROR_MESSAGE);
                }
                model.recordStep(selectedPoint, point, count, null);
                count++;
                model.moveChessPiece(selectedPoint, point);
                ChessboardPoint[] sendingData = {this.selectedPoint, point};//用于传输数据，前面是srcPoint，后面是destPoint
                possibleMovePoints = null;
                view.setChessComponentAtGrid(point, view.removeChessComponentAtGrid(selectedPoint));
                selectedPoint = null;
                if(this.aiStatus ==4 && currentPlayer == PlayerColor.BLUE){//这里是服务端是蓝色，对面是红色
                    server.sendData(sendingData);
                }
                if(this.aiStatus ==5 && currentPlayer == PlayerColor.RED){//这里是客户端是蓝色，对面是蓝色
                    client.sendData(sendingData);
                }
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
                    VictoryDialog a = new VictoryDialog();
                    VictoryDialog.displayWinning(winner, a);
                    this.restart();
                    this.gameFrame.setVisible(false);
                    this.gameFrame.firstFrame.setVisible(true);
                }// finish the game if the chess enter the Den
            } // finish the game
        }
        if (this.currentPlayer.equals(PlayerColor.RED)) {
            AIPlayIntegrated(getAiStatus());
        }
    }

    // click a cell with a chess
    @Override
    public void onPlayerClickChessPiece(ChessboardPoint point, AnimalChessComponent component) {
        if (selectedPoint == null) {
            if (model.getChessPieceOwner(point).equals(currentPlayer)) {
                possibleMovePoints = getAndSetIsValidMovePoints(point);
                possibleMovePoints = new ArrayList<>(getAndSetIsValidMovePoints(point));
                selectedPoint = point;
                model.findPossibleStep(point);
                component.setSelected(true);
                component.revalidate();
                component.repaint();
                view.repaint();
                view.revalidate();
            }
        } else if (selectedPoint.equals(point)) {//click the same chess again and cancel selection
            selectedPoint = null;
            possibleMovePoints = null;
            setCanStepFalse();
            component.setSelected(false);
            component.repaint();
            view.repaint();
            view.revalidate();
        } else if (!model.isNull(point)) {
            if (model.isValidCapture(selectedPoint, point)) {// 如果能吃
                possibleMovePoints = null;
                setCanStepFalse();
                AnimalChessComponent chessComponent = (AnimalChessComponent) view.getGridComponentAt(point).getComponents()[0];
                model.recordStep(selectedPoint, point, count, chessComponent);
                count++;
                model.captureChessPiece(selectedPoint, point);
                new BGMofClick().PlayClickBGM("resource/Music/tear.wav");
                //zhe li!
                view.removeChessComponentAtGrid(point);
                view.setChessComponentAtGrid(point, view.removeChessComponentAtGrid(selectedPoint));
                ChessboardPoint[] sendingData = {selectedPoint, point};
                selectedPoint = null;
                if(this.aiStatus ==4&& currentPlayer == PlayerColor.BLUE){//这里是服务端是蓝色，对面是红色
                    server.sendData(sendingData);
                }
                if(this.aiStatus ==5 && currentPlayer == PlayerColor.RED){//这里是客户端是蓝色，对面是蓝色
                    client.sendData(sendingData);
                }
                swapColor();
                view.repaint();

                if (point.getName().equals("Trap") && ((this.currentPlayer.equals(PlayerColor.BLUE) && point.getRow() < 3)
                        || (this.currentPlayer.equals(PlayerColor.RED) && point.getRow() > 6))) {
                    this.model.getChessPieceAt(point).setRank(0);
                }
            }
        } else {

            JOptionPane.showMessageDialog(null, "行棋步骤错误，错误编码:105", "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (this.currentPlayer.equals(PlayerColor.RED)) {
            AIPlayIntegrated(getAiStatus());
        }
    }

//5.11写：

    public ArrayList<ChessboardPoint> getAndSetIsValidMovePoints(ChessboardPoint sourcePoint) {
        ArrayList<ChessboardPoint> validMovePointsList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                ChessboardPoint destPoint = new ChessboardPoint(i, j);
                if (model.isValidMove(sourcePoint, destPoint)) {
                    //仅限于地图上的性质是否可以移动
                    if (!model.isNull(destPoint)) {
                        if (!model.isValidCapture(sourcePoint, destPoint)) {
                            continue;
                        }
                    }
                    view.gridComponents[i][j].canStep = true;
                    validMovePointsList.add(destPoint);
                }
            }
        }
        return validMovePointsList;

    }

    //将每个Cell的canstep状态设为不可以
    public void setCanStepFalse() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                view.gridComponents[i][j].canStep = false;
            }
        }
    }

    public int getAiStatus() {
        return aiStatus;
    }

    public void setAiStatus(int aiStatus) {
        this.aiStatus = aiStatus;
    }

    public void setSelectedPoint(ChessboardPoint selectedPoint) {
        this.selectedPoint = selectedPoint;
    }

    public ChessboardComponent getView() {
        return view;
    }

    public void setView(ChessboardComponent view) {
        this.view = view;
    }

    public void setGameFrame(ChessGameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }
}



