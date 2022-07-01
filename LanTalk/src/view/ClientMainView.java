/*
 * Created by JFormDesigner on Wed Jun 08 21:39:22 CST 2022
 */

package view;

import controller.control.ButtonMonitor;
import controller.control.KeyMonitor;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.Objects;

/**
 * @author JuLiy
 */
public class ClientMainView extends JFrame {

    public ClientMainView() {
        initComponents();
        addListener();
        setComponentsName();
        cbBox_target.addItem("");
        setVisible(true);
    }

    /** 添加事件监听器 */
    private void addListener() {
        //按钮
        btn_sendMsgToAll.addActionListener(ButtonMonitor.btnListener);
        btn_sendFileToAll.addActionListener(ButtonMonitor.btnListener);
        btn_sendMsgToA.addActionListener(ButtonMonitor.btnListener);
        btn_sendFileToA.addActionListener(ButtonMonitor.btnListener);
        btn_sendMsgToB.addActionListener(ButtonMonitor.btnListener);
        btn_sendFileToB.addActionListener(ButtonMonitor.btnListener);
        btn_sendMsgToC.addActionListener(ButtonMonitor.btnListener);
        btn_sendFileToC.addActionListener(ButtonMonitor.btnListener);
        btn_sendMsgToSb.addActionListener(ButtonMonitor.btnListener);
        btn_sendFileToSb.addActionListener(ButtonMonitor.btnListener);
        btn_showFileAll.addActionListener(e -> {
            frame_allFile.setVisible(true);
            frame_allFile.setLocationRelativeTo(this);
        });
        btn_showFileA.addActionListener(e -> {
            frame_depFile.setVisible(true);
            frame_depFile.setLocationRelativeTo(this);
        });
        btn_showFileB.addActionListener(e -> {
            frame_depFile.setVisible(true);
            frame_depFile.setLocationRelativeTo(this);
        });
        btn_showFileC.addActionListener(e -> {
            frame_depFile.setVisible(true);
            frame_depFile.setLocationRelativeTo(this);
        });

        //输入框
        textArea_allSend.addKeyListener(KeyMonitor.keyListener);
        textArea_ASend.addKeyListener(KeyMonitor.keyListener);
        textArea_BSend.addKeyListener(KeyMonitor.keyListener);
        textArea_CSend.addKeyListener(KeyMonitor.keyListener);
        textArea_sbSend.addKeyListener(KeyMonitor.keyListener);
    }

    /** 设置控件名称 */
    private void setComponentsName() {
        btn_sendMsgToAll.setName("client_sendMsgToAll");
        btn_sendFileToAll.setName("client_sendFileToAll");
        btn_sendMsgToA.setName("client_sendMsgToA");
        btn_sendFileToA.setName("client_sendFileToA");
        btn_sendMsgToB.setName("client_sendMsgToB");
        btn_sendFileToB.setName("client_sendFileToB");
        btn_sendMsgToC.setName("client_sendMsgToC");
        btn_sendFileToC.setName("client_sendFileToC");
        btn_sendMsgToSb.setName("client_sendMsgToSb");
        btn_sendFileToSb.setName("client_sendFileToSb");
        btn_showFileAll.setName("client_showFileAll");

        textArea_allSend.setName("area_allSend");
        textArea_ASend.setName("area_ASend");
        textArea_BSend.setName("area_BSend");
        textArea_CSend.setName("area_CSend");
        textArea_sbSend.setName("area_sbSend");
    }

    /** 设置部门，以隐藏其余部门聊天窗口 */
    public void setDepartment(String department) {
        //使用的是JFormDesigner,窗口默认全部添加，需要手动去除
        switch (department) {
            case "行政部门A" -> {
                tabbedPane.remove(panel_B);
                tabbedPane.remove(panel_C);
            }
            case "行政部门B" -> {
                tabbedPane.remove(panel_A);
                tabbedPane.remove(panel_C);
            }
            case "行政部门C" -> {
                tabbedPane.remove(panel_A);
                tabbedPane.remove(panel_B);
            }
        }
    }

    /** 更新下拉框中在线人员名称 */
    public void updateOnlineClient(String name, boolean isOnline) {
        if (isOnline) {
            cbBox_target.addItem(name);
        } else {
            cbBox_target.removeItem(name);
        }
    }

