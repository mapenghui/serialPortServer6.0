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
	int flag; //̽�⵽D: �� P:  �������MemoryUsageDemo�ĺ���

	/** Creates new form SerialPortsWindLines */
	public SerialPortsWindLines() {
		// ���ô������Ϊwindows
		/*
		 * try{ javax.swing.UIManager.setLookAndFeel(
		 * "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		 * 
		 * }catch(Exception e){ Log.debug("���ô���windows���ʧ��"); }
		 */
		// initComponents();
		// ����Swing JFrame��ʼλ��Ϊ��Ļ����
		/*
		 * this.setLayout(null); this.setLocationRelativeTo(null);
		 */
		// �򿪴���
		// openSerialPort();
	}

	// ת��Ԥ�����żУ��λ��ֵ
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

	// ��ȡ����ֵ,�򿪴���
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
		String paramsMsg = "�򿪴��� " + port + "," + rate + "," + parity + ","
				+ dataBit + "," + stopBit;

		params.put(SerialReader.PARAMS_PORT, port); // �˿�����
		params.put(SerialReader.PARAMS_RATE, rate); // ������
		params.put(SerialReader.PARAMS_DATABITS, dataBit); // ����λ
		params.put(SerialReader.PARAMS_STOPBITS, stopBit); // ֹͣλ
		params.put(SerialReader.PARAMS_PARITY, parityInt); // ����żУ��
		params.put(SerialReader.PARAMS_TIMEOUT, 1000); // �豸��ʱʱ�� 1��
		params.put(SerialReader.PARAMS_DELAY, 200); // �˿�����׼��ʱ�� 1��
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

	// ���ô򿪶˿�ʧ�ܵ���Ϣ
	void successOpenPort(String text, JLabel operResultLabel,
			JComboBox portComboBox, JToggleButton openOrCloseToggleButton) {
		Log.debug(text);
		setOperationText("����", operResultLabel);
		setResultText(text, operResultLabel);
		// ���ô򿪻�رմ��ڰ�ť��״̬Ϊ�ر�
		setToggleBtnClose(portComboBox, openOrCloseToggleButton);
	}

	// ���ô򿪶˿�ʧ�ܵ���Ϣ
	void failOpenPort(String text, JComboBox portComboBox,
			JToggleButton openOrCloseToggleButton, JLabel operResultLabel) {
		JOptionPane.showMessageDialog(null, text, "",
				JOptionPane.WARNING_MESSAGE);

		Log.debug(text);
		setResultText(text, operResultLabel);

		// ���ô򿪻�رմ��ڰ�ť��״̬Ϊ��
		setToggleBtnOpen(portComboBox, openOrCloseToggleButton);
	}

	// ���������ݵ��߼�
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
			inputTextArea.setText("");  //��շ�����

		} else {
			sr.start();
			sr.run(message);
			inputTextArea.setText(""); //��շ�����
		}
	}
	

	// ����direct����pitch �Ƕ�
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
	
	// ���尴�� ����direct����pitch �Ƕ�
	public void sendAngButton(SerialReader sr, String s) {
		sr.start();
		sr.run(s);
	}

	public void setToggleBtnOpen(JComboBox portComboBox,
			JToggleButton openOrCloseToggleButton) {
		portComboBox.setEnabled(true);
		openOrCloseToggleButton.setText("�򿪴���");
		openOrCloseToggleButton.setToolTipText("�򿪴���");
	}

	private void setToggleBtnClose(JComboBox portComboBox,
			JToggleButton openOrCloseToggleButton) {
		portComboBox.setEnabled(false);
		openOrCloseToggleButton.setText("�رմ���");
		openOrCloseToggleButton.setToolTipText("�رմ���");
	}

	public void openOrCloseToggleButtonMouseClicked(
			java.awt.event.MouseEvent evt, SerialReader sr,
			JComboBox portComboBox, JComboBox rateComboBox,
			JComboBox dataBitComboBox, JComboBox stopBitComboBox,
			JComboBox parityComboBox, JToggleButton openOrCloseToggleButton,
			JLabel operationLabel, JLabel operResultLabel) {
		// ���û�д���򿪶˿ڣ�����رն˿�
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

	// ֹͣ��ʾ
	public void stopDisToggleMouseClicked(JToggleButton stopDisToggleButton) {
		// TODO Auto-generated method stub
		if (stopDisSig == false) {
			stopDisSig = true;
			stopDisToggleButton.setText("ֹͣ��ʾ");
			stopDisToggleButton.setToolTipText("ֹͣ��ʾ");
		} else {
			stopDisSig = false;
			stopDisToggleButton.setText("������ʾ");
			stopDisToggleButton.setToolTipText("������ʾ");
		}
	}

	// ��������
	public void saveToggleMouseClicked(JToggleButton saveToggleButton) {
		if (saveSig == false) {
			saveSig = true;
			saveToggleButton.setText("ֹͣ����");
			saveToggleButton.setToolTipText("ֹͣ����");
		} else {
			saveSig = false;
			saveToggleButton.setText("��������");
			saveToggleButton.setToolTipText("��������");
		}
	}

	// �������ݺ���
	private void saveData(String v) {
		try {
			fw = new FileWriter("D:\\��������.txt", true);
			fw.write(v);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("����д���ļ�ʧ�ܣ���");
		}
	}


	// //̽�����ݵ㣬����ǳ���
	// private void dectiveButtonMouseClicked(java.awt.event.MouseEvent evt) {
	// try {
	// int index = Integer.valueOf(detectiveSpinner.getValue().toString());
	//
	// //��ȡ�����������ֵ
	// XYDataItem item = dynamicChart.getXYSeries().getDataItem(index);
	// double clickX = Double.valueOf(item.getX().toString());
	// double clickY = Double.valueOf(item.getY().toString());
	//
	// final CircleDrawer cd = new CircleDrawer(Color.red, new
	// BasicStroke(5.0f), new Color(231 ,242 ,255 ));// ����ȦԲȦ�İ뾶
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
	// // �ı���ʾ����
	// textpointer = new XYTextAnnotation(clickX+","+clickY,clickX+15 ,
	// clickY-15);
	// textpointer.setBackgroundPaint(Color.YELLOW);
	// textpointer.setPaint(Color.CYAN);
	// dynamicChart.getXYPlot().addAnnotation(textpointer);
	//
	// } catch (Exception e) {
	// JOptionPane.showMessageDialog(null, "����������ֵ", "",
	// JOptionPane.WARNING_MESSAGE);
	// setResultText("����������ֵ");
	// }
	// }

	// �������������
	public void clearButtonMouseClicked(java.awt.event.MouseEvent evt,
			JTextArea inputTextArea) {
		inputTextArea.setText("");
	}

	// �������������
	public void clearRecButtonMouseClicked(java.awt.event.MouseEvent evt,
			JTextArea shuchuText) {
		shuchuText.setText("");
	}

	int lastIndex = 0;

	// ����window1��������hexSignalR
	public void getHexSignalR(boolean hexSignalR) {
		hexsig = hexSignalR;
	}

	// �����յ������ݽ��и���ͼ��
	@Override
	public void update(Observable o, Object arg) {
		Log.debug("��ȡ��������λ����" + ((byte[]) arg).length);
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
				// �������������򱣴�����
				if (saveSig == true) {
					saveData(v + "\r\n");
					try {
						fw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						// �رձ����ʱ���ر���Դ
						e.printStackTrace();
						System.out.println("�ز���Դʧ�ܣ���");
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
	 * ������ASCII�ַ��ϳ�һ���ֽڣ� �磺"EF"--> 0xEF
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
	 * ��ָ���ַ���src����ÿ�����ַ��ָ�ת��Ϊ16������ʽ �磺"2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF,
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

	// ��16�����ַ���ת����int
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
			setResultText("��������ݲ���ȷ", operResultLabel);
		}
		return null;
	}

	/**
	 * ��ղ�����ʾ�ı�
	 *
	 */
	private void clearOperationText(JLabel operationLabel) {
		operationLabel.setText("");
	}

	/**
	 * ���ò�����ʾ�ı�
	 *
	 */
	private void setOperationText(String text, JLabel operationLabel) {
		operationLabel.setText(text);
	}

	/**
	 * ��ղ��������ʾ�ı�
	 *
	 */
	private void clearResultText(JLabel operResultLabel) {
		operResultLabel.setText("");
	}

	/**
	 * ���ò��������ʾ�ı�
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

	public void setAng() {    //�Ӵ��ڽ������ҵ��Ƕȹؼ��֣�D:xxx.xx P:xxx.xx�������ҵ��ĽǶ��������õ�ʵʱ�ǶȽ�����
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
