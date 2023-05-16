//收个页面

package view;

import javax.swing.*;
import java.awt.*;

public class FirstFrame extends JFrame {
    private JLabel backgroundLabel;

    public ChessGameFrame getGameFrame() {
        return gameFrame;
    }

    ChessGameFrame gameFrame;
    JLabel background0;

    //AIFrame aiFrame;

    private final int WIDTH;
    private final int HEIGHT;


    public FirstFrame() {
        setTitle("Jungle");
        this.WIDTH = 600;
        this.HEIGHT = 750;

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);

        ChessGameFrame gameFrame = new ChessGameFrame(1100, 810);
        this.gameFrame = gameFrame;

        addGeneralModeButton();
        addAIModeButton();
        addBackground();

    }

    private void addGeneralModeButton() {
        JButton button = new JButton("General Mode ");
        button.addActionListener((e) -> {
            this.setVisible(false);
            gameFrame.setVisible(true);
            gameFrame.getClock().setVisible(true);
            beginTimer();
        });
        button.setLocation(150, 150);
        button.setSize(300, 120);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void addAIModeButton() {
        JButton button = new JButton("AI Mode");

        //新建一个AI模式选择框
        JFrame SelectAIFrame = new JFrame();
        SelectAIFrame.setTitle("难度选择");
        SelectAIFrame.setSize(600, 750);
        SelectAIFrame.setLocationRelativeTo(null); // Center the window.
        SelectAIFrame.setDefaultCloseOperation(HIDE_ON_CLOSE); //设置关闭按键
        SelectAIFrame.setLayout(null);


        //Easy模式按钮
        JButton buttonEasy = new JButton("Easy ");
        buttonEasy.addActionListener((e) -> {
            SelectAIFrame.setVisible(false);
            gameFrame.gameController.setAiStatus(0);
            gameFrame.ModeStatusButton.setText("AI-Easy Mode");
            gameFrame.setVisible(true);
            gameFrame.getClock().setVisible(true);
            beginTimer();
        });
        buttonEasy.setLocation(150, 150);
        buttonEasy.setSize(300, 120);
        buttonEasy.setFont(new Font("Rockwell", Font.BOLD, 20));
        SelectAIFrame.add(buttonEasy);
        //Middle模式按钮
        JButton buttonMedium = new JButton("Medium");
        buttonMedium.addActionListener((e) -> {
            SelectAIFrame.setVisible(false);
            gameFrame.gameController.setAiStatus(2);
            gameFrame.ModeStatusButton.setText("AI-Medium Mode");
            gameFrame.setVisible(true);
            beginTimer();
        });
        buttonMedium.setLocation(150, 300);
        buttonMedium.setSize(300, 120);
        buttonMedium.setFont(new Font("Rockwell", Font.BOLD, 20));
        SelectAIFrame.add(buttonMedium);
        //Hard模式按钮
        JButton buttonHard = new JButton("Hard");
        buttonHard.addActionListener((e) -> {
            SelectAIFrame.setVisible(false);
            gameFrame.gameController.setAiStatus(1);
            gameFrame.ModeStatusButton.setText("AI-Hard Mode");
            gameFrame.setVisible(true);
            gameFrame.getClock().setVisible(true);
            beginTimer();

        });
        buttonHard.setLocation(150, 450);
        buttonHard.setSize(300, 120);
        buttonHard.setFont(new Font("Rockwell", Font.BOLD, 20));
        SelectAIFrame.add(buttonHard);


        button.addActionListener((e) -> {
            this.setVisible(false);
            SelectAIFrame.setVisible(true);
            
        });

        button.setLocation(150, 450);
        button.setSize(300, 120);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        ChessGameFrame gameFrame = new ChessGameFrame(1100, 810);
        this.gameFrame = gameFrame;

    }


    public void beginTimer() {
        TimerCounter.timeOfThisTurn=30;
        if(this.gameFrame.gameController.timerCounter == null)
        {
            this.gameFrame.gameController.timerCounter = new TimerCounter(gameFrame.getChessboardComponent().gameController);
            this.gameFrame.gameController.timerCounter.start();
        }
    }
    public ChessGameFrame getChessBoardFrame() {
        return this.gameFrame;
    }

    public void addBackground() {
        Image image = new ImageIcon("resource/BackgroundPicture/space2.jpg").getImage();
        image = image.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(image);
        background0 = new JLabel(icon);
        background0.setSize(WIDTH, HEIGHT);
        background0.setLocation(0, 0);
        this.add(background0);
        this.revalidate();
        this.repaint();
    }

}
