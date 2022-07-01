package controller.service;

import model.dao.impl.MessageDAOImpl;
import view.ServerMainView;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** 服务器端 */
public class Server {

    private final ServerSocket server;
    private final Map<String, OutputStream> allInfo = new HashMap<>();
    private final Map<String, InputStream> allInfoInput = new HashMap<>();
    private final Map<String, OutputStream> aInfo = new HashMap<>();
    private final Map<String, OutputStream> bInfo = new HashMap<>();
    private final Map<String, OutputStream> cInfo = new HashMap<>();

    private final ExecutorService exec;
    public static ServerMainView ui;
    SimpleDateFormat sdf = new SimpleDateFormat();
    Date date;
    File logPath = new File("LanTalk/file/serverLog");
    File log = new File(logPath + "/log.txt");
    FileWriter fw;
    static BufferedWriter bw;

    public Server() throws IOException {
        int port = 549;
        server = new ServerSocket(port);
        exec = Executors.newCachedThreadPool();
        ui = new ServerMainView();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        date = new Date();

        checkLog();

        ui.showMsg(sdf.format(date) + " 服务器启动  端口号：" + port);
        bw.write(sdf.format(date) + " 服务器启动  端口号：" + port);
        bw.newLine();

        //服务器关闭后向日志中输出关闭消息
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                bw.write(sdf.format(date) + " 服务器关闭");
                bw.newLine();
                bw.newLine();
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        start();
    }

    //处理while语句无法正确抛出异常问题
    @SuppressWarnings("InfiniteLoopStatement")
    public void start() throws IOException {
        while (true) {
            Socket socket = server.accept();

            //获取登录账号的名字和部门
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            String name = inputStream.readUTF();
            String department = inputStream.readUTF();

            //检查该账号是否已登录，并作出回应
            if (allInfo.containsKey(name)) {
                outputStream.writeUTF("unavailable");
                outputStream.flush();
                continue;
            } else {
                outputStream.writeUTF("available");
                outputStream.flush();
            }

            //向所在部门广播该用户上线消息
            Set<Map.Entry<String, OutputStream>> list;
            switch (department) {
                case "行政部门A" -> list = aInfo.entrySet();
                case "行政部门B" -> list = bInfo.entrySet();
                case "行政部门C" -> list = cInfo.entrySet();
                default -> list = allInfo.entrySet();
            }
            for (Map.Entry<String, OutputStream> object : list) {
                outputStream = new DataOutputStream(object.getValue());
                outputStream.writeUTF("online");
                outputStream.flush();
                outputStream.writeUTF(name);
                outputStream.flush();
            }

            //保存登录用户名称和说的内容并分配线程
            allInfo.put(name, socket.getOutputStream());
            allInfoInput.put(name, socket.getInputStream());
            switch (department) {
                case "行政部门A" -> {
                    aInfo.put(name, socket.getOutputStream());
                    exec.execute(new ServerThread(socket, allInfo, allInfoInput, aInfo, name, department));
                }
                case "行政部门B" -> {
                    bInfo.put(name, socket.getOutputStream());
                    exec.execute(new ServerThread(socket, allInfo, allInfoInput, bInfo, name, department));
                }
                case "行政部门C" -> {
                    cInfo.put(name, socket.getOutputStream());
                    exec.execute(new ServerThread(socket, allInfo, allInfoInput, cInfo, name, department));
                }
                //default -> exec.execute(new ServerThread(socket, allInfo, allInfoInput, allInfo, name, department));
            }

            //服务器端显示上线消息
            date = new Date();
            ui.showMsg(sdf.format(date) + " notice   : " + department + " " + name + " 上线了 ip" + socket.getInetAddress());
            bw.write(sdf.format(date) + " notice   : " + department + " " + name + " 上线了 ip" + socket.getInetAddress());
            bw.newLine();
            //更新人数
            ui.updateOnlineNumber(allInfo.size());
        }
    }

