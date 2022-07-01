package test;

import com.formdev.flatlaf.FlatIntelliJLaf;
import controller.service.Client;
import view.LoginView;

import javax.swing.*;

public class ClientTest {
    //创建各窗口对象
    public static LoginView loginView;
    public static Client client;

    public static void main(String[] args) {
        initializeFlatLaf();
        loginView = new LoginView();
    }

    /** 初始化swing美化包 需在main方法开头引用 */
    private static void initializeFlatLaf() {
        try {
            //四种风格：FlatDarkLaf()、FlatDarculaLaf()、FlatLightLaf()、FlatIntelliJLaf()
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (Exception ex) {
            java.lang.System.err.println("Failed to initialize FlatLaF");
        }
    }
}
