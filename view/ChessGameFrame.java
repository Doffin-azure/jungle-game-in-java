package view;

import controller.GameController;
import view.Clock.clock.ClockFrame;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame {
    //    public final Dimension FRAME_SIZE ;
    private final int WIDTH;
    private final int HEIGHT;
    private ClockFrame clock;
    private Thread t;
    public GameController gameController;
    private BGM bgm;
    JLabel background;
    JButton TurnStatusButton;
    JButton TimeCounterButton;

    public JButton turnButton;
    private String[] backgroundPath={"resource/BackgroundPicture/rocket.jpg",
            "resource/BackgroundPicture/earth.png",
            "resource/BackgroundPicture/space.jpg"
    };//背景图片路径
    private String[] bgmPath={"Music/baby.wav",
    };//bgm路径
    private final int ONE_CHESS_SIZE;

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


        //try to make clock always showing



        addTurnButton();
        addTimeCounterButton();
        addChessboard();
        addClock();
        //右侧按钮
        addStopButton();
        addAccountButton();
        addLoadButton();
        addPlaybackButton();
        addUndoButton();
        addSaveButton();
        addModeButton();
        addRestartButton();
        addExitButton();
        addSettingsButton();


//        clock.setLocationRelativeTo(this);
        //上方按钮


        bgm = new BGM(bgmPath[0]);
        t = new Thread(bgm);
        t.start();
        bgm.stopBGM();

        addBackground();
        changBackground(backgroundPath[2]);

        }




    //settingsList

    private void changBackground(String backgroundPath) {
        background.setIcon(new ImageIcon(backgroundPath));
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
        chessboardComponent = new ChessboardComponent(ONE_CHESS_SIZE,TurnStatusButton,TimeCounterButton);
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
//侧面按钮

    private void addSaveButton() {
        JButton button = new JButton("Save");
        button.setLocation(HEIGHT, HEIGHT / 10 + 10);
        button.setSize(160, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }
    private void addLoadButton() {
        JButton button = new JButton("Load");
        button.setLocation(HEIGHT, HEIGHT / 10 + 70);
        button.setSize(160, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click load");
            String path = JOptionPane.showInputDialog(this,"Input Path here");
//            gameController.loadGameFromFile(path);
        });
    }
    private void addPlaybackButton() {
        JButton button = new JButton("Playback");
        button.setLocation(HEIGHT, HEIGHT / 10 + 130 );
        button.setSize(160, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void addUndoButton() {
        JButton button = new JButton("Undo");
        button.addActionListener((e) -> JOptionPane.showMessageDialog(this, "傻逼"));
        button.setLocation(HEIGHT, HEIGHT / 10 + 190 );
        button.setSize(160, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void addStopButton() {
        JButton button = new JButton("Stop");
        button.setLocation(HEIGHT, HEIGHT / 10 + 250);
        button.setSize(160, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.addActionListener((e) -> JOptionPane.showMessageDialog(this, "我是傻逼傻逼吗"));
    }
    private void addRestartButton() {
        JButton button = new JButton("Restart");
        button.setLocation(HEIGHT, HEIGHT / 10 + 310);
        button.setSize(160, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.addActionListener(e -> {
            ModeDialog modeDialog = new ModeDialog(this);
            modeDialog.setVisible(true);
        });
    }




    private void addModeButton() {
        JButton button = new JButton("Mode");
        button.setLocation(HEIGHT, HEIGHT / 10 + 370);
        button.setSize(160, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.addActionListener(e -> {
            ModeDialog modeDialog = new ModeDialog(this);
            modeDialog.setVisible(true);
        });
    }


    private void addAccountButton() {
        JButton button = new JButton("Account");
        button.setLocation(HEIGHT, HEIGHT / 10 + 430);
        button.setSize(160, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.addActionListener(e -> {
            LoginDialog loginDialog = new LoginDialog(this);
            loginDialog.setVisible(true);
        });

    }

    private void  addSettingsButton() {
        JButton button = new JButton("Settings");
        button.setLocation(HEIGHT, HEIGHT / 10 + 490);
        button.setSize(160, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.addActionListener(e -> {
            String[] options = {"Language", "BGM", "Background","Clock","Color"};
            String[] bgmOptions = {"Begin", "Close"};
            String[] backgroundOptions = {"Background-rocket", "Background-earth", "Background-space"};
            String[] ClockOptions = {"Clock-Visible", "Clock-invisible"};

            JPanel panel = new JPanel();
            JComboBox<String> optionList = new JComboBox<>(options);
            optionList.addActionListener(evt -> {
                JComboBox<String> selectedList;
                switch ((String) optionList.getSelectedItem()) {
                    case "BGM":
                        selectedList = new JComboBox<>(bgmOptions);
                        selectedList.addActionListener(Bgmmmmme -> {
                            switch ((String) selectedList.getSelectedItem()) {
                                case "Begin":
                                    bgm.stopBGM();
                                    bgm.setBGMPath(bgmPath[0]);
                                    bgm.startBGM();
                                    break;

                                case "Close":
                                    bgm.stopBGM();
                                    break;
                                default:
                            }
                        });
                        panel.removeAll();
                        panel.add(selectedList);
                        panel.revalidate();
                        panel.repaint();


                        break;
                    case "Background":
                        selectedList = new JComboBox<>(backgroundOptions);
                        selectedList.addActionListener(Backgrounde -> {
                            switch ((String) selectedList.getSelectedItem()) {
                                case "Background-rocket":
                                    changBackground(backgroundPath[0]);
                                    break;
                                case "Background-earth":
                                    changBackground(backgroundPath[1]);
                                    break;
                                case "Background-space":
                                    changBackground(backgroundPath[2]);
                                    break;
                                default:
                            }
                        });
                        panel.removeAll();
                        panel.add(selectedList);
                        panel.revalidate();
                        panel.repaint();

                        break;

                    case "Clock":
                        selectedList = new JComboBox<>(ClockOptions);
                        selectedList.addActionListener(Clocke -> {
                            switch ((String) selectedList.getSelectedItem()) {
                                case "Clock-Visible":
                                    clock.setVisible(true);
                                    break;
                                case "Clock-invisible":
                                    clock.setVisible(false);
                                    break;

                                default:
                            }
                        });
                        panel.removeAll();
                        panel.add(selectedList);
                        panel.revalidate();
                        panel.repaint();

                    case "Color":



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
    private void addExitButton() {
        JButton button = new JButton("Exit");
        button.setLocation(HEIGHT, HEIGHT / 10 + 550);
        button.setSize(160, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.addActionListener((e) ->
                System.exit(0));}


    private void addClock() {
        clock = new ClockFrame();
        clock.setLocation(100, 100);
        clock.setVisible(false);
    }



//上面两个按钮

    private void addTurnButton() {
        TurnStatusButton = new JButton();
        TurnStatusButton.setText("Turn1 : Player Blue");
        TurnStatusButton.setLocation(WIDTH/2, HEIGHT / 25 );
        TurnStatusButton.setSize(300, 40);
        TurnStatusButton.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(TurnStatusButton);
    }


    private void addTimeCounterButton() {
       TimeCounterButton = new JButton("Time Remaining: 30");
       TimeCounterButton.setLocation(WIDTH/10, HEIGHT / 25);
       TimeCounterButton.setSize(300, 40);
       TimeCounterButton.setFont(new Font("Rockwell", Font.BOLD, 20));

//        if(gameController.timerCounter == null){
//            gameController.timerCounter = new TimerCounter(this.getChessboardComponent().gameController);
//            gameController.timerCounter.start();
//        }


        add(TimeCounterButton);
    }
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
    public ClockFrame getClock() {
        return clock;
    }

    public void setTurnButtonText(String status){
        this.turnButton.setText(status);
    }



}




//        JButton button = new JButton("Settings");
//        button.setLocation(HEIGHT, HEIGHT / 10 + 260);
//        button.setSize(160, 60);
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
//        settingsList.setSize(160, 60);
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