    /** 检查日志文件 */
    private void checkLog() throws IOException {
        //创建日志文件夹
        if (!logPath.exists()) {
            System.out.println("日志文件夹不存在，准备创建文件夹...");
            if (logPath.mkdirs()) {
                System.out.println("日志文件夹创建成功");
            } else {
                System.out.println("日志文件夹创建失败");
            }
        }
        //若日志文件大于1M，则删除旧的日志文件
        if (log.length() > 1024 * 1024) {
            System.out.println("日志文件大于1M，准备删除旧的日志文件...");
            if (log.delete()) {
                System.out.println("旧日志删除成功");
            } else {
                System.out.println("旧日志删除失败");
            }
        }

        //创建日志文件
        if (!log.exists()) {
            System.out.println("日志文件不存在，准备创建文件...");
            if (log.createNewFile()) {
                System.out.println("日志文件创建成功");
            } else {
                System.out.println("日志文件创建失败");
            }
        }

        fw = new FileWriter(log, true);
        bw = new BufferedWriter(fw);
    }

    /** 服务器端线程 */
    public static class ServerThread implements Runnable {
        private final Socket socket;
        private final String name;
        private final String department;
        private final Map<String, OutputStream> allInfo;
        private final Map<String, InputStream> allInfoInput;
        private final Map<String, OutputStream> depInfo;
        private final DataInputStream inputStream;
        private final DataOutputStream outputStream;
        SimpleDateFormat sdf = new SimpleDateFormat();
        Date date;

        ServerThread(Socket socket, Map<String, OutputStream> allInfo, Map<String, InputStream> allInfoInput, Map<String, OutputStream> depInfo, String name, String department) throws IOException {
            this.socket = socket;
            this.name = name;
            this.department = department;
            this.allInfo = allInfo;
            this.allInfoInput = allInfoInput;
            this.depInfo = depInfo;
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        }

        /** 向目标客户端发送消息 */
        private void sendMsg(String msg, String target) throws IOException {
            switch (target) {
                case "all" -> {
                    date = new Date();
                    Set<Map.Entry<String, OutputStream>> list = allInfo.entrySet();
                    for (Map.Entry<String, OutputStream> object : list) {
                        if (!object.getKey().equals(name)) {
                            DataOutputStream tempOutputStream = new DataOutputStream(object.getValue());
                            tempOutputStream.writeUTF("msg");
                            tempOutputStream.flush();
                            tempOutputStream.writeUTF("all");
                            tempOutputStream.flush();
                            tempOutputStream.writeUTF(sdf.format(date) + " [" + department + "] " + name + " : " + msg);
                            tempOutputStream.flush();
                        }
                    }
                    ui.showMsg(sdf.format(date) + " msg      : [" + department + "] " + name + " to all : " + msg);
                    bw.write(sdf.format(date) + " msg      : [" + department + "] " + name + " to all : " + msg);
                    bw.newLine();
                    saveMsgHistory("全部", sdf.format(date), msg);
                }
                case "行政部门A", "行政部门B", "行政部门C" -> {
                    date = new Date();
                    Set<Map.Entry<String, OutputStream>> list = depInfo.entrySet();
                    for (Map.Entry<String, OutputStream> object : list) {
                        if (!object.getKey().equals(name)) {
                            DataOutputStream tempOutputStream = new DataOutputStream(object.getValue());
                            tempOutputStream.writeUTF("msg");
                            tempOutputStream.flush();
                            tempOutputStream.writeUTF("dep");
                            tempOutputStream.flush();
                            tempOutputStream.writeUTF(sdf.format(date) + " " + name + " : " + msg);
                            tempOutputStream.flush();
                        }
                    }
                    ui.showMsg(sdf.format(date) + " msg      : [" + department + "] " + name + " to dep : " + msg);
                    bw.write(sdf.format(date) + " msg      : [" + department + "] " + name + " to dep : " + msg);
                    bw.newLine();
                    saveMsgHistory(department, sdf.format(date), msg);
                }
                default -> {
                    date = new Date();
                    DataOutputStream tempOutputStream = new DataOutputStream(allInfo.get(target));
                    tempOutputStream.writeUTF("msg");
                    tempOutputStream.flush();
                    tempOutputStream.writeUTF("sb");
                    tempOutputStream.flush();
                    tempOutputStream.writeUTF(sdf.format(date) + " " + name + " 对你说 : " + msg);
                    tempOutputStream.flush();
                    ui.showMsg(sdf.format(date) + " msg      : [" + department + "] " + name + " to " + target + " : " + msg);
                    bw.write(sdf.format(date) + " msg      : [" + department + "] " + name + " to " + target + " : " + msg);
                    bw.newLine();
                    saveMsgHistory(target, sdf.format(date), msg);
                }
            }
        }

