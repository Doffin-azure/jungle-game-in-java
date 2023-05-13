package view;
import controller.GameController;
import model.PlayerColor;

public class TimerCounter extends Thread{
    public static int timeOfThisTurn = 30;
    public GameController gameController;

    public TimerCounter(GameController controller){
        this.gameController = controller;
    }
    @Override
    public void run(){
        synchronized (this){
            while(true){
                PlayerColor player = gameController.currentPlayer;
                boolean b = true;
                while(timeOfThisTurn > 0){
                    timeOfThisTurn--;
                    try{
                        Thread.sleep(1000);
                        gameController.TimerCounterButton.setText("Time Remained:"+timeOfThisTurn);
                        if(gameController.currentPlayer != player){
                            gameController.swapColor();
                            b = false;
                            break;
                        }
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                timeOfThisTurn = 30;

                if(b){
                    ///controller.swapColor();
                    ///AI接口
                }

            }
        }
    }
}
