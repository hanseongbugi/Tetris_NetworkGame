package TetrisGame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import WaitingRoom.GameInitPanel;
import WaitingRoom.UserMessage;
import WaitingRoom.WaitingPanel;
import utility.PlayerKeySetting;
import utility.Settings;

public class TetrisGame extends JFrame{
	
	private GamePanel gamePanel;
	private GameInitPanel initPanel;
	private WaitingPanel waitingPanel;
	
	public static boolean isMain;
	public static boolean isGame;
	public static boolean isChange;
	public static boolean isInit;
	public static boolean isWaitingRoom;
	public static boolean isDead;
	public static boolean gameStart;
	private boolean isFirst = true; 
	
	private GameProcessThread gameThread;
	//private CreateThread cthread;
	public static boolean player1Dead = false;
	public static boolean player2Dead = false;
	
	private int [] alreadySet = {0, 0, 0, 0, 0, 0, 0, 0, 0}; //골고루 랜덤 번호가 나오도록 1이 나오면 2번째 요소가 1로 set
	private boolean keyReady = true; //키 이벤트에 딜레이 주기 위한 변수
	private int [] randomNumberArray = new int[5]; 
	private volatile int [][] fallBlocks = new int[4][2]; //현재 떨어지고 있는 블럭들의 위치 저장
	private int fallBlockLength; //떨어지는 블록의 크기 3칸짜리 또는 4칸짜리
	private int currentBlockNumber; //현재 블록 종류 - 랜덤에서 나온값
	private int centerX; //회전중심 x축
	private int centerY; //회전중심 y축
	
	private static final int UP = 0; 
	private static final int DOWN = 1;
	private static final int LEFT = 2;
	private static final int RIGHT = 3;
	private static final int SPACE = 4;
	