        /** 向目标客户端发送文件 */
        private void sendFile(String fileName, String target) throws IOException {
            switch (target) {
                case "all" -> {
                    String filePath = "LanTalk/file/source/all/";
                    long length = uploadFile(fileName, filePath, "all");

                    date = new Date();
                    ui.showMsg(sdf.format(date) + " file     : [" + department + "] " + name + " to all : " + fileName + "(" + length + " bytes)");
                    bw.write(sdf.format(date) + " file     : [" + department + "] " + name + " to all : " + fileName + "(" + length + " bytes)");
                    bw.newLine();
                }

                case "行政部门A", "行政部门B", "行政部门C" -> {
                    target = "department" + target.substring(4);
                    String filePath = "LanTalk/file/source/" + target + "/";
                    long length = uploadFile(fileName, filePath, department);

                    date = new Date();
                    ui.showMsg(sdf.format(date) + " file     : [" + department + "] " + name + " to dep : " + fileName + "(" + length + " bytes)");
                    bw.write(sdf.format(date) + " file     : [" + department + "] " + name + " to dep : " + fileName + "(" + length + " bytes)");
                    bw.newLine();
                }
                default -> {
                    DataOutputStream tempOutputStream = new DataOutputStream(allInfo.get(target));
                    tempOutputStream.writeUTF("file");
                    tempOutputStream.flush();
                    tempOutputStream.writeUTF("[" + department + "] " + name + " 通过 “私聊” 向你发来文件 : " + fileName);
                    tempOutputStream.flush();
                    tempOutputStream.writeUTF(fileName);
                    tempOutputStream.flush();

                    byte[] data = new byte[1024];
                    int temp = 1024, len = 0;
                    while (temp >= 1024) {
                        temp = inputStream.read(data, 0, 1024);
                        len += temp;
                        tempOutputStream.write(data, 0, temp);
                        tempOutputStream.flush();
                    }

                    date = new Date();
                    ui.showMsg(sdf.format(date) + " file     : [" + department + "] " + name + " to " + target + " : " + fileName);
                    bw.write(sdf.format(date) + " file     : [" + department + "] " + name + " to " + target + " : " + fileName);
                    bw.newLine();
                }
            }
        }

        /** 上传文件 */
        private long uploadFile(String fileName, String filePath, String target) throws IOException {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String newFileName = department + "_" + name + "_" + fileName;
            file = new File(filePath + newFileName);
            if (file.exists()) {
                int i = 1;
                while (file.exists()) {
                    file = new File(filePath + "/" + newFileName.substring(0, newFileName.lastIndexOf(".")) +
                                            "(" + i + ")" + newFileName.substring(newFileName.lastIndexOf(".")));
                    i++;
                }
            }
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            int len = 1024;
            byte[] data = new byte[1024];
            while (len >= 1024) {
                len = inputStream.read(data);
                fos.write(data, 0, len);
                fos.flush();
            }
            fos.close();

            fileName = file.getName().substring(file.getName().lastIndexOf("_") + 1);
            addFile(target, fileName, file.length(), department, name, "add");

            return file.length();
        }

