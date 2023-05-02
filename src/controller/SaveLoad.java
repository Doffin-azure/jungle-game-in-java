package controller;

import model.*;
import java.io.*;
import java.util.regex.Pattern;

import model.*;

public class SaveLoad {
    private Chessboard cb;

    public SaveLoad(Chessboard cb) {
        this.cb = cb;
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

    public void Load() {
        try (BufferedReader br = new BufferedReader(new FileReader("save.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                Pattern pattern = Pattern.compile("\\d+");
                java.util.regex.Matcher matcher = pattern.matcher(line);
                int count = 0;
                int[] arr = new int[5];
                while (matcher.find() && count < 5) {
                    arr[count] = Integer.parseInt(matcher.group());
                    count++;
                }

                ChessboardPoint src = new ChessboardPoint(arr[0], arr[1]);
                ChessboardPoint dest = new ChessboardPoint(arr[2], arr[3]);
                int turn = arr[4];
                Step step = new Step(src, dest, null, null, turn);
                cb.doStep(step);
                SharedData.loadStepList.add(step);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
