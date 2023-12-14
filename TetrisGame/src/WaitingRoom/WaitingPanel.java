package WaitingRoom;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import javax.swing.UIManager;

import TetrisGame.ChatPanel;
import TetrisGame.GamePanel;
import TetrisGame.TetrisGame;
import TetrisGame.GameObejct.GameManager;
import utility.Settings;

public class WaitingPanel extends JLayeredPane{
	private TetrisGame tetris;
	private GamePanel gamePanel;
	private ChatPanel chatPanel;
	private GameManager gameManager;
	
	private Timer typingTimer;
    private int currentIndex;
    private String[] typingText = { "Waiting", "Waiting.", "Waiting..", "Waiting...", "Waiting...." };
	
	public static String userName;
	private static int playerNum;
	
	private JLabel p1NameLabel;
	private JLabel p2NameLabel;
	
	private JLabel imageLabel1;
	private JLabel imageLabel2;
	
	private JLabel joinPlayer;
	private JButton startBtn;
	
	private static Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	private static ObjectInputStream ois;
	private static ObjectOutputStream oos;
	
	public static String[] playerList = new String[2];
	private String rival;
	
	//private int rank = 2;
	private boolean isReady = false;
	public JLabel [] labelStatus = new JLabel[2];
	
	public WaitingPanel(String userName, TetrisGame tetris) {
		setLayout(null);
		this.userName = userName;
		this.tetris = tetris;
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
		add(p2NameLabel);
		
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
				UserMessage msg = new UserMessage(userName,"200");
				msg.setIsReady(!isReady);
				isReady = !isReady;
				SendMessage(msg);
			}
		});
		startBtn.setVisible(false);
		add(startBtn);
		
		labelStatus[0] = new JLabel();
		labelStatus[0].setBounds(135,300,50,50);
		labelStatus[0].setBackground(Color.WHITE);
		labelStatus[0].setOpaque(true);
		add(labelStatus[0]);
		
		setOpaque(true);
		
		serverConnect();
	}
	
	public static int getPlayerNum() {
		return playerNum;
	}
	public void setGamePanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	public void setChatPanel(ChatPanel chatPanel) {
		this.chatPanel = chatPanel;
	}
	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}
	
	private void serverConnect() {
		try {
			socket = new Socket("127.0.0.1",30000);
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());

			// SendMessage("/login " + userName);
			UserMessage userMsg = new UserMessage(userName, "101");
			SendMessage(userMsg);

			ListenServer listen = new ListenServer();
			listen.start();

		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	public String getRival() {
		return rival;
	}
	
	public void setAllJoinPlayer(String info[]) {
		typingTimer.stop();
		joinPlayer.setText("Player Connected");
		joinPlayer.setBounds(220, 290, 250, 100);
		// player2 = Toolkit.getDefaultToolkit().createImage("src/image/player2-waiting.gif");
		//add(p2NameLabel);
		
		labelStatus[1] = new JLabel();
		labelStatus[1].setBounds(525,300,50,50);
		labelStatus[1].setBackground(Color.WHITE);
		labelStatus[1].setOpaque(true);
		add(labelStatus[1]);
		startBtn.setVisible(true);
		
		if (info[0].trim().equals("player1")) {
			p2NameLabel.setText(info[1]);
			playerNum = 1;
			
			imageLabel2.setVisible(true);
		} else {
			p1NameLabel.setText(info[1]);
			p2NameLabel.setText(userName);
			playerNum = 2;
			imageLabel2.setVisible(true);  
		}
	}
	//채팅 보내기
	public void sendChat(String chatMsg) {
		UserMessage msg = new UserMessage(userName, "203");
		msg.setData(chatMsg);
		SendMessage(msg);
	}
	
	// Server에게 network으로 전송
	public static void SendMessage(UserMessage msg) {
		try {
			oos.writeObject(msg);
			oos.reset();
			oos.flush();
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


	public void startGame() {
		TetrisGame.isChange = true;
		TetrisGame.isGame = true;
		
		setVisible(false);
	}
	public void exitGame() {
		tetris.isChange = true;
		tetris.isDead = true;
		tetris.gameStart = false;
		UserMessage msg = new UserMessage(userName,"600");
		SendMessage(msg);
	}
	// 상대방 준비 상태를 set
	public void setReady(String name) {
		if(p1NameLabel.getText().equals(name)) {
			labelStatus[0].setIcon(Settings.ready_icon);
		}
		else {
			labelStatus[1].setIcon(Settings.ready_icon);
		}
	}

	// 상대방 준비 상태를 끔
	public void setNotReady(String name) {
		if(p1NameLabel.getText().equals(name)) {
			labelStatus[0].setIcon(null);
		}
		else {
			labelStatus[1].setIcon(null);
		}
	}

	
	// Server Message를 수신해서 화면에 표시
	class ListenServer extends Thread {
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
					if (socket == null) break;
					
					if(obj instanceof UserMessage) {
						msg = (UserMessage) obj;
					}
					
					switch (msg.getCode()) {
					case "102":
						// 대기실 2명 입장 시
						String[] msgUserList = msg.getUserList();
						int count = 0;
						for (int i = 0; i < msgUserList.length; i++) {
							if (msgUserList[i] != null && !msgUserList[i].equals(userName)) {
								playerList[count++] = msgUserList[i];
							}
						}
						setAllJoinPlayer(msg.getUserName().split("@@"));
						break;

					case "201":
						// 상태 아이콘을 준비상태로 바꿈
						setReady(msg.getUserName());
						break;
					case "202":
						// 상태 아이콘 제거
						setNotReady(msg.getUserName());
						break;
					case "203":
						// 채팅 출력
						chatPanel.setChatMessage(msg.getUserName(), msg.getData());
						break;
					case "300":
						// 게임 시작
						setReady(msg.getUserName());
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						startGame();
						break;
					case "401":
						// 상대방 상태 업데이트
						if (!TetrisGame.gameStart)
							break;
						rival = msg.getUserName();
						for (int i = 0; i < 10; i++) {
							for (int j = 0; j < 20; j++) {
								TetrisGame.rivalStatus[i][j] = msg.getBlockStatus()[i][j];
							}
						}
						gameManager.updateRivalStatus(msg.getItemStatus()[0], msg.getItemStatus()[1]);
						break;
					case "402":
						// 라인 추가 (공격)
						if (!TetrisGame.gameStart)
							break;
						if (!msg.getUserName().equals(userName))
							tetris.countAttackFromRival += msg.getAttackLines();
						break;
					case "403":
						// 상대방에게 아이템
						if (!TetrisGame.gameStart)
							break;
						if (msg.getItem() == 1)
							tetris.countAttackFromRival += 2;
						tetris.updateItemState(msg.getItem());
						tetris.attackCount++;
						break;
					case "404":
						// 상대 이모티콘 박스 변경
						if (!TetrisGame.gameStart)
							break;
						if (!msg.getUserName().equals(userName)) {
							for (int i = 0; i < 2; i++) {
								if (playerList[i] != null && playerList[i].equals(msg.getUserName())) {
									gameManager.showEmoji(i, msg.getEmoji());
								}
							}
						}
						break;
					case "405":
						// 상대방의 죽음 게임 종료 (이긴 대상은 승리 표시)
						if (!TetrisGame.gameStart) break;
						if(!TetrisGame.isDead) {
							for(int i = 0;i<2;i++) {
								if(playerList[i] !=null && playerList[i].equals(msg.getUserName())) {
									tetris.setLoser(msg.getUserName());
								}
							}
						}
						exitGame();
						break;
					case "500":
						// 게임 종료 
						if (!TetrisGame.gameStart)
							break;
						exitGame();
						break;
					case "600":
						// 로그아웃
						if (TetrisGame.gameStart) {
							UserMessage sendMsg = new UserMessage(userName, "500");
							sendMsg.setData(userName);
							SendMessage(sendMsg);
							exitGame();
						}
						break;
					}
				} catch (IOException e) {
					//AppendText("ois.readObject() error");
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
