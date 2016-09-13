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
	String clientSendAng;   //从客户端发送来的角度数据
	
	Window1 window1;
	
	//保存客户端的连接
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
		this.window1 = w;            //持有window1的引用
	}

	public void start() {
		numObs = new NumObservable();
		numObs.addObserver(this);
		try {
			ss = new ServerSocket(6666);
			started = true;
		} catch (BindException e) {
			JOptionPane.showMessageDialog(null, "服务器已开启，请不要重复开启！", "重复开启警告",
					JOptionPane.WARNING_MESSAGE);
			System.out.println("端口使用中。。。");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			while (started) {
				Socket s = ss.accept();
				Client c = new Client(s);
System.out.println("a client connected!");
				
				checkString = c.dis.readUTF();    //得到CheckUserPw发送来的字符串
				if(checkString.indexOf("Check") != -1) {  //如果是用户密码检查的请求
					String[] checkS = checkString.split("\\s+");
					UserModel um = new UserModel();
					String res = um.checkUser(checkS[1], checkS[2]);
					System.out.println("return passwd: " + res);							
					
					if(res == null || res.equals(checkS[2]) == false) {          		//如果用户验证不成功，关闭资源
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
					else if(res.equals(checkS[2])) {  //如果返回的密码等于用户输入的密码
						if(existUsers.add(checkS[1])) { //判断是否已存在此用户，如果成功加入Set
							c.setUseName(checkS[1]);
							clients.add(c);
							c.send("success");
System.out.println("success");
	
							NumObservable.ClientCount++;     //客户端数量+1
							//userName.add(checkS[1]);    //向userName中加入登陆的用户名
							numObs.setClientCount(NumObservable.ClientCount); //通知observer,更改客户端计数
							new Thread(c).start();
						
						}
						else {     //如果加入Set不成功
							c.send("user have exist");  //返回错误信息
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
		
		//向客户端写出数据
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
					//判断，如果此客户端是在list列表里的第一个客户，则服务与之通讯
					//如果不是，发送等待消息
					if(this.equals(clients.get(0))) {
					int keyWord;
					if((keyWord = str.indexOf("satellite")) != -1  && window1.sr.isOpen()) {   //如果从客户端接收到的数据包含satellite，则向串口发送此信息包含的教的信息
						clientSendAng = str.substring(keyWord + 10, keyWord + 24);
						window1.sr.start();
						window1.sr.run(clientSendAng);
					}
					
System.out.println(str);
					window1.jChatta.append("客户端" + clients.indexOf(this) + "：" + str + "\r\n");
					clients.get(0).send("消息发送成功！！");				
					}
					else {
						int numOfWait = clients.indexOf(this);
						this.send("前面还有" + numOfWait + "个用户，请等待~");
					}
				}
			} catch (IOException e) {
				System.out.println("Client Closed");
				//userName.remove(this.getUseName());
				existUsers.remove(this.getUseName());  // 将用户名从Set中移除
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
	   	 window1.itemOfUsers.add(i, iterator.next());  ////用户数改变的时候，动态的改变中userName中的值
	   }
	   i = 0;

	   window1.chatNmComboBox.setModel(new DefaultComboBoxModel(window1.itemOfUsers));
	   window1.jChatlabel.setText("用户数：" + String.valueOf(num)); 
	}
}

//int i = 0;
//window1.itemOfUsers.clear();
////userName = (String[]) existUsers.toArray();
//
//for(Iterator<String> iterator = existUsers.iterator(); iterator.hasNext();) {
//	 window1.itemOfUsers.add(i, iterator.next());  ////用户数改变的时候，动态的改变中userName中的值
//}
//i = 0;

//int i = 0;
//for(Iterator<String> iterator = existUsers.iterator(); iterator.hasNext();) {
//window1.itemOfUsers.add(i, iterator.next());  ////用户数改变的时候，动态的改变中userName中的值
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