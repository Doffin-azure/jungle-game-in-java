package view.Dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModeDialog extends JDialog {


    private JButton GeneralButton;
    private JButton AIButton;
    public ModeDialog(JFrame owner) {
        super(owner, "Mode Dialog", true);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // 创建普通模式和AI模式按钮
        GeneralButton = new JButton("General");
        AIButton = new JButton("AI");


        JPanel panel = new JPanel(new GridLayout(2, 1,5,5));
        panel.add(GeneralButton);
        panel.add(AIButton);
        add(panel);

        // 为确认和取消按钮添加点击事件
        GeneralButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(GeneralButton, "Dear, Now it is already General Mode");
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
