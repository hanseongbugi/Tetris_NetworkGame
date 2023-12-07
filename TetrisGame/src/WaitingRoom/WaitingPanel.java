package WaitingRoom;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import javax.swing.UIManager;

import TetrisGame.GamePanel;
import TetrisGame.TetrisGame;
import WaitingRoom.GameInitPanel.RoundJTextField;

public class WaitingPanel extends JLayeredPane{
	
	private Timer typingTimer;
    private int currentIndex;
    private String[] typingText = { "Waiting", "Waiting.", "Waiting..", "Waiting...", "Waiting...." };
	
	private GamePanel gamePanel;
	public static String userName;
	private static int playerNum;
	
	private JLabel p1NameLabel;
	private JLabel p2NameLabel;
	
	private JLabel imageLabel1;
	private JLabel imageLabel2;
	
	private JLabel joinPlayer;
	private JButton startBtn;
	
	private Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	private ObjectInputStream ois;
	private static ObjectOutputStream oos;
	
	public WaitingPanel(String userName) {
		setLayout(null);
		this.userName = userName;
		
		currentIndex = 0;
        typingTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                joinPlayer.setText(typingText[currentIndex]);
                currentIndex = (currentIndex + 1) % typingText.length;
            }
        });
        
        typingTimer.start();
        
		setBackground(new Color(173, 216, 250)); //배경색 하늘색 지정
        
		Font font = new Font("HBIOS-SYS", Font.PLAIN, 30);
		
		joinPlayer = new JLabel("Waiting...");
		joinPlayer.setBounds(250,300, 250, 100);
		joinPlayer.setFont(new Font("Rockwell", Font.BOLD, 25));
		//joinPlayer.setForeground(new Color(255,186,0));
		joinPlayer.setForeground(Color.blue);
		joinPlayer.setHorizontalAlignment(JTextField.CENTER);
		add(joinPlayer);
		
		imageLabel1 = new JLabel();
		imageLabel1.setBounds(100,100,250,150); //이미지의 위치와 크기 설정
		add(imageLabel1, JLayeredPane.DEFAULT_LAYER);
		
		ImageIcon imageIcon1 = new ImageIcon("images/kurome.png"); //이미지 설정
		imageLabel1.setIcon(imageIcon1);
		
		font = new Font("HBIOS-SYS", Font.PLAIN, 30);
		p1NameLabel = new JLabel(userName);
		p1NameLabel.setBounds(60, 250, 200, 40);
		p1NameLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
		//p1NameLabel.setForeground(new Color(0, 255, 0));
		p1NameLabel.setForeground(Color.BLACK);
		p1NameLabel.setHorizontalAlignment(JTextField.CENTER);
		p1NameLabel.setBorder(BorderFactory.createCompoundBorder(null, null));
		add(p1NameLabel);
		
        
		imageLabel2 = new JLabel();
		imageLabel2.setBounds(500,100,280,160); //이미지의 위치와 크기 설정
		imageLabel2.setVisible(false);
		add(imageLabel2, JLayeredPane.DEFAULT_LAYER);
		
		ImageIcon imageIcon2 = new ImageIcon("images/mymelody.png"); //이미지 설정
		imageLabel2.setIcon(imageIcon2);
		
		font = new Font("HBIOS-SYS", Font.PLAIN, 30);
		p2NameLabel = new JLabel("");
		p2NameLabel.setBounds(450 ,250, 200, 40);
		p2NameLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
		p2NameLabel.setForeground(new Color(255, 105, 185));
		p2NameLabel.setHorizontalAlignment(JTextField.CENTER);
		
		font = new Font("HBIOS-SYS", Font.PLAIN, 40);
		startBtn = new JButton("START");
		startBtn.setBounds(250, 450, 200, 40);
		startBtn.setFont(font);
		startBtn.setOpaque(true);
		startBtn.setForeground(Color.RED);
		startBtn.setBorderPainted(false);
		startBtn.setFocusPainted(false);
		
		// hover 효과를 위한 MouseListener 추가
        startBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startBtn.setBackground(Color.RED);
                startBtn.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                startBtn.setBackground(UIManager.getColor("control"));
                startBtn.setForeground(Color.RED);
            }
        });
		
		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub\
				UserMessage userMsg = new UserMessage(userName,"103","start");
				SendObject(userMsg);
				startGame();
			}
		});
		
		setOpaque(true);
		
		serverConnect();
	}
	
	public void setGamePanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	public static int getPlayerNum() {
		return playerNum;
	}
	
	private void serverConnect() {
		try {
			socket = new Socket("127.0.0.1",30000);
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());

			// SendMessage("/login " + userName);
			UserMessage userMsg = new UserMessage(userName, "101", "connect Room");
			SendObject(userMsg);

			ListenServer listen = new ListenServer();
			listen.start();

		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setAllJoinPlayer(String info[]) {
		typingTimer.stop();
		joinPlayer.setText("Player Connected");
		joinPlayer.setBounds(220, 290, 250, 100);
		// player2 = Toolkit.getDefaultToolkit().createImage("src/image/player2-waiting.gif");
		add(p2NameLabel);
		
		if (info[0].trim().equals("player1")) {
			p2NameLabel.setText(info[1]);
			add(startBtn);
			playerNum = 1;
			
			imageLabel2.setVisible(true);
		} else {
			p1NameLabel.setText(info[1]);
			p2NameLabel.setText(userName);
			playerNum = 2;
			
			imageLabel2.setVisible(true);  
		}
	}
	
	// Server에게 network으로 전송
	public void SendMessage(String msg, String code) {
		try {
			UserMessage userMsg = new UserMessage(userName, code, msg);
			oos.writeObject(userMsg);
		} catch (IOException e) {
			try {
				ois.close();
				oos.close();
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(0);
			}
		}
	}

	static public void SendObject(Object obj) { // 서버로 메세지를 보내는 메소드
		try {
			oos.writeObject(obj);
		} catch (IOException e) {

		}
	}
	public void startGame() {
		TetrisGame.isChange = true;
		TetrisGame.isGame = true;
		
		setVisible(false);
	}
	
	// Server Message를 수신해서 화면에 표시
		class ListenServer extends Thread {
			public void run() {
				while (true) {
					try {
						Object obj = null;
						String msg = null;
						UserMessage userMsg;
						try {
							obj = ois.readObject();
							if (obj == null) break;
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
							break;
						}
						if (obj == null)
							break;
						if (obj instanceof UserMessage) {
							userMsg = (UserMessage) obj;
							msg = String.format("[%s] %s", userMsg.getUserID(), userMsg.getData());
						} else
							continue;
						switch (userMsg.getCode()) {
						case "102": // 대기실 2명 입장
							setAllJoinPlayer(userMsg.getData().split("@@"));
							break;
						case "103": // 게임 시작
							startGame();
							break;
						case "104": // 상대방 방 나감
							startGame();
							break;

						case "401": // 게임 player 움직임
							if (gamePanel != null) {
								gamePanel.movePlayer(userMsg.getData().split("@@"));
							}
							break;
						
						case "501": // currentBlock 메시지
							if (gamePanel != null) {
								//gamePanel.SocketMeetBubbleMonster(userMsg.getData().split(","));
							}
							break;
						case "502": // preBlock 메시지
							if (gamePanel != null) {
								//gamePanel.SocketbubbleMove(userMsg.getData().split(","));
							}
							break;
						case "601": // bubble 터짐 > item create
							if (gamePanel != null) {
								// gamePanel.SocketChangeItem(cm.getData().split(","));
							}
							break;
						case "602": // item 위치 조정
							if (gamePanel != null) {
								// gamePanel.SocketItemLocation(cm.getData().split(","));
							}
							break;
						case "603": // item 점수 증가
							if (gamePanel != null) {
								// gamePanel.SocketIncrementScore(cm.getData().split(","));
							}
							break;
						}
					} catch (IOException e) {
						try {
							ois.close();
							oos.close();
							socket.close();

							break;
						} catch (Exception ee) {
							break;
						} // catch문 끝
					} // 바깥 catch문끝
				}
			}
		}

}
