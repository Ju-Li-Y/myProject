package controller.service;

import model.bean.Message;
import model.bean.User;
import model.dao.impl.MessageDAOImpl;
import test.ClientTest;
import view.ClientMainView;
import view.LoginView;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/** 客户端 */
public class Client {

    public static ClientMainView ui;
    public ClientThread clientThread;


    public Client() throws IOException {
        Socket socket;
        try {
            int port = 549;
            String ip = "127.0.0.1";
            socket = new Socket(ip, port);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "连接服务器失败！请检查服务器是否开启",
                                          "登录消息", JOptionPane.ERROR_MESSAGE);
            return;
        }
        User user = LoginView.user;
        clientThread = new ClientThread(socket, user.getName(), user.getDepartment());
        ClientThread.sendMsg(user.getName());
        ClientThread.sendMsg(user.getDepartment());

        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        String type = inputStream.readUTF();
        if ("unavailable".equals(type)) {
            JOptionPane.showMessageDialog(null, "该账号已登录！请勿重复登录",
                                          "登录消息", JOptionPane.ERROR_MESSAGE);
        } else if ("available".equals(type)) {

            JOptionPane.showMessageDialog(null, "登录成功！",
                                          "登录消息", JOptionPane.INFORMATION_MESSAGE);

            //初始化在线名单
            ClientThread.sendMsg("online");

            ClientTest.loginView.dispose();
            ui = new ClientMainView();
            ui.setFrameTitle("客户端 登录用户: " + user.getName());
            ui.setDepartment(user.getDepartment());

            //创建线程，用于持续与服务器交互
            Thread t = new Thread(clientThread);
            t.start();

            ClientThread.loadMsgHistory();

            //关闭后向服务器发送"offline",以供进行相应数据更新
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    ClientThread.sendMsg("offline");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
        }
    }

    public static class ClientThread implements Runnable {
        private static DataInputStream inputStream;
        private static DataOutputStream outputStream;
        private static String name;
        private static String department;
        static SimpleDateFormat sdf = new SimpleDateFormat();
        static Date date;
        static boolean flag = false;
        Socket socket;
        Vector<Vector<String>> allTableData = new Vector<>();
        Vector<Vector<String>> depTableData = new Vector<>();


        public ClientThread(Socket socket, String name, String department) throws IOException {
            ClientThread.name = name;
            ClientThread.department = department;
            this.socket = socket;
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        }

        /** 向所有人发送消息 */
        public static void sendMsgToAll() {
            String msg = ui.getSendArea("all");
            ui.clearSendArea("all");
            date = new Date();
            ui.showMsg(sdf.format(date) + " [" + department + "] " + name + " : " + msg + "\n", "all");
            try {
                sendMsg("msg");
                sendMsg("all");
                sendMsg(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /** 向所在部门发送消息 */
        public static void sendMsgToDep() {
            String msg = ui.getSendArea(department);
            ui.clearSendArea(department);
            date = new Date();
            ui.showMsg(sdf.format(date) + " " + name + " : " + msg + "\n", department);
            try {
                sendMsg("msg");
                sendMsg(department);
                sendMsg(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /** 向选择的私聊对象发送消息 */
        public static void sendMsgToSb() {
            String msg = ui.getSendArea("sb");
            String target = ui.getTarget();
            if ("".equals(target)) {
                JOptionPane.showMessageDialog(ui, "请选择私聊对象！",
                                              "私聊消息", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ui.clearSendArea("sb");
            date = new Date();
            ui.showMsg(sdf.format(date) + " 我对 " + target + " 说: " + msg + "\n", "sb");
            try {
                sendMsg("msg");
                sendMsg(target);
                sendMsg(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /** 向服务器端发送消息 */
        public static void sendMsg(String msg) throws IOException {
            outputStream.writeUTF(msg);
            outputStream.flush();
        }

        /** 向所有人发送文件 */
        public static void sendFileToAll() {
            JFileChooser fc = new JFileChooser();
            //打开文件选择框
            int val = fc.showOpenDialog(null);
            if (val == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                ui.showMsg("我上传了文件: " + file.getName() + "\n", "all");
                try {
                    sendMsg("file");
                    sendMsg("all");
                    sendMsg(file.getName());
                    sendFile(file.toString());
                } catch (IOException a) {
                    a.printStackTrace();
                }
            }
        }

        /** 向所在部门发送文件 */
        public static void sendFileToDep() {
            JFileChooser fc = new JFileChooser();
            //打开文件选择框
            int val = fc.showOpenDialog(null);
            if (val == JFileChooser.APPROVE_OPTION) {
                File F = fc.getSelectedFile();
                ui.showMsg("我上传了文件: " + F.getName() + "\n", department);
                try {
                    sendMsg("file");
                    sendMsg(department);
                    sendMsg(F.getName());
                    sendFile(F.toString());
                } catch (IOException a) {
                    a.printStackTrace();
                }
            }
        }

        /** 向选择的私聊对象发送文件 */
        public static void sendFileToSb() {
            String target = ui.getTarget();
            if ("".equals(target)) {
                JOptionPane.showMessageDialog(ui, "请选择私聊对象！", "私聊消息",
                                              JOptionPane.ERROR_MESSAGE);
                return;
            }
            JFileChooser fc = new JFileChooser();
            //打开文件选择框
            int val = fc.showOpenDialog(null);
            if (val == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                ui.showMsg("我向 " + target + " 发送了文件: " + file.getName() + "\n", "sb");
                try {
                    sendMsg("file");
                    sendMsg(target);
                    sendMsg(file.getName());
//                    String ans = inputStream.readUTF();
//                    if ("continue".equals(ans)) {
//                        System.out.println("continue");
                    sendFile(file.toString());
//                    } else {
//                        System.out.println("stop");
//                    }
                } catch (IOException a) {
                    a.printStackTrace();
                }
            }
        }

        /** 向服务器端发送文件 */
        public static void sendFile(String path) {
            int len;
            try {
                File file = new File(path);
                FileInputStream fis = new FileInputStream(file);//读文件对象

                byte[] data = new byte[1024];
                //读取输入流, 一次读取1024个字节
                while ((len = fis.read(data, 0, 1024)) != -1) {
                    outputStream.write(data, 0, len);
                    outputStream.flush();
                }
                fis.close();
                System.out.println("文件发送成功");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /** 接收消息 */
        private void getMsg() throws IOException {
            String source = inputStream.readUTF();
            switch (source) {
                case "all" -> {
                    String msg = inputStream.readUTF();
                    ui.showMsg(msg + "\n", "all");
                }
                case "dep" -> {
                    String msg = inputStream.readUTF();
                    ui.showMsg(msg + "\n", department);
                }
                case "sb" -> {
                    String msg = inputStream.readUTF();
                    ui.showMsg(msg + "\n", "sb");
                }
            }
        }

        /** 接收文件 */
        private void getFile() throws Exception {
            String msg = inputStream.readUTF();
            int result = 0;
            if (!"download".equals(msg)) {
                result = JOptionPane.showConfirmDialog(ui, msg + "\n是否接收该文件?",
                                                       "文件消息", JOptionPane.YES_NO_OPTION);
            }
            String fileName = inputStream.readUTF();
            //设定接收目录，文件名为原文件名
            String path = "LanTalk/file/receive/" + department + "_" + name;
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(path + "/" + fileName);
            //若文件存在，则后缀自增1
            if (file.exists()) {
                int i = 1;
                while (file.exists()) {
                    //需要将原文件名拆分，为原文件扩展名前的内容＋数字＋原文件扩展名
                    file = new File(path + "/" + fileName.substring(0, fileName.lastIndexOf(".")) +
                                            "(" + i + ")" + fileName.substring(fileName.lastIndexOf(".")));
                    i++;
                }
            }
            file.createNewFile();

            FileOutputStream fos = new FileOutputStream(file);
            int n = 1024, len = 1024;
            byte[] data = new byte[n];
            while (len >= n) {
                len = inputStream.read(data);
                fos.write(data, 0, len);
                fos.flush();
            }
            fos.close();
            //若拒绝接收文件，则将其删除
            if (result == JOptionPane.NO_OPTION) {
                file.delete();
            } else {
                System.out.print("文件下载成功!\n目录：" + file.getAbsolutePath());
                JOptionPane.showMessageDialog(null, "成功下载文件: " + fileName, "文件消息",
                                              JOptionPane.PLAIN_MESSAGE);
            }
        }

        /** 下载“全部”中的文件 */
        public static void downloadAllFile() throws IOException {
            //获取要下载的文件名称
            String fileName = ui.getAllFileName();

            int result = JOptionPane.showConfirmDialog(null, "是否要下载文件 " + fileName.substring(fileName.lastIndexOf("_") + 1) + " ?",
                                                       "文件消息", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                outputStream.writeUTF("download");
                outputStream.flush();
                outputStream.writeUTF("all");
                outputStream.flush();
                outputStream.writeUTF(fileName);
                outputStream.flush();
            }
        }

        /** 下载“部门”中的文件 */
        public static void downloadDepFile() throws IOException {
            //获取要下载的文件名称
            String fileName = ui.getDepFileName();

            int result = JOptionPane.showConfirmDialog(null, "是否要下载文件 " + fileName.substring(fileName.lastIndexOf("_") + 1) + " ?",
                                                       "文件消息", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                outputStream.writeUTF("download");
                outputStream.flush();
                outputStream.writeUTF(department);
                outputStream.flush();
                outputStream.writeUTF(fileName);
                outputStream.flush();
            }
        }

        /** 加载聊天记录 */
        public static void loadMsgHistory() {
            List<Message> messages = new MessageDAOImpl().queryMessage();
            for (Message message : messages) {
                if ("全部".equals(message.getTarget())) {
                    ui.showMsg(message.getTime() + " [" + message.getDepartment() + "] " + message.getName() + " : " + message.getMessage() + "\n", "all");
                } else if (department.equals(message.getTarget())) {
                    ui.showMsg(message.getTime() + " " + message.getName() + " : " + message.getMessage() + "\n", department);
                } else if (name.equals(message.getTarget())) {
                    ui.showMsg(message.getTime() + " " + message.getName() + " 对你说 : " + message.getMessage() + "\n", "sb");
                } else if (name.equals(message.getName())) {
                    ui.showMsg(message.getTime() + " 我对 " + message.getTarget() + " 说 : " + message.getMessage() + "\n", "sb");
                }
            }
            ui.showMsg("--------------------以上为历史消息--------------------\n", "all");
            ui.showMsg("--------------------以上为历史消息--------------------\n", department);
            ui.showMsg("--------------------以上为历史消息--------------------\n", "sb");

        }

        /** 初始化文件表格 */
        public void initFileTable() {
            if (!flag) {
                AbstractTableModel allModel = initFileTableModel(allTableData);
                AbstractTableModel depModel = initFileTableModel(depTableData);
                ui.setTable_allFile(allModel);
                ui.setTable_depFile(depModel);
                flag = true;
            }
        }

        /** 初始化文件表格模型 */
        public AbstractTableModel initFileTableModel(Vector<Vector<String>> tableData) {
            //列名
            Vector<String> columnName = new Vector<>();
            columnName.add("文件名");
            columnName.add("大小");
            columnName.add("来源");
            columnName.add("下载");

            //自定义表格模型
            class tableModel extends AbstractTableModel {
                //设置行数
                @Override
                public int getRowCount() {
                    return tableData.size();
                }

                //设置列数
                @Override
                public int getColumnCount() {
                    return columnName.size();
                }

                //显示表格内容
                @Override
                public Object getValueAt(int rowIndex, int columnIndex) {
                    return tableData.get(rowIndex).get(columnIndex);
                }

                //显示列名
                @Override
                public String getColumnName(int columnIndex) {
                    return columnName.get(columnIndex);
                }

                //设置单元格是否可编辑
                @Override
                public boolean isCellEditable(int row, int column) {
                    return getColumnName(column).equals("下载");
                }
            }
            return new tableModel();
        }

        /** 获取服务器端的文件信息 */
        public void loadFile() throws IOException {
            String target = inputStream.readUTF();
            String fileName = inputStream.readUTF();
            String length = inputStream.readUTF();
            String department = inputStream.readUTF();
            String name = inputStream.readUTF();

            Vector<String> rowData = new Vector<>();
            rowData.add(fileName);
            rowData.add(length + " bytes");
            rowData.add(department + "_" + name);
            rowData.add("下载");

            if (target.equals("all")) {
                allTableData.add(rowData);
            } else {
                depTableData.add(rowData);
            }
        }

        @Override
        public void run() {
            try {
                boolean flag = true;
                do {
                    String type = inputStream.readUTF();
                    switch (type) {
                        //"msg"表示接收到的是消息
                        case "msg" -> getMsg();
                        //"file"表示接收到的是文件
                        case "file" -> getFile();
                        case "bye" -> flag = false;
                        //"list"表示接收到的是在线名单
                        case "list" -> {
                            String name = inputStream.readUTF();
                            ui.updateOnlineClient(name, true);
                        }
                        case "addFile" -> {
                            initFileTable();
                            loadFile();
                        }
                        //"online"表示接收到的是上线通知
                        case "online" -> {
                            String name = inputStream.readUTF();
                            date = new Date();
                            ui.showMsg("[系统通知] " + name + " 上线了\n", department);
                            ui.updateOnlineClient(name, true);
                        }
                        //"offline"表示接收到的是下线通知
                        case "offline" -> {
                            String name = inputStream.readUTF();
                            ui.showMsg("[系统通知] " + name + " 下线了\n", department);
                            ui.updateOnlineClient(name, false);
                        }
                        default -> System.out.println("[来自服务器的未知指令]" + type);
                    }
                } while (flag);
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
