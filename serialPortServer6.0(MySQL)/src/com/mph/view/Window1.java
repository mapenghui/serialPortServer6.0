package com.mph.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;
import javax.swing.text.html.CSS;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

import com.mph.Server.ChatServer;
import com.mph.Server.NumObservable;
//import com.mph.Server.ChatServer.Client;
import com.mph.desk.Log;
import com.mph.desk.SerialPortsWindLines;
import com.mph.desk.SerialReader;
import com.mph.view.MemoryUsageDemo.DataGenerator;

public class Window1 extends JFrame implements ActionListener, ItemListener {
	SerialPortsWindLines spwl = new SerialPortsWindLines();
	public SerialReader sr = new SerialReader();
	MemoryUsageDemo memoryusagedemo; 
	
	JPanel portConfigPanel, recConfigPanel,sendConfigPanel, chartPanel, dectivePanel, sendDataPanel, jPanelEN, jPanelEC, jPanelB, jPanel1, jPanel2, jPanel12, jPanel13,jPanel21, jPanel22, jPanel23, jPanelLN, jpanelLC, jpanelLC1, jpanelLC2, jpanelLC3, mudJPanel, jChatPanel;
	JLabel portLabel, rateLabel, parityBitLabel, dataBitLabel, stopBitLabel, operationLabel, operResultLabel, authorLabel, portStateLabel, directLabel, pitchJLabel, recDirJLabel, recPitLabel, recDirDisLabel, recPitDisLabel; 
	JComboBox portComboBox, rateComboBox, parityComboBox, dataBitComboBox, stopBitComboBox;
	JToggleButton openOrCloseToggleButton, stopDisToggleButton, saveToggleButton;
	JSpinner detectiveSpinner; 
	JButton dectiveButton, clearButton, sendDataBtn, normalButton, turnButton, clearRecButton, sendAngButton, dataRecButton, figureButton, jchatbButton, chatButton, jChatb, jChatClear, jbZXWH, jbSAT2, jbYZYH, jbYZEH, jbMXEH; 
	JCheckBox checkHexCheckBox, checkHexCheckBoxR;
	JScrollPane inputTextScrollPane, outputTextScrollPane, jChatsp; 
	JTextArea inputTextArea;
	JToolBar operationToolBar, operaResultToolBar, anthorToolBar; 
	JTextField jTextFieldDirect, jTextFieldPitch, jChattf;
	JTabbedPane jtbPane1, jtbPane2;
	CardLayout clayout, clayoutL;
	ImageIcon image, image1;
	MaskFormatter formatter;  
	boolean hexSignal, hexSignalR;
	double direct, pitch;
	Pattern pattern;
	Matcher matcher;
	
	public JTextArea jChatta;
	public JComboBox chatNmComboBox;
	public JLabel jChatlabel;
	
	ServerSocket ss;
	boolean started = false;
	String checkString;
	ChatServer chatServer = new ChatServer(this);
	
	public static JTextArea shuchuText;
	public Vector<String> itemOfUsers = new Vector<String>();  //当前用户姓名的列表
	
	public static void main(String[] args) {
		
		Window1 window1 = new Window1();
		
	}
    
