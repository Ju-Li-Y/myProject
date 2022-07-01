package controller.service;

import controller.control.mainSystem;
import model.bean.User;
import model.dao.impl.AccountDAOImpl;
import view.LoginView;
import view.ManagerMainView;
import view.StudentMainView;
import view.TeacherMainView;

import javax.swing.*;

/** 登录界面各种操作的具体实现 */
public class LoginMethod {

    /** 跳转到管理员主界面 */
    public static void openManagerMainView() {
        mainSystem.loginView.dispose();
        mainSystem.mgrMainView = new ManagerMainView();
        ManagerMethod.loadInfo();
    }

    /** 跳转到教师主界面 */
    public static void openTeacherMainView() {
        mainSystem.loginView.dispose();
        mainSystem.tchMainView = new TeacherMainView();
        TeacherMethod.loadInfo();
    }

    /** 跳转到学生主界面 */
    public static void openStudentMainView() {
        mainSystem.loginView.dispose();
        mainSystem.stuMainView = new StudentMainView();
        StudentMethod.loadInfo();
    }

    /** 检查账号密码是否正确 */
    public static void loginCheck() {
        String account = mainSystem.loginView.getAccount();
        String password = mainSystem.loginView.getPassword();

        if (account.equals("") || password.equals("")) {
            JOptionPane.showMessageDialog(null, "账号或密码不能为空！",
                                          "登录消息", JOptionPane.ERROR_MESSAGE);
        } else {
            //内置默认管理员账号,账号密码均为admin
            if (account.equals("admin") && password.equals("admin")) {
                LoginView.user = User.MANAGER;
                LoginView.user.setNumber("admin");
            } else {
                //依据输入的账号密码进行查询
                LoginView.user = new AccountDAOImpl().queryAccount(account, password);
            }
            //为null表示非默认管理员或账号验证失败，弹出提示并清空输入框
            if (LoginView.user == null) {
                JOptionPane.showMessageDialog(null, "账号或密码错误！请重新输入",
                                              "登录消息", JOptionPane.INFORMATION_MESSAGE);
                mainSystem.loginView.clearText();
            }
            //进行相应界面跳转
            else if (LoginView.user == User.MANAGER) {
                JOptionPane.showMessageDialog(null, "登录成功！",
                                              "登录消息", JOptionPane.INFORMATION_MESSAGE);
                openManagerMainView();
            } else if (LoginView.user == User.TEACHER) {
                JOptionPane.showMessageDialog(null, "登录成功！",
                                              "登录消息", JOptionPane.INFORMATION_MESSAGE);
                openTeacherMainView();
            } else if (LoginView.user == User.STUDENT) {
                JOptionPane.showMessageDialog(null, "登录成功！",
                                              "登录消息", JOptionPane.INFORMATION_MESSAGE);
                openStudentMainView();
            }
        }
    }
}
