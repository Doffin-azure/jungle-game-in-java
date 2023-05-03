package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ModeDialog extends JDialog {


    private JButton GeneralButton;
    private JButton AIButton;
    public ModeDialog(JFrame owner) {
        super(owner, "Settings Dialog", true);
        setSize(300, 200);
        setLocationRelativeTo(null);

        // 创建用户名和密码标签以及对应的输入框


        // 创建确认和取消按钮
        GeneralButton = new JButton("General");
        AIButton = new JButton("AI");

        // 将用户名、密码标签和对应的输入框以及确认、取消按钮添加到登录对话框中
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(new JLabel());
        panel.add(GeneralButton);
        panel.add(AIButton);
        add(panel);

        // 为确认和取消按钮添加点击事件
        GeneralButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 在此处编写登录逻辑
                dispose(); // 关闭登录对话框
            }
        });

        AIButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 在此调用AI
                dispose(); // 关闭登录对话框
            }
        });
    }
}
