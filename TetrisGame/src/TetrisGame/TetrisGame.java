package TetrisGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import java.util.Random;

import WaitingRoom.GameInitPanel;
import WaitingRoom.UserMessage;
import WaitingRoom.WaitingPanel;
import utility.Settings;

// 테트리스 게임의 프레임 (패널을 교체하며 화면 전환)
public class TetrisGame extends JFrame {

	private GamePanel gamePanel;
	private GameInitPanel initPanel;
	private WaitingPanel waitingPanel;
	private GameThreadFactory threadFactory;
	private JSplitPane splitPane;
	private EndingPanel endingPanel;

	public static boolean isMain;
	public static boolean isGame;
	public static boolean isChange;
	public static boolean isInit;
	public static boolean isWaitingRoom;
	public static boolean isDead;
	public static boolean gameStart;
	public static boolean isFirst = true;
	public static boolean player1Dead = false;
	public static boolean player2Dead = false;
	
	private int[] randomNumberArray;
	private volatile int[][] fallBlocks; // 현재 떨어지고 있는 블럭들의 위치 저장

	private String rival; // 방해받은 상태에 사용
	public static char[][] rivalStatus; // 상대방 블록 상태를 저장
	public int countAttackLine = 0; // 공격할 라인 수
	public int countAttackFromRival = 0; // 공격 받은 라인 수
	public int attackCount = 0; 
	public int removeLine = 0; // 제거한 줄 수 - 일정줄 제거시 공격/아이템생성
	private int currentItem = 0; // 현재 사용가능 아이템이 무엇인지 없으면 0
	public boolean spinable = true; // false면 회전 불가
	public int speed = 1000; // 떨어지는 속도
	private String loser;

	public TetrisGame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setTitle("TETRIS");
		setSize(720, 640);

