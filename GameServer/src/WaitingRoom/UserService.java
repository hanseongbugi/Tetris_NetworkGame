package WaitingRoom;

import java.io.*;
import java.net.Socket;
import java.util.*;

//User 당 생성되는 Thread
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
	public static int readyPlayer = 0;
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의

	private GameServer gameServer;

	public UserService(Socket clientSocket, ArrayList userList, GameServer gameServer) {
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



	public void Login(UserMessage msg) {
		gameServer.AppendText("새로운 참가자 " + UserName + " 입장.");
	}

	public void Logout() {
		String msg = "[" + UserName + "]님이 퇴장 하였습니다.\n";
		userList.remove(this); // Logout한 현재 객체를 배열에서 지운다
		UserMessage userMsg = new UserMessage(UserName,"600");
		String[] msgUserList = userMsg.getUserList();
		int count = 0;
		for (int i = 0; i < userList.size(); i++) {
			UserService user = (UserService) userList.get(i);
			if (user != this)
				msgUserList[count++] = user.UserName;
		}
		userMsg.setUserList(msgUserList);
		WriteAll(userMsg); // 나를 제외한 다른 User들에게 전송
		readyPlayer = 0;
		gameServer.AppendText("사용자 " + "[" + UserName + "] 퇴장. 현재 참가자 수 " + userList.size());
	}

	// 모든 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
	public void WriteAll(UserMessage msg) {
		for (int i = 0; i < userList.size(); i++) {
			UserService user = (UserService) userList.get(i);
			user.WriteMessage(msg);
		}
	}
	public void WriteMe(UserMessage msg) {
		for(int i = 0;i<userList.size();i++) {
			UserService user = (UserService) userList.get(i);
			if(user==this)
				user.WriteMessage(msg);
		}
	}

	// 나를 제외한 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
	public void WriteOthers(UserMessage msg) {
		for (int i = 0; i < userList.size(); i++) {
			UserService user = (UserService) userList.get(i);
			if (user != this)
				user.WriteMessage(msg);
		}
	}
	public void WriteJoin(UserMessage msg) {
		for(int i = 0;i<userList.size();i++) {
			UserService user = (UserService) userList.get(i);
			if(user!=this) {
				msg.setCode("102");
				msg.setUserName("player1@@"+UserName);
				user.WriteMessage(msg);
				
				msg.setUserName("player2@@"+user.UserName);
				WriteMessage(msg);
			}
		}
	}
	
	public void WriteMessage(UserMessage msg) {
		try {
			oos.writeObject(msg.getCode());
			oos.reset();
			oos.writeObject(msg.getUserName());
			oos.reset();
			switch(msg.getCode()) {
			case "101":
				oos.writeObject(msg.getUserList());
				break;
			case "102":
				oos.writeObject(msg.getUserList());
				break;
			case "103":
				oos.writeObject(msg.getUserID());
				break;
			case "201":
			case "202":
				oos.writeObject(msg.getIsReady());
				break;
			case "203":
				oos.writeObject(msg.getData());
				break;
			case "401":
				oos.writeObject(msg.getBlockStatus());
				oos.reset();
		    	oos.writeObject(msg.getItemStatus());
				break;
			case "402":
				oos.writeObject(msg.getAttackLines());
				break;
			case "403":
				oos.writeObject(msg.getItem());
				break;
			case "404":
				oos.writeObject(msg.getEmoji());
				break;
			case "600":
				oos.writeObject(msg.getUserList());
				break;
			}
			oos.reset();
		}catch(IOException e) {
			gameServer.AppendText("oos.writeObject(ob) error");		
			try {
				ois.close();
				oos.close();
				clientSocket.close();
				clientSocket = null;
				ois = null;
				oos = null;				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Logout();
		}
	}
	
	@SuppressWarnings("unchecked")
	public UserMessage ReadData() {
		Object obj = null;
		UserMessage msg= new UserMessage("","");
		try {
			obj = ois.readObject();
			msg.setCode((String)obj);
			obj = ois.readObject();
			msg.setUserName((String)obj);
			
			switch(msg.getCode()) {
			case "200":
				obj = ois.readObject();
				msg.setIsReady((boolean) obj);
				break;
			case "203": // 체팅
				obj = ois.readObject();
				msg.setData((String)obj);
				break;
			case "401":
				obj = ois.readObject();
				msg.setBlockStatus((char[][])obj);
				obj = ois.readObject();
				msg.setItemStatus((boolean[])obj);
				break;
			case"402":
				obj = ois.readObject();
				msg.setAttackLines((int) obj);
				break;
			case"403":
				obj = ois.readObject();
				msg.setItem((int) obj);
				break;
			case"404":
				obj = ois.readObject();
				msg.setEmoji((int) obj);
				break;
			}
		}catch (ClassNotFoundException e) {
			Logout();
			return null;
		} catch (IOException e) {
			Logout();
			return null;
		}
		return msg;
	}

	public void run() {
		while (true) { 
			UserMessage msg = null; 
			if (clientSocket == null)
				break;
			msg = ReadData();
			if (msg==null)
				break;
			if (msg.getCode().length()==0)
				break;
			gameServer.AppendObject(msg);
			switch(msg.getCode()) {
			case "101": // 로그인
				UserName = msg.getUserName();
				int count = 0;
				String[] msgUserList = msg.getUserList();
				msgUserList[count++] = UserName;
				for(int i = 0;i<userList.size();i++) {
					UserService user = (UserService) userList.get(i);
					if(user!=this) {
						msgUserList[count++] = user.UserName;
					}
				}
				msg.setUserList(msgUserList);
				msg.setUserID(userList.size());
				WriteJoin(msg);
				Login(msg);
				break;

			case "200": // 200 - 준비 요청, 준비 안되면 201, 준비 되어 있다면 202(준비 취소)
				boolean isReady = msg.getIsReady();
				if(isReady) {
					readyPlayer++;
					msg.setCode("201");
				}else {
					readyPlayer--;
					msg.setCode("202");
				}
				if(readyPlayer >= 2) msg.setCode("300");
				WriteAll(msg);
				break;	
			case "401": 
			case "402":
			case "403":
			case "404":
			case "405":
				WriteOthers(msg);
				break;
			case "203": // 체팅
			case "300":
				WriteAll(msg);
				break;
			case "500":
				WriteOthers(msg);
			case "600":
				Logout();
				break;
			}
			
		}
	}
}