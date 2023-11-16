import java.io.*;
import java.util.*;
import java.net.*;
public class Server {
	final static int ServerPort = 9999;
	static ArrayList<ServerThread> list = new ArrayList<>();
	static int clientCount = 0;
	
	public Server() throws IOException{
		ServerSocket ssocket = new ServerSocket(ServerPort);
		Socket s;
		while(true) {
			s = ssocket.accept();
			
			DataInputStream is = new DataInputStream(s.getInputStream());
			DataOutputStream os = new DataOutputStream(s.getOutputStream());
			
			ServerThread thread = new ServerThread(s, clientCount, is, os);
			list.add(thread);
			thread.start();
			clientCount ++;
			
			if(list.size() == 2) {
				for(ServerThread t : list) {
					System.out.println("Send Message");
					t.requestStartButton();
				}
			}
			
		}
	}
	
	public static void main(String[] args) throws IOException {
		new Server();
	}

}
