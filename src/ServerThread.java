import java.io.*;
import java.net.*;

public class ServerThread extends Thread {
	private int clientId;
	final DataInputStream is;
	final DataOutputStream os;
	Socket s;

	public ServerThread(Socket s, int clientId, DataInputStream is, DataOutputStream os) {
		this.is = is;
		this.os = os;
		this.clientId = clientId;
		this.s = s;
	}

	@Override
	public void run() {
		String message;
		while (true) {
			try {
				message = is.readUTF();
				if(message.equals("gameOver")) {
					Server.list.remove(this);
					return;
				}
				
				for (ServerThread t : Server.list) {
					t.os.writeUTF(clientId + " : " + message);
				}
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
		try {
			this.is.close();
			this.os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void requestStartButton() {
		String message = "makeStartButton";
		try {
			os.writeUTF(clientId+ message);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}