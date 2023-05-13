package view.Dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginDialog extends JDialog {
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton okButton;
    private JButton cancelButton;

    public LoginDialog(JFrame owner) {
        super(owner, "Login Dialog", true);
        setSize(300, 200);
        setLocationRelativeTo(null);

        // 创建用户名和密码标签以及对应的输入框
        usernameLabel = new JLabel("Username:");
        passwordLabel = new JLabel("Password:");
        usernameField = new JTextField(10);
        passwordField = new JPasswordField(10);

        // 创建确认和取消按钮
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");

        // 将用户名、密码标签和对应的输入框以及确认、取消按钮添加到登录对话框中
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(okButton);
        panel.add(cancelButton);
        add(panel);

        // 为确认和取消按钮添加点击事件
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 在此处编写登录逻辑
                dispose(); // 关闭登录对话框
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // 关闭登录对话框
            }
        });
    }
}
