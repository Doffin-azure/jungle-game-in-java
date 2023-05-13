package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SharedData {
    public static Map<Integer, ChessboardPoint> chessboardPointMap = new HashMap<>();
    public static ArrayList<Step> stepList = new ArrayList<>();
    public static ArrayList<Step> loadStepList = new ArrayList<>();
    public static ArrayList<ChessPiece> chessPieceList = new ArrayList<>();

    public static ArrayList<ChessboardPoint> possibleMoveList = new ArrayList<>();

    public static ArrayList<ChessboardPoint> possibleCaptureList = new ArrayList<>();

    public static Map<Integer, Step> possibleMoveMap = new HashMap<>();

    public static Map<Integer, Step> RandomMoveMapWithValue = new HashMap<>();


}