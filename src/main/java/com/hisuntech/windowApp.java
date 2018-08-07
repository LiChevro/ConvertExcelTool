package com.hisuntech;

import com.hisuntech.entity.Table;
import com.hisuntech.utils.GenerateSqlUtil;
import com.hisuntech.utils.TransferExcelUtil;
import com.hisuntech.utils.outToFileUtil;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import java.awt.EventQueue;

import javax.swing.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.filechooser.FileNameExtensionFilter;



public class windowApp {

    private JFrame frame;
    private JTextField textField;
    private JTextField textField_1;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("RootPane.setupButtonVisible", false);
            BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    windowApp window = new windowApp();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public windowApp() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Init DataBase Tool");
        frame.setBounds(100, 100, 500, 393);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JButton btnNewButton = new JButton("\u9009\u62E9Excel");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();// 文件选择对话框
                //chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);// 取消所有文件过滤项
                chooser.setFileFilter(new FileNameExtensionFilter("Excel文件", "xlsx"));// 设置只过滤扩展名为.xls的Excel文件

                int i = chooser.showOpenDialog(frame);// 打开窗口
                if (i == JFileChooser.APPROVE_OPTION) {
                    textField_1.setText(chooser.getSelectedFile().getAbsolutePath());
                    //解释下这里,弹出个对话框,可以选择要上传的文件,如果选择了,就把选择的文件的绝对路径打印出来,有了绝对路径,通过JTextField的settext就能设置进去了,那个我没写
                    System.out.println(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        btnNewButton.setBounds(346, 90, 91, 32);
        frame.getContentPane().add(btnNewButton);

        JButton button = new JButton("\u8F93\u51FA\u76EE\u5F55");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fileChooser.showOpenDialog(frame);
                if(returnVal == JFileChooser.APPROVE_OPTION)
                { String filePath= fileChooser.getSelectedFile().getAbsolutePath();
                    textField.setText(filePath);
                    System.out.println(filePath);
                }

            }
        });
        button.setBounds(346, 183, 91, 32);
        frame.getContentPane().add(button);

        textField = new JTextField();
        textField.setEditable(false);
        textField.setBounds(35, 174, 266, 41);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        textField_1 = new JTextField();
        textField_1.setEditable(false);
        textField_1.setColumns(10);
        textField_1.setBounds(35, 90, 266, 41);
        frame.getContentPane().add(textField_1);

        JButton btnNewButton_1 = new JButton("\u751F\u6210SQL");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!textField.getText().equals("")&&!textField_1.getText().equals("")) {
                    System.out.println(textField.getText());//输出路径
                    System.out.println(textField_1.getText());//输入路径

                    //这里写文件处理程序
                    String savePath = textField.getText();
                    String path = textField_1.getText();
                    List<Table> tableList = new ArrayList<>();
                    tableList = TransferExcelUtil.readExcel(path);
                    Map<String,List> map = GenerateSqlUtil.outSql(tableList);
                    //1.生成创建表的SQL
                    List<StringBuffer> sqlList = map.get("createSqlList");
                    //2.设置主键的SQL
                    List<StringBuffer> sqlList2 = map.get("primarySqlList");
                    //3.注释的SQL
                    List<StringBuffer> sqlList3 = map.get("commentSQL");
                    //4.生成索引
                    List<StringBuffer> sqlList4 = null;
                    //5.输出到文件
                    try {
                        outToFileUtil.outToFile(sqlList,sqlList2,sqlList3,sqlList4,tableList,savePath);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                }else {
                    JOptionPane.showMessageDialog(frame, "请选择文件与路径", "错误",JOptionPane.WARNING_MESSAGE);
                }


            }
        });
        btnNewButton_1.setBounds(35, 256, 391, 60);
        frame.getContentPane().add(btnNewButton_1);
    }
}
