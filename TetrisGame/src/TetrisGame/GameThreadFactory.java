package TetrisGame;

import java.util.Random;

import utility.Settings;

// 게임 중 사용하는 스레드를 관리하는 클래스 (스레드 생성 담당)
public class GameThreadFactory {
	private TetrisGame tetrisGame;
	private GamePanel gamePanel;
	
	private int fallBlockLength; // 떨어지는 블록의 크기 3칸짜리 또는 4칸짜리
	private int centerX; // 회전중심 x축
	private int centerY; // 회전중심 y축
	private int currentBlockNumber; // 현재 블록 종류 - 랜덤에서 나온값
	private int[] alreadySet = { 0, 0, 0, 0, 0, 0, 0, 0, 0 }; // 골고루 랜덤 번호가 나오도록 1이 나오면 2번째 요소가 1로 set
	private char[] blockType = { 'O', 'L', 'J', 'I', 'Z', 'S', 'T', 'V', '-' };
	
	public GameThreadFactory(TetrisGame tetrisGame, GamePanel gamePanel){
		this.tetrisGame = tetrisGame;
		this.gamePanel = gamePanel;
	}
	
	public void makeSendStatusThread() {
		SendStatus ss = new SendStatus();
		ss.start();
	}
	// 1초마다 자신 상태를 상대방에게 보내는 스레드
	class SendStatus extends Thread {
		public void run() {
			while (true) {
				if (tetrisGame.isDead || !tetrisGame.gameStart)
					return;
				if (tetrisGame.isFirst == true) {
					try {
						sleep(200);
					} catch (InterruptedException e) {
						return;
					}
					tetrisGame.isFirst = false;
				}
				if (tetrisGame.isDead || !tetrisGame.gameStart)
					return;

				tetrisGame.sendStatusToRival();

				if (tetrisGame.isDead || !tetrisGame.gameStart)
					return;
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					return;
				}
				if (tetrisGame.isDead || !tetrisGame.gameStart)
					return;
			}
		}
	}
	
	public void makeGameProcessThread() {
		GameProcessThread gpt = new GameProcessThread();
		gpt.start();
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
				if (!tetrisGame.gameStart)
					return;
				if (tetrisGame.isDead)
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
				int[] randomNumberArray = tetrisGame.getRandomNumberArray();
				
				currentBlockNumber = randomNumberArray[0];
				for (int i = 0; i < randomNumberArray.length - 1; i++) {
					randomNumberArray[i] = randomNumberArray[i + 1];
				}
				randomNumberArray[randomNumberArray.length - 1] = tmp;

				for (int i = 0; i < randomNumberArray.length; i++) {
					gamePanel.drawhintBox(i, blockType[randomNumberArray[i]],
							tetrisGame.getColor(blockType[randomNumberArray[i]]));
				}
				tetrisGame.setRandomNumberArray(randomNumberArray);
				
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
					tetrisGame.getColor(blockType[currentBlockNumber]), "CurrentFall");
		}

		public void fallBlock() {
			boolean flag = false;
			if (tetrisGame.isDead || !tetrisGame.gameStart)
				return;
			while (true) {
				try {
					sleep(tetrisGame.speed);
				} catch (InterruptedException e) {
					return;
				}
				if (tetrisGame.isDead || !tetrisGame.gameStart)
					return;
				synchronized (this) {
					fallBlockLength = 0;
					int[][] fallBlocks = tetrisGame.getFallBlocks();
					
					for (int i = 0; i < 23; i++) {
						for (int j = 0; j < 10; j++) {
							if (gamePanel.getBlockStatus(j, i).equals("CurrentFall")) {
								fallBlocks[fallBlockLength][0] = j;
								fallBlocks[fallBlockLength][1] = i;
								fallBlockLength++;
							}
						}
					}
					tetrisGame.setFallBlocks(fallBlocks);
					if (fallBlockLength <= 0) {
						tetrisGame.checkLine();
						break;
					}
					
					fallBlocks = tetrisGame.getFallBlocks();
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
						tetrisGame.checkLine();
						break;
					}
					fallBlocks = tetrisGame.getFallBlocks();
					for (int k = 0; k < fallBlockLength; k++) {
						gamePanel.drawBlock(fallBlocks[k][0], fallBlocks[k][1], ' ', null, "Empty");
					}
					for (int k = 0; k < fallBlockLength; k++) {
						gamePanel.drawBlock(fallBlocks[k][0], fallBlocks[k][1] - 1, blockType[currentBlockNumber],
								tetrisGame.getColor(blockType[currentBlockNumber]), "CurrentFall");
					}
					centerY--;

					gamePanel.repaint();

				}
			}
			if (tetrisGame.countAttackFromRival > 0) {
				tetrisGame.attackFromRival(tetrisGame.countAttackFromRival);
				tetrisGame.countAttackFromRival = 0;
			}

		}

	}
	
	public void makeMoveThread(int key) {
		MoveThread mt = new MoveThread(key);
		mt.start();
	}
	// 움직이는 스레드 키입력 쿨타임을 위해 스레드로 설정
	class MoveThread extends Thread {
		int key;
		private static final int UP = 0;
		private static final int DOWN = 1;
		private static final int LEFT = 2;
		private static final int RIGHT = 3;
		private static final int SPACE = 4;
		
		public MoveThread(int key) {
			this.key = key;
		}

		public void run() {
			if (!tetrisGame.gameStart)
				return;

			synchronized (this) {
				moveBlock();
			}
			try {
				sleep(50);
			} catch (InterruptedException e) {
				return;
			}
		}

		public void moveBlock() {
			fallBlockLength = 0;
			int[][] fallBlocks = tetrisGame.getFallBlocks();
			for (int i = 0; i < 23; i++) {
				for (int j = 0; j < 10; j++) {
					if (gamePanel.getBlockStatus(j, i).equals("CurrentFall")) {
						fallBlocks[fallBlockLength][0] = j;
						fallBlocks[fallBlockLength][1] = i;
						fallBlockLength++;
					}
				}
			}
			tetrisGame.setFallBlocks(fallBlocks);
			fallBlocks = tetrisGame.getFallBlocks();
			if (key == SPACE) {
				for (int k = 0; k < fallBlockLength; k++) {
					gamePanel.drawBlock(fallBlocks[k][0], fallBlocks[k][1], ' ', null, "Empty");
				}
				while (true) {
					boolean flag = false;
					for (int i = 0; i < fallBlockLength; i++) {
						if (fallBlocks[i][1] == 0) {
							flag = true;
							break;
						}
						if (gamePanel.getBlockStatus(fallBlocks[i][0], fallBlocks[i][1] - 1).equals("AlreadySet")) {
							flag = true;
							break;
						}
					}
					if (flag)
						break;
					for (int j = 0; j < fallBlockLength; j++) {
						fallBlocks[j][1]--;
					}
					centerY--;
				}
				for (int k = 0; k < fallBlockLength; k++) {
					gamePanel.drawBlock(fallBlocks[k][0], fallBlocks[k][1], blockType[currentBlockNumber],
							tetrisGame.getColor(blockType[currentBlockNumber]), "CurrentFall");
				}
				tetrisGame.checkLine();
			}

			if (key == DOWN) {
				for (int i = 0; i < fallBlockLength; i++) {
					if (fallBlocks[i][1] == 0) {
						for (int k = 0; k < fallBlockLength; k++) {
							gamePanel.box[fallBlocks[k][0]][fallBlocks[k][1]].setStatus("AlreadySet");
						}
						return;
					}
					if (gamePanel.getBlockStatus(fallBlocks[i][0], fallBlocks[i][1] - 1).equals("AlreadySet")) {
						for (int k = 0; k < fallBlockLength; k++) {
							gamePanel.box[fallBlocks[k][0]][fallBlocks[k][1]].setStatus("AlreadySet");
						}
						return;
					}
				}
				for (int k = 0; k < fallBlockLength; k++) {
					gamePanel.drawBlock(fallBlocks[k][0], fallBlocks[k][1], ' ', null, "Empty");
				}
				for (int k = 0; k < fallBlockLength; k++) {
					gamePanel.drawBlock(fallBlocks[k][0], fallBlocks[k][1] - 1, blockType[currentBlockNumber],
							tetrisGame.getColor(blockType[currentBlockNumber]), "CurrentFall");
				}
				centerY--;
			}

			if (key == LEFT) {
				for (int i = 0; i < fallBlockLength; i++) {
					if (fallBlocks[i][0] - 1 < 0)
						return;
					if (gamePanel.getBlockStatus(fallBlocks[i][0] - 1, fallBlocks[i][1]).equals("AlreadySet"))
						return;
				}
				for (int k = 0; k < fallBlockLength; k++) {
					gamePanel.drawBlock(fallBlocks[k][0], fallBlocks[k][1], ' ', null, "Empty");
				}
				for (int k = 0; k < fallBlockLength; k++) {
					gamePanel.drawBlock(fallBlocks[k][0] - 1, fallBlocks[k][1], blockType[currentBlockNumber],
							tetrisGame.getColor(blockType[currentBlockNumber]), "CurrentFall");
				}
				centerX--;
			}

			else if (key == RIGHT) {
				for (int i = 0; i < fallBlockLength; i++) {
					if (fallBlocks[i][0] + 1 > 9)
						return;
					if (gamePanel.getBlockStatus(fallBlocks[i][0] + 1, fallBlocks[i][1]).equals("AlreadySet"))
						return;
				}
				for (int k = 0; k < fallBlockLength; k++) {
					gamePanel.drawBlock(fallBlocks[k][0], fallBlocks[k][1], ' ', null, "Empty");
				}
				for (int k = 0; k < fallBlockLength; k++) {
					gamePanel.drawBlock(fallBlocks[k][0] + 1, fallBlocks[k][1], blockType[currentBlockNumber],
							tetrisGame.getColor(blockType[currentBlockNumber]), "CurrentFall");
				}
				centerX++;
			}

			else if (key == UP) {
				int f = gamePanel.rotateBlock(centerX, centerY, blockType[currentBlockNumber],
						tetrisGame.getColor(blockType[currentBlockNumber]));
				if (f == 0) {
					centerX++;
				} else if (f == 1) {
					centerX--;
				} else if (f == 2) {
					centerY++;
				} else if (f == 3) {
					centerX += 2;
				} else if (f == 4) {
					centerX -= 2;
				} else if (f == 5) {
					centerX += 3;
				} else if (f == 6) {
					centerX -= 3;
				}
			}
			gamePanel.repaint();
		}
	}

	public void makeItemFromRival(int n) {
		ItemFromRival ifr = new ItemFromRival(n);
		ifr.start();
	}
	// 방해받은 아이템 지속 스레드
	class ItemFromRival extends Thread {
		int n;
		public ItemFromRival(int n) {
			this.n = n;
		}

		public void run() {
			if (n == 1) {
				gamePanel.attackFromRival.setIcon(Settings.Item1ImgIcon);
			} else if (n == 2) {
				tetrisGame.speed = 200;
				gamePanel.attackFromRival.setIcon(Settings.Item2ImgIcon);
			} else if (n == 3) {
				tetrisGame.spinable = false;
				gamePanel.attackFromRival.setIcon(Settings.Item3ImgIcon);
			}

			if (tetrisGame.isDead || !tetrisGame.gameStart)
				return;
			try {
				sleep(5000);
			} catch (InterruptedException e) {
				return;
			}
			
			if (tetrisGame.isDead || !tetrisGame.gameStart) return;
			 
			 tetrisGame.attackCount--; if (tetrisGame.attackCount > 0) return;
			  
			 if (n == 2) { tetrisGame.speed = 1000; } else if (n == 3) { tetrisGame.spinable = true; }
			 
			gamePanel.attackFromRival.setIcon(null);
		}
	}
}
