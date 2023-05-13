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


//        这是什么东西？
//        gameFrame.beginFrame = this;
//        this.aiFrame = new AIFrame();
//        aiFrame.beginFrame = this;

        addGeneralModeButton();
        addAIModeButton();
        addBackground();

    }

    private void addGeneralModeButton() {
        JButton button = new JButton("General AI Mode ");
        button.addActionListener((e) -> {
            this.setVisible(false);
            gameFrame.setVisible(true);
            gameFrame.getClock().setVisible(true);


        });
        button.setLocation(150, 150);
        button.setSize(300, 120);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void addAIModeButton() {
        JButton button = new JButton("AI Mode");
        button.addActionListener((e) -> {
            this.setVisible(false);
//            gameFrame.getBoardView().controller.AIPlaying = true;
//            aiFrame.setVisible(true);

        });
        button.setLocation(150, 450);
        button.setSize(300, 120);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
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