		init();
		GameModeThread gameMode = new GameModeThread();
		gameMode.start();
		
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(waitingPanel!=null) {
					isDead = true;
					WaitingPanel.SendMessage(new UserMessage(WaitingPanel.userName, "405"));
				}
			}
			
		});
	}

	// 변수 초기화
	public void init() {
		new Settings();
		isMain = false;
		isGame = false;
		isChange = true;
		isInit = true;
		isWaitingRoom = false;
		isDead = false;
		gameStart = false;
		
	}

	public void gameModeChanged() {
		this.isChange = false;
		if (isMain) {
			isMain = false;
		} else if (isGame) {
			setSize(920, 640);
			splitPane = new JSplitPane();
			splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);

			gamePanel = new GamePanel();
			ChatPanel chatPanel = new ChatPanel(waitingPanel);
			waitingPanel.setChatPanel(chatPanel);
			waitingPanel.setGamePanel(gamePanel);

			this.rival = waitingPanel.getRival();
			isGame = false;
			gameStart = true;
			isDead = false;

			threadFactory = new GameThreadFactory(this, gamePanel);
			threadFactory.initGameStateArray();
			threadFactory.makeGameProcessThread();
			threadFactory.makeSendStatusThread();

			gamePanel.setFocusable(true);
			gamePanel.requestFocus();
			gamePanel.addKeyListener(new MyKeyListener());
			gamePanel.networkStatusBox.setIcon(Settings.connectIcon);
			gamePanel.nameBox.setText(WaitingPanel.playerList[0]);

			splitPane.setResizeWeight(0.78);
			splitPane.setDividerSize(0);
			splitPane.setLeftComponent(gamePanel);
			splitPane.setRightComponent(chatPanel);
			
			
			add(splitPane);
			setVisible(true);

		} else if (isInit) {
			this.isInit = false;
			fallBlocks = new int[4][2];
			rivalStatus = new char[10][20];
			randomNumberArray = new int[5];
			initPanel = new GameInitPanel();

			add(initPanel);
			setVisible(true);
		} else if (isWaitingRoom) {
			this.isWaitingRoom = false;
			waitingPanel = new WaitingPanel(initPanel.getUserName(), this);
			add(waitingPanel);
			setVisible(true);
		}
		else if(isDead) {
			setSize(720, 640);
			splitPane.setVisible(false);
			if(loser==null) loser = WaitingPanel.userName; // 서버에서 loser 정보를 받지 못한자가 패자
			endingPanel = new EndingPanel(this, loser);
			add(endingPanel);
			setVisible(true);
		}
	}

	public Color getColor(char type) {
		switch (type) {
		case 'O':
			return new Color(255, 255, 0);
		case 'L':
			return new Color(255, 116, 0);
		case 'J':
			return new Color(0, 0, 255);
		case 'I':
			return new Color(0, 255, 255);
		case 'Z':
			return new Color(255, 0, 0);
		case 'S':
			return new Color(0, 255, 0);
		case 'T':
			return new Color(255, 0, 255);
		case 'V':
			return new Color(176, 0, 255);
		case '-':
			return new Color(58, 146, 98);
		case '.':
			return new Color(128, 128, 128);
		default:
			return null;
		}
	}

	public ImageIcon getItemIcon(int type) {
		switch (type) {
		case 1:
			return Settings.Item1ImgIcon;
		case 2:
			return Settings.Item2ImgIcon;
		case 3:
			return Settings.Item3ImgIcon;
		default:
			return null;
		}
	}

	// 아이템 보내기
	public void sendItem(int n) {
		UserMessage data = new UserMessage(WaitingPanel.userName, "403");
		data.setItem(n);
		WaitingPanel.SendMessage(data);
	}

	// 상대방의 아이템 상태 업데이트 하기
	public void updateRivalStatus(boolean item2, boolean item3) {
		int num = -1;
		this.rival = waitingPanel.getRival();
		for (int i = 0; i < 2; i++) {
			if (rival.equals(WaitingPanel.playerList[i]))
				num = i;
		}
		if (num > -1) {
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 20; j++) {
					gamePanel.drawRivalBlock(i, j, rivalStatus[i][j], getColor(rivalStatus[i][j]));
				}
			}
		}
		if (item2)
			gamePanel.rivalItemBox[0].setIcon(Settings.Item2ImgIcon);
		else
			gamePanel.rivalItemBox[0].setIcon(null);
		if (item3)
			gamePanel.rivalItemBox[1].setIcon(Settings.Item3ImgIcon);
		else
			gamePanel.rivalItemBox[1].setIcon(null);

		gamePanel.repaint();
	}

	// 제거 라인 체크하는 함수 라인 제거 시 상대방에게 공격이 가능하고 일정 라인 제거시 아이템을 쓸 수 있게된다
	public void checkLine() {
		for (int i = 0; i < 10; i++) {
			if (gamePanel.board[i][19].getStatus().equals("AlreadySet")) {
				isDead = true;
				WaitingPanel.SendMessage(new UserMessage(WaitingPanel.userName, "405"));
				for (int j = 0; j < 10; j++) {
					for (int k = 0; k < 23; k++) {
						if (gamePanel.board[j][k].getStatus().equals("AlreadySet")) {
							gamePanel.drawBlock(j, k, '.', getColor('.'), "AlreadySet");
						}
					}
				}
				return;
			}
		}

		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 10; j++) {
				if (!gamePanel.board[j][i].getStatus().equals("AlreadySet"))
					break;
				if (j == 9) {
					clearLine(i--);
					countAttackLine++;
					removeLine++;
				}
			}
		}

		if (removeLine >= 2) {
			if (currentItem == 0)
				setItem();
			removeLine -= 2;
		}

		if (countAttackLine >= 2) {
			UserMessage data = new UserMessage(WaitingPanel.userName, "402");
			data.setAttackLines(countAttackLine - 1);
			countAttackLine = 0;
			WaitingPanel.SendMessage(data);
		}
		gamePanel.repaint();
	}

	// 라인 제거 함수
	public void clearLine(int line) {
		for (int i = line; i < 19; i++) {
			for (int j = 0; j < 10; j++) {
				gamePanel.drawBlock(j, i, gamePanel.board[j][i + 1].getType(),
						getColor(gamePanel.board[j][i + 1].getType()), gamePanel.board[j][i + 1].getStatus());
			}
		}
		for (int i = 0; i < 10; i++) {
			gamePanel.drawBlock(i, 19, ' ', null, "Empty");
		}

	}
	

	// 아이템을 얻는 함수
	public void setItem() {
		Random r = new Random();
		r.nextInt(3);
		currentItem = r.nextInt(3) + 1;
		gamePanel.itemBox.setIcon(getItemIcon(currentItem));
	}
	
	// 패자를 저장하는 함수
	public void setLoser(String loser) {
		this.loser = loser;
	}
	
	public int[] getRandomNumberArray() {
		return randomNumberArray;
	}
	public void setRandomNumberArray(int[] randomNumberArray) {
		this.randomNumberArray = randomNumberArray;
	}
	public int[][] getFallBlocks(){
		return fallBlocks;
	}
	public void setFallBlocks(int[][] fallBlocks) {
		this.fallBlocks = fallBlocks;
	}


	// 자신의 블록 상태 보내기
	public void sendStatusToRival() {
		UserMessage data = new UserMessage(WaitingPanel.userName, "401");
		char[][] blockStatus = data.getBlockStatus();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 20; j++) {
				blockStatus[i][j] = gamePanel.board[i][j].getType();
			}
		}
		if (speed == 1000)
			data.getItemStatus()[0] = false;
		else
			data.getItemStatus()[0] = true;

		if (spinable)
			data.getItemStatus()[1] = false;
		else
			data.getItemStatus()[1] = true;
		WaitingPanel.SendMessage(data);
	}

	// 상대방에게 공격 받는 함수 공격 받은 만큼 블럭들이 위로 올라간다
	public void attackFromRival(int lines) {
		for (int i = 19; i >= lines; i--) {
			for (int j = 0; j < 10; j++) {
				if (!gamePanel.board[j][i - lines].getStatus().equals("CurrentFall")
						|| !gamePanel.board[j][i].getStatus().equals("CurrentFall"))
					gamePanel.drawBlock(j, i, gamePanel.board[j][i - lines].getType(),
							getColor(gamePanel.board[j][i - lines].getType()), gamePanel.board[j][i - lines].getStatus());
			}
		}

		for (int i = 0; i < lines; i++) {
			Random r = new Random();
			int n = r.nextInt(10);
			for (int j = 0; j < 10; j++) {
				if (n != j)
					gamePanel.drawBlock(j, i, '.', getColor('.'), "AlreadySet");
				else
					gamePanel.drawBlock(j, i, ' ', null, "Empty");
			}
		}

	}

	public ImageIcon getEmoji(int type) {
		switch (type) {
		case 1:
			return Settings.emoji1;
		case 2:
			return Settings.emoji2;
		case 3:
			return Settings.emoji3;
		default:
			return null;
		}
	}
	
	public void updateItemState(int n) {
		threadFactory.makeItemFromRival(n);
	}

	// 자신 혹은 상대방의 이모티콘 업데이트
	public void showEmoji(int player, int type) {
		if (player == -1) {
			gamePanel.myEmoticon.setIcon(getEmoji(type));
		} else {
			gamePanel.rivalEmoticon.setIcon(getEmoji(type));
		}
		gamePanel.repaint();
	}

	// 이모티콘 보내기
	public void sendEmoji(int n) {
		UserMessage msg = new UserMessage(WaitingPanel.userName, "404");
		msg.setEmoji(n);
		WaitingPanel.SendMessage(msg);

		showEmoji(-1, n);
	}
	
	// 상하좌후키, 스페이스바 - 움직이기
	// ZXC키 - 이모티콘 보내기
	// Shift키 - 아이템 사용
	class MyKeyListener extends KeyAdapter {
		private static final int UP = 0;
		private static final int DOWN = 1;
		private static final int LEFT = 2;
		private static final int RIGHT = 3;
		private static final int SPACE = 4;
		
		public void keyPressed(KeyEvent e) {
			if (isDead)
				return;
			if (!gameStart)
				return;

			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				threadFactory.makeMoveThread(SPACE);
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				threadFactory.makeMoveThread(DOWN);
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				threadFactory.makeMoveThread(LEFT);
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				threadFactory.makeMoveThread(RIGHT);
			} else if (e.getKeyCode() == KeyEvent.VK_UP && spinable == true) {
				threadFactory.makeMoveThread(UP);
			}
		}

		public void keyReleased(KeyEvent e) {
			if (!gameStart)
				return;

			if (e.getKeyCode() == KeyEvent.VK_Z) {
				sendEmoji(1);
			} else if (e.getKeyCode() == KeyEvent.VK_X) {
				sendEmoji(2);
			} else if (e.getKeyCode() == KeyEvent.VK_C) {
				sendEmoji(3);
			} else if (e.getKeyCode() == KeyEvent.VK_SHIFT && !isDead) {
				if (currentItem != 0) {
					sendItem(currentItem);
					gamePanel.itemBox.setIcon(null);
					currentItem = 0;
				}
			}
		}
	}
	class GameModeThread extends Thread {
		public void run() {
			while (true) {
				if (isChange) {
					isChange = false;
					gameModeChanged();
				}
				if (player1Dead && player2Dead) {

				}
				repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}
	
}