        /** 发送要下载的文件 */
        void sendDownload(String fileName, String target) throws IOException {
            outputStream.writeUTF("file");
            outputStream.flush();
            outputStream.writeUTF("download");
            outputStream.flush();
            outputStream.writeUTF(fileName.substring(fileName.lastIndexOf("_") + 1));
            outputStream.flush();

            if (!target.equals("all")) {
                target = "department" + target.substring(4);
            }
            String filePath = "LanTalk/file/source/" + target + "/" + fileName;
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            int len;
            byte[] data = new byte[1024];
            while ((len = fis.read(data, 0, 1024)) != -1) {
                outputStream.write(data, 0, len);
                outputStream.flush();
            }
            fis.close();
            date = new Date();
            ui.showMsg(sdf.format(date) + " download : [" + department + "] " + name + " from " + target + " : " + fileName);
            bw.write(sdf.format(date) + " download : [" + department + "] " + name + " from " + target + " : " + fileName);
            bw.newLine();
        }

        /** 用户上线后为其推送同部门已在线用户信息 */
        private void initOnlineClient() throws IOException {
            Set<Map.Entry<String, OutputStream>> list = depInfo.entrySet();
            for (Map.Entry<String, OutputStream> object : list) {
                String onlineClient = object.getKey();
                if (!onlineClient.equals(name)) {
                    outputStream.writeUTF("list");
                    outputStream.flush();
                    outputStream.writeUTF(onlineClient);
                    outputStream.flush();
                }
            }
        }

