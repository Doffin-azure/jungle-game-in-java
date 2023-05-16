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
                while(timeOfThisTurn > 0){
                    timeOfThisTurn--;
                    try{
                        Thread.sleep(1000);
                        gameController.TimerCounterButton.setText("Time Remained:"+timeOfThisTurn);
                        if(timeOfThisTurn == 0){
                            gameController.swapColor();
                            gameController.setCount(gameController.getCount()+1);
                        }
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                timeOfThisTurn = 30;
            }
        }
    }

    public static int getTimeOfThisTurn() {
        return timeOfThisTurn;
    }

    public static void setTimeOfThisTurn(int timeOfThisTurn) {
        TimerCounter.timeOfThisTurn = timeOfThisTurn;
    }
}
