package view;

import controller.GameController;
import view.Clock.clock.ClockFrame;
import view.Dialog.LoginDialog;
import view.Dialog.ModeDialog;

import javax.swing.*;
import java.awt.*;


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
    JButton ModeStatusButton;

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
        addLoadButton();
        addPlaybackButton();//这个原来是restart？
        addUndoButton();
        addSaveButton();
        addModeButton();
        addExitButton();
        addSettingsButton();
        addModeStatusButton();


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
        button.setLocation(HEIGHT, HEIGHT / 10 + 40);
        button.setSize(160, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.addActionListener(e -> {
            System.out.println("Click save");
//            String path = JOptionPane.showInputDialog("存档名(Format:)");
            new BGMofClick().PlayClickBGM("resource/Music/ding.wav");
            JOptionPane.showMessageDialog(null, "存档成功!!!", "存档成功", JOptionPane.INFORMATION_MESSAGE);
            gameController.Save();
        });
    }

    private void addLoadButton() {
        JButton button = new JButton("Load");
        button.setLocation(HEIGHT, HEIGHT / 10 + 100);
        button.setSize(160, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click load");
            String path = JOptionPane.showInputDialog("加载档名(Format:)");
            while (path.equals("")){
                JOptionPane.showMessageDialog(null, "加载档名不能为空");
                path = JOptionPane.showInputDialog("加载档名Format: ");
            }

            if(!path.endsWith(".txt")){
                JOptionPane.showMessageDialog(null, "文档格式错误\n导致无法识别文档",
                        "文件路径后缀错误", JOptionPane.ERROR_MESSAGE);
                System.out.println("文档路径后缀不为.txt");
                System.out.println("后缀错误");
            }

            try {

            } catch (Exception exception) {
                exception.printStackTrace();
            }

            gameController.loading();
        });
    }
    private void addPlaybackButton() {
        JButton button = new JButton("Restart");
        button.setLocation(HEIGHT, HEIGHT / 10 + 160);
        button.setSize(160, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.addActionListener(e -> {
            System.out.println("Click restart");
            gameController.restart();
        });
    }
    private void addUndoButton() {
        JButton button = new JButton("Undo");
        button.addActionListener((e) -> JOptionPane.showMessageDialog(this, "就知道你会后悔的"));
        button.setLocation(HEIGHT, HEIGHT / 10 + 220);
        button.setSize(160, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.addActionListener(e -> {
            System.out.println("Click undo");
            gameController.undo();
        });
    }

    private void addModeButton() {
        JButton button = new JButton("Mode");
        button.setLocation(HEIGHT, HEIGHT / 10 + 280);
        button.setSize(160, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.addActionListener(e -> {
            ModeDialog modeDialog = new ModeDialog(this);
            modeDialog.setVisible(true);
        });
    }
        private void addAIButton() {
        JButton button = new JButton("AI");
        button.setLocation(HEIGHT - 100, HEIGHT / 10 + 400);
        button.setSize(160, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.addActionListener(e -> {
            System.out.println("Click AI");
//            gameController.AIPlay2();
        });
    }


    private void  addSettingsButton() {
        JButton button = new JButton("Settings");
        button.setLocation(HEIGHT, HEIGHT / 10 + 340);
        button.setSize(160, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.addActionListener(e -> {
            String[] options = {"BGM", "Theme","Clock"};
            String[] bgmOptions = {"Begin", "Close"};
            String[] themeBackgroundOptions = {"rocket", "earth", "space"};
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
                    case "Theme":
                        selectedList = new JComboBox<>(themeBackgroundOptions);
                        selectedList.addActionListener(Backgrounde -> {
                            switch ((String) selectedList.getSelectedItem()) {
                                case "rocket":
                                    changBackground(backgroundPath[0]);
                                    break;
                                case "earth":
                                    changBackground(backgroundPath[1]);
                                    break;
                                case "space":
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
        button.setLocation(HEIGHT, HEIGHT / 10 + 400);
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
       add(TimeCounterButton);
    }

    private void addModeStatusButton(){
        ModeStatusButton = new JButton("Mode: General");
        ModeStatusButton.setLocation(HEIGHT-70, HEIGHT / 10 + 460);
        ModeStatusButton.setSize(300, 40);
        ModeStatusButton.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(ModeStatusButton);
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


    public String getModeString(){
        int temp = this.gameController.getAiStatus();
        if(temp == 0){
            return "AI - Easy";
        }else if(temp == 1){
            return "AI - Medium";
        }else if(temp == 2) {
            return "AI - Hard";
        }else {
            return "General";
        }
    }



}









