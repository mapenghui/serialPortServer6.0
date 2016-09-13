package com.mph.Server;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.mph.desk.SerialReader;
import com.mph.model.UserModel;
import com.mph.view.Window1;

public class ChatServer extends JFrame implements Observer {
	
	ServerSocket ss = null;
	boolean started = false;
	String checkString;
	String clientSendAng;   //�ӿͻ��˷������ĽǶ�����
	
	Window1 window1;
	
	//����ͻ��˵�����
	public ArrayList<Client> clients = new ArrayList<Client>();
    LinkedHashSet<String> existUsers  = new LinkedHashSet<String>();
    String[] userName;
	
	NumObservable numObs = null;
	
	public static void main(String[] args) {
		new ChatServer().start();
	}
	
	public ChatServer() {
	}
	
	public ChatServer(Window1 w) {
		this.window1 = w;            //����window1������
	}

	public void start() {
		numObs = new NumObservable();
		numObs.addObserver(this);
		try {
			ss = new ServerSocket(6666);
			started = true;
		} catch (BindException e) {
			JOptionPane.showMessageDialog(null, "�������ѿ������벻Ҫ�ظ�������", "�ظ���������",
					JOptionPane.WARNING_MESSAGE);
			System.out.println("�˿�ʹ���С�����");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			while (started) {
				Socket s = ss.accept();
				Client c = new Client(s);
System.out.println("a client connected!");
				
				checkString = c.dis.readUTF();    //�õ�CheckUserPw���������ַ���
				if(checkString.indexOf("Check") != -1) {  //������û������������
					String[] checkS = checkString.split("\\s+");
					UserModel um = new UserModel();
					String res = um.checkUser(checkS[1], checkS[2]);
					System.out.println("return passwd: " + res);							
					
					if(res == null || res.equals(checkS[2]) == false) {          		//����û���֤���ɹ����ر���Դ
						try {
							c.send("User error");
System.out.println("User error");
							if (c.dis != null) c.dis.close();
							if (c.dos != null) c.dos.close();
							if (s != null) s.close();
						} catch (IOException e1) {
							e1.printStackTrace();
							System.out.println("A client closed!");
						}
					}
					else if(res.equals(checkS[2])) {  //������ص���������û����������
						if(existUsers.add(checkS[1])) { //�ж��Ƿ��Ѵ��ڴ��û�������ɹ�����Set
							c.setUseName(checkS[1]);
							clients.add(c);
							c.send("success");
System.out.println("success");
	
							NumObservable.ClientCount++;     //�ͻ�������+1
							//userName.add(checkS[1]);    //��userName�м����½���û���
							numObs.setClientCount(NumObservable.ClientCount); //֪ͨobserver,���Ŀͻ��˼���
							new Thread(c).start();
						
						}
						else {     //�������Set���ɹ�
							c.send("user have exist");  //���ش�����Ϣ
System.out.println("user have exist");
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO: handle exception
		} finally {
			try {
				ss.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public class Client implements Runnable {
		private Socket s = null;
		private boolean bConnected = false;
		private DataInputStream dis = null;
		private DataOutputStream dos = null;
		private String useName = null;

		public Client(Socket s) {
			this.s = s;
			bConnected = true;
			try {
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//��ͻ���д������
		public void send(String str) {
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void run() {
			try {
				while (this.bConnected) {
					String str = dis.readUTF();
					//�жϣ�����˿ͻ�������list�б���ĵ�һ���ͻ����������֮ͨѶ
					//������ǣ����͵ȴ���Ϣ
					if(this.equals(clients.get(0))) {
					int keyWord;
					if((keyWord = str.indexOf("satellite")) != -1  && window1.sr.isOpen()) {   //����ӿͻ��˽��յ������ݰ���satellite�����򴮿ڷ��ʹ���Ϣ�����Ľ̵���Ϣ
						clientSendAng = str.substring(keyWord + 10, keyWord + 24);
						window1.sr.start();
						window1.sr.run(clientSendAng);
					}
					
System.out.println(str);
					window1.jChatta.append("�ͻ���" + clients.indexOf(this) + "��" + str + "\r\n");
					clients.get(0).send("��Ϣ���ͳɹ�����");				
					}
					else {
						int numOfWait = clients.indexOf(this);
						this.send("ǰ�滹��" + numOfWait + "���û�����ȴ�~");
					}
				}
			} catch (IOException e) {
				System.out.println("Client Closed");
				//userName.remove(this.getUseName());
				existUsers.remove(this.getUseName());  // ���û�����Set���Ƴ�
				NumObservable.ClientCount--;
				numObs.setClientCount(NumObservable.ClientCount);
				clients.remove(this);
			} finally {
				try {
					if (dis != null) dis.close();
					if (dos != null) dos.close();
					if (s != null) s.close();
				} catch (IOException e1) {
					e1.printStackTrace();
					System.out.println("A client closed!");
				}
			}
		}
		
		public String getUseName() {
			return useName;
		}

		public void setUseName(String useName) {
			this.useName = useName;
		}
		
		public DataInputStream getDis() {
			return dis;
		}

		public DataOutputStream getDos() {
			return dos;
		}

		public void setDis(DataInputStream dis) {
			this.dis = dis;
		}

		public void setDos(DataOutputStream dos) {
			this.dos = dos;
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
	   NumObservable myObserable=(NumObservable)o;
	   int num = myObserable.getClientCount();
	     // myObserable.setClientCount(num);
	     //userName = null;
	   int i = 0;
	   window1.itemOfUsers.clear();
	   for(Iterator<String> iterator = existUsers.iterator(); iterator.hasNext();) {
	   	 window1.itemOfUsers.add(i, iterator.next());  ////�û����ı��ʱ�򣬶�̬�ĸı���userName�е�ֵ
	   }
	   i = 0;

	   window1.chatNmComboBox.setModel(new DefaultComboBoxModel(window1.itemOfUsers));
	   window1.jChatlabel.setText("�û�����" + String.valueOf(num)); 
	}
}

//int i = 0;
//window1.itemOfUsers.clear();
////userName = (String[]) existUsers.toArray();
//
//for(Iterator<String> iterator = existUsers.iterator(); iterator.hasNext();) {
//	 window1.itemOfUsers.add(i, iterator.next());  ////�û����ı��ʱ�򣬶�̬�ĸı���userName�е�ֵ
//}
//i = 0;

//int i = 0;
//for(Iterator<String> iterator = existUsers.iterator(); iterator.hasNext();) {
//window1.itemOfUsers.add(i, iterator.next());  ////�û����ı��ʱ�򣬶�̬�ĸı���userName�е�ֵ
//}
//i = 0;

//for(Iterator<Client> it = clients.iterator(); it.hasNext();) {
//Client c = it.next();
//c.send(str);
//}
//Iterator<Client> it = clients.iterator();
//while(it.hasNext()) {
//Client c = it.next();
//c.send(str);
//}