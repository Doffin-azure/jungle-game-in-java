package listener;

import model.ChessboardPoint;
import view.CellColorView;
import view.AnimalChessComponent;

public interface GameListener {

    void onPlayerClickCell(ChessboardPoint point, CellColorView component);


    void onPlayerClickChessPiece(ChessboardPoint point, AnimalChessComponent component);

}