        /** 发送文件信息 */
        public void loadFile(String target) throws IOException {
            if (!target.equals("all")) {
                target = "department" + target.substring(4);
            }
            String sourcePath = "LanTalk/file/source/" + target + "/";
            File baseFile = new File(sourcePath);
            if (baseFile.isFile() || !baseFile.exists()) {
                return;
            }
            File[] fileList = baseFile.listFiles();
            for (File file : Objects.requireNonNull(fileList)) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    String department = fileName.substring(0, fileName.indexOf("_"));
                    fileName = fileName.substring(fileName.indexOf("_") + 1);
                    String name = fileName.substring(0, fileName.indexOf("_"));
                    fileName = fileName.substring(fileName.indexOf("_") + 1);

                    addFile(target, fileName, file.length(), department, name, "init");
                }
            }
        }

        /** 发送文件信息 */
        public void addFile(String target, String fileName, long length, String department, String name, String type) throws IOException {
            sendFileInfo(target, fileName, length, department, name, outputStream);

            if (type.equals("add")) {
                Set<Map.Entry<String, OutputStream>> list = allInfo.entrySet();
                for (Map.Entry<String, OutputStream> object : list) {
                    if (!object.getKey().equals(name)) {
                        DataOutputStream tempOutputStream = new DataOutputStream(object.getValue());
                        sendFileInfo(target, fileName, length, department, name, tempOutputStream);
                        //发送消息
                        tempOutputStream.writeUTF("msg");
                        tempOutputStream.flush();
                        if ("all".equals(target)) {
                            tempOutputStream.writeUTF("all");
                            tempOutputStream.flush();
                            date = new Date();
                            tempOutputStream.writeUTF(sdf.format(date) + " " + department + " " + name + " 上传了文件 : " + fileName);
                            tempOutputStream.flush();
                        } else {
                            tempOutputStream.writeUTF("dep");
                            tempOutputStream.flush();
                            date = new Date();
                            tempOutputStream.writeUTF(sdf.format(date) + " " + name + " 上传了文件 : " + fileName);
                            tempOutputStream.flush();
                        }
                    }
                }
            }
        }

        /** 发送文件信息 */
        private void sendFileInfo(String target, String fileName, long length, String department, String name, DataOutputStream tempOutputStream) throws IOException {
            tempOutputStream.writeUTF("addFile");
            tempOutputStream.flush();
            tempOutputStream.writeUTF(target);
            tempOutputStream.flush();
            tempOutputStream.writeUTF(fileName);
            tempOutputStream.flush();
            tempOutputStream.writeUTF(String.valueOf(length));
            tempOutputStream.flush();
            tempOutputStream.writeUTF(department);
            tempOutputStream.flush();
            tempOutputStream.writeUTF(name);
            tempOutputStream.flush();
        }


        /** 保存聊天记录 */
        private void saveMsgHistory(String target, String time, String msg) {
            new MessageDAOImpl().recordMessage(name, department, target, msg, time);
        }

        /** 清空日志框 */
        public static void clearLog() throws IOException {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            Date date;
            int result = JOptionPane.showConfirmDialog(ui, "确定要清空日志吗?", "提示",
                                                       JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                date = new Date();
                ui.showMsg(sdf.format(date) + " operate  : 清空日志");
                bw.write(sdf.format(date) + " operate  : 清空日志");
                bw.newLine();
                ui.clearLog();
                JOptionPane.showMessageDialog(ui, "清空日志成功", "提示",
                                              JOptionPane.INFORMATION_MESSAGE);
            }
        }

        /** 删除所有聊天记录 */
        public static void clearHistory() throws IOException {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            Date date;
            int result = JOptionPane.showConfirmDialog(ui, "确定要删除所有聊天记录吗？\n删除的聊天记录将无法恢复",
                                                       "提示", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                date = new Date();
                ui.showMsg(sdf.format(date) + " operate  : 删除聊天记录...");
                bw.write(sdf.format(date) + " operate  : 删除聊天记录...");
                bw.newLine();
                if (new MessageDAOImpl().deleteMessage()) {
                    JOptionPane.showMessageDialog(ui, "聊天记录删除成功！",
                                                  "提示", JOptionPane.INFORMATION_MESSAGE);
                    date = new Date();
                    ui.showMsg(sdf.format(date) + " response : 聊天记录删除成功！");
                    bw.write(sdf.format(date) + " response : 聊天记录删除成功！");
                    bw.newLine();
                } else {
                    JOptionPane.showMessageDialog(ui, "出现未知错误，聊天记录删除失败！",
                                                  "提示", JOptionPane.ERROR_MESSAGE);
                    date = new Date();
                    ui.showMsg(sdf.format(date) + " response : 聊天记录删除失败！");
                    bw.write(sdf.format(date) + " response : 聊天记录删除失败！");
                    bw.newLine();
                }
            }
        }

        @Override
        public void run() {
            try {
                boolean flag = true;
                //该循环用于持续接收客户端发来的信息，只有当客户端下线并自动发送“offline”后，跳出循环
                do {
                    //从客户端接收指令
                    String type = inputStream.readUTF();
                    switch (type) {
                        case "msg" -> {
                            String target = inputStream.readUTF();
                            String msg = inputStream.readUTF();
                            sendMsg(msg, target);
                        }
                        case "file" -> {
                            String target = inputStream.readUTF();
                            String fileName = inputStream.readUTF();
                            sendFile(fileName, target);
                        }
                        case "download" -> {
                            String target = inputStream.readUTF();
                            String fileName = inputStream.readUTF();
                            sendDownload(fileName, target);
                        }
                        case "online" -> {
                            initOnlineClient();
                            loadFile("all");
                            loadFile(department);
                        }
                        case "offline" -> flag = false;
                        default -> {
                            date = new Date();
                            ui.showMsg(sdf.format(date) + " [来自客户端 " + name + " 的未知指令] " + type);
                            bw.write(sdf.format(date) + " [来自客户端 " + name + " 的未知指令] " + type);
                            bw.newLine();
                        }
                    }
                } while (flag);

                //用户下线后续操作

                //移除用户信息
                allInfo.remove(name);
                allInfoInput.remove(name);
                depInfo.remove(name);

                //更新在线人数
                ui.updateOnlineNumber(allInfo.size());

                //向所在部门推送下线消息
                Set<Map.Entry<String, OutputStream>> list = depInfo.entrySet();
                for (Map.Entry<String, OutputStream> object : list) {
                    DataOutputStream tempOutputStream = new DataOutputStream(object.getValue());
                    tempOutputStream.writeUTF("offline");
                    tempOutputStream.flush();
                    tempOutputStream.writeUTF(name);
                    tempOutputStream.flush();
                }
                date = new Date();
                ui.showMsg(sdf.format(date) + " notice   : " + department + " " + name + " 下线了 ip" + socket.getInetAddress());
                bw.write(sdf.format(date) + " notice   : " + department + " " + name + " 下线了 ip" + socket.getInetAddress());
                bw.newLine();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


