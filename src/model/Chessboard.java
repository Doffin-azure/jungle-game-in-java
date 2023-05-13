package model;
import model.SharedData;
import view.*;
/**
 * This class store the real chess information.
 * The Chessboard has 9*7 cells, and each cell has a position for chess
 */
public class Chessboard {
    private Cell[][] grid;//that is outer grid for chessboard to store the chess piece

    public Chessboard() {
        this.grid = new Cell[Constant.CHESSBOARD_ROW_SIZE.getNum()][Constant.CHESSBOARD_COL_SIZE.getNum()];// 19X19

        initGrid();
        initPieces();
    }

    private void initGrid() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                grid[i][j] = new Cell();

            }
        }
    }

    private void initPieces() {
        // Here is to initialize the chess's position, which waits to fix.
        grid[0][0].setPiece(new ChessPiece(PlayerColor.BLUE, "Lion", 7,"/Animal Supporter Asset Pack/King Lion/R.gif"));
        grid[8][6].setPiece(new ChessPiece(PlayerColor.RED, "Lion", 7,"/Animal Supporter Asset Pack/King Lion/R.gif"));
        grid[0][6].setPiece(new ChessPiece(PlayerColor.BLUE, "Tiger", 6,"/Animal Supporter Asset Pack/Walking Tiger/tiger.gif"));
        grid[8][0].setPiece(new ChessPiece(PlayerColor.RED, "Tiger", 6,"/Animal Supporter Asset Pack/Walking Tiger/tiger.gif"));
        grid[1][1].setPiece(new ChessPiece(PlayerColor.BLUE, "Dog", 3,"/Animal Supporter Asset Pack/Faithful Dog/R.gif"));
        grid[7][5].setPiece(new ChessPiece(PlayerColor.RED, "Dog", 3,"/Animal Supporter Asset Pack/Faithful Dog/R.gif"));
        grid[1][5].setPiece(new ChessPiece(PlayerColor.BLUE, "Cat", 2,"/Animal Supporter Asset Pack/Meowing Cat/R.gif"));
        grid[7][1].setPiece(new ChessPiece(PlayerColor.RED, "Cat", 2,"/Animal Supporter Asset Pack/Meowing Cat/R.gif"));
        grid[2][0].setPiece(new ChessPiece(PlayerColor.BLUE, "Rat", 1,"/Animal Supporter Asset Pack/Stinky Skunk/R.gif"));
        grid[6][6].setPiece(new ChessPiece(PlayerColor.RED, "Rat", 1,"/Animal Supporter Asset Pack/Stinky Skunk/R.gif"));
        grid[2][2].setPiece(new ChessPiece(PlayerColor.BLUE, "Leopard", 5,"/Animal Supporter Asset Pack/Running Leopard/leopard.gif"));
        grid[6][4].setPiece(new ChessPiece(PlayerColor.RED, "Leopard", 5,"/Animal Supporter Asset Pack/Running Leopard/leopard.gif"));
        grid[2][4].setPiece(new ChessPiece(PlayerColor.BLUE, "Wolf", 4,"/Animal Supporter Asset Pack/Timber Wolf/R.gif"));
        grid[6][2].setPiece(new ChessPiece(PlayerColor.RED, "Wolf", 4,"/Animal Supporter Asset Pack/Timber Wolf/R.gif"));
        grid[2][6].setPiece(new ChessPiece(PlayerColor.BLUE, "Elephant", 8,"/Animal Supporter Asset Pack/Stomping Elephant/R.gif"));
        grid[6][0].setPiece(new ChessPiece(PlayerColor.RED, "Elephant", 8,"/Animal Supporter Asset Pack/Stomping Elephant/R.gif"));
    }

    public ChessPiece getChessPieceAt(ChessboardPoint point) {
        return getGridAt(point).getPiece();
    }

    private Cell getGridAt(ChessboardPoint point) {
        return grid[point.getRow()][point.getCol()];
    }

    private int calculateDistance(ChessboardPoint src, ChessboardPoint dest) {
        return Math.abs(src.getRow() - dest.getRow()) + Math.abs(src.getCol() - dest.getCol());
    }// calculate the distance between two points

    private ChessPiece removeChessPiece(ChessboardPoint point) {
        ChessPiece chessPiece = getChessPieceAt(point);
        getGridAt(point).removePiece();
        return chessPiece;
    }

    public void setChessPiece(ChessboardPoint point, ChessPiece chessPiece) {
        getGridAt(point).setPiece(chessPiece);
    }

    public void moveChessPiece(ChessboardPoint src, ChessboardPoint dest) {
        if (!isValidMove(src, dest)) {
            throw new IllegalArgumentException("Illegal chess move!");
        }
        if (isNull(dest)) {
            setChessPiece(dest, removeChessPiece(src));
        }
    }// move the chess piece

    public void captureChessPiece(ChessboardPoint src, ChessboardPoint dest) {
        if (!isValidCapture(src, dest)) {
            throw new IllegalArgumentException("Illegal chess capture!");
        }
        // TODO: Finish the method.
        removeChessPiece(dest);
        moveChessPiece(src, dest);// capture the chess piece
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public PlayerColor getChessPieceOwner(ChessboardPoint point) {
        return getGridAt(point).getPiece().getOwner();
    }

    public boolean isValidMove(ChessboardPoint src, ChessboardPoint dest) {
        if (getChessPieceAt(src) == null) {
            return false;
        } // if the source is empty , return false
        if (dest.getName().equals("River")) {
            if (!getChessPieceAt(src).getName().equals("Rat")) {
                return false;
            }
        }
        if (calculateDistance(src, dest) != 1) {
            if (getChessPieceAt(src).getName().equals("Lion") || getChessPieceAt(src).getName().equals("Tiger")) {
                if(dest.getName().equals("River")){
                    return false;
                }
                if (src.getCol() == dest.getCol()) {
                    int row = src.getRow();
                    int col = src.getCol();
                    if (row > dest.getRow()) {
                        row--;
                    } else {
                        row++;
                    }
                    while (row != dest.getRow()) {
                        if (getChessPieceAt(new ChessboardPoint(row, col)) != null) {
                            return false;
                        }
                        if(!SharedData.chessboardPointMap.get(row*10+col).getName().equals("River")){
                            return false;
                        }
                        if (row > dest.getRow()) {
                            row--;
                        } else {
                            row++;
                        }
                    }
                    return true;
                }
                if (src.getRow() == dest.getRow()) {
                    int row = src.getRow();
                    int col = src.getCol();
                    if (col > dest.getCol()) {
                        col--;
                    } else {
                        col++;
                    }
                    while (col != dest.getCol()) {
                        if (getChessPieceAt(new ChessboardPoint(row, col)) != null) {
                            return false;
                        }
                        if (!SharedData.chessboardPointMap.get(row * 10 + col).getName().equals("River")) {
                            return false;
                        }
                        if (col > dest.getCol()) {
                            col--;
                        } else {
                            col++;
                        }
                    }
                    return true;
                }
            }
        }
        return calculateDistance(src, dest) == 1;
    }

    public boolean isNull(ChessboardPoint dest) {
        if (getChessPieceAt(dest) == null) {
            return true;
        }
        return false;
    }

    public boolean isValidCapture(ChessboardPoint src, ChessboardPoint dest) {
        // TODO:Fix this method
        if (!isValidMove(src, dest)) {
            return false;
        } // if the move is not valid, return false
        if (getChessPieceAt(src).canCapture(getChessPieceAt(dest))) {
            if (src.getName().equals("River") && getChessPieceAt(src).getName().equals("Rat")) {
                return false;
            }
            return true;
        }
        return false;

    }

    public void recordStep(ChessboardPoint src, ChessboardPoint dest, int turn, AnimalChessComponent animalChessComponent){
        ChessPiece test=getChessPieceAt(dest);
        Step step = new Step(src, dest, getChessPieceAt(src), getChessPieceAt(dest), turn, animalChessComponent);
        SharedData.stepList.add(step);
    }

    public void findPossibleStep(ChessboardPoint selectedPoint){
        SharedData.possibleMoveList.clear();
        SharedData.possibleCaptureList.clear();
        for(int i=0;i<9;i++){
            for(int j=0;j<7;j++){
                if(isValidMove(selectedPoint,SharedData.chessboardPointMap.get(i*10+j))){
                    SharedData.possibleMoveList.add(SharedData.chessboardPointMap.get(i*10+j));
                }
                if(isValidCapture(selectedPoint,SharedData.chessboardPointMap.get(i*10+j))){
                    SharedData.possibleCaptureList.add(SharedData.chessboardPointMap.get(i*10+j));
                }
            }
        }
    }

    public void restart() {
        SharedData.stepList.clear();
        this.initGrid();
        this.initPieces();
    }

}
