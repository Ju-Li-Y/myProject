/*
 * Created by JFormDesigner on Wed Jun 08 21:36:43 CST 2022
 */

package view;

import controller.control.ButtonMonitor;

import javax.swing.*;
import java.awt.*;

/**
 * @author JuLiy
 */
public class ServerMainView extends JFrame {
    public ServerMainView() {
        initComponents();
        addListener();
        setComponentsName();
        setVisible(true);
    }

    /** 添加事件监听器 */
    private void addListener() {
        btn_clearLog.addActionListener(ButtonMonitor.btnListener);
        btn_clearHistory.addActionListener(ButtonMonitor.btnListener);
    }

    /** 设置控件名称 */
    private void setComponentsName() {
        btn_clearLog.setName("server_clearLog");
        btn_clearHistory.setName("server_clearHistory");
    }

    /** 在文本域中显示信息 */
    public void showMsg(String str) {
        textArea.append(str + "\n");
    }

    /** 更新在线人数 */
    public void updateOnlineNumber(int number) {
        label_number.setText(String.valueOf(number));
    }

    /** 清空日志框 */
    public void clearLog() {
        textArea.setText("");
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        label1 = new JLabel();
        label_number = new JLabel();
        scrollPane1 = new JScrollPane();
        textArea = new JTextArea();
        btn_clearHistory = new JButton();
        btn_clearLog = new JButton();

        //======== this ========
        setTitle("\u670d\u52a1\u5668");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        var contentPane = getContentPane();

        //---- label1 ----
        label1.setText("\u5f53\u524d\u5728\u7ebf\u4eba\u6570\uff1a");
        label1.setHorizontalAlignment(SwingConstants.TRAILING);

        //---- label_number ----
        label_number.setText("0");

        //======== scrollPane1 ========
        {

            //---- textArea ----
            textArea.setEditable(false);
            scrollPane1.setViewportView(textArea);
        }

        //---- btn_clearHistory ----
        btn_clearHistory.setText("\u5220\u9664\u804a\u5929\u8bb0\u5f55");

        //---- btn_clearLog ----
        btn_clearLog.setText("\u6e05\u7a7a\u65e5\u5fd7");

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addComponent(label1)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(label_number)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 95, Short.MAX_VALUE)
                            .addComponent(btn_clearLog)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btn_clearHistory)))
                    .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(label1, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                        .addComponent(label_number, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_clearHistory)
                        .addComponent(btn_clearLog))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                    .addContainerGap())
        );
        contentPaneLayout.linkSize(SwingConstants.VERTICAL, new Component[] {label1, label_number});
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel label1;
    private JLabel label_number;
    private JScrollPane scrollPane1;
    private JTextArea textArea;
    private JButton btn_clearHistory;
    private JButton btn_clearLog;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
