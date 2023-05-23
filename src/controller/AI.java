package controller;

import model.*;
import model.SharedData;
import controller.*;
import view.*;

import java.util.*;

public class AI {
    int count;
    Chessboard SupposeModel;

    PlayerColor currentPlayer;
    GameController gameController;
    Chessboard thisModel;
    ChessboardComponent view;

    public AI(GameController gameController, Chessboard model, ChessboardComponent view) {
        this.gameController = gameController;
        this.thisModel = model;
        this.view = view;
        this.SupposeModel = new Chessboard();
        this.CopyModel();
        this.count = 0;
        this.currentPlayer = gameController.getCurrentPlayer();
    }

    public void JudgeValue(Chessboard model) {
        SharedData.possibleMoveMap.clear();//清空可能的移动
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                if (!model.isNull(SharedData.chessboardPointMap.get(i * 10 + j))) {
                    if (model.getChessPieceOwner(SharedData.chessboardPointMap.get(i * 10 + j)) == gameController.getCurrentPlayer()) {//如果是当前玩家的棋子 则遍历所有可能的移动 并计算价值
                        for (int k = 0; k < 9; k++) {
                            for (int p = 0; p < 7; p++) {
                                if (model.isValidMove(SharedData.chessboardPointMap.get(i * 10 + j), SharedData.chessboardPointMap.get(k * 10 + p))) {
                                    Step step = new Step(SharedData.chessboardPointMap.get(i * 10 + j), SharedData.chessboardPointMap.get(k * 10 + p), model.getChessPieceAt(SharedData.chessboardPointMap.get(i * 10 + j)), model.getChessPieceAt(SharedData.chessboardPointMap.get(k * 10 + p)), 1, null);
                                    if (model.getChessPieceAt(SharedData.chessboardPointMap.get(k * 10 + p)) != null) {
                                        if (model.getChessPieceAt(SharedData.chessboardPointMap.get(k * 10 + p)).getOriginRank() == 7) {
                                            step.setValue(step.getValue() + 3);
                                        }//if the chess piece is a lion, it will be more valuable
                                        if (model.getChessPieceAt(SharedData.chessboardPointMap.get(k * 10 + p)).getOriginRank() == 6) {
                                            step.setValue(step.getValue() + 2);
                                        }//if the chess piece is a tiger, it will be more valuable
                                        if (!model.isValidCapture(SharedData.chessboardPointMap.get(i * 10 + j), SharedData.chessboardPointMap.get(k * 10 + p))) {
                                            continue;
                                        }//if the chess piece is a mouse, it will be more valuable
                                        else {
                                            step.setValue(step.getValue() + 100 + model.getChessPieceAt(SharedData.chessboardPointMap.get(k * 10 + p)).getOriginRank());
                                            if (model.getChessPieceAt(SharedData.chessboardPointMap.get(k * 10 + p)).getOriginRank() == 1) {
                                                step.setValue(step.getValue() + 100);
                                            }//if the chess piece is a mouse, it will be more valuable
                                        }
                                    }

                                    for (int a = 0; a < 9; a++) {
                                        for (int b = 0; b < 7; b++) {
                                            if (SupposeModel.isValidCapture(SharedData.chessboardPointMap.get(a * 10 + b), SharedData.chessboardPointMap.get(i * 10 + j))) {
                                                step.setValue(step.getValue() + 40);
                                            }
                                        }
                                    }//如果对方可以吃掉我方的棋子 则价值加40
                                    SupposeMove(step);//假设移动
                                    for (int a = 0; a < 9; a++) {
                                        for (int b = 0; b < 7; b++) {
                                            if (SupposeModel.isValidCapture(SharedData.chessboardPointMap.get(k * 10 + p), SharedData.chessboardPointMap.get(a * 10 + b))) {
                                                step.setValue(step.getValue() + 100 - SupposeModel.getChessPieceAt(SharedData.chessboardPointMap.get(k * 10 + p)).getOriginRank() - 1);
                                            }
                                        }
                                    }//如果移动后对方可以吃掉我方的棋子 则价值减100
                                    for (int a = 0; a < 9; a++) {
                                        for (int b = 0; b < 7; b++) {
                                            if (SupposeModel.isValidCapture(SharedData.chessboardPointMap.get(a * 10 + b), SharedData.chessboardPointMap.get(k * 10 + p))) {
                                                step.setValue(step.getValue() - 100 - SupposeModel.getChessPieceAt(SharedData.chessboardPointMap.get(k * 10 + p)).getOriginRank() - 1);
                                            }
                                        }
                                    }//如果移动后对方可以吃掉我方的棋子 则价值减100
                                    if (gameController.JudgeWin(SupposeModel)) {
                                        step.setValue(step.getValue() + 1000);
                                    }
                                    SupposeUndo(step);

                                    step.setValue(step.getValue() + (SupposeModel.getChessPieceAt(SharedData.chessboardPointMap.get(i * 10 + j)).getOriginRank() - SupposeModel.calculateWinDistance(SharedData.chessboardPointMap.get(i * 10 + j), SharedData.chessboardPointMap.get(k * 10 + p))));//如果移动后可以升级 则价值加升级后的等级
                                    SharedData.possibleMoveMap.put(step.getValue(), step);
                                }
                            }
                        }
                    }
                }
            }
        }//找到所有可能的移动 以及其价值 保存在possibleMoveMap中 以价值为key 以step为value 从小到大排序 价值越大越好 价值相同的话 选择后面的 因为后面的是吃子 价值更大  价值为0的话 说明是无子可走 也就是输了

    }

    public void AIPlay_1() {
        CopyModel();
        this.currentPlayer = gameController.getCurrentPlayer();
        JudgeValue(thisModel);
        Step step = SharedData.possibleMoveMap.get(Collections.max(SharedData.possibleMoveMap.keySet()));
        for (int i = 1; i < 3; i++) {
            if (SharedData.stepList.size() < i * 4) {
                break;
            }
            if (step.getFrom() == SharedData.stepList.get(SharedData.stepList.size() - i * 4).getFrom()) {
                if (step.getTo() == SharedData.stepList.get(SharedData.stepList.size() - i * 4).getTo()) {
                    if (i == 2) {
                        SharedData.possibleMoveMap.remove(Collections.max(SharedData.possibleMoveMap.keySet()));
                        step = SharedData.possibleMoveMap.get(Collections.max(SharedData.possibleMoveMap.keySet()));
                    }
                    continue;
                }
            }

            break;
        }
        gameController.doStep(step);
    }

    public void AIPlay_0() {
        this.currentPlayer = gameController.getCurrentPlayer();
        gameController.doStep(RandomStep(gameController.getModel()));
    }

    private Step RandomStep(Chessboard model) {
        Random roll = new Random();
        int RandomMove;
        Step RandomStep = null;
        int end = 0;
        while ((true)) {
            int RandomRow = roll.nextInt(0, 9);
            int RandomCol = roll.nextInt(0, 7);
            if (gameController.JudgeWin(model)) {
                return null;
            }

            if (model.getChessPieceAt(SharedData.chessboardPointMap.get(RandomRow * 10 + RandomCol)) != null) {
                end++;
                if (end > 100) {

                    return null;

                }
                if (model.getChessPieceAt(SharedData.chessboardPointMap.get(RandomRow * 10 + RandomCol)).getOwner() == currentPlayer) {
                    model.findPossibleStep(SharedData.chessboardPointMap.get(RandomRow * 10 + RandomCol));
                    if (SharedData.possibleMoveList.size() == 1) {
                        RandomMove = 0;
                    } else if (SharedData.possibleMoveList.size() < 1) {
                        continue;
                    } else {
                        RandomMove = roll.nextInt(0, SharedData.possibleMoveList.size());
                    }
                    if (model.getChessPieceAt(SharedData.possibleMoveList.get(RandomMove)) != null) {
                        if (model.getChessPieceOwner(SharedData.chessboardPointMap.get(RandomRow * 10 + RandomCol)) == model.getChessPieceOwner(SharedData.possibleMoveList.get(RandomMove))) {
                            continue;
                        }
                        if (!model.isValidCapture(SharedData.chessboardPointMap.get(RandomRow * 10 + RandomCol), SharedData.possibleMoveList.get(RandomMove))) {
                            continue;
                        }
                    }
                    RandomStep = new Step(SharedData.chessboardPointMap.get(RandomRow * 10 + RandomCol), SharedData.possibleMoveList.get(RandomMove), model.getChessPieceAt(SharedData.chessboardPointMap.get(RandomRow * 10 + RandomCol)), model.getChessPieceAt(SharedData.chessboardPointMap.get(RandomMove)), 0, null);
                    break;
                }
            }
        }
        return RandomStep;
    }

    public void AIPlay_2() {
        this.CopyModel();
        JudgeRandomMoveValue(1000);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                if (!thisModel.isNull(SharedData.chessboardPointMap.get(i * 10 + j))) {
                    thisModel.getChessPieceAt(SharedData.chessboardPointMap.get(i * 10 + j)).setRank(thisModel.getChessPieceAt(SharedData.chessboardPointMap.get(i * 10 + j)).getOriginRank());
                }
            }
        }
        Step step = SharedData.RandomMoveMapWithValue.get(Collections.max(SharedData.RandomMoveMapWithValue.keySet()));
        gameController.doStep(step);
    }

    private void JudgeRandomMoveValue(int CountStep) {
        SharedData.RandomMoveMapWithValue.clear();
        int countMax = 100;
        for (int i = 0; i < CountStep; i++) {
            this.CopyModel();
            int count = 0;
            int value = 0;
            this.currentPlayer = gameController.getCurrentPlayer();
            Step RandomStep0 = RandomStep(SupposeModel);
            SupposeMove(RandomStep0);
            swapColor();
            while (true) {
                if (gameController.JudgeWin(SupposeModel)) {
                    if (currentPlayer == gameController.getCurrentPlayer()) {
                        value = value + 100;
                    } else {
                        value = value - 100;
                    }
                    break;
                }

                JudgeValue(SupposeModel);
                Step SupposeStep = SharedData.possibleMoveMap.get(Collections.max(SharedData.possibleMoveMap.keySet()));
                for (int k = 1; k < 3; k++) {
                    if (SharedData.stepList.size() < k * 4) {
                        break;
                    }
                    if (SupposeStep.getFrom() == SharedData.stepList.get(SharedData.stepList.size() - k * 4).getFrom()) {
                        if (SupposeStep.getTo() == SharedData.stepList.get(SharedData.stepList.size() - k * 4).getTo()) {
                            if (k == 2) {
                                SharedData.possibleMoveMap.remove(Collections.max(SharedData.possibleMoveMap.keySet()));
                                SupposeStep = SharedData.possibleMoveMap.get(Collections.max(SharedData.possibleMoveMap.keySet()));
                            }
                            continue;
                        }
                    }
                }
                if (SupposeStep == null) {
                    break;
                }
                SupposeMove(SupposeStep);
                count++;
                swapColor();
                if (count > countMax) {
                    break;
                }
            }
            for (int k = 0; k < 9; k++) {
                for (int j = 0; j < 7; j++) {
                    if (SupposeModel.getChessPieceAt(SharedData.chessboardPointMap.get(k * 10 + j)) != null) {
                        if (SupposeModel.getChessPieceAt(SharedData.chessboardPointMap.get(k * 10 + j)).getOwner() == gameController.getCurrentPlayer()) {
                            value = value + 7 * SupposeModel.getChessPieceAt(SharedData.chessboardPointMap.get(k * 10 + j)).getOriginRank();
                        } else {
                            value = value - 10 * SupposeModel.getChessPieceAt(SharedData.chessboardPointMap.get(k * 10 + j)).getOriginRank();
                        }
                    }
                }
            }

            if (100 - count + value < 0) {
            } else {
                SharedData.RandomMoveMapWithValue.put(100 - count + value, RandomStep0);
                countMax = 100 - Collections.max(SharedData.RandomMoveMapWithValue.keySet()) + 1;
            }
        }
        CopyModel();
        JudgeValue(thisModel);
        Step step1 = SharedData.possibleMoveMap.get(Collections.max(SharedData.possibleMoveMap.keySet()));
        SharedData.RandomMoveMapWithValue.put(Collections.max(SharedData.possibleMoveMap.keySet()), step1);
        List<Integer> list = new ArrayList<Integer>(SharedData.RandomMoveMapWithValue.keySet());
        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });//Descending order
        List<Step> listStep1 = new ArrayList<Step>();
        List<Step> listStep2 = new ArrayList<Step>();
        for (int i = 0; i < list.size(); i++) {
            listStep1.add(SharedData.RandomMoveMapWithValue.get(list.get(i)));
            listStep2.add(SharedData.RandomMoveMapWithValue.get(list.get(i)));
        }
        for (Step step : listStep1) {
            int repeatNum = 0;
            for (Step step0 : listStep2) {
                if (step.getFrom() == step0.getFrom()) {
                    if (step.getTo() == step0.getTo()) {
                        repeatNum++;
                    }
                }
            }
            SharedData.RandomMoveMapWithValue.put(repeatNum + 1000, step);
        }
    }

    private void swapColor() {
        currentPlayer = currentPlayer == PlayerColor.BLUE ? PlayerColor.RED : PlayerColor.BLUE;
    }

    private void SupposeMove(Step step) {
        ChessboardPoint src = step.getFrom();
        ChessboardPoint dest = step.getTo();
        if (SupposeModel.isNull(dest)) {
            count++;
            SupposeModel.moveChessPiece(src, dest);
            if (dest.getName().equals("Trap")) {
                if (dest.getPlayerColor() != SupposeModel.getChessPieceOwner(dest)) {
                    SupposeModel.getChessPieceAt(dest).setRank(0);
                }
            }
            if (src.getName().equals("Trap")) {
                SupposeModel.getChessPieceAt(dest).setRank(SupposeModel.getChessPieceAt(dest).getOriginRank()
                );
            }

        } else if (SupposeModel.isValidCapture(src, dest)) {

            SupposeModel.captureChessPiece(src, dest);
            count++;
            if (dest.getName().equals("Trap")) {
                if (dest.getPlayerColor() != SupposeModel.getChessPieceOwner(dest)) {
                    SupposeModel.getChessPieceAt(dest).setRank(0);
                }
            }
            if (src.getName().equals("Trap")) {
                SupposeModel.getChessPieceAt(dest).setRank(SupposeModel.getChessPieceAt(dest).getOriginRank()
                );
            }
        } else {
            throw new RuntimeException("Invalid step");
        }
    }

    public void SupposeUndo(Step step) {
        ChessboardPoint src = step.getFrom();
        ChessboardPoint dest = step.getTo();
        if (step.getCapturedChessPiece() == null) {
            SupposeModel.moveChessPiece(dest, src);

        } else if (step.getCapturedChessPiece() != null) {
            SupposeModel.moveChessPiece(dest, src);

        } else {
            throw new RuntimeException("Invalid step");
        }
        count--;

    }


    private void CopyModel() {
        SupposeModel = new Chessboard();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                SupposeModel.getGrid()[i][j].setPiece(null);
                if (thisModel.getGrid()[i][j].getPiece() != null) {
                    SupposeModel.getGrid()[i][j].setPiece(thisModel.getGrid()[i][j].getPiece());
                }
            }
        }
    }
}