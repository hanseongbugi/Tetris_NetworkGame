package WatingRoom;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class AcceptServer extends Thread{
	private GameServer gameServer;
	private ServerSocket socket;
	private ArrayList userList;
	private Socket clientSocket; // accept() 에서 생성된 client 소켓
	public AcceptServer(GameServer gameServer, ServerSocket socket, ArrayList userList) {
		this.gameServer = gameServer;
		this.socket = socket;
		this.userList = userList;
	}
	
	public void run() {
		while (true) { // 사용자 접속을 계속해서 받기 위해 while문
			try {
				gameServer.AppendText("Waiting new clients ...");
				clientSocket = socket.accept(); // accept가 일어나기 전까지는 무한 대기중
				gameServer.AppendText("새로운 참가자 from " + clientSocket);
				// User 당 하나씩 Thread 생성
				UserService newUser = new UserService(clientSocket, userList, gameServer);
				userList.add(newUser); // 새로운 참가자 배열에 추가
				newUser.start(); // 만든 객체의 스레드 실행
				gameServer.AppendText("현재 참가자 수 " + userList.size());
			} catch (IOException e) {
				gameServer.AppendText("accept error");
			}
		}
	}
}