	//private String [] playerList = new String [4]; //플레이어들의 이름들을 저장
	private String rival; //방해받은 상태에 사용
	public static char[][] rivalStatus = new char[10][20]; //상대방 블록 상태를 저장
	public int countAttackLine = 0; //공격할 라인 수
	public int countAttackFromRival = 0; //공격 받은 라인 수
	public int attackCount = 0; //
	public int removeLine = 0; //제거한 줄 수 - 일정줄 제거시 공격/아이템생성
	private int currentItem = 0; //현재 사용가능 아이템이 무엇인지 없으면 0
	private boolean spinable = true; //false면 회전 불가
	private int speed = 1000; //떨어지는 속도
	private int rank = 4; //순위
	private int readyPlayers = 0; //준비된 플레이어의 수
	private char [] blockType = {'O', 'L', 'J', 'I', 'Z', 'S', 'T', 'V', '-'};
	
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
	}
	// 변수 초기화
	public void init() {
		new Settings();
		new PlayerKeySetting();
		isMain = false;
		isGame = false;
		isChange = true;
		isInit = true;
		isWaitingRoom = false;
		isDead = false;
		gameStart =false;
	}
	
	// Frame에 Panel을 붙이기 위함
	public void setPanel(JPanel panel) {
		add(panel);
	}
	
	// Game Paenl 설정
	private void setGamePanel(GamePanel gamePanel) {
		Container con = getContentPane();
		con.setLayout(new BorderLayout());
		add(gamePanel, BorderLayout.CENTER);
	}
	
	public void gameModeChanged() {
		this.isChange = false;
		if(isMain) {
			isMain = false;
		}
		else if(isGame) {
			gamePanel = new GamePanel(this);
			setGamePanel(gamePanel);
			waitingPanel.setGamePanel(gamePanel);
			this.rival = waitingPanel.getRival();
			isGame = false;
			gameStart = true;
			gameThread = new GameProcessThread();
			gameThread.start();
			new SendStatus().start();
			
			gamePanel.setFocusable(true);
			gamePanel.requestFocus();
			gamePanel.addKeyListener(new MyKeyListener());
			add(gamePanel);
			setVisible(true);
			
		}else if(isInit) {
			this.isInit = false;
			initPanel = new GameInitPanel();
			
			add(initPanel);
			setVisible(true);
		}else if(isWaitingRoom) {
			this.isWaitingRoom = false;
			waitingPanel = new WaitingPanel(initPanel.getUserName(),this);
			add(waitingPanel);
			this.setVisible(true);
		}
	}
	
	public Color getColor(char type) {
		switch(type) {
			case 'O' : return new Color(255, 255, 0);
			case 'L' : return new Color(255, 116, 0);
			case 'J' : return new Color(0, 0, 255);
			case 'I' : return new Color(0, 255, 255);
			case 'Z' : return new Color(255, 0, 0);
			case 'S' : return new Color(0, 255, 0);
			case 'T' : return new Color(255, 0, 255);
			case 'V' : return new Color(176, 0, 255);
			case '-' : return new Color(58, 146, 98);
			case '.' : return new Color(128, 128, 128);
			default : return null;
		}
	}
	public ImageIcon getItemIcon(int type) {
		switch(type) {
			case 1: return Settings.Item1ImgIcon;
			case 2: return Settings.Item2ImgIcon;
			case 3: return Settings.Item3ImgIcon;
			default: return null;
		}
	}
	//아이템 보내기
	public void sendItem(int n) {
		UserMessage data = new UserMessage(WaitingPanel.userName, "403");
		data.setItem(n);
		WaitingPanel.SendMessage(data);
	}
	//상대방의 아이템 상태 업데이트 하기
	public void updateRivalStatus(boolean item2, boolean item3) {
		int num = -1;
		this.rival = waitingPanel.getRival();
		for(int i=0; i<2; i++) {
			if(rival.equals(WaitingPanel.playerList[i])) num = i;
		}
		if(num > -1) {
			for(int i=0; i<10; i++) {
				for(int j=0; j<20; j++) {
					gamePanel.drawRivalBlock(num, i, j, rivalStatus[i][j], getColor(rivalStatus[i][j]));
				}
			}
		}
		if(item2) gamePanel.rivalItemBox[num][0].setIcon(Settings.Item2ImgIcon);
		else gamePanel.rivalItemBox[num][0].setIcon(null);
		if(item3) gamePanel.rivalItemBox[num][1].setIcon(Settings.Item3ImgIcon);
		else gamePanel.rivalItemBox[num][1].setIcon(null);
		
		gamePanel.repaint();
	}
	
	class GameModeThread extends Thread{
		public void run() {
			while (true) {
				if (isChange) {
					isChange = false;
					gameModeChanged();
				}
				if(player1Dead && player2Dead) {
					
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
	//1초마다 자신 상태를 상대방에게 보내는 스레드
		class SendStatus extends Thread {
			public void run() {
				while(true) {
					if(isDead || !gameStart) return;
					if(isFirst == true) {
						try {
							sleep(200);
						} catch(InterruptedException e) { return; }
						isFirst = false;
					}
					if(isDead || !gameStart) return;
					
					sendStatusToRival();
					
					if(isDead || !gameStart) return;
					try {
						sleep(1000);
					} catch(InterruptedException e) { return; }
					if(isDead || !gameStart) return;
				}			
			}
		}
	// 랜덤으로 블록을 생성하고 떨어트리는 스레드 바닦에 떨어지면 반복이 종료되고 다시 생성하고 떨어트림
	class GameProcessThread extends Thread {
		int count = 0;
		Random random;

		public GameProcessThread() {
			random = new Random();
		}

		public void run() {
			while (true) {
				if (!gameStart)
					return;
				if (isDead)
					break;
				int tmp;

				if (count == 7) {
					for (int i = 0; i < 9; i++)
						alreadySet[i] = 0;
					count = 0;
				}
				while (true) {
					tmp = random.nextInt(9);
					if (alreadySet[tmp] == 0) {
						alreadySet[tmp] = 1;
						count++;
						break;
					}
				}
				currentBlockNumber = randomNumberArray[0];
				for (int i = 0; i < randomNumberArray.length - 1; i++) {
					randomNumberArray[i] = randomNumberArray[i + 1];
				}
				randomNumberArray[randomNumberArray.length - 1] = tmp;

				for (int i = 0; i < randomNumberArray.length; i++) {
					gamePanel.drawhintBox(i, blockType[randomNumberArray[i]],
							getColor(blockType[randomNumberArray[i]]));
				}

				createBlock();
				fallBlock();
			}
		}

		public void createBlock() {
			centerX = 4;
			centerY = 20;
			if (currentBlockNumber == 0 || currentBlockNumber == 7)
				centerY = 19;

			gamePanel.drawEntireBlock(centerX, centerY, blockType[currentBlockNumber],
					getColor(blockType[currentBlockNumber]), "CurrentFall");
		}

		public void fallBlock() {
			boolean flag = false;
			if (isDead || !gameStart)
				return;
			while (true) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					return;
				}
				if (isDead || !gameStart)
					return;
				synchronized (this) {
					fallBlockLength = 0;

					for (int i = 0; i < 23; i++) {
						for (int j = 0; j < 10; j++) {
							if (gamePanel.getBlockStatus(j, i).equals("CurrentFall")) {
								fallBlocks[fallBlockLength][0] = j;
								fallBlocks[fallBlockLength][1] = i;
								fallBlockLength++;
							}
						}
					}
					if (fallBlockLength <= 0) {
						checkLine();
						break;
					}

					for (int i = 0; i < fallBlockLength; i++) {
						if (fallBlocks[i][1] == 0) {
							for (int k = 0; k < fallBlockLength; k++) {
								gamePanel.box[fallBlocks[k][0]][fallBlocks[k][1]].setStatus("AlreadySet");
							}
							flag = true;
							break;
						} else if (gamePanel.getBlockStatus(fallBlocks[i][0], fallBlocks[i][1] - 1)
								.equals("AlreadySet")) {
							for (int k = 0; k < fallBlockLength; k++) {
								gamePanel.box[fallBlocks[k][0]][fallBlocks[k][1]].setStatus("AlreadySet");
							}
							flag = true;
							break;
						}
					}
					if (flag == true) {
						checkLine();
						break;
					}

					for (int k = 0; k < fallBlockLength; k++) {
						gamePanel.drawBlock(fallBlocks[k][0], fallBlocks[k][1], ' ', null, "Empty");
					}
					for (int k = 0; k < fallBlockLength; k++) {
						gamePanel.drawBlock(fallBlocks[k][0], fallBlocks[k][1] - 1, blockType[currentBlockNumber],
								getColor(blockType[currentBlockNumber]), "CurrentFall");
					}
					centerY--;

					gamePanel.repaint();

				}
			}
			if (countAttackFromRival > 0) {
				attackFromRival(countAttackFromRival);
				countAttackFromRival = 0;
			}

		}

	}
	//제거 라인 체크하는 함수 라인 제거 시 상대방에게 공격이 가능하고 일정 라인 제거시 아이템을 쓸 수 있게된다
		public void checkLine() {
			for(int i=0; i<10; i++) {
				if(gamePanel.box[i][19].getStatus().equals("AlreadySet")) {
					isDead = true;
					WaitingPanel.SendMessage(new UserMessage(WaitingPanel.userName, "405"));
					for(int j=0; j<10; j++) {
						for(int k=0; k<23; k++) {
							if(gamePanel.box[j][k].getStatus().equals("AlreadySet")) {
								gamePanel.drawBlock(j, k, '.', getColor('.'), "AlreadySet");
							}
						}
					}
					return;
				}
			}
			
			for(int i=0; i<20; i++) {
				for(int j=0; j<10; j++) {
					if(!gamePanel.box[j][i].getStatus().equals("AlreadySet")) break;
					if(j==9) {
						clearLine(i--);
						countAttackLine++;
						removeLine++;
					}
				}
			}
			
			if(removeLine >= 2) {
				if(currentItem == 0) setItem();
				removeLine -= 2;
			}
			
			if(countAttackLine >= 2) {
				UserMessage data = new UserMessage(WaitingPanel.userName, "402");
				data.setAttackLines(countAttackLine -1);
				countAttackLine = 0;
				WaitingPanel.SendMessage(data);
			}
			gamePanel.repaint();
		}

		//라인 제거 함수
		public void clearLine(int line) {
			for(int i=line; i<19; i++) {
				for(int j=0; j<10; j++) {
					gamePanel.drawBlock(j, i, gamePanel.box[j][i+1].getType(), 
							getColor(gamePanel.box[j][i+1].getType()), 
							gamePanel.box[j][i+1].getStatus());
				}
			}
			for(int i=0; i<10; i++) {
				gamePanel.drawBlock(i, 19, ' ', null, "Empty");
			}
			
		}
		
		//아이템을 얻는 함수
		public void setItem() {
			Random r = new Random();
			r.nextInt(3);
			currentItem = r.nextInt(3)+1;
			gamePanel.itemBox.setIcon(getItemIcon(currentItem));
		}
		//자신의 블록 상태 보내기
		public void sendStatusToRival() {
			UserMessage data = new UserMessage(WaitingPanel.userName, "401");
			char[][] blockStatus = data.getBlockStatus();
			for(int i=0; i<10; i++) {
				for(int j=0; j<20; j++) {
					blockStatus[i][j] = gamePanel.box[i][j].getType();
				}
			}
			if(speed == 1000) data.getItemStatus()[0] = false;
			else data.getItemStatus()[0] = true;
			
			if(spinable) data.getItemStatus()[1] = false;
			else data.getItemStatus()[1] = true;
			WaitingPanel.SendMessage(data);
		}
		//상대방에게 공격 받는 함수 공격 받은 만큼 블럭들이 위로 올라간다
		public void attackFromRival(int lines) {
			for(int i=19; i>=lines; i--) {
				for(int j=0; j<10; j++) {
					if(!gamePanel.box[j][i-lines].getStatus().equals("CurrentFall") || !gamePanel.box[j][i].getStatus().equals("CurrentFall"))
						gamePanel.drawBlock(j, i, 
							gamePanel.box[j][i-lines].getType(), 
							getColor(gamePanel.box[j][i-lines].getType()),
							gamePanel.box[j][i-lines].getStatus());
				}
			}
			
			for(int i=0; i<lines; i++) {
				Random r = new Random();
				int n = r.nextInt(10);
				for(int j=0; j<10; j++) {
					if(n!=j)
						gamePanel.drawBlock(j, i, '.', getColor('.'), "AlreadySet");
					else
						gamePanel.drawBlock(j, i, ' ', null, "Empty");
				}
			}
				
		}
		public ImageIcon getEmoji(int type) {
			switch(type) {
				case 1: return Settings.emoji1;
				case 2: return Settings.emoji2;
				case 3: return Settings.emoji3;
				default: return null;
			}
		}
		
		//자신 혹은 상대방의 이모티콘 업데이트
		public void showEmoticon(int player, int type) {
			if(player == -1) {
				gamePanel.myEmoticon.setIcon(getEmoji(type));
			}
			else {
				gamePanel.rivalEmoticon[player].setIcon(getEmoji(type));
			}
			gamePanel.repaint();
		}
		// 이모티콘 보내기
		public void sendEmoji(int n) {
			UserMessage data = new UserMessage(WaitingPanel.userName, "404");
			data.setEmoji(n);
			WaitingPanel.SendMessage(data);
			
			showEmoticon(-1, n);
		}
		
		//상하좌후키, 스페이스바 - 움직이기
		//ZXC키 - 이모티콘 보내기
		//Shift키 - 아이템 사용
		class MyKeyListener extends KeyAdapter {
			public void keyPressed(KeyEvent e) {
				if (isDead) return;
				if(!gameStart) return;
				
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					if(keyReady) new MoveThread(UP).start();
				}
				else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					if(keyReady) new MoveThread(DOWN).start();
				}
				else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					if(keyReady) new MoveThread(LEFT).start();
				}
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					if(keyReady) new MoveThread(RIGHT).start();
				}
				else if (e.getKeyCode() == KeyEvent.VK_UP && spinable==true) {
					if(keyReady) new MoveThread(SPACE).start();
				}
			}
			
			public void keyReleased(KeyEvent e) {
				if(!gameStart) return;
				
				if (e.getKeyCode() == KeyEvent.VK_Z) {
					sendEmoji(1);
				}
				else if (e.getKeyCode() == KeyEvent.VK_X) {
					sendEmoji(2);
				}
				else if (e.getKeyCode() == KeyEvent.VK_C) {
					sendEmoji(3);
				}
				else if (e.getKeyCode() == KeyEvent.VK_SHIFT && !isDead) {
					if(currentItem != 0) {
						sendItem(currentItem);
						gamePanel.itemBox.setIcon(null);
						currentItem = 0;
					}
				}
			}
		}
		
		//움직이는 스레드 키입력 쿨타임을 위해 스레드로 설정
		class MoveThread extends Thread {
			int key;
			
			public MoveThread(int key) {
				this.key=key;
			}
			
			public void run() {
				if(!gameStart) return;
				keyReady = false;
				synchronized(this) {
					moveBlock();
				}
				try {
					sleep(50);
				} catch(InterruptedException e) { return; }
				keyReady = true;
			}
			
			public void moveBlock() {	
				fallBlockLength = 0;
				for(int i = 0; i < 23; i++) {
					for(int j = 0; j < 10; j++) {
						if(gamePanel.getBlockStatus(j, i).equals("CurrentFall")) {
							fallBlocks[fallBlockLength][0] = j;
							fallBlocks[fallBlockLength][1] = i;
							fallBlockLength++;
						}
					}
				}
					
				if(key == UP) {
					for(int k = 0; k < fallBlockLength; k++) {
						gamePanel.drawBlock(fallBlocks[k][0], fallBlocks[k][1],' ', null, "Empty");
					}
					while(true) {
						boolean flag = false;
						for(int i=0; i<fallBlockLength; i++) {
							if (fallBlocks[i][1] == 0) {
								flag = true;
								break;
							}
							if(gamePanel.getBlockStatus(fallBlocks[i][0], fallBlocks[i][1]-1).equals("AlreadySet")) {
								flag = true;
								break;
							}
						}
						if(flag) break;
						for(int j=0; j<fallBlockLength; j++) {
							fallBlocks[j][1]--;
						}
						centerY--;
					}
					for(int k = 0; k < fallBlockLength; k++) {
						gamePanel.drawBlock(fallBlocks[k][0], fallBlocks[k][1], blockType[currentBlockNumber], getColor(blockType[currentBlockNumber]), "CurrentFall");
					}
					checkLine();
				}
				
				if(key == DOWN) {
					for(int i=0; i<fallBlockLength; i++) {
						if (fallBlocks[i][1] == 0) {
							for(int k = 0; k < fallBlockLength; k++) {
								gamePanel.box[fallBlocks[k][0]][fallBlocks[k][1]].setStatus("AlreadySet");
							}
							return;
						}
						if(gamePanel.getBlockStatus(fallBlocks[i][0], fallBlocks[i][1]-1).equals("AlreadySet")) {
							for(int k = 0; k < fallBlockLength; k++) {
								gamePanel.box[fallBlocks[k][0]][fallBlocks[k][1]].setStatus("AlreadySet");
							}
							return;
						}
					}
					for(int k = 0; k < fallBlockLength; k++) {
						gamePanel.drawBlock(fallBlocks[k][0], fallBlocks[k][1],' ', null, "Empty");
					}
					for(int k = 0; k < fallBlockLength; k++) {
						gamePanel.drawBlock(fallBlocks[k][0], fallBlocks[k][1]-1, blockType[currentBlockNumber], getColor(blockType[currentBlockNumber]), "CurrentFall");
					}
					centerY--;
				}
				
				if(key == LEFT) {
					for(int i=0; i<fallBlockLength; i++) {
						if (fallBlocks[i][0]-1 < 0) return;
						if(gamePanel.getBlockStatus(fallBlocks[i][0]-1, fallBlocks[i][1]).equals("AlreadySet")) return;
					}
					for(int k = 0; k < fallBlockLength; k++) {
						gamePanel.drawBlock(fallBlocks[k][0], fallBlocks[k][1],' ', null, "Empty");
					}
					for(int k = 0; k < fallBlockLength; k++) {
						gamePanel.drawBlock(fallBlocks[k][0]-1, fallBlocks[k][1], blockType[currentBlockNumber], getColor(blockType[currentBlockNumber]), "CurrentFall");
					}
					centerX--;
				}
				
				else if(key == RIGHT) {
					for(int i=0; i<fallBlockLength; i++) {
						if (fallBlocks[i][0]+1 > 9) return;
						if(gamePanel.getBlockStatus(fallBlocks[i][0]+1, fallBlocks[i][1]).equals("AlreadySet")) return;
					}
					for(int k = 0; k < fallBlockLength; k++) {
						gamePanel.drawBlock(fallBlocks[k][0], fallBlocks[k][1],' ', null, "Empty");
					}
					for(int k = 0; k < fallBlockLength; k++) {
						gamePanel.drawBlock(fallBlocks[k][0]+1, fallBlocks[k][1], blockType[currentBlockNumber], getColor(blockType[currentBlockNumber]), "CurrentFall");
					}
					centerX++;
				}
				
				else if(key == SPACE) {
					int f = gamePanel.rotateBlock(centerX, centerY, blockType[currentBlockNumber], getColor(blockType[currentBlockNumber]));
					if(f == 0) {
						centerX++;
					}
					else if(f == 1) {
						centerX--;
					}
					else if(f == 2) {
						centerY++;
					}
					else if(f == 3) {
						centerX += 2;
					}
					else if(f == 4) {
						centerX -= 2;
					}
					else if(f == 5) {
						centerX += 3;
					}
					else if(f == 6) {
						centerX -= 3;
					}
				}
				gamePanel.repaint();
			}
		}
}
