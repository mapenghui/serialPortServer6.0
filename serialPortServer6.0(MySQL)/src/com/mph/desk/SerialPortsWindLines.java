/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SerialPortsWindLines.java
 *
 * Created on 2010-7-29, 19:13:00
 */

package com.mph.desk;

import gnu.io.SerialPort;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.Buffer;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;




import com.mph.view.MemoryUsageDemo;
import com.mph.view.Window1;

/**
 * http://hitangjun.com
 * 
 * @author John
 */
public class SerialPortsWindLines extends javax.swing.JFrame implements
		Observer {

	// DynamicChart dynamicChart = new DynamicChart();
	// SerialReader sr =new SerialReader();

	boolean hexsig, saveSig, stopDisSig = true;
	String dirString, pitString, findDirPit = null;
	FileWriter fw;
	BufferedWriter bw;
	JLabel recDirDisLabel, recPitDisLabel;
	int flag; //探测到D: 或 P:  ，则更新MemoryUsageDemo的横轴

	/** Creates new form SerialPortsWindLines */
	public SerialPortsWindLines() {
		// 设置窗体外观为windows
		/*
		 * try{ javax.swing.UIManager.setLookAndFeel(
		 * "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		 * 
		 * }catch(Exception e){ Log.debug("设置窗体windows外观失败"); }
		 */
		// initComponents();
		// 设置Swing JFrame初始位置为屏幕中央
		/*
		 * this.setLayout(null); this.setLocationRelativeTo(null);
		 */
		// 打开串口
		// openSerialPort();
	}

	// 转换预设的奇偶校验位的值
	private int getParityValue(String parity) {
		if ("NONE".equals(parity)) {
			return SerialPort.PARITY_NONE;
		} else if ("ODD".equals(parity)) {
			return SerialPort.PARITY_ODD;
		} else if ("EVEN".equals(parity)) {
			return SerialPort.PARITY_EVEN;
		} else if ("MARK".equals(parity)) {
			return SerialPort.PARITY_MARK;
		} else if ("SPACE".equals(parity)) {
			return SerialPort.PARITY_SPACE;
		} else {
			return SerialPort.PARITY_NONE;
		}
	}

	// 读取参数值,打开串口
	public void openSerialPort(SerialReader sr, JComboBox portComboBox,
			JComboBox rateComboBox, JComboBox dataBitComboBox,
			JComboBox stopBitComboBox, JComboBox parityComboBox,
			JToggleButton openOrCloseToggleButton, JLabel operResultLabel) {

		HashMap<String, Comparable> params = new HashMap<String, Comparable>();

		String port = portComboBox.getSelectedItem().toString();
		String rate = rateComboBox.getSelectedItem().toString();
		String dataBit = dataBitComboBox.getSelectedItem().toString();
		String stopBit = stopBitComboBox.getSelectedItem().toString();
		String parity = parityComboBox.getSelectedItem().toString();

		int parityInt = getParityValue(parity);
		String paramsMsg = "打开串口 " + port + "," + rate + "," + parity + ","
				+ dataBit + "," + stopBit;

		params.put(SerialReader.PARAMS_PORT, port); // 端口名称
		params.put(SerialReader.PARAMS_RATE, rate); // 波特率
		params.put(SerialReader.PARAMS_DATABITS, dataBit); // 数据位
		params.put(SerialReader.PARAMS_STOPBITS, stopBit); // 停止位
		params.put(SerialReader.PARAMS_PARITY, parityInt); // 无奇偶校验
		params.put(SerialReader.PARAMS_TIMEOUT, 1000); // 设备超时时间 1秒
		params.put(SerialReader.PARAMS_DELAY, 200); // 端口数据准备时间 1秒
		try {
			sr.open(params);
			sr.addObserver(this);
			successOpenPort(paramsMsg, operResultLabel, portComboBox,
					openOrCloseToggleButton);
		} catch (SerialPortException e) {
			failOpenPort(e.getMessage(), portComboBox, openOrCloseToggleButton,
					operResultLabel);
		}
	}

	// 设置打开端口失败的信息
	void successOpenPort(String text, JLabel operResultLabel,
			JComboBox portComboBox, JToggleButton openOrCloseToggleButton) {
		Log.debug(text);
		setOperationText("就绪", operResultLabel);
		setResultText(text, operResultLabel);
		// 设置打开或关闭串口按钮的状态为关闭
		setToggleBtnClose(portComboBox, openOrCloseToggleButton);
	}

	// 设置打开端口失败的信息
	void failOpenPort(String text, JComboBox portComboBox,
			JToggleButton openOrCloseToggleButton, JLabel operResultLabel) {
		JOptionPane.showMessageDialog(null, text, "",
				JOptionPane.WARNING_MESSAGE);

		Log.debug(text);
		setResultText(text, operResultLabel);

		// 设置打开或关闭串口按钮的状态为打开
		setToggleBtnOpen(portComboBox, openOrCloseToggleButton);
	}

	// 处理发送数据的逻辑
	public void sendDataBtnMouseClicked(java.awt.event.MouseEvent evt,
			SerialReader sr, JTextArea inputTextArea, boolean hexSignal) {
		// updateChart(inputTextArea.getText());
		String message = inputTextArea.getText();
		if (hexSignal == true) {
			message = message.replaceAll("\\s*", "");
			byte[] byteArr = HexString2Bytes(message);
			sr.start();
			for (int i = 0; i < byteArr.length; i++) {
				sr.run(byteArr[i]);
			}
			inputTextArea.setText("");  //清空发送区

		} else {
			sr.start();
			sr.run(message);
			inputTextArea.setText(""); //清空发送区
		}
	}
	

	// 发送direct、和pitch 角度
	public void sendAngBtnMouseClicked(SerialReader sr, double direct,
			double pitch) {
		DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
		df.applyPattern("000.00");
		// String directS=String.format("%03d", direct);
		// String pitchS=String.format("%03d", pitch);
		String directS = df.format(direct);
		String pitchS = df.format(pitch);
		String DirectPitch = " " + directS + "  " + pitchS + "e";
		sr.start();
		sr.run(DirectPitch);
	}
	
	// 具体按键 发送direct、和pitch 角度
	public void sendAngButton(SerialReader sr, String s) {
		sr.start();
		sr.run(s);
	}

	public void setToggleBtnOpen(JComboBox portComboBox,
			JToggleButton openOrCloseToggleButton) {
		portComboBox.setEnabled(true);
		openOrCloseToggleButton.setText("打开串口");
		openOrCloseToggleButton.setToolTipText("打开串口");
	}

	private void setToggleBtnClose(JComboBox portComboBox,
			JToggleButton openOrCloseToggleButton) {
		portComboBox.setEnabled(false);
		openOrCloseToggleButton.setText("关闭串口");
		openOrCloseToggleButton.setToolTipText("关闭串口");
	}

	public void openOrCloseToggleButtonMouseClicked(
			java.awt.event.MouseEvent evt, SerialReader sr,
			JComboBox portComboBox, JComboBox rateComboBox,
			JComboBox dataBitComboBox, JComboBox stopBitComboBox,
			JComboBox parityComboBox, JToggleButton openOrCloseToggleButton,
			JLabel operationLabel, JLabel operResultLabel) {
		// 如果没有打开则打开端口，否则关闭端口
		if (!sr.isOpen()) {
			// setToggleBtnOpen();
			openSerialPort(sr, portComboBox, rateComboBox, dataBitComboBox,
					stopBitComboBox, parityComboBox, openOrCloseToggleButton,
					operResultLabel);
		} else {
			try {
				sr.close();
				setToggleBtnOpen(portComboBox, openOrCloseToggleButton);
			} catch (SerialPortException e) {
				setOperationText(e.getMessage(), operationLabel);
			}
		}
	}

	// 停止显示
	public void stopDisToggleMouseClicked(JToggleButton stopDisToggleButton) {
		// TODO Auto-generated method stub
		if (stopDisSig == false) {
			stopDisSig = true;
			stopDisToggleButton.setText("停止显示");
			stopDisToggleButton.setToolTipText("停止显示");
		} else {
			stopDisSig = false;
			stopDisToggleButton.setText("继续显示");
			stopDisToggleButton.setToolTipText("继续显示");
		}
	}

	// 保存数据
	public void saveToggleMouseClicked(JToggleButton saveToggleButton) {
		if (saveSig == false) {
			saveSig = true;
			saveToggleButton.setText("停止保存");
			saveToggleButton.setToolTipText("停止保存");
		} else {
			saveSig = false;
			saveToggleButton.setText("保存数据");
			saveToggleButton.setToolTipText("保存数据");
		}
	}

	// 保存数据函数
	private void saveData(String v) {
		try {
			fw = new FileWriter("D:\\串口数据.txt", true);
			fw.write(v);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("数据写入文件失败！！");
		}
	}


	// //探测数据点，并标记出来
	// private void dectiveButtonMouseClicked(java.awt.event.MouseEvent evt) {
	// try {
	// int index = Integer.valueOf(detectiveSpinner.getValue().toString());
	//
	// //获取索引点的数据值
	// XYDataItem item = dynamicChart.getXYSeries().getDataItem(index);
	// double clickX = Double.valueOf(item.getX().toString());
	// double clickY = Double.valueOf(item.getY().toString());
	//
	// final CircleDrawer cd = new CircleDrawer(Color.red, new
	// BasicStroke(5.0f), new Color(231 ,242 ,255 ));// 设置圈圆圈的半径
	// bestBid = new XYDrawableAnnotation(clickX, clickY, 0.1, 0.1, cd);
	// dynamicChart.getXYPlot().addAnnotation(bestBid);
	//
	// double x = clickX+15;
	// double y = clickY-15;
	//
	// if(y<5){
	// y=clickY+15;
	// }
	//
	// // 文本显示标题
	// textpointer = new XYTextAnnotation(clickX+","+clickY,clickX+15 ,
	// clickY-15);
	// textpointer.setBackgroundPaint(Color.YELLOW);
	// textpointer.setPaint(Color.CYAN);
	// dynamicChart.getXYPlot().addAnnotation(textpointer);
	//
	// } catch (Exception e) {
	// JOptionPane.showMessageDialog(null, "请输入合理的值", "",
	// JOptionPane.WARNING_MESSAGE);
	// setResultText("请输入合理的值");
	// }
	// }

	// 清空输入框的数据
	public void clearButtonMouseClicked(java.awt.event.MouseEvent evt,
			JTextArea inputTextArea) {
		inputTextArea.setText("");
	}

	// 清空输出框的数据
	public void clearRecButtonMouseClicked(java.awt.event.MouseEvent evt,
			JTextArea shuchuText) {
		shuchuText.setText("");
	}

	int lastIndex = 0;

	// 接收window1发送来的hexSignalR
	public void getHexSignalR(boolean hexSignalR) {
		hexsig = hexSignalR;
	}

	// 根据收到的数据进行更新图表
	@Override
	public void update(Observable o, Object arg) {
		Log.debug("读取到的数据位数：" + ((byte[]) arg).length);
		String v = new String((byte[]) arg);
		findDirPit = v;
		setAng();
		// updateChart(v);
		Log.debug(v);
		if (stopDisSig == true) {
			if (hexsig == true) {
				v = WriteToHex((byte[]) arg);
				Window1.shuchuText.append(v);
				Window1.shuchuText.replaceRange(v, 0, 3000);
			} else {
				// 如果保存键按下则保存数据
				if (saveSig == true) {
					saveData(v + "\r\n");
					try {
						fw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						// 关闭保存键时，关闭资源
						e.printStackTrace();
						System.out.println("关不资源失败！！");
					}
				}
			}

			Window1.shuchuText.append(v);
			Window1.shuchuText.replaceRange(v, 0, 3000);
		} else {
			return;
		}
	}

	/**
	 * 将两个ASCII字符合成一个字节； 如："EF"--> 0xEF
	 * 
	 * @param src0
	 *            byte
	 * @param src1
	 *            byte
	 * @return byte
	 */
	public byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
				.byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
				.byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}

	/**
	 * 将指定字符串src，以每两个字符分割转换为16进制形式 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF,
	 * 0xD9}
	 * 
	 * @param src
	 *            String
	 * @return byte[]
	 */
	public byte[] HexString2Bytes(String src) {
		byte[] ret = new byte[(int) ((src.length() + 1) / 2)];
		byte[] tmp = src.getBytes();
		for (int i = 0; i < ret.length; i++) {
			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
		}
		return ret;
	}

	public String WriteToHex(byte[] b) {
		String hexA = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexA = hexA + hex.toUpperCase() + " ";
		}
		return hexA;
	}

	// 将16进制字符串转换成int
	public int[] hexStrToInt(String hexStr, JLabel operResultLabel) {
		clearResultText(operResultLabel);
		String[] hexStrArray = hexStr.split(" ");
		int[] result;
		try {
			result = new int[hexStrArray.length];
			for (int i = 0; i < hexStrArray.length; i++) {
				result[i] = Integer.parseInt(hexStrArray[i].substring(2), 16);
			}
			return result;
		} catch (NumberFormatException e) {
			setResultText("输入的数据不正确", operResultLabel);
		}
		return null;
	}

	/**
	 * 清空操作提示文本
	 *
	 */
	private void clearOperationText(JLabel operationLabel) {
		operationLabel.setText("");
	}

	/**
	 * 设置操作提示文本
	 *
	 */
	private void setOperationText(String text, JLabel operationLabel) {
		operationLabel.setText(text);
	}

	/**
	 * 清空操作结果提示文本
	 *
	 */
	private void clearResultText(JLabel operResultLabel) {
		operResultLabel.setText("");
	}

	/**
	 * 设置操作结果提示文本
	 * 
	 * @param text
	 */
	private void setResultText(String text, JLabel operResultLabel) {
		operResultLabel.setText(text);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents(JPanel sendDataPanel) {

		// ChartPanel chartPanel = dynamicChart.createChartpanel();
		// dc.startThread();

		sendDataPanel.setName("sendDataPanel"); // NOI18N

		pack();
	}// </editor-fold>

	public void setDirPit(JLabel recDirDisLabel, JLabel recPitDisLabel) {
		// TODO Auto-generated method stub
		this.recDirDisLabel = recDirDisLabel;
		this.recPitDisLabel = recPitDisLabel;
	}

	public void setAng() {    //从串口接收区找到角度关键字（D:xxx.xx P:xxx.xx），将找到的角度数据设置到实时角度接收中
		// TODO Auto-generated method stub
		int g1, g2;
			if ((g1 = findDirPit.indexOf("D:")) != -1) {   
				dirString = findDirPit.substring(g1 + 2, g1 + 8);
				recDirDisLabel.setText(dirString);
				MemoryUsageDemo.direct = Double.parseDouble(dirString);
				//MemoryUsageDemo.i++;
				flag = 1;
			}
			if ((g2 = findDirPit.indexOf("P:")) != -1) {
				dirString = findDirPit.substring(g2 + 2, g2 + 8);
				recPitDisLabel.setText(dirString);
				MemoryUsageDemo.pitch = Double.parseDouble(dirString);
				flag = 1;
			}
			if(flag == 1) {
				MemoryUsageDemo.i++;
				flag = 0;
			}
			else
				return;
		}
}
