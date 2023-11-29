package WaitingRoom;

import java.io.*;
import java.net.Socket;
import java.util.*;

//User 당 생성되는 Thread
//Read One 에서 대기 -> Write All
public class UserService extends Thread {
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	private Socket clientSocket;
	private ArrayList userList;
	public String UserName = "";
	public String UserStatus;
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	
	private GameServer gameServer;

	public UserService(Socket clientSocket, ArrayList userList, GameServer gameServer) {
		// TODO Auto-generated constructor stub
		// 매개변수로 넘어온 자료 저장
		this.clientSocket = clientSocket;
		this.userList = userList;
		this.gameServer = gameServer;
		try {
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(clientSocket.getInputStream());

		} catch (Exception e) {
			this.gameServer.AppendText("userService error");
		}
	}



	public void Login() {
		gameServer.AppendText("새로운 참가자 " + UserName + " 입장.");
		String msg = "[" + UserName + "]님이 입장 하였습니다.\n";
	}

	public void Logout() {
		String msg = "[" + UserName + "]님이 퇴장 하였습니다.\n";
		userList.remove(this); // Logout한 현재 객체를 배열에서 지운다
		WriteAll(msg, "104"); // 나를 제외한 다른 User들에게 전송
		gameServer.AppendText("사용자 " + "[" + UserName + "] 퇴장. 현재 참가자 수 " + userList.size());
	}

	// 모든 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
	public void WriteAll(String str, String code) {
		for (int i = 0; i < userList.size(); i++) {
			UserService user = (UserService) userList.get(i);
			if (user.UserStatus == "O")
				user.WriteOne(str, code);
		}
	}
	// 모든 User들에게 Object를 방송. 채팅 message와 image object를 보낼 수 있다
	public void WriteAllObject(Object ob) {
		for (int i = 0; i < userList.size(); i++) {
			UserService user = (UserService) userList.get(i);
			if (user.UserStatus == "O")
				user.WriteOneObject(ob);
		}
	}

	// 나를 제외한 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
	public void WriteOthers(String str, String code) {
		for (int i = 0; i < userList.size(); i++) {
			UserService user = (UserService) userList.get(i);
			if (user != this)
				user.WriteOne(str, code);
		}
	}
	
	// 나를 제외한 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
			public void WriteJoin() {
				System.out.println("writeJoin");
				for (int i = 0; i < userList.size(); i++) {
					UserService user = (UserService) userList.get(i);
					if (user != this) {
						user.WriteOne("player1@@" + UserName, "102");
						WriteOne("player2@@" + user.UserName, "102");
					}
				}
			}

	// Windows 처럼 message 제외한 나머지 부분은 NULL 로 만들기 위한 함수
	public byte[] MakePacket(String msg) {
		byte[] packet = new byte[BUF_LEN];
		byte[] bb = null;
		int i;
		for (i = 0; i < BUF_LEN; i++)
			packet[i] = 0;
		try {
			bb = msg.getBytes("euc-kr");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (i = 0; i < bb.length; i++)
			packet[i] = bb[i];
		return packet;
	}

	// UserService Thread가 담당하는 Client 에게 1:1 전송
	public void WriteOne(String msg, String code) {
		try {
			UserMessage userMsg = new UserMessage("SERVER", code, msg);
			if(userMsg!=null) oos.writeObject(userMsg);
		} catch (IOException e) {
			gameServer.AppendText("dos.writeObject() error");
			try {
				ois.close();
				oos.close();
				clientSocket.close();
				clientSocket = null;
				ois = null;
				oos = null;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Logout(); // 에러가난 현재 객체를 벡터에서 지운다
		}
	}

	// 귓속말 전송
	public void WritePrivate(String msg) {
		try {
			UserMessage obcm = new UserMessage("귓속말", "200", msg);
			oos.writeObject(obcm);
		} catch (IOException e) {
			gameServer.AppendText("dos.writeObject() error");
			try {
				oos.close();
				clientSocket.close();
				clientSocket = null;
				ois = null;
				oos = null;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Logout(); // 에러가난 현재 객체를 벡터에서 지운다
		}
	}
	
	
	
	public void WriteOneObject(Object ob) {
		try {
		    oos.writeObject(ob);
		} 
		catch (IOException e) {
			gameServer.AppendText("oos.writeObject(ob) error");		
			try {
				ois.close();
				oos.close();
				clientSocket.close();
				clientSocket = null;
				ois = null;
				oos = null;				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Logout();
		}
	}
	
	public void run() {
		while (true) { // 사용자 접속을 계속해서 받기 위해 while문
			try {
				Object obj = null;
				String msg = null;
				UserMessage userMsg = null;
				if (gameServer.getServerSocket() == null)
					break;
				try {
					obj = ois.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
				if (obj == null)
					break;
				if (obj instanceof UserMessage) {
					userMsg = (UserMessage) obj;
					gameServer.AppendObject(userMsg);
				} else
					continue;
				if (userMsg.getCode().matches("101")) { // 방 만들기/방 참가
					UserName = userMsg.getUserID();
					UserStatus = "O"; // Online 상태
					//1,2,3
					if(userList.size()>=1)
						WriteJoin();
					
					Login();
				} else if (userMsg.getCode().matches("103")) { // 게임 시작
					WriteOthers("start","103");
				}else if (userMsg.getCode().matches("300")) { // stage 이동
					WriteOthers(userMsg.getData(),"300"); 
					WriteOne(userMsg.getData(),"300");
				} else if (userMsg.getCode().matches("401")) { // player 움직임 keyPressed
					System.out.println("401");
					
					WriteOthers(userMsg.getData(),"401"); 
					WriteOne(userMsg.getData(),"401");
				} else if (userMsg.getCode().matches("402")) { // player 움직임 keyReleased
					System.out.println("402");
					
					WriteOthers(userMsg.getData(),"402"); 
					WriteOne(userMsg.getData(),"402");
				} else if (userMsg.getCode().matches("403")) { // player 움직임 (x,y)
					System.out.println("403");
					
					WriteOthers(userMsg.getData(),"403"); 
					WriteOne(userMsg.getData(),"403");
				} else if (userMsg.getCode().matches("501")) { // bubble이랑 monster이랑 만남
					System.out.println("501");
					
					WriteOthers(userMsg.getData(),"501"); 
					WriteOne(userMsg.getData(),"501");
				} else if (userMsg.getCode().matches("502")) { // bubble 천장 랜덤 움직임
					System.out.println("502");
					
					WriteOthers(userMsg.getData(),"502"); 
				} else if (userMsg.getCode().matches("601")) { // bubble 터짐 > item create
					System.out.println("601");
					
					WriteOthers(userMsg.getData(),"601"); 
					WriteOne(userMsg.getData(),"601");
				} else if (userMsg.getCode().matches("602")) { // item 위치 조정
					System.out.println("602");
					
					WriteOthers(userMsg.getData(),"602"); 
					WriteOne(userMsg.getData(),"602");
				}   else if (userMsg.getCode().matches("603")) { // item 점수 증가
					System.out.println("603");
					
					WriteOthers(userMsg.getData(),"603"); 
					WriteOne(userMsg.getData(),"603");
				} 
			} catch (IOException e) {
				gameServer.AppendText("ois.readObject() error");
				try {
//					dos.close();
//					dis.close();
					ois.close();
					oos.close();
					clientSocket.close();
					Logout(); // 에러가난 현재 객체를 벡터에서 지운다
					break;
				} catch (Exception ee) {
					break;
				} 
			} 
		} 
	} 
}