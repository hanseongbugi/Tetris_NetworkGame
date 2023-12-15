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
		UserMessage userMsg = new UserMessage(UserName,"500");
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

	// 모든 User들에게 방송. 
	public void WriteAll(UserMessage msg) {
		for (int i = 0; i < userList.size(); i++) {
			UserService user = (UserService) userList.get(i);
			user.WriteMessage(msg);
		}
	}
	// 나에게만 방송.
	public void WriteMe(UserMessage msg) {
		for(int i = 0;i<userList.size();i++) {
			UserService user = (UserService) userList.get(i);
			if(user==this)
				user.WriteMessage(msg);
		}
	}

	// 나를 제외한 User들에게 방송. 
	public void WriteOthers(UserMessage msg) {
		for (int i = 0; i < userList.size(); i++) {
			UserService user = (UserService) userList.get(i);
			if (user != this)
				user.WriteMessage(msg);
		}
	}
	
	// 2명의 사용자 섭속 시 호출
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
	
	// UserMessage 객체를 버퍼에 작성하는 함수
	public void WriteMessage(UserMessage msg) {
		try {
			oos.writeObject(msg);
			oos.reset();
			oos.flush();
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
	
	public void run() {
		while (true) {
			try {
				Object obj = null; 
				UserMessage msg = null;
				try {
					obj = ois.readObject();
					if (obj == null) break;
				}catch(ClassNotFoundException e) {
					e.printStackTrace();
					break;
				}
				
				if(obj == null) break;
				if (clientSocket == null) break;
				
				if(obj instanceof UserMessage) {
					msg = (UserMessage) obj;
				}
				if (msg == null)
					break;
				if (msg.getCode().length() == 0)
					break;
				
				gameServer.AppendObject(msg);
				switch (msg.getCode()) {
				case "101": // 로그인
					UserName = msg.getUserName();
					int count = 0;
					String[] msgUserList = msg.getUserList();
					msgUserList[count++] = UserName;
					for (int i = 0; i < userList.size(); i++) {
						UserService user = (UserService) userList.get(i);
						if (user != this) {
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
					if (isReady) {
						readyPlayer++;
						msg.setCode("201");
					} else {
						readyPlayer--;
						msg.setCode("202");
					}
					if (readyPlayer >= 2)
						msg.setCode("300");
					WriteAll(msg);
					break;
				case "401": // 상태 업데이트
				case "402": // 라인 추가 (공격)
				case "403": // 상대방에게 아이템
				case "404": // 상대 이모티콘 박스 변경
					WriteOthers(msg);
					break;
				case "405": // 상대방의 죽음 게임 종료 (이긴 대상은 승리 표시)
					WriteOthers(msg);
					break;
				case "203": // 채팅
					WriteAll(msg);
					break;
				case "500": // 게임 종료
					WriteOthers(msg);
					break;
				case "600": // 로그아웃 (서버가 관리하는 객체에서 제거)
					Logout();
					break;
				}
			} catch (IOException e) {
				gameServer.AppendText("ois.readObject() error");
				try {
					ois.close();
					oos.close();
					clientSocket.close();

					break;
				} catch (Exception ee) {
					break;
				} // catch문 끝
			} // 바깥 catch문끝

		}
	}
}