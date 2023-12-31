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
import javax.swing.JOptionPane;
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
	
	// 기다리는 중에 Watiging 문구가 움직이는 것 처럼 보이게 만들기 위한 변수
	private Timer typingTimer; 
    private int currentIndex;
    private String[] typingText = { "Waiting", "Waiting.", "Waiting..", "Waiting...", "Waiting...." };
	
	public static String userName; // 현재 WaitingPanel을 띄우고 있는 사용자 이름
	
	// 방에 접속한 사용자들 이름과 이미지를 출력하기 위한 Label
	private JLabel p1NameLabel; 
	private JLabel p2NameLabel;
	private JLabel imageLabel1;
	private JLabel imageLabel2;
	
	private JLabel joinPlayer; // 사용자 2명 접속시 연결 완료 출력
	private JButton startBtn;
	
	private static Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	private static ObjectInputStream ois;
	private static ObjectOutputStream oos;
	
	public static String[] playerList = new String[2]; // 사용자 명단
	private String rival; // 라이벌 이름
	
	private boolean isReady = false; // 준비 상태인지 준비 취소 상태인지 나타냄
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
		joinPlayer.setForeground(Color.blue);
		joinPlayer.setHorizontalAlignment(JTextField.CENTER);
		add(joinPlayer);
		
		imageLabel1 = new JLabel();
		imageLabel1.setBounds(100,100,250,150); //이미지의 위치와 크기 설정
		add(imageLabel1, JLayeredPane.DEFAULT_LAYER);
		imageLabel1.setIcon(Settings.playerImage1);
		
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
		imageLabel2.setIcon(Settings.playerImage2);
		
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
		
		serverConnect(); // 서버 연결
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
	
	// 서버 연결을 위한 함수
	private void serverConnect() {
		try {
			socket = new Socket("127.0.0.1",30000);
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());

			UserMessage userMsg = new UserMessage(userName, "101"); // 로그인 메시지 생성
			SendMessage(userMsg);

			ListenServer listen = new ListenServer(); // 서버의 응답을 받는 스레드 생성
			listen.start();

		} catch (NumberFormatException | IOException e) {
			// 소켓 생성 실패 시 초기 화면으로 돌아감
			JOptionPane.showMessageDialog(getParent(), "서버와 연결할 수 없습니다", "WARNING",
                    JOptionPane.WARNING_MESSAGE);
            tetris.isChange = true;
            tetris.isInit = true;
            this.setVisible(false);
		}
	}
	public String getRival() {
		return rival;
	}
	
	// 2명의 사용자가 모두 접속 하였을 때 호출
	public void setAllJoinPlayer(String info[]) {
		typingTimer.stop();
		joinPlayer.setText("Player Connected");
		joinPlayer.setBounds(220, 290, 250, 100);
		
		labelStatus[1] = new JLabel();
		labelStatus[1].setBounds(525,300,50,50);
		labelStatus[1].setBackground(Color.WHITE);
		labelStatus[1].setOpaque(true);
		add(labelStatus[1]);
		startBtn.setVisible(true);
		
		if (info[0].trim().equals("player1")) {
			p2NameLabel.setText(info[1]);
			imageLabel2.setVisible(true);
		} else {
			p1NameLabel.setText(info[1]);
			p2NameLabel.setText(userName);
			imageLabel2.setVisible(true);  
		}
	}
	//채팅 보내기
	public void sendChat(String chatMsg) {
		UserMessage msg = new UserMessage(userName, "203");
		msg.setChatMessage(chatMsg);
		SendMessage(msg);
	}
	
	// Server에게 메시지를 전송
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
	
	// 게임을 시작하도록 하는 함수
	public void startGame() {
		TetrisGame.isChange = true;
		TetrisGame.isGame = true;
		
		setVisible(false);
	}
	// 게임을 종료하도록 하는 함수
	public void exitGame() {
		tetris.isChange = true;
		tetris.isDead = true;
		tetris.gameStart = false;
		UserMessage msg = new UserMessage(userName,"600"); // 로그아웃 메시지 생성
		SendMessage(msg); // 서버에 메시지 전송
	}
	// 준비 상태 
	public void setReady(String name) {
		if(p1NameLabel.getText().equals(name)) { // 내가 준비 상태인 경우
			labelStatus[0].setIcon(Settings.ready_icon);
		}
		else { // 라이벌이 준비 대기 상태인 경우
			labelStatus[1].setIcon(Settings.ready_icon);
		}
	}

	// 준비 대기 상태
	public void setNotReady(String name) {
		if(p1NameLabel.getText().equals(name)) { // 내가 준비 대기 상태인 경우
			labelStatus[0].setIcon(null);
		}
		else { // 라이벌이 준비 대기 상태인 경우
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
						chatPanel.setChatMessage(msg.getUserName(), msg.getChatMessage());
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
								tetris.rivalStatus[i][j] = msg.getBlockStatus()[i][j];
							}
						}
						boolean[] itemStatus = msg.getItemStatus();
						gameManager.updateRivalStatus(itemStatus[0],itemStatus[1], itemStatus[2]);
						break;
					case "402":
						// 라인 추가 (공격)
						if (!TetrisGame.gameStart)
							break;
						if (!msg.getUserName().equals(userName))
							tetris.countAttackFromRival += msg.getAttackLines();
						break;
					case "403":
						// 상대방에게 아이템을 받은 경우
						if (!TetrisGame.gameStart)
							break;
						if (msg.getItem() == 1) // 1번 공격을 받은 경우 라인을 올리는 공격 진행
							tetris.countAttackFromRival += 2;
						tetris.updateItemState(msg.getItem());
						tetris.attackCount++;
						break;
					case "404":
						// 이모티콘 박스 변경
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
						// 상대방 로그아웃
						if (TetrisGame.gameStart) {
							UserMessage sendMsg = new UserMessage(userName, "500");
							SendMessage(sendMsg);
							exitGame();
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
