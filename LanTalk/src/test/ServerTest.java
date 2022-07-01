package test;

import com.formdev.flatlaf.FlatIntelliJLaf;
import controller.service.Server;

import javax.swing.*;
import java.io.IOException;

public class ServerTest {
    public static void main(String[] args) throws IOException {
        initializeFlatLaf();
        new Server();
    }

    /** 初始化swing美化包 需在main方法开头引用 */
    private static void initializeFlatLaf() {
        try {
            //四种风格：FlatDarkLaf()、FlatDarculaLaf()、FlatLightLaf()、FlatIntelliJLaf()
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
