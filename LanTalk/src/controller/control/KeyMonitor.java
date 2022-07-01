package controller.control;

import controller.service.Client;
import controller.service.LoginMethod;

import javax.swing.text.JTextComponent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

/**
 * 按键监听器类，用于监听输入按键并进行响应
 */
public class KeyMonitor implements KeyListener {
    //定义静态实例以供view层调用
    public static KeyListener keyListener = new KeyMonitor();


    //按键按下触发事件重写
    @Override
    public void keyPressed(KeyEvent e) {
        //获取触发的文本框
        JTextComponent text = (JTextComponent) e.getSource();
        //获取文本框name
        String name = text.getName();
        //判断按键类型是否为回车
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            switch (name) {
                case "login_account", "login_password" -> {
                    try {
                        LoginMethod.loginCheck();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        //ctrl + 回车  发送消息
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
            switch (name) {
                case "area_allSend" -> Client.ClientThread.sendMsgToAll();
                case "area_ASend", "area_BSend", "area_CSend" -> Client.ClientThread.sendMsgToDep();
                case "area_sbSend" -> Client.ClientThread.sendMsgToSb();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
