package view.TimerFrame.clock;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RectTimer extends JLabel {
    private JLabel timeLabel;

    public RectTimer() {

        // 创建标签来显示时间
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Arial", Font.BOLD, 50));
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);


        // 创建一个计时器，每隔一秒更新一次时间标签
        Timer timer = new Timer(1000, e -> updateTime());
        timer.start();
    }

    private void updateTime() {
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        String t = df.format(new Date());
        timeLabel.setText(t);
    }


    }

