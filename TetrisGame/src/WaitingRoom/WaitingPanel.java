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
import java.util.ArrayList;
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
import utility.Settings;

public class WaitingPanel extends JLayeredPane{
	TetrisGame tetris;
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
	
	private static Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	private static ObjectInputStream ois;
	private static ObjectOutputStream oos;
	
	public static String[] playerList = new String[2];
	private String rival;
	
	private int readyPlayers = 0; //준비된 플레이어의 수
	private int rank = 2;
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
				System.out.println("Rank = "+rank+" Ready = "+readyPlayers);
				if(isReady) {
					labelStatus[0].setIcon(null);
					isReady = false;
					sendNotReady();
				}
				else {
					labelStatus[0].setIcon(Settings.ready_icon);
					isReady = true;
					sendReady();
				}
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
	
	// Server에게 network으로 전송
	public static void SendMessage(UserMessage msg) {
		try {
			oos.writeObject(msg.getCode());
			oos.reset();
			oos.writeObject(msg.getUserName());
			oos.reset();
			
			switch(msg.getCode()) {
			case "202":
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
			}
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
	public UserMessage ReadMessage() {
		Object obj = null;
		UserMessage data = new UserMessage("", "");
		try {
			obj = ois.readObject();
			data.setCode((String) obj);
			obj = ois.readObject();
			data.setUserName((String) obj);
			System.out.println("code = "+data.getCode());
			switch(data.getCode()) {
			// 101번 코드는 로그인에 성공 했다는 의미이며 상대방의 로그인 성공시에도 전달 - 유저리스트가 이 시점에서 업데이트
			case "101":
				obj = ois.readObject();
				data.setUserList((ArrayList<String>) obj);
				break;

			case "102":
				obj = ois.readObject();
				data.setUserList((ArrayList<String>) obj);
				break;
				
			
			//채팅 메시지
			case "202": 
				obj = ois.readObject();
				data.setData((String) obj);
				break;
			//상대방의 블록 상태와 아이템 상태가 전달됨
			case "401":
				obj = ois.readObject();
				data.setBlockStatus((char[][]) obj);
				obj = ois.readObject();
				data.setItemStatus((boolean[]) obj);
				break;
			//상대방에게 공격받은 라인수
			case "402":
				obj = ois.readObject();
				data.setAttackLines((int) obj);
				break;
			//받은 아이템 번호
			case "403":
				obj = ois.readObject();
				data.setItem((int) obj);
				break;
			//받은 이모티콘 번호
			case "404":
				obj = ois.readObject();
				data.setEmoji((int) obj);
				break;
			//누가 로그아웃시 전달 - 이 시점에서도 유저리스트 업데이트됨
			case "600":
				obj = ois.readObject();
				data.setUserList((ArrayList<String>) obj);
				break;
			}
			
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			try {
				oos.close();
				socket.close();
				ois.close();
				socket = null;
				return null;
			} catch (IOException e1) {
				e1.printStackTrace();
				try {
					oos.close();
					socket.close();
					ois.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				socket = null;
				return null;
			}
		}
		return data;
	}
	// 상대방 준비 상태를 set
	public void setReady(int n) {
		labelStatus[n].setIcon(Settings.ready_icon);
	}
	public void sendReady() {
		readyPlayers ++;
		if(readyPlayers == rank) SendMessage(new UserMessage(userName,"300"));
		else SendMessage(new UserMessage(userName,"200"));
	}
	// 상대방 준비 상태를 끔
	public void setNotReady(int n) {
		labelStatus[n].setIcon(null);
	}
	public void sendNotReady() {
		SendMessage(new UserMessage(userName,"201"));
		readyPlayers --;
	}
	
	// Server Message를 수신해서 화면에 표시
		class ListenServer extends Thread {
			public void run() {
				while (true) {
					synchronized(this) {
						UserMessage data = ReadMessage();
						if (data==null)
							break;
						if (socket == null)
							break;
						switch (data.getCode()) {
						case "102":
							// 대기실 2명 입장 시
							ArrayList<String> userList = data.getUserList();
							int count = 0;
							for(int i=0; i<userList.size(); i++) {
								if(userList.get(i)!=null && !userList.get(i).equals(userName)) {
									playerList[count++] = userList.get(i);
								}
							}
							setAllJoinPlayer(data.getUserName().split("@@"));
							rank = count+1;
							break;
							
						case "200":
							//상태 아이콘을 준비상태로 바꿈
							readyPlayers++;
							for(int i=0; i<2; i++) {
								if(playerList[i]!=null && playerList[i].equals(data.getUserName()))
									setReady(i);
							}
							
							break;
						case "201":
							//상태 아이콘 제거
							readyPlayers--;
							for(int i=0; i<2; i++) {
								if(playerList[i]!=null && playerList[i].equals(data.getUserName()))
									setNotReady(i);
							}
							
							break;
						case "202":
							//채팅 출력
							//waitingPanel.textAreaChat.append("[" + data.username + "] " + data.chatMsg + "\n");
							//waitingPanel.textAreaChat.setCaretPosition(waitingPanel.textAreaChat.getText().length());
							break;
						case "300":
							//게임 시작
							startGame();
							break;
						case "401":
							//상대방 상태 업데이트
							if (!TetrisGame.gameStart) break;
							rival = data.getUserName();
							for(int i=0; i<10; i++) {
								for(int j=0; j<20; j++) {
									TetrisGame.rivalStatus[i][j] = data.getBlockStatus()[i][j];
								}
							}
							tetris.updateRivalStatus(data.getItemStatus()[0], data.getItemStatus()[1]);
							break;
						case "402":
							//아래 라인 추가
							if (!TetrisGame.gameStart) break;
							if(!data.getUserName().equals(userName))
								tetris.countAttackFromRival += data.getAttackLines();
							break;
						case "403":
							//상대방에게 아이템
							if (!TetrisGame.gameStart) break;
							if(data.getItem() == 1) tetris.countAttackFromRival += 2;
							//new ItemFromRival(data.item).start();
							tetris.attackCount++;
							break;
						case "404":
							//상대 이모티콘 박스 변경
							if (!TetrisGame.gameStart) break;
							if(!data.getUserName().equals(userName)) {
								for(int i=0; i<3; i++) 
									if(playerList[i]!=null && playerList[i].equals(data.getUserName()))
										tetris.showEmoticon(i, data.getEmoji());		
							}
							break;
						case "405":
							//상대방의 죽음 메시지 랭크가 상승
							if (!TetrisGame.gameStart) break;
							//if(!isDead) {
							//	rank--;
							//	for(int i = 0; i<3; i++) {
							//		if(playerList[i]!=null && playerList[i].equals(data.username)) {
							//			rivalDead(i);
							//		}
							//	}
							//}
							//if(rank == 1) {
							//	isDead = true;
							//	WriteData(new Data(userId, "500"));
							//	gameEnd();
							//}
							break;
						case "500":
							//게임 종료 메시지
							if (!TetrisGame.gameStart) break;
							//gameEnd();
							break;
						case "600":
							//상대방의 로그아웃 메시지 유저리스트 업데이트함
							if (TetrisGame.gameStart) {
							//	for(int i=0; i<3; i++) {
							//		if(playerList[i]!=null && playerList[i].equals(data.username))
							//			gamePanel.networkStatusBox[i].setIcon(disconnectImgIcon);
							//	}
							//	rank--;
							//	if(rank == 1) {
							//		isDead = true;
							//		WriteData(new Data(userId, "500"));
							//		gameEnd();
							//	}
							//}
							//else {
							//	for(int i=0; i<playerList.length; i++) {
							//		playerList[i] = null;
							//	}
							//	count = 0;
							//	for(int i=0; i<data.userList.length; i++) {
							//		if(data.userList[i]!=null && !data.userList[i].equals(userId)) {
							//			playerList[count++] = data.userList[i];
							//		}
							//	}
							//	readyPlayers = 0;
							//	for(int i=0; i<4; i++) {
							//		waitingPanel.labelStatus[i].setIcon(null);
							//	}
							//	waitingPanel.notReady();
							//	rank = count + 1;
							//	WriteData(new Data(userId, "103"));
							//	userlist();	
							}
							break;
						}
					}
				}
			}
		}
		

}