	public void initNormalSet() {
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("卫星远程控制服务器");
        setName("SerialPortsWindLinesFrame"); // NOI18N
        setResizable(false);
		
        chartPanel = new JPanel();
        chartPanel.setName("chartPanel"); // NOI18N
        
        dectivePanel = new JPanel();
        dectivePanel.setName("dectivePanel"); // NOI18N

        detectiveSpinner = new JSpinner();
        detectiveSpinner.setModel(new SpinnerNumberModel(0, 0, 511, 1));
        detectiveSpinner.setName("detectiveSpinner"); // NOI18N

        dectiveButton = new JButton();
        dectiveButton.setText("探测");
        dectiveButton.setName("dectiveButton"); // NOI18N
        dectiveButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
         //   	dectiveButtonMouseClicked(evt);
            }
        });
                   
        operationToolBar = new JToolBar();
        operationToolBar.setFloatable(false);
        operationToolBar.setRollover(true);
        operationToolBar.setEnabled(false);
        operationToolBar.setName("operationToolBar"); // NOI18N

        operationLabel = new JLabel();
        operationLabel.setName("operationLabel"); // NOI18N
        operationLabel.setPreferredSize(new Dimension(80, 20));
        operationToolBar.add(operationLabel);

        operaResultToolBar = new JToolBar();
        operaResultToolBar.setFloatable(false);
        operaResultToolBar.setRollover(true);
        operaResultToolBar.setName("operaResultToolBar"); // NOI18N

        operResultLabel = new JLabel();
        operResultLabel.setName("operResultLabel"); // NOI18N
        operaResultToolBar.add(operResultLabel);

        anthorToolBar = new JToolBar();
        anthorToolBar.setFloatable(false);
        anthorToolBar.setRollover(true);
        anthorToolBar.setToolTipText("QQ:52");
        anthorToolBar.setName("anthorToolBar"); // NOI18N

        authorLabel = new JLabel();
        authorLabel.setText("技术支持:H H U        ");
        authorLabel.setName("authorLabel"); // NOI18N             
        
        ///////////////////////////////////////////////////
        
        //添加两个按键的面板jPanelLN
        dataRecButton = new JButton("串口收发"); 
        dataRecButton.addActionListener(this);
        figureButton = new JButton("图形显示");
        figureButton.addActionListener(this);
        chatButton = new JButton("网络收发"); 
        chatButton.addActionListener(this);
        jChatlabel = new JLabel("用户数：0");
        chatNmComboBox = new JComboBox();
        chatNmComboBox.setModel(new DefaultComboBoxModel(new String[] {" "}));
        chatNmComboBox.setToolTipText("用户数");
        chatNmComboBox.setName("chatNmComboBox"); // NOI18N
        jPanelLN = new JPanel();
        jPanelLN.setLayout(new FlowLayout());
        jPanelLN.add(chatButton);
        jPanelLN.add(dataRecButton);
        jPanelLN.add(figureButton);
        jPanelLN.add(jChatlabel);
        jPanelLN.add(chatNmComboBox);
        //jPanelLN.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        jPanelLN.setBounds(3, 2, 470, 30);
        
        //数据接收区设置
        shuchuText=new JTextArea();
        shuchuText.setLineWrap(true);                     //激活自动换行功能 
        shuchuText.setWrapStyleWord(true);                // 激活断行不断字功能
        shuchuText.setBounds(7, 10, 470, 200);
        //滚动条自动跟踪
        shuchuText.getDocument().addDocumentListener(new DocumentListener(){
			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				shuchuText.setCaretPosition(shuchuText.getText().length());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
			}  	
        	});
        
        outputTextScrollPane = new JScrollPane(shuchuText);
        outputTextScrollPane.setName("outputTextScrollPane"); // NOI18N
        outputTextScrollPane.setBounds(7, 10, 470, 240);
        
        //数据输出区设置
        inputTextArea = new JTextArea();
        inputTextArea.setColumns(20);
        inputTextArea.setRows(7);
        inputTextArea.setToolTipText("输入数据");
        inputTextArea.setName("inputTextArea"); // NOI18N
        inputTextArea.setLineWrap(true);                     //激活自动换行功能 
        inputTextArea.setWrapStyleWord(true); 
        inputTextArea.setBounds(7, 300, 470, 240);
        
        inputTextScrollPane = new JScrollPane(inputTextArea);
        inputTextScrollPane.setName("inputTextScrollPane"); // NOI18N
        inputTextScrollPane.setViewportView(inputTextArea);
        inputTextScrollPane.setBounds(7, 300, 470, 400);
        
        jpanelLC1 = new JPanel(new BorderLayout());
        jpanelLC1.add(outputTextScrollPane, BorderLayout.CENTER);
        jpanelLC1.add(inputTextScrollPane, BorderLayout.SOUTH);
        jpanelLC1.setBounds(7, 32, 470, 420);
        
        //画图板
        memoryusagedemo = new MemoryUsageDemo(30000);
        memoryusagedemo.setBounds(27, 32, 430, 420);
        (memoryusagedemo.new DataGenerator(100)).start();
		
        jpanelLC2 = new JPanel(new BorderLayout());
        jpanelLC2.add(memoryusagedemo, BorderLayout.CENTER);
        
        jChatta = new JTextArea();
        //滚动条自动跟踪
        jChatta.getDocument().addDocumentListener(new DocumentListener(){
			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				jChatta.setCaretPosition(jChatta.getText().length());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
			}  	
        	});
        
		jChatsp = new JScrollPane(jChatta);
		
		jChattf = new JTextField(55);
		jChatb = new JButton("发送");
		jChatb.addActionListener(this);
		jChatClear = new JButton("清空");
		jChatClear.addActionListener(this);
		jChatPanel = new JPanel();
		jChatPanel.add(jChattf);
		jChatPanel.add(jChatb); 
		jChatPanel.add(jChatClear);
		//jChatlabel.setText("当前客户连接数量：" + String.valueOf(numObs.getClientCount()));
		
		jpanelLC3 = new JPanel(new BorderLayout());
		jpanelLC3.add(jChatsp, "Center");
		jpanelLC3.add(jChatPanel, "South");
		//this.setTitle("服务器");
		jpanelLC3.setSize(400, 300);
		//this.setVisible(true);
        //jpanelLC3.add(memoryusagedemo, BorderLayout.CENTER);
        
        clayoutL = new CardLayout();
        jpanelLC = new JPanel(clayoutL);
        //jpanelLC.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        jpanelLC.setBounds(7, 32, 470, 420);
             
        jpanelLC.add(jpanelLC3, "3");
        jpanelLC.add(jpanelLC1, "1");
        jpanelLC.add(jpanelLC2, "2");
        
        //向左边JPanel添加组件
        JPanel jPanelL = new JPanel(new BorderLayout(10, 10));
        jPanelL.setPreferredSize(new Dimension(580, 500));//设置JPanel的大小
        jPanelL.setBorder(BorderFactory.createLineBorder(Color.red));
        jPanelL.add(jPanelLN, BorderLayout.NORTH);
        jPanelL.add(jpanelLC, BorderLayout.CENTER);
        
        JPanel jPanelR = new JPanel(new BorderLayout());
        jPanelR.setPreferredSize(new Dimension(200, 500));//设置JPanel的大小
        jPanelR.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        
        //常规设置的组件
        portLabel = new JLabel();
        portLabel.setText("串口号");
        portLabel.setName("portLabel"); // NOI18N

        rateLabel = new JLabel();
        rateLabel.setText("波特率");
        rateLabel.setName("rateLabel"); // NOI18N

        parityBitLabel = new JLabel();
        parityBitLabel.setText("校验位");
        parityBitLabel.setName("parityBitLabel"); // NOI18N
        
        dataBitLabel = new JLabel();
        dataBitLabel.setText("数据位");
        dataBitLabel.setName("dataBitLabel"); // NOI18N

        stopBitLabel = new JLabel();
        stopBitLabel.setText("停止位");
        stopBitLabel.setName("stopBitLabel"); // NOI18N
        
        portStateLabel = new JLabel("  串口状态");

        portComboBox = new JComboBox();
        portComboBox.setModel(new DefaultComboBoxModel(new String[] { "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "COM10", "COM11", "COM12", "COM13", "COM14", "COM15", "COM16", "COM17", "COM18", "COM19", "COM20", "COM21"}));
        portComboBox.setToolTipText("选择串口");
        portComboBox.setName("portComboBox"); // NOI18N
        //portComboBox.setEditable(true);

        rateComboBox = new JComboBox();
        rateComboBox.setModel(new DefaultComboBoxModel(new String[] { "4800", "9600", "14400", "19200", "38400", "56000", "57600", "115200" }));
        rateComboBox.setSelectedIndex(4);
        rateComboBox.setToolTipText("选择波特率");
        rateComboBox.setName("rateComboBox"); // NOI18N
        
        parityComboBox = new JComboBox();
        parityComboBox.setModel(new DefaultComboBoxModel(new String[] { "NONE", "ODD", "EVEN", "MARK", "SPACE" }));
        parityComboBox.setToolTipText("选择校验位");
        parityComboBox.setName("parityComboBox"); // NOI18N

        dataBitComboBox = new JComboBox();
        dataBitComboBox.setModel(new DefaultComboBoxModel(new String[] { "5", "6", "7", "8" }));
        dataBitComboBox.setSelectedIndex(3);
        dataBitComboBox.setToolTipText("选择数据位");
        dataBitComboBox.setName("dataBitComboBox"); // NOI18N

        stopBitComboBox = new JComboBox();
        stopBitComboBox.setModel(new DefaultComboBoxModel(new String[] { "1", "1.5", "2" }));
        stopBitComboBox.setToolTipText("选择停止位");
        stopBitComboBox.setName("stopBitComboBox"); // NOI18N

        openOrCloseToggleButton = new JToggleButton();
        openOrCloseToggleButton.setText("关闭串口");
        openOrCloseToggleButton.setToolTipText("关闭串口");
        openOrCloseToggleButton.setName("openOrCloseToggleButton"); // NOI18N
        openOrCloseToggleButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
            	spwl.openOrCloseToggleButtonMouseClicked(evt, sr, portComboBox, rateComboBox, dataBitComboBox, stopBitComboBox, parityComboBox, openOrCloseToggleButton, operationLabel, operResultLabel);
            }
        });
        spwl.setToggleBtnOpen(portComboBox, openOrCloseToggleButton);
        
        portConfigPanel = new JPanel();
        portConfigPanel.setBorder(BorderFactory.createTitledBorder("常规设置"));
        portConfigPanel.setToolTipText("");
        portConfigPanel.setName("serialPortsConfigPanel"); // NOI18N
        GridBagLayout gbl = new GridBagLayout();
        portConfigPanel.setLayout(gbl);  
        
        portConfigPanel.add(portLabel);
        portConfigPanel.add(portComboBox);
        portConfigPanel.add(rateLabel);
        portConfigPanel.add(rateComboBox);
        portConfigPanel.add(parityBitLabel);
        portConfigPanel.add(parityComboBox);
        portConfigPanel.add(dataBitLabel);
        portConfigPanel.add(dataBitComboBox);
        portConfigPanel.add(stopBitLabel);
        portConfigPanel.add(stopBitComboBox);
        portConfigPanel.add(portStateLabel);
        portConfigPanel.add(openOrCloseToggleButton);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; //设置组件具有填充模式为BOTH
        gbc.anchor = GridBagConstraints.CENTER;//设置当组件小于显示区域时，居中对齐
        gbc.insets = new java.awt.Insets(0, 0, 10, 0);//设置组件的位置
        
        gbc.gridwidth = 1;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
        gbc.weightx = 0;//该方法设置组件水平的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
        gbc.weighty = 0;//该方法设置组件垂直的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
        gbl.setConstraints(portLabel, gbc);//设置组件
        
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbl.setConstraints(portComboBox, gbc);
        
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbl.setConstraints(rateLabel, gbc);
        
        gbc.gridwidth = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbl.setConstraints(rateComboBox, gbc);
        
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbl.setConstraints(parityBitLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbl.setConstraints(parityComboBox, gbc);
        
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbl.setConstraints(dataBitLabel, gbc);
        
        gbc.gridwidth = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbl.setConstraints(dataBitComboBox, gbc);
        
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbl.setConstraints(stopBitLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbl.setConstraints(stopBitComboBox, gbc);
        
        gbc.gridwidth = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbl.setConstraints(portStateLabel, gbc);
        
        gbc.gridwidth = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbl.setConstraints(openOrCloseToggleButton, gbc);
        
        //接收数据设置
        checkHexCheckBoxR = new JCheckBox();
        checkHexCheckBoxR.setText("十六进制");
        checkHexCheckBoxR.setName("checkHexCheckBox"); // NOI18N 
        checkHexCheckBoxR.addItemListener(this);
        
        clearRecButton = new JButton();
        clearRecButton.setText("清空显示");
        clearRecButton.setToolTipText("清空数据");
        clearRecButton.setName("clearRecButton"); // NOI18N
        clearRecButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                spwl.clearRecButtonMouseClicked(evt, shuchuText);
            }
        });
        
        stopDisToggleButton = new JToggleButton();
        stopDisToggleButton.setText("停止显示");
        stopDisToggleButton.setToolTipText("停止数据");
        stopDisToggleButton.setName("stopDisToggleButton"); // NOI18N
        stopDisToggleButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
            	spwl.stopDisToggleMouseClicked(stopDisToggleButton);
            }
        });
        
        saveToggleButton = new JToggleButton();
        saveToggleButton.setText("保存数据");
        saveToggleButton.setToolTipText("保存数据");
        saveToggleButton.setName("saveToggleButton"); // NOI18N
        saveToggleButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
            	spwl.saveToggleMouseClicked(saveToggleButton);
            }
        });
        
        recConfigPanel = new JPanel();
        recConfigPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("接收数据设置"));
        recConfigPanel.setToolTipText("");
        recConfigPanel.setName("ReceiveConfigPanel"); // NOI18N
        
        GridBagLayout gbl1 = new GridBagLayout();
        recConfigPanel.setLayout(gbl1);  
        
        recConfigPanel.add(checkHexCheckBoxR);
        recConfigPanel.add(stopDisToggleButton);
        recConfigPanel.add(clearRecButton);
        recConfigPanel.add(saveToggleButton);
        
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.fill = GridBagConstraints.BOTH; //设置组件具有填充模式为BOTH
        gbc1.anchor = GridBagConstraints.CENTER;//设置当组件小于显示区域时，居中对齐
        gbc1.insets = new java.awt.Insets(0, 0, 10, 0);//设置组件的位置
        
        gbc1.gridwidth = 1;
        gbc1.gridheight = 1;
        gbc1.weightx = 0;
        gbc1.weighty = 0;
        gbl1.setConstraints(checkHexCheckBoxR, gbc1);

        gbc1.gridwidth = 0;
        gbc1.gridheight = 1;
        gbc1.weightx = 0;
        gbc1.weighty = 0;
        gbl1.setConstraints(stopDisToggleButton, gbc1);
        
        gbc1.gridwidth = 0;
        gbc1.gridheight = 1;
        gbc1.weightx = 0;
        gbc1.weighty = 0;
        gbl1.setConstraints(clearRecButton, gbc1);
        
        gbc1.gridwidth = 0;
        gbc1.gridheight = 0;
        gbc1.weightx = 0;
        gbc1.weighty = 0;
        gbl1.setConstraints(saveToggleButton, gbc1);
        
        //发送数据设置
        sendDataBtn = new JButton();
        sendDataBtn.setText("发送数据");
        sendDataBtn.setToolTipText("发送数据");
        sendDataBtn.setName("sendDataBtn"); // NOI18N
        sendDataBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                try {
                	spwl.sendDataBtnMouseClicked(evt, sr, inputTextArea, hexSignal);
        		} catch(NullPointerException e) {
        			JOptionPane.showMessageDialog(null, "串口未打开！", "串口警告",
    						JOptionPane.WARNING_MESSAGE);
        		}
            }
        });
        
        clearButton = new JButton();
        clearButton.setText("清空发送");
        clearButton.setToolTipText("清空发送");
        clearButton.setName("clearButton"); // NOI18N
        clearButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                spwl.clearButtonMouseClicked(evt, inputTextArea);
            }
        });

        checkHexCheckBox = new JCheckBox();
        checkHexCheckBox.setText("十六进制");
        checkHexCheckBox.setName("checkHexCheckBox"); // NOI18N 
        checkHexCheckBox.addItemListener(this);
        
        sendConfigPanel = new JPanel();
        sendConfigPanel.setBorder(BorderFactory.createTitledBorder("发送数据设置"));
        sendConfigPanel.setToolTipText("");
        sendConfigPanel.setName("sendConfigPanel"); // NOI18N
        
        GridBagLayout gbl2 = new GridBagLayout();
        sendConfigPanel.setLayout(gbl2);  
        
        sendConfigPanel.add(checkHexCheckBox);
        sendConfigPanel.add(sendDataBtn);
        sendConfigPanel.add(clearButton);
        
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.fill = GridBagConstraints.BOTH; //设置组件具有填充模式为BOTH
        gbc2.anchor = GridBagConstraints.CENTER;//设置当组件小于显示区域时，居中对齐
        gbc2.insets = new java.awt.Insets(0, 0, 10, 0);//设置组件的位置
        
        gbc2.gridwidth = 1;
        gbc2.gridheight = 0;
        gbc2.weightx = 0;
        gbc2.weighty = 0;
        gbl2.setConstraints(checkHexCheckBox, gbc2);
        
        gbc2.gridwidth = 1;
        gbc2.gridheight = 1;
        gbc2.weightx = 0;
        gbc2.weighty = 0;
        gbl2.setConstraints(sendDataBtn, gbc2);

        gbc2.gridwidth = 0;
        gbc2.gridheight = 0;
        gbc2.weightx = 0;
        gbc2.weighty = 0;
        gbl2.setConstraints(clearButton, gbc2);

        //第一张卡片
        jPanel1 = new JPanel();
        jPanel1.setLayout(new BorderLayout(5, 10));
        jPanel1.add(portConfigPanel, BorderLayout.NORTH);
        jPanel1.add(recConfigPanel, BorderLayout.CENTER);
        jPanel1.add(sendConfigPanel, BorderLayout.SOUTH);
        
        jPanelL.setLayout(new BorderLayout());
        
        //第二张卡片
        //创建三个面板，放入第二个卡片的三个格子内
        //第二张卡片的第一个Jpanel
        directLabel = new JLabel("方位角  ");
        jTextFieldDirect = new JTextField(10);
        jTextFieldDirect.setHorizontalAlignment(JTextField.LEFT);
        //监听键盘，只能输入数字和小数点
        jTextFieldDirect.addKeyListener(new KeyAdapter(){
        	public void keyTyped(KeyEvent e) {  
                int keyChar = e.getKeyChar();                 
                if((keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9) || keyChar == KeyEvent.VK_PERIOD){  
                      
                }else{  
                    e.consume(); //关键，屏蔽掉非法输入  
                }  
            }  
        });  
        
        pitchJLabel = new JLabel("俯仰角  ");
        jTextFieldPitch = new JTextField(10);
        jTextFieldPitch.setHorizontalAlignment(JTextField.LEFT);
        jTextFieldPitch.addKeyListener(new KeyAdapter(){
        	public void keyTyped(KeyEvent e) {  
                int keyChar = e.getKeyChar();                 
                if((keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9) || keyChar == KeyEvent.VK_PERIOD ){  
                      
                }else{  
                    e.consume(); //关键，屏蔽掉非法输入  
                }  
            }  
        });  
        sendAngButton = new JButton();
        sendAngButton.setText("发送");
        sendAngButton.setToolTipText("发送");
        sendAngButton.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent evt) {
        		try{
        			direct = Double.parseDouble(jTextFieldDirect.getText());
            		pitch = Double.parseDouble(jTextFieldPitch.getText());
            		
            		//pattern = Pattern.compile("^-?\\d*(\\.)?\\d*$");
            		// matcher = pattern.matcher(jTextFieldDirect.getText());
            		if(jTextFieldDirect.getText().matches("^-?\\d*(\\.)?\\d*$") && jTextFieldPitch.getText().matches("^-?\\d*(\\.)?\\d*$")) {
            			if(direct<360 && direct>=0 && pitch<360 && pitch>=0) {
            				spwl.sendAngBtnMouseClicked(sr, direct, pitch);
            			}
            			else {
            				JOptionPane.showMessageDialog(null, "请输入 0~360 之间的角度数据", "数据格式警告",
            						JOptionPane.WARNING_MESSAGE);
            			}
            		}
        		} catch(NumberFormatException e) {
        			JOptionPane.showMessageDialog(null, "数据不能为空", "数据格式警告",
    						JOptionPane.WARNING_MESSAGE);
        		}
        	}
		});
        
        jPanel2 = new JPanel(new GridLayout(3, 1));//第二张卡片三行一列
        
        //创建三个面板，放入第二个卡片的三个格子内
        //第二张卡片的第一个Jpanel
        recDirJLabel = new JLabel("实时方位角: ");
        recDirDisLabel = new JLabel();
        recDirDisLabel.setText("000.00");
        recDirDisLabel.setHorizontalAlignment(JTextField.LEFT);
        recPitLabel = new JLabel("实时俯仰角: ");
        recPitDisLabel = new JLabel();
        recPitDisLabel.setText("000.00");
        
        recPitDisLabel.setHorizontalAlignment(JTextField.LEFT);
        spwl.setDirPit(recDirDisLabel, recPitDisLabel);    
        
        jPanel22 = new JPanel();
        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder("实时角度接收"));
        jPanel22.setToolTipText("");
        jPanel22.setName("jPanel22"); // NOI18N
        GridBagLayout gbl22 = new GridBagLayout();
        jPanel22.setLayout(gbl22);  
        
        jPanel22.add(recDirJLabel);
        jPanel22.add(recDirDisLabel);
        jPanel22.add(recPitLabel);
        jPanel22.add(recPitDisLabel);
        
        GridBagConstraints gbc22 = new GridBagConstraints();
        gbc22.fill = GridBagConstraints.BOTH; //设置组件具有填充模式为BOTH
        gbc22.anchor = GridBagConstraints.CENTER;//设置当组件小于显示区域时，居中对齐
        gbc22.insets = new java.awt.Insets(0, 0, 5, 0);//设置组件的位置
        
        gbc22.gridwidth = 1;
        gbc22.gridheight = 1;
        gbc22.weightx = 0;
        gbc22.weighty = 0;
        gbl22.setConstraints(recDirJLabel, gbc22);
        
        gbc22.gridwidth = 0;
        gbc22.gridheight = 1;
        gbc22.weightx = 0;
        gbc22.weighty = 0;
        gbl22.setConstraints(recDirDisLabel, gbc22);
        
        gbc22.gridwidth = 1;
        gbc22.gridheight = 0;
        gbc22.weightx = 0;
        gbc22.weighty = 0;
        gbl22.setConstraints(recPitLabel , gbc22);
        
        gbc22.gridwidth = 0;
        gbc22.gridheight = 0;
        gbc22.weightx = 0;
        gbc22.weighty = 0;
        gbl22.setConstraints(recPitDisLabel, gbc22);
        
        jPanel2.add(jPanel22);
        
        //创建三个面板，放入第二个卡片的三个格子内
        //第二张卡片的第一个Jpanel
        jPanel21 = new JPanel();
        jPanel21.setBorder(BorderFactory.createTitledBorder("发送角度"));
        jPanel21.setToolTipText("");
        jPanel21.setName("jPanel21"); // NOI18N
        GridBagLayout gbl21 = new GridBagLayout();
        jPanel21.setLayout(gbl21);  
        
        jPanel21.add(directLabel);
        jPanel21.add(jTextFieldDirect);
        jPanel21.add(pitchJLabel);
        jPanel21.add(jTextFieldPitch);
        jPanel21.add(sendAngButton);
        
        GridBagConstraints gbc21 = new GridBagConstraints();
        gbc21.fill = GridBagConstraints.BOTH; //设置组件具有填充模式为BOTH
        gbc21.anchor = GridBagConstraints.CENTER;//设置当组件小于显示区域时，居中对齐
        gbc21.insets = new java.awt.Insets(0, 0, 10, 0);//设置组件的位置
        
        gbc21.gridwidth = 1;
        gbc21.gridheight = 1;
        gbc21.weightx = 0;
        gbc21.weighty = 0;
        gbl21.setConstraints(directLabel, gbc21);
        
        gbc21.gridwidth = 0;
        gbc21.gridheight = 1;
        gbc21.weightx = 0;
        gbc21.weighty = 0;
        gbl21.setConstraints(jTextFieldDirect, gbc21);

        gbc21.gridwidth = 1;
        gbc21.gridheight = 1;
        gbc21.weightx = 0;
        gbc21.weighty = 0;
        gbl21.setConstraints(pitchJLabel, gbc21);
        
        gbc21.gridwidth = 0;
        gbc21.gridheight = 1;
        gbc21.weightx = 0;
        gbc21.weighty = 0;
        gbl21.setConstraints(jTextFieldPitch, gbc21);
        
        gbc21.gridwidth = 0;
        gbc21.gridheight = 0;
        gbc21.weightx = 0;
        gbc21.weighty = 0;
        gbl21.setConstraints(sendAngButton, gbc21);
        
        jPanel2.add(jPanel21);
        
        //创建三个面板，放入第二个卡片的三个格子内
        //第二张卡片的第三个Jpanel ,放按键在里面
        jPanel23 = new JPanel();
        jPanel23.setBorder(BorderFactory.createTitledBorder("卫星按键"));
        jPanel23.setToolTipText("");
        jPanel23.setName("jPanel23"); // NOI18N
        GridLayout gl = new GridLayout(4, 2);
        jPanel23.setLayout(gl);  
        
    	jbZXWH = new JButton("中星五号");// 118.56  58.33
        jbZXWH.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent evt) {
        		try {
        			spwl.sendAngButton(sr, "118.56 058.33e");
        		} catch(NullPointerException e) {
        			JOptionPane.showMessageDialog(null, "串口未打开！", "串口警告",
    						JOptionPane.WARNING_MESSAGE);
        		}
        	}
        });
		jbSAT2 = new JButton("SAT-2");  // 199.31  57.16
		jbSAT2.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent evt) {
        		try {
        			spwl.sendAngButton(sr, "199.31 057.16e");
        		} catch(NullPointerException e) {
        			JOptionPane.showMessageDialog(null, "串口未打开！", "串口警告",
    						JOptionPane.WARNING_MESSAGE);
        		}
        	}
        });
		jbYZYH = new JButton("亚洲一号");// 209.06  55.10
		jbYZYH.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent evt) {
        		try {
        			spwl.sendAngButton(sr, "209.06 055.10e");
        		} catch(NullPointerException e) {
        			JOptionPane.showMessageDialog(null, "串口未打开！", "串口警告",
    						JOptionPane.WARNING_MESSAGE);
        		}
        	}
        });
		jbYZEH = new JButton("亚洲二号");// 248.32  34.26
		jbYZEH.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent evt) {
        		try {
        			spwl.sendAngButton(sr, "248.32 34.26e");
        		} catch(NullPointerException e) {
        			JOptionPane.showMessageDialog(null, "串口未打开！", "串口警告",
    						JOptionPane.WARNING_MESSAGE);
        		}
        	}
        });
		jbMXEH = new JButton("马星二号");// 133.47   41.28
		jbMXEH.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent evt) {
        		try {
        			spwl.sendAngButton(sr, "248.32 34.26e");
        		} catch(NullPointerException e) {
        			JOptionPane.showMessageDialog(null, "串口未打开！", "串口警告",
    						JOptionPane.WARNING_MESSAGE);
        		}
        	}
        });
		
		jPanel23.add(jbZXWH);
		jPanel23.add(jbSAT2);
		jPanel23.add(jbYZYH);
		jPanel23.add(jbYZEH);
		jPanel23.add(jbMXEH);
		
		jPanel2.add(jPanel23);
		
        //向右下的面板添加两个面板
        jtbPane1 = new JTabbedPane(JTabbedPane.TOP);
        jtbPane1.setPreferredSize(new Dimension(200, 456));
		jtbPane1.setFont(new Font("宋体", 0, 12));
		
		jtbPane1.add("转向设置", jPanel2);
		jtbPane1.add("常用设置", jPanel1);
        
        jPanelR.add(jtbPane1, BorderLayout.CENTER);
        
        authorLabel = new JLabel();
        authorLabel.setText("技术支持:HHU        ");
        authorLabel.setName("authorLabel"); // NOI18N 
        
        jPanelB = new JPanel(new BorderLayout());
        jPanelB.add(authorLabel, BorderLayout.EAST);
        
        this.setSize(700, 500);
        this.add(jPanelL);
        this.add(jPanelR);
        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setHgap(10);
        
        this.setLayout(borderLayout);
        //this.add(inputTextScrollPane);
        this.add(jPanelL, BorderLayout.CENTER);
        this.add(jPanelR, BorderLayout.EAST);
        this.add(jPanelB, BorderLayout.SOUTH);
	}
	
	public Window1() {
		
		//设置窗体外观为windows
    	try{
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            
        }catch(Exception e){
            Log.debug("设置窗体windows外观失败");
        	System.out.println("设置窗体windows外观失败");
        }
		
		initNormalSet();
		this.setVisible(true);
		this.setLayout(null);
        this.setLocationRelativeTo(null);
        chatServer.start();
        addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == dataRecButton) {
			clayoutL.show(jpanelLC, "1");
		}
		if(e.getSource() == figureButton) {
			clayoutL.show(jpanelLC, "2");
		}
		if(e.getSource() == chatButton) {
			clayoutL.show(jpanelLC, "3");
		}
		if(e.getSource() == jChatb) {
			String info = jChattf.getText();
			try {
				chatServer.clients.get(0).getDos().writeUTF(info);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("写出错误");
				
			}
			catch (IndexOutOfBoundsException e1) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				JOptionPane.showMessageDialog(null, "无用户连接！！", "网络警告", JOptionPane.WARNING_MESSAGE);
				System.out.println("无用户连接！！");
				
			}
			jChatta.append(info + "\r\n");
			jChattf.setText("");
		}
		if(e.getSource() == jChatClear) {
			jChatta.setText("");
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		JCheckBox box = (JCheckBox)e.getItemSelectable();
		//16进制发送选择
		if(box.equals(checkHexCheckBox)) {
			if( box.isSelected()) {
				hexSignal = true;
			}
			else {
				hexSignal = false;
			}
		}
		
		//16进制接收选择
		if(box.equals(checkHexCheckBoxR)) {
			if(box.isSelected()) {
				hexSignalR = true;
				spwl.getHexSignalR(hexSignalR);
			}
			else {
				hexSignalR = false;
				spwl.getHexSignalR(hexSignalR);
			}
		}
	}
}


