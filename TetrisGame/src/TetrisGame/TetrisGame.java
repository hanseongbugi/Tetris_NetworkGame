package TetrisGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import TetrisGame.GameObejct.GameManager;

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
	private GameManager gameManager;
	private ChatPanel chatPanel;

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
	
	public int countAttackFromRival = 0; // 공격 받은 라인 수
	public int attackCount = 0; 
	
	private int currentItem = 0; // 현재 사용가능 아이템이 무엇인지 없으면 0
	public boolean spinable = true; // false면 회전 불가
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

			gameManager = new GameManager(this,waitingPanel);
			gamePanel = new GamePanel(gameManager);
			chatPanel = new ChatPanel(waitingPanel, gamePanel);
			
			waitingPanel.setChatPanel(chatPanel);
			waitingPanel.setGamePanel(gamePanel);
			waitingPanel.setGameManager(gameManager);
			
			gameManager.setGamePanel(gamePanel);
			this.rival = waitingPanel.getRival();
			isGame = false;
			gameStart = true;
			isDead = false;
			
			threadFactory = new GameThreadFactory(this, gamePanel, gameManager);
			threadFactory.initGameStateArray();
			threadFactory.makeGameProcessThread();
			threadFactory.makeSendStatusThread();

			gamePanel.requestFocus();
			gamePanel.addKeyListener(new MyKeyListener());
			gamePanel.networkStatusBox.setIcon(Settings.connectIcon);
			gamePanel.nameBox.setText(WaitingPanel.playerList[0]);
			gamePanel.addMouseListener(new GameFocusListener());

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
			loser = null;
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
			gameStart = false;
			splitPane.setVisible(false);
			if(loser==null) loser = WaitingPanel.userName; // 서버에서 loser 정보를 받지 못한자가 패자
			endingPanel = new EndingPanel(this, loser);
			waitingPanel = null;
			add(endingPanel);
			setVisible(true);
		}
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
	public void setRival(String rival) {
		this.rival = rival;
	}
	public String getRival() {
		return rival;
	}
	public void setCurrentItem(int currentItem) {
		this.currentItem = currentItem;
	}
	public int getCurrentItem() {
		return currentItem;
	}
	public void updateItemState(int n) {
		threadFactory.makeItemFromRival(n);
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
				gameManager.sendEmoji(1);
			} else if (e.getKeyCode() == KeyEvent.VK_X) {
				gameManager.sendEmoji(2);
			} else if (e.getKeyCode() == KeyEvent.VK_C) {
				gameManager.sendEmoji(3);
			} else if (e.getKeyCode() == KeyEvent.VK_SHIFT && !isDead) {
				if (currentItem != 0) {
					gameManager.sendItem(currentItem);
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
	
	class GameFocusListener extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			if(gameStart) {
				chatPanel.setConnectedFocus(false);
				gamePanel.setFocusable(true);
				gamePanel.requestFocus();
			}
		}
	}
	
}
