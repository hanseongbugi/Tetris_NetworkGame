import java.io.*;
import java.net.*;

public class GameThread extends Thread{
	final static int ServerPort = 9999;
	DataInputStream is;
	DataOutputStream os;
	public GameThread() throws IOException{
		InetAddress ip = InetAddress.getByName("localhost");
		Socket s = new Socket(ip,ServerPort);
		is = new DataInputStream(s.getInputStream());
		os = new DataOutputStream(s.getOutputStream());
	}
	
	@Override
	public void run() {
		String message;
		while(true) {
			try {
				message = is.readUTF();
				System.out.println(message);
			}catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
	public void sendGameOver() throws IOException{
		os.writeUTF("gameOver");
	}
}
