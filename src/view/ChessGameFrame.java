package view;

import controller.GameController;
import model.Chessboard;
import model.ChessboardPoint;
import model.SharedData;
import view.Clock.clock.ClockFrame;
import view.Dialog.LoginDialog;
import view.Dialog.ModeDialog;

import javax.swing.*;
import java.awt.*;

import static model.Constant.CHESSBOARD_COL_SIZE;
import static model.Constant.CHESSBOARD_ROW_SIZE;


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
    public FirstFrame firstFrame;
    JLabel background;
    JButton TurnStatusButton;
    JButton TimeCounterButton;
    JButton ModeStatusButton;

    public JButton turnButton;
    private String[] backgroundPath = {"resource/BackgroundPicture/Jungle.jfif",
            "resource/BackgroundPicture/space.jpg"};//背景图片路径
    private String[] bgmPath = {"Music/CLDY.wav"};//bgm路径
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
        addExitButton();
        addSettingsButton();
        addModeStatusButton();
        addRulesButton();


        bgm = new BGM(bgmPath[0]);
        t = new Thread(bgm);
        t.start();

        addBackground();

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
        chessboardComponent = new ChessboardComponent(ONE_CHESS_SIZE, TurnStatusButton, TimeCounterButton);
        chessboardComponent.setLocation(HEIGHT / 5, HEIGHT / 10);
        add(chessboardComponent);
    }

    /**
     * 在游戏面板中添加标签
     */

    private void addBackground() {

        Image image = new ImageIcon("resource/BackgroundPicture/Jungle.jfif").getImage();
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
            String path = JOptionPane.showInputDialog("存档名(Format:)");
            new BGMofClick().PlayClickBGM("resource/Music/ding.wav");
            JOptionPane.showMessageDialog(null, "存档成功!!!", "存档成功", JOptionPane.INFORMATION_MESSAGE);
            gameController.Save(path);
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
            while (path.equals("")) {
                JOptionPane.showMessageDialog(null, "加载档名不能为空");
                path = JOptionPane.showInputDialog("加载档名Format: ");
            }

            if (!path.endsWith(".txt")) {
                JOptionPane.showMessageDialog(null, "文档格式错误\n导致无法识别文档",
                        "文件路径后缀错误", JOptionPane.ERROR_MESSAGE);
                System.out.println("文档路径后缀不为.txt");
                System.out.println("后缀错误");
            }

            try {

            } catch (Exception exception) {
                exception.printStackTrace();
            }

            gameController.loading(path);
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

    private void addSettingsButton() {
        JButton button = new JButton("Settings");
        button.setLocation(HEIGHT, HEIGHT / 10 + 280);
        button.setSize(160, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.addActionListener(e -> {
            String[] options = {"BGM", "Theme", "Clock"};
            String[] bgmOptions = {"Begin", "Close"};
            String[] themeBackgroundOptions = {"jungle", "space"};
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
                                case "jungle":
                                    changBackground(backgroundPath[0]);
                                    changeDenAndTrapGif(2);
                                    break;
                                case "space":
                                    changBackground(backgroundPath[1]);
                                    changeDenAndTrapGif(1);
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
        button.setLocation(HEIGHT, HEIGHT / 10 + 340);
        button.setSize(160, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.addActionListener((e) -> {
            this.gameController.restart();
            this.setVisible(false);
            firstFrame.setVisible(true);
        });
    }

    private void addRulesButton() {
        JButton button = new JButton("Rules");
        button.setLocation(HEIGHT, HEIGHT / 10 + 400);
        button.setSize(160, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        JFrame rulesFrame = new JFrame("Rules");
        rulesFrame.setSize(800, 600);
        rulesFrame.setLocationRelativeTo(null);
        rulesFrame.setDefaultCloseOperation(HIDE_ON_CLOSE);
        ImageIcon rules = new ImageIcon("resource/BackgroundPicture/rules.png");
        rulesFrame.add(new JLabel(rules));
//        popup.getContentPane().add(label, BorderLayout.CENTER);
        rulesFrame.pack();
        button.addActionListener((e) ->
                rulesFrame.setVisible(true));

    }

    private void addClock() {
        clock = new ClockFrame();
        clock.setLocation(100, 100);
        clock.setVisible(false);
    }


//上面两个按钮

    private void addTurnButton() {
        TurnStatusButton = new JButton();
        TurnStatusButton.setText("Turn1 : Player Blue");
        TurnStatusButton.setLocation(WIDTH / 2, HEIGHT / 25);
        TurnStatusButton.setSize(300, 40);
        TurnStatusButton.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(TurnStatusButton);
    }

    private void addTimeCounterButton() {
        TimeCounterButton = new JButton("Time Remaining: 30");
        TimeCounterButton.setLocation(WIDTH / 10, HEIGHT / 25);
        TimeCounterButton.setSize(300, 40);
        TimeCounterButton.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(TimeCounterButton);
    }

    private void addModeStatusButton() {
        ModeStatusButton = new JButton("Mode: General");
        ModeStatusButton.setLocation(HEIGHT - 70, HEIGHT / 10 + 460);
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

    public void setTurnButtonText(String status) {
        this.turnButton.setText(status);
    }


    public String getModeString() {
        int temp = this.gameController.getAiStatus();
        if (temp == 0) {
            return "AI - Easy";
        } else if (temp == 1) {
            return "AI - Medium";
        } else if (temp == 2) {
            return "AI - Hard";
        } else {
            return "General";
        }
    }

    public void changeDenAndTrapGif(int type) {
        for (int i = 0; i < CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < CHESSBOARD_COL_SIZE.getNum(); j++) {
                ChessboardPoint temp = new ChessboardPoint(i, j);

                if (chessboardComponent.getTrapCell().contains(temp)) {
                    if (type == 2) {//2对应jungle
                        chessboardComponent.gridComponents[i][j].isTrap1 = false;
                        chessboardComponent.gridComponents[i][j].isTrap2 = true;
                    } else if (type == 1) {         //1对应space
                        chessboardComponent.gridComponents[i][j].isTrap2 = false;
                        chessboardComponent.gridComponents[i][j].isTrap1 = true;
                    }
                }

                else if (chessboardComponent.getDensCell().contains(temp)) {
                    if (type == 2) {//2对应jungle
                        chessboardComponent.gridComponents[i][j].isDen1 = false;
                        chessboardComponent.gridComponents[i][j].isDen2 = true;
                    } else if (type == 1) {//1对应space
                        chessboardComponent.gridComponents[i][j].isDen2 = false;
                        chessboardComponent.gridComponents[i][j].isDen1 = true;
                    }
                }


                else if (chessboardComponent.getRiverCell().contains(temp)) {
                    if (type == 1) {//2对应jungle
                        chessboardComponent.gridComponents[i][j].isRiver1 = false;
                        chessboardComponent.gridComponents[i][j].isRiver2  = true;
                    } else if (type == 2) {         //1对应space
                        chessboardComponent.gridComponents[i][j].isRiver2 = false;
                        chessboardComponent.gridComponents[i][j].isRiver1 = true;
                    }
                }
                
                else {
                    if (type == 1) {//2对应jungle
                        chessboardComponent.gridComponents[i][j].isGrass1 = false;
                        chessboardComponent.gridComponents[i][j].isGrass2  = true;
                    } else if (type == 2) {         //1对应space
                        chessboardComponent.gridComponents[i][j].isGrass2 = false;
                        chessboardComponent.gridComponents[i][j].isGrass1 = true;
                    }
                }
                
            }
        }


    }

//    public void setTurnButtonText(String message) {
//        this.TimeCounterButton.setText(message);
//    }
}