    /** 获取下拉框选择的发送目标 */
    public String getTarget() {
        return Objects.requireNonNull(cbBox_target.getSelectedItem()).toString();
    }

    /** 将消息显示在消息框 */
    public void showMsg(String msg, String target) {
        switch (target) {
            case "all" -> textArea_allShow.append(msg);
            case "行政部门A" -> textArea_AShow.append(msg);
            case "行政部门B" -> textArea_BShow.append(msg);
            case "行政部门C" -> textArea_CShow.append(msg);
            case "sb" -> textArea_sbShow.append(msg);
        }
        ;
    }

    /** 获取输入框内容 */
    public String getSendArea(String target) {
        switch (target) {
            case "all" -> {
                return textArea_allSend.getText();
            }
            case "行政部门A" -> {
                return textArea_ASend.getText();
            }
            case "行政部门B" -> {
                return textArea_BSend.getText();
            }
            case "行政部门C" -> {
                return textArea_CSend.getText();
            }
            case "sb" -> {
                return textArea_sbSend.getText();
            }
            default -> {
                return "";
            }
        }
    }

    /** 清空"私聊"页面的输入框内容 */
    public void clearSendArea(String target) {
        switch (target) {
            case "all" -> textArea_allSend.setText("");
            case "行政部门A" -> textArea_ASend.setText("");
            case "行政部门B" -> textArea_BSend.setText("");
            case "行政部门C" -> textArea_CSend.setText("");
            case "sb" -> textArea_sbSend.setText("");
        }
    }

    /** 设置界面标题 */
    public void setFrameTitle(String title) {
        this.setTitle(title);
    }

    /** 设置“全部”页面中的文件下载表格 */
    public void setTable_allFile(AbstractTableModel model) {
        int[] columnWidth = {150, 150, 150, 70};
        tableSet(table_allFile, model, columnWidth, "all");
        //设置表格禁止选中行，否则点击按钮会被响应为选中行
        table_allFile.setRowSelectionAllowed(false);
    }

    /** 设置“部门”页面中的文件下载表格 */
    public void setTable_depFile(AbstractTableModel model) {
        int[] columnWidth = {150, 150, 150, 70};
        tableSet(table_depFile, model, columnWidth, "dep");
        //设置表格禁止选中行，否则点击按钮会被响应为选中行
        table_depFile.setRowSelectionAllowed(false);
    }

