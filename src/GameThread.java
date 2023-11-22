import java.io.*;
import java.net.*;

public class GameThread extends Thread {
    final static int ServerPort = 9999;
    DataInputStream is;
    DataOutputStream os;
    
    private volatile boolean running = false;
    private Object lock = new Object();

    public GameThread() throws IOException {
        InetAddress ip = InetAddress.getByName("localhost");
        Socket s = new Socket(ip, ServerPort);
        is = new DataInputStream(s.getInputStream());
        os = new DataOutputStream(s.getOutputStream());
        
        running=false; //스레드 생성 시 running을 false로 초기화
    }

    @Override
    public void run() {
        waitUntilStart();

        String message;
        while (running) {
            try {
                message = is.readUTF();
                System.out.println("send>> " + message);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    // "시작" 버튼 클릭 시 호출되는 메서드
    public void startGame() {
        synchronized (lock) {
            running = true;
            lock.notifyAll();
        }
    }

    // 게임 루프 시작 대기
    private void waitUntilStart() {
        synchronized (lock) {
            while (!running) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendGameOver() throws IOException {
        os.writeUTF("gameOver");
    }
}