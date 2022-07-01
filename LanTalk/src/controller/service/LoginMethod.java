package controller.service;

import model.dao.impl.AccountDAOImpl;
import test.ClientTest;
import view.LoginView;

import javax.swing.*;
import java.io.IOException;

/** 登录界面各种操作的具体实现 */
public class LoginMethod {

    /** 检查账号密码是否正确 */
    public static void loginCheck() throws IOException {
        String account = ClientTest.loginView.getAccount();
        String password = ClientTest.loginView.getPassword();

        if (account.equals("") || password.equals("")) {
            JOptionPane.showMessageDialog(null, "账号或密码不能为空！",
                                          "登录消息", JOptionPane.ERROR_MESSAGE);
        } else {
            //依据输入的账号密码进行查询
            LoginView.user = new AccountDAOImpl().queryAccount(account, password);
            //为null表示非默认管理员或账号验证失败，弹出提示并清空输入框
            if (LoginView.user == null) {
                JOptionPane.showMessageDialog(null, "账号或密码错误！请重新输入",
                                              "登录消息", JOptionPane.INFORMATION_MESSAGE);
                ClientTest.loginView.clearText();
            }
            //跳转到主界面
            else {
                ClientTest.client = new Client();
            }
        }
    }
}
