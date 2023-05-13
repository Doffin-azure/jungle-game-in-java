package model;

import view.AnimalChessComponent;

public class Step {
    public int getValue() {
        return Value;
    }

    public void setValue(int value) {
        Value = value;
    }

    int Value = 0;
    private ChessboardPoint fromWhere;

    private ChessboardPoint toWhere;
    private ChessPiece MovedChessPiece;
    private ChessPiece CapturedChessPiece;

    public AnimalChessComponent getAcc() {
        return acc;
    }

    private AnimalChessComponent acc;
    private int turn;

    public ChessboardPoint getFrom() {
        return fromWhere;
    }

    public ChessboardPoint getTo() {
        return toWhere;
    }

    public ChessPiece getMovedChessPiece() {
        return MovedChessPiece;
    }

    public ChessPiece getCapturedChessPiece() {
        return CapturedChessPiece;
    }

    public int getTurn() {
        return turn;
    }

    public Step(ChessboardPoint fromWhere, ChessboardPoint toWhere, ChessPiece MovedChessPiece, ChessPiece CapturedChessPiece,
                int turn, AnimalChessComponent acc) {
        this.fromWhere = fromWhere;
        this.toWhere = toWhere;
        this.MovedChessPiece = MovedChessPiece;
        this.CapturedChessPiece = CapturedChessPiece;
        this.turn = turn;
        this.acc = acc;
    }

    @Override
    public String toString() {
        return "Step{" +
                "fromWhere=" + fromWhere +
                ", toWhere=" + toWhere +
                ", MovedChessPiece=" + MovedChessPiece +
                ", CapturedChessPiece=" + CapturedChessPiece +
                ", turn=" + turn +
                '}';
    }
}