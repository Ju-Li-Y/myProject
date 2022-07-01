package controller.control;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/** 窗口监听器类，用于监听窗口状态并响应 */
public class WindowMonitor implements WindowListener {
    //定义静态实例以供view层调用
    public static final WindowMonitor windowMonitor = new WindowMonitor();


    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    //窗口关闭事件重写
    @Override
    public void windowClosed(WindowEvent e) {
        //获取触发的窗口
        JFrame frame = (JFrame) e.getSource();
        if ("frame_stuInfo".equals(frame.getName())) {
            mainSystem.stuInfoView = null;
        } else if ("frame_tchInfo".equals(frame.getName())) {
            mainSystem.tchInfoView = null;
        } else if ("frame_courseInfo".equals(frame.getName())) {
            mainSystem.courseInfoView = null;
        } else if ("frame_changePwd".equals(frame.getName())) {
            mainSystem.changePasswordView = null;
        }
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
