package view;

import controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame {
    //    public final Dimension FRAME_SIZE ;
    private final int WIDTH;
    private final int HEIGHT;

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    private GameController gameController;
    JLabel background;
    JButton loginButton;
    private final int ONE_CHESS_SIZE;

    //    public final JLabel earthBG;
//    public final JLabel spaceXBG;
//    public final JLabel BG;
    private ChessboardComponent chessboardComponent;

    public ChessGameFrame(int width, int height) {
        setTitle("斗兽棋"); //设置标题
        this.WIDTH = width;
        this.HEIGHT = height;
        this.ONE_CHESS_SIZE = (HEIGHT * 4 / 5) / 9;

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);


        addChessboard();
        addAccountButton();
        addUndoButton();
        addSaveButton();

        addSettingsList();
        Thread t = new Thread(new BGM());
        t.start();
        addBackground();

    }


    public ChessboardComponent getChessboardComponent() {
        return chessboardComponent;
    }

    public void setChessboardComponent(ChessboardComponent chessboardComponent) {
        this.chessboardComponent = chessboardComponent;
    }

    /**
     * 在游戏面板中添加棋盘
     */
    private void addChessboard() {
        chessboardComponent = new ChessboardComponent(ONE_CHESS_SIZE);
        chessboardComponent.setLocation(HEIGHT / 5, HEIGHT / 10);
        add(chessboardComponent);
    }

    /**
     * 在游戏面板中添加标签
     */

    private void addBackground() {

        Image image = new ImageIcon("resource/BackgroundPicture/rocket.jpg").getImage();
        image = image.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(image);
        background = new JLabel(icon);
        background.setSize(WIDTH, HEIGHT);
        background.setLocation(0, 0);
        this.add(background);
        this.revalidate();
        this.repaint();
    }

    private void addSaveButton() {
        JButton button = new JButton("Save");
        button.setLocation(HEIGHT, HEIGHT / 10 + 40);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.addActionListener(e -> {
            System.out.println("Click save");
           gameController.loading();
        });

       button.addActionListener(e -> {
           System.out.println("Click save");
           gameController.Save();
         });}

    private void addUndoButton() {
        JButton button = new JButton("Undo");
        button.addActionListener((e) -> JOptionPane.showMessageDialog(this, "菜就一个字"));
        button.setLocation(HEIGHT, HEIGHT / 10 + 120);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.addActionListener(e -> {
            System.out.println("Click undo");
            gameController.undo();
        });
    }





    //settingsList
    private void addSettingsList() {
        JButton button = new JButton("Settings");
        button.setLocation(HEIGHT, HEIGHT / 10 + 260);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.addActionListener(e -> {
            gameController.loading();
            String[] options = {"Language", "BGM", "Background"};
            String[] languageOptions = {"Language-Chinese", "Language-English", "Language-Japanese"};
            String[] bgmOptions = {"BGM-Baby", "BGM-Let me love you", "BGM-summer Paris"};
            String[] backgroundOptions = {"Background-lovely duck", "Background-earth", "Background-spaceX"};
            JPanel panel = new JPanel();
            JComboBox<String> optionList = new JComboBox<>(options);
            optionList.addActionListener(evt -> {
                JComboBox<String> selectedList;
                switch ((String) optionList.getSelectedItem()) {
                    case "Language":
                        selectedList = new JComboBox<>(languageOptions);
                        break;
                    case "BGM":
                        selectedList = new JComboBox<>(bgmOptions);
                        break;
                    case "Background":
                        selectedList = new JComboBox<>(backgroundOptions);
                        break;
                    default:
                        return;
                }
                panel.removeAll();
                panel.add(selectedList);
                panel.revalidate();
                panel.repaint();
            });
            panel.add(optionList);
            JOptionPane.showMessageDialog(null, panel, "Settings", JOptionPane.PLAIN_MESSAGE);
        });

    }

    private void addAccountButton() {
        JButton button = new JButton("Account");
        button.setLocation(HEIGHT, HEIGHT / 10 + 440);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.addActionListener(e -> {
            gameController.restart();
            LoginDialog loginDialog = new LoginDialog(this);
            loginDialog.setVisible(true);
        });

    }
}






//        JButton button = new JButton("Settings");
//        button.setLocation(HEIGHT, HEIGHT / 10 + 260);
//        button.setSize(200, 60);
//        button.setFont(new Font("Rockwell", Font.BOLD, 20));
//        add(button);
//        button.addActionListener(e ->
//        {
//            JComboBox<String> settingsList = new JComboBox<>();
//            settingsList.addItem("Settings");
//            settingsList.addItem("Language");
//            settingsList.addItem("Mode");
//            settingsList.addItem("Music");
//            settingsList.addItem("Background");
//            settingsList.addActionListener((f) ->
//            {
//                String selectedItem = (String) settingsList.getSelectedItem();
//                switch (selectedItem) {
//                    case "Language":
//                        JComboBox<String> languageList = new JComboBox<>();
//                        languageList.addItem("Chinese");
//                        languageList.addItem("English");
//                        JOptionPane.showMessageDialog(this, languageList);
//                        break;
//                    case "Mode":
//                        JComboBox<String> modeList = new JComboBox<>();
//                        modeList.addItem("AI");
//                        modeList.addItem("Online opponent");
//                        JOptionPane.showMessageDialog(this, modeList);
//                        break;
//                    case "Music":
//                        JComboBox<String> musicList = new JComboBox<>();
//                        musicList.addItem("Drowning");
//                        musicList.addItem("Baby");
//                        JOptionPane.showMessageDialog(this, musicList);
//                        break;
//                    case "Background":
//                        JComboBox<String> backgroundList = new JComboBox<>();
//                        backgroundList.addItem("SpaceX");
//                        backgroundList.addItem("earth");
//                        JOptionPane.showMessageDialog(this, backgroundList);
//                        break;
//                    default:
//                        break;
//            }
//        });
//        settingsList.setLocation(HEIGHT, HEIGHT / 10 + 340);
//        settingsList.setSize(200, 60);
//        settingsList.setFont(new Font("Rockwell", Font.BOLD, 20));
//        add(settingsList);
//        this.revalidate();
//        this.repaint();
//
//    });










//            // 创建一个按钮
//            JButton button = new JButton("Click me");
//            button.setBounds(50, 50, 100, 30);

            // 将按钮添加到 JFrame 中
//            frame.add(button);



            // 调用 JFrame 的 revalidate() 和 repaint() 方法，重新布局并绘制组件
//            this.revalidate();
//            frame.repaint();

            // 显示 JFrame
//            frame.setVisible(true);











//    private void addLoadButton() {
//        JButton button = new JButton("Load");
//        button.setLocation(HEIGHT, HEIGHT / 10 + 240);
//        button.setSize(200, 60);
//        button.setFont(new Font("Rockwell", Font.BOLD, 20));
//        add(button);
//
//        button.addActionListener(e -> {
//            System.out.println("Click load");
//            String path = JOptionPane.showInputDialog(this,"Input Path here");
//            gameController.loadGameFromFile(path);
//        });
//    }



