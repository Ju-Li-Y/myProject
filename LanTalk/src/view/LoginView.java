package view;

import controller.control.ButtonMonitor;
import controller.control.KeyMonitor;
import model.bean.User;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    //创建user类对象，记录登录账号的学号/工号
    public static User user;

    public LoginView() {
        initComponents();
        setComponentsName();
        addActionListener();
        setVisible(true);
    }

    /** 给需要监听的控件添加对应自定义监听器 */
    private void addActionListener() {
        //监听点击事件
        btn_login.addActionListener(ButtonMonitor.btnListener);
        //监听回车
        field_account.addKeyListener(KeyMonitor.keyListener);
        field_password.addKeyListener(KeyMonitor.keyListener);
    }

    /** 给需要监听的控件设置name，以便识别 */
    private void setComponentsName() {
        btn_login.setName("login_login");
        field_account.setName("login_account");
        field_password.setName("login_password");
    }

    /** 获取登陆界面输入的用户名 */
    public String getAccount() {
        return field_account.getText();
    }

    /** 获取登陆界面输入的密码 */
    public String getPassword() {
        return new String(field_password.getPassword());
    }

    /** 清空输入框 */
    public void clearText() {
        field_account.setText("");
        field_password.setText("");
        field_account.requestFocus();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        label3 = new JLabel();
        label1 = new JLabel();
        field_account = new JTextField();
        label2 = new JLabel();
        field_password = new JPasswordField();
        btn_login = new JButton();

        //======== this ========
        setTitle("\u767b\u5f55");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        var contentPane = getContentPane();

        //---- label3 ----
        label3.setText("\u5c40\u57df\u7f51\u901a\u4fe1\u8f6f\u4ef6");
        label3.setFont(new Font("\u5b8b\u4f53", Font.BOLD, 24));
        label3.setForeground(new Color(102, 153, 255));

        //---- label1 ----
        label1.setText("\u8d26\u53f7\uff1a");
        label1.setIcon(new ImageIcon(getClass().getResource("/content/iamges/account.png")));
        label1.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 16));

        //---- label2 ----
        label2.setText("\u5bc6\u7801\uff1a");
        label2.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 16));
        label2.setIcon(new ImageIcon(getClass().getResource("/content/iamges/password.png")));

        //---- btn_login ----
        btn_login.setText("\u767b\u5f55");

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                          .addGroup(contentPaneLayout.createParallelGroup()
                                                            .addGroup(contentPaneLayout.createSequentialGroup()
                                                                              .addGap(89, 89, 89)
                                                                              .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                                                                .addGroup(contentPaneLayout.createSequentialGroup()
                                                                                                                  .addComponent(label1)
                                                                                                                  .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                  .addComponent(field_account, GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE))
                                                                                                .addGroup(contentPaneLayout.createSequentialGroup()
                                                                                                                  .addComponent(label2)
                                                                                                                  .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                  .addComponent(field_password, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
                                                            .addGroup(contentPaneLayout.createSequentialGroup()
                                                                              .addGap(151, 151, 151)
                                                                              .addComponent(btn_login))
                                                            .addGroup(contentPaneLayout.createSequentialGroup()
                                                                              .addGap(109, 109, 109)
                                                                              .addComponent(label3)))
                                          .addContainerGap(95, Short.MAX_VALUE))
        );
        contentPaneLayout.linkSize(SwingConstants.HORIZONTAL, new Component[]{field_account, field_password});
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                          .addGap(24, 24, 24)
                                          .addComponent(label3, GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                                          .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                          .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                            .addComponent(field_account, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(label1, GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE))
                                          .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                          .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                            .addComponent(field_password, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(label2, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                                          .addGap(27, 27, 27)
                                          .addComponent(btn_login)
                                          .addGap(34, 34, 34))
        );
        contentPaneLayout.linkSize(SwingConstants.VERTICAL, new Component[]{label1, label2});
        setSize(400, 300);
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel label3;
    private JLabel label1;
    private JTextField field_account;
    private JLabel label2;
    private JPasswordField field_password;
    private JButton btn_login;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