    /** 设置默认表格格式 */
    private void tableSet(JTable table, AbstractTableModel model, int[] columnWidth, String type) {
        table.setModel(model);
        table.setSelectionBackground(new Color(0x78B2B1));
        table.getTableHeader().setReorderingAllowed(false);
        //table.getTableHeader().setResizingAllowed(false);
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (!"下载".equals(value)) {
                    if (row % 2 == 0)
                        setBackground(Color.WHITE);
                    else if (row % 2 == 1)
                        setBackground(new Color(0xDDE9F7));
                    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                } else {
                    JButton button = new JButton();
                    button.setText(String.valueOf(value));
                    return button;
                }
            }
        };
        tcr.setHorizontalAlignment(JLabel.CENTER);
        class MyButtonEditor extends DefaultCellEditor {
            private final JButton button = new JButton();

            public MyButtonEditor() {
                super(new JTextField());
                setClickCountToStart(1);
                button.addActionListener(ButtonMonitor.btnListener);
                if ("all".equals(type)) {
                    button.setName("client_downloadAll");
                } else if ("dep".equals(type)) {
                    button.setName("client_downloadDep");
                }

            }

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                //不加空格的话按下后文字会偏右，不知道为什么
                button.setText(value + "    ");
                return button;
            }
        }

        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setPreferredWidth(columnWidth[i]);
            column.setCellRenderer(tcr);
            if (column.getHeaderValue().equals("下载")) {
                column.setCellEditor(new MyButtonEditor());
            }
        }
    }

    /** 获取“全部”文件下载页面中预下载文件的名称 */
    public String getAllFileName() {
        int rowIndex = table_allFile.getSelectedRow();
        String fileName = table_allFile.getValueAt(rowIndex, 0).toString();
        String prefix = table_allFile.getValueAt(rowIndex, 2).toString();
        fileName = prefix + "_" + fileName;
        return fileName;
    }

    /** 获取“部门”文件下载页面中预下载文件的名称 */
    public String getDepFileName() {
        int rowIndex = table_depFile.getSelectedRow();
        String fileName = table_depFile.getValueAt(rowIndex, 0).toString();
        String prefix = table_depFile.getValueAt(rowIndex, 2).toString();
        fileName = prefix + "_" + fileName;
        return fileName;
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        tabbedPane = new JTabbedPane();
        panel_all = new JPanel();
        scrollPane1 = new JScrollPane();
        textArea_allShow = new JTextArea();
        scrollPane2 = new JScrollPane();
        textArea_allSend = new JTextArea();
        btn_showFileAll = new JButton();
        btn_sendMsgToAll = new JButton();
        btn_sendFileToAll = new JButton();
        panel_A = new JPanel();
        scrollPane3 = new JScrollPane();
        textArea_AShow = new JTextArea();
        scrollPane4 = new JScrollPane();
        textArea_ASend = new JTextArea();
        btn_showFileA = new JButton();
        btn_sendMsgToA = new JButton();
        btn_sendFileToA = new JButton();
        panel_B = new JPanel();
        scrollPane5 = new JScrollPane();
        textArea_BShow = new JTextArea();
        scrollPane6 = new JScrollPane();
        textArea_BSend = new JTextArea();
        btn_showFileB = new JButton();
        btn_sendMsgToB = new JButton();
        btn_sendFileToB = new JButton();
        panel_C = new JPanel();
        scrollPane7 = new JScrollPane();
        textArea_CShow = new JTextArea();
        scrollPane8 = new JScrollPane();
        textArea_CSend = new JTextArea();
        btn_showFileC = new JButton();
        btn_sendMsgToC = new JButton();
        btn_sendFileToC = new JButton();
        panel_sb = new JPanel();
        scrollPane9 = new JScrollPane();
        textArea_sbShow = new JTextArea();
        scrollPane10 = new JScrollPane();
        textArea_sbSend = new JTextArea();
        label1 = new JLabel();
        cbBox_target = new JComboBox();
        btn_sendMsgToSb = new JButton();
        btn_sendFileToSb = new JButton();
        frame_allFile = new JFrame();
        scrollPane11 = new JScrollPane();
        table_allFile = new JTable();
        frame_depFile = new JFrame();
        scrollPane12 = new JScrollPane();
        table_depFile = new JTable();

        //======== this ========
        setTitle("\u5ba2\u6237\u7aef");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        var contentPane = getContentPane();
        contentPane.setLayout(new GridLayout());

        //======== tabbedPane ========
        {
            tabbedPane.setTabPlacement(SwingConstants.LEFT);

            //======== panel_all ========
            {
                panel_all.setLayout(new GridBagLayout());
                ((GridBagLayout) panel_all.getLayout()).columnWidths = new int[]{100, 272, 100, 5, 100, 0, 0};
                ((GridBagLayout) panel_all.getLayout()).rowHeights = new int[]{289, 125, 0, 0};
                ((GridBagLayout) panel_all.getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0E-4};
                ((GridBagLayout) panel_all.getLayout()).rowWeights = new double[]{0.0, 0.0, 1.0, 1.0E-4};

                //======== scrollPane1 ========
                {

                    //---- textArea_allShow ----
                    textArea_allShow.setBorder(new TitledBorder("\u6d88\u606f"));
                    textArea_allShow.setEditable(false);
                    textArea_allShow.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 16));
                    scrollPane1.setViewportView(textArea_allShow);
                }
                panel_all.add(scrollPane1, new GridBagConstraints(0, 0, 6, 1, 0.0, 0.0,
                                                                  GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                                  new Insets(0, 0, 0, 0), 0, 0));

                //======== scrollPane2 ========
                {

                    //---- textArea_allSend ----
                    textArea_allSend.setBorder(new TitledBorder("\u8f93\u5165"));
                    textArea_allSend.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 16));
                    scrollPane2.setViewportView(textArea_allSend);
                }
                panel_all.add(scrollPane2, new GridBagConstraints(0, 1, 6, 1, 0.0, 0.0,
                                                                  GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                                  new Insets(0, 0, 0, 0), 0, 0));

                //---- btn_showFileAll ----
                btn_showFileAll.setText("\u6587\u4ef6");
                btn_showFileAll.setToolTipText("\u5feb\u6377\u952e\u201cctrl+enter\u201d\u53d1\u9001\u6d88\u606f");
                panel_all.add(btn_showFileAll, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                                                                      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                                      new Insets(0, 0, 0, 0), 0, 0));

                //---- btn_sendMsgToAll ----
                btn_sendMsgToAll.setText("\u53d1\u9001\u6d88\u606f");
                btn_sendMsgToAll.setToolTipText("\u5feb\u6377\u952e\u201cctrl+enter\u201d\u53d1\u9001\u6d88\u606f");
                panel_all.add(btn_sendMsgToAll, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                                                                       GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                                       new Insets(0, 0, 0, 0), 0, 0));

                //---- btn_sendFileToAll ----
                btn_sendFileToAll.setText("\u53d1\u9001\u6587\u4ef6");
                panel_all.add(btn_sendFileToAll, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0,
                                                                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                                        new Insets(0, 0, 0, 0), 0, 0));
            }
            tabbedPane.addTab("\u5168\u90e8", panel_all);

            //======== panel_A ========
            {
                panel_A.setLayout(new GridBagLayout());
                ((GridBagLayout) panel_A.getLayout()).columnWidths = new int[]{100, 272, 100, 5, 100, 0, 0};
                ((GridBagLayout) panel_A.getLayout()).rowHeights = new int[]{289, 125, 0, 0};
                ((GridBagLayout) panel_A.getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0E-4};
                ((GridBagLayout) panel_A.getLayout()).rowWeights = new double[]{0.0, 0.0, 1.0, 1.0E-4};

                //======== scrollPane3 ========
                {

                    //---- textArea_AShow ----
                    textArea_AShow.setBorder(new TitledBorder("\u6d88\u606f"));
                    textArea_AShow.setEditable(false);
                    textArea_AShow.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 16));
                    scrollPane3.setViewportView(textArea_AShow);
                }
                panel_A.add(scrollPane3, new GridBagConstraints(0, 0, 6, 1, 0.0, 0.0,
                                                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                                new Insets(0, 0, 0, 0), 0, 0));

                //======== scrollPane4 ========
                {

                    //---- textArea_ASend ----
                    textArea_ASend.setBorder(new TitledBorder("\u8f93\u5165"));
                    textArea_ASend.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 16));
                    scrollPane4.setViewportView(textArea_ASend);
                }
                panel_A.add(scrollPane4, new GridBagConstraints(0, 1, 6, 1, 0.0, 0.0,
                                                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                                new Insets(0, 0, 0, 0), 0, 0));

                //---- btn_showFileA ----
                btn_showFileA.setText("\u6587\u4ef6");
                btn_showFileA.setToolTipText("\u5feb\u6377\u952e\u201cctrl+enter\u201d\u53d1\u9001\u6d88\u606f");
                panel_A.add(btn_showFileA, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                                                                  GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                                  new Insets(0, 0, 0, 0), 0, 0));

                //---- btn_sendMsgToA ----
                btn_sendMsgToA.setText("\u53d1\u9001\u6d88\u606f");
                btn_sendMsgToA.setToolTipText("\u5feb\u6377\u952e\u201cctrl+enter\u201d\u53d1\u9001\u6d88\u606f");
                panel_A.add(btn_sendMsgToA, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                                                                   GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                                   new Insets(0, 0, 0, 0), 0, 0));

                //---- btn_sendFileToA ----
                btn_sendFileToA.setText("\u53d1\u9001\u6587\u4ef6");
                panel_A.add(btn_sendFileToA, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0,
                                                                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                                    new Insets(0, 0, 0, 0), 0, 0));
            }
            tabbedPane.addTab("\u90e8\u95e8A", panel_A);

            //======== panel_B ========
            {
                panel_B.setLayout(new GridBagLayout());
                ((GridBagLayout) panel_B.getLayout()).columnWidths = new int[]{100, 272, 100, 5, 100, 0, 0};
                ((GridBagLayout) panel_B.getLayout()).rowHeights = new int[]{289, 125, 0, 0};
                ((GridBagLayout) panel_B.getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0E-4};
                ((GridBagLayout) panel_B.getLayout()).rowWeights = new double[]{0.0, 0.0, 1.0, 1.0E-4};

                //======== scrollPane5 ========
                {

                    //---- textArea_BShow ----
                    textArea_BShow.setBorder(new TitledBorder("\u6d88\u606f"));
                    textArea_BShow.setEditable(false);
                    textArea_BShow.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 16));
                    scrollPane5.setViewportView(textArea_BShow);
                }
                panel_B.add(scrollPane5, new GridBagConstraints(0, 0, 6, 1, 0.0, 0.0,
                                                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                                new Insets(0, 0, 0, 0), 0, 0));

                //======== scrollPane6 ========
                {

                    //---- textArea_BSend ----
                    textArea_BSend.setBorder(new TitledBorder("\u8f93\u5165"));
                    textArea_BSend.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 16));
                    scrollPane6.setViewportView(textArea_BSend);
                }
                panel_B.add(scrollPane6, new GridBagConstraints(0, 1, 6, 1, 0.0, 0.0,
                                                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                                new Insets(0, 0, 0, 0), 0, 0));

                //---- btn_showFileB ----
                btn_showFileB.setText("\u6587\u4ef6");
                btn_showFileB.setToolTipText("\u5feb\u6377\u952e\u201cctrl+enter\u201d\u53d1\u9001\u6d88\u606f");
                panel_B.add(btn_showFileB, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                                                                  GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                                  new Insets(0, 0, 0, 0), 0, 0));

                //---- btn_sendMsgToB ----
                btn_sendMsgToB.setText("\u53d1\u9001\u6d88\u606f");
                btn_sendMsgToB.setToolTipText("\u5feb\u6377\u952e\u201cctrl+enter\u201d\u53d1\u9001\u6d88\u606f");
                panel_B.add(btn_sendMsgToB, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                                                                   GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                                   new Insets(0, 0, 0, 0), 0, 0));

                //---- btn_sendFileToB ----
                btn_sendFileToB.setText("\u53d1\u9001\u6587\u4ef6");
                panel_B.add(btn_sendFileToB, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0,
                                                                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                                    new Insets(0, 0, 0, 0), 0, 0));
            }
            tabbedPane.addTab("\u90e8\u95e8B", panel_B);

            //======== panel_C ========
            {
                panel_C.setLayout(new GridBagLayout());
                ((GridBagLayout) panel_C.getLayout()).columnWidths = new int[]{100, 272, 100, 5, 100, 0, 0};
                ((GridBagLayout) panel_C.getLayout()).rowHeights = new int[]{289, 125, 0, 0};
                ((GridBagLayout) panel_C.getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0E-4};
                ((GridBagLayout) panel_C.getLayout()).rowWeights = new double[]{0.0, 0.0, 1.0, 1.0E-4};

                //======== scrollPane7 ========
                {

                    //---- textArea_CShow ----
                    textArea_CShow.setBorder(new TitledBorder("\u6d88\u606f"));
                    textArea_CShow.setEditable(false);
                    textArea_CShow.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 16));
                    scrollPane7.setViewportView(textArea_CShow);
                }
                panel_C.add(scrollPane7, new GridBagConstraints(0, 0, 6, 1, 0.0, 0.0,
                                                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                                new Insets(0, 0, 0, 0), 0, 0));

                //======== scrollPane8 ========
                {

                    //---- textArea_CSend ----
                    textArea_CSend.setBorder(new TitledBorder("\u8f93\u5165"));
                    textArea_CSend.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 16));
                    scrollPane8.setViewportView(textArea_CSend);
                }
                panel_C.add(scrollPane8, new GridBagConstraints(0, 1, 6, 1, 0.0, 0.0,
                                                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                                new Insets(0, 0, 0, 0), 0, 0));

                //---- btn_showFileC ----
                btn_showFileC.setText("\u6587\u4ef6");
                btn_showFileC.setToolTipText("\u5feb\u6377\u952e\u201cctrl+enter\u201d\u53d1\u9001\u6d88\u606f");
                panel_C.add(btn_showFileC, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                                                                  GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                                  new Insets(0, 0, 0, 0), 0, 0));

                //---- btn_sendMsgToC ----
                btn_sendMsgToC.setText("\u53d1\u9001\u6d88\u606f");
                btn_sendMsgToC.setToolTipText("\u5feb\u6377\u952e\u201cctrl+enter\u201d\u53d1\u9001\u6d88\u606f");
                panel_C.add(btn_sendMsgToC, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                                                                   GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                                   new Insets(0, 0, 0, 0), 0, 0));

                //---- btn_sendFileToC ----
                btn_sendFileToC.setText("\u53d1\u9001\u6587\u4ef6");
                panel_C.add(btn_sendFileToC, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0,
                                                                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                                    new Insets(0, 0, 0, 0), 0, 0));
            }
            tabbedPane.addTab("\u90e8\u95e8C", panel_C);

            //======== panel_sb ========
            {
                panel_sb.setLayout(new GridBagLayout());
                ((GridBagLayout) panel_sb.getLayout()).columnWidths = new int[]{187, 60, 120, 5, 100, 5, 100, 0, 0};
                ((GridBagLayout) panel_sb.getLayout()).rowHeights = new int[]{290, 125, 0, 0};
                ((GridBagLayout) panel_sb.getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0E-4};
                ((GridBagLayout) panel_sb.getLayout()).rowWeights = new double[]{0.0, 0.0, 1.0, 1.0E-4};

                //======== scrollPane9 ========
                {

                    //---- textArea_sbShow ----
                    textArea_sbShow.setBorder(new TitledBorder("\u6d88\u606f"));
                    textArea_sbShow.setEditable(false);
                    textArea_sbShow.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 16));
                    scrollPane9.setViewportView(textArea_sbShow);
                }
                panel_sb.add(scrollPane9, new GridBagConstraints(0, 0, 8, 1, 0.0, 0.0,
                                                                 GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                                 new Insets(0, 0, 0, 0), 0, 0));

                //======== scrollPane10 ========
                {

                    //---- textArea_sbSend ----
                    textArea_sbSend.setBorder(new TitledBorder("\u8f93\u5165"));
                    textArea_sbSend.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 16));
                    scrollPane10.setViewportView(textArea_sbSend);
                }
                panel_sb.add(scrollPane10, new GridBagConstraints(0, 1, 8, 1, 0.0, 0.0,
                                                                  GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                                  new Insets(0, 0, 0, 0), 0, 0));

                //---- label1 ----
                label1.setText("\u53d1\u9001\u7ed9");
                label1.setHorizontalAlignment(SwingConstants.TRAILING);
                panel_sb.add(label1, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                                                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                            new Insets(0, 0, 0, 0), 0, 0));

                //---- cbBox_target ----
                cbBox_target.setMinimumSize(new Dimension(120, 38));
                cbBox_target.setPreferredSize(new Dimension(120, 38));
                panel_sb.add(cbBox_target, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                                                                  GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                                  new Insets(0, 0, 0, 0), 0, 0));

                //---- btn_sendMsgToSb ----
                btn_sendMsgToSb.setText("\u53d1\u9001\u6d88\u606f");
                btn_sendMsgToSb.setToolTipText("\u5feb\u6377\u952e\u201cctrl+enter\u201d\u53d1\u9001\u6d88\u606f");
                panel_sb.add(btn_sendMsgToSb, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0,
                                                                     GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                                     new Insets(0, 0, 0, 0), 0, 0));

                //---- btn_sendFileToSb ----
                btn_sendFileToSb.setText("\u53d1\u9001\u6587\u4ef6");
                panel_sb.add(btn_sendFileToSb, new GridBagConstraints(6, 2, 1, 1, 0.0, 0.0,
                                                                      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                                      new Insets(0, 0, 0, 0), 0, 0));
            }
            tabbedPane.addTab("\u79c1\u804a", panel_sb);
        }
        contentPane.add(tabbedPane);
        pack();
        setLocationRelativeTo(getOwner());

        //======== frame_allFile ========
        {
            frame_allFile.setTitle("\u6587\u4ef6\u4e0b\u8f7d");
            frame_allFile.setResizable(false);
            var frame_allFileContentPane = frame_allFile.getContentPane();

            //======== scrollPane11 ========
            {
                scrollPane11.setViewportView(table_allFile);
            }

            GroupLayout frame_allFileContentPaneLayout = new GroupLayout(frame_allFileContentPane);
            frame_allFileContentPane.setLayout(frame_allFileContentPaneLayout);
            frame_allFileContentPaneLayout.setHorizontalGroup(
                    frame_allFileContentPaneLayout.createParallelGroup()
                            .addGroup(frame_allFileContentPaneLayout.createSequentialGroup()
                                              .addContainerGap()
                                              .addComponent(scrollPane11, GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                                              .addContainerGap())
            );
            frame_allFileContentPaneLayout.setVerticalGroup(
                    frame_allFileContentPaneLayout.createParallelGroup()
                            .addGroup(frame_allFileContentPaneLayout.createSequentialGroup()
                                              .addContainerGap()
                                              .addComponent(scrollPane11, GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
                                              .addContainerGap())
            );
            frame_allFile.pack();
            frame_allFile.setLocationRelativeTo(frame_allFile.getOwner());
        }

        //======== frame_depFile ========
        {
            frame_depFile.setTitle("\u6587\u4ef6\u4e0b\u8f7d");
            frame_depFile.setResizable(false);
            var frame_depFileContentPane = frame_depFile.getContentPane();

            //======== scrollPane12 ========
            {
                scrollPane12.setViewportView(table_depFile);
            }

            GroupLayout frame_depFileContentPaneLayout = new GroupLayout(frame_depFileContentPane);
            frame_depFileContentPane.setLayout(frame_depFileContentPaneLayout);
            frame_depFileContentPaneLayout.setHorizontalGroup(
                    frame_depFileContentPaneLayout.createParallelGroup()
                            .addGroup(frame_depFileContentPaneLayout.createSequentialGroup()
                                              .addContainerGap()
                                              .addComponent(scrollPane12, GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                                              .addContainerGap())
            );
            frame_depFileContentPaneLayout.setVerticalGroup(
                    frame_depFileContentPaneLayout.createParallelGroup()
                            .addGroup(frame_depFileContentPaneLayout.createSequentialGroup()
                                              .addContainerGap()
                                              .addComponent(scrollPane12, GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
                                              .addContainerGap())
            );
            frame_depFile.pack();
            frame_depFile.setLocationRelativeTo(frame_depFile.getOwner());
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JTabbedPane tabbedPane;
    private JPanel panel_all;
    private JScrollPane scrollPane1;
    private JTextArea textArea_allShow;
    private JScrollPane scrollPane2;
    private JTextArea textArea_allSend;
    private JButton btn_showFileAll;
    private JButton btn_sendMsgToAll;
    private JButton btn_sendFileToAll;
    private JPanel panel_A;
    private JScrollPane scrollPane3;
    private JTextArea textArea_AShow;
    private JScrollPane scrollPane4;
    private JTextArea textArea_ASend;
    private JButton btn_showFileA;
    private JButton btn_sendMsgToA;
    private JButton btn_sendFileToA;
    private JPanel panel_B;
    private JScrollPane scrollPane5;
    private JTextArea textArea_BShow;
    private JScrollPane scrollPane6;
    private JTextArea textArea_BSend;
    private JButton btn_showFileB;
    private JButton btn_sendMsgToB;
    private JButton btn_sendFileToB;
    private JPanel panel_C;
    private JScrollPane scrollPane7;
    private JTextArea textArea_CShow;
    private JScrollPane scrollPane8;
    private JTextArea textArea_CSend;
    private JButton btn_showFileC;
    private JButton btn_sendMsgToC;
    private JButton btn_sendFileToC;
    private JPanel panel_sb;
    private JScrollPane scrollPane9;
    private JTextArea textArea_sbShow;
    private JScrollPane scrollPane10;
    private JTextArea textArea_sbSend;
    private JLabel label1;
    private JComboBox cbBox_target;
    private JButton btn_sendMsgToSb;
    private JButton btn_sendFileToSb;
    private JFrame frame_allFile;
    private JScrollPane scrollPane11;
    private JTable table_allFile;
    private JFrame frame_depFile;
    private JScrollPane scrollPane12;
    private JTable table_depFile;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
