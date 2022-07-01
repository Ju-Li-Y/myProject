package controller.control;

import controller.service.Client;
import controller.service.LoginMethod;
import controller.service.Server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * 按钮监听器类，响应各界面按钮的触发并调用相应方法
 */
public class ButtonMonitor implements ActionListener {
    //定义静态实例以供view层调用
    public static ButtonMonitor btnListener = new ButtonMonitor();

    @Override
    public void actionPerformed(ActionEvent e) {
        //获取触发按钮的名称,并进行匹配
        JButton btn = (JButton) e.getSource();
        String btnName = btn.getName();
        try {
            switch (btnName) {
                case "login_login" -> LoginMethod.loginCheck();
                case "server_clearLog" -> Server.ServerThread.clearLog();
                case "server_clearHistory" -> Server.ServerThread.clearHistory();
                case "client_sendMsgToAll" -> Client.ClientThread.sendMsgToAll();
                case "client_sendMsgToA", "client_sendMsgToB", "client_sendMsgToC" -> Client.ClientThread.sendMsgToDep();
                case "client_sendMsgToSb" -> Client.ClientThread.sendMsgToSb();
                case "client_sendFileToAll" -> Client.ClientThread.sendFileToAll();
                case "client_sendFileToA", "client_sendFileToB", "client_sendFileToC" -> Client.ClientThread.sendFileToDep();
                case "client_sendFileToSb" -> Client.ClientThread.sendFileToSb();
                case "client_downloadAll" -> Client.ClientThread.downloadAllFile();
                case "client_downloadDep" -> Client.ClientThread.downloadDepFile();

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
