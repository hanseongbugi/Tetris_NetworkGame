package TetrisGame;

import java.util.Random;

import TetrisGame.GameObejct.GameManager;
import utility.Settings;

// 게임 중 사용하는 스레드를 관리하는 클래스 (스레드 생성 담당)
public class GameThreadFactory {
	private TetrisGame tetrisGame;
	private GamePanel gamePanel;
	private GameManager gameManager;
	
	private int fallBlockLength; // 떨어지는 블록의 크기 3칸짜리 또는 4칸짜리
	private int centerX; // 회전중심 x축
	private int centerY; // 회전중심 y축
	private int currentBlockNumber; // 현재 블록 종류 - 랜덤에서 나온값
	private int[] randomNumberHelper = { 0, 0, 0, 0, 0, 0, 0, 0, 0 }; // 골고루 랜덤 번호가 나오도록 1이 나오면 2번째 요소가 1로 set
	private char[] blockType = { 'O', 'L', 'J', 'I', 'Z', 'S', 'T', 'V', '-' }; // 블록 형태 저장
	private int [] blockNumberArray; // 블록 5개를 저장할 배열
	private int sendItemNumber = 0;
	
	public GameThreadFactory(TetrisGame tetrisGame, GamePanel gamePanel, GameManager gameManager){
		this.tetrisGame = tetrisGame;
		this.gamePanel = gamePanel;
		this.gameManager = gameManager;
		initGameStateArray();
	}
	
	// 랜덤 수를 생성하여 초기 다음 블록 5개 결정
	public void initGameStateArray() {
		Random random = new Random();
		random.nextInt(9);
		blockNumberArray = new int[5]; 
		for(int i = 0; i<blockNumberArray.length; i++) {
			blockNumberArray[i] = random.nextInt(9);
			gameManager.drawhintBoard(i, blockType[blockNumberArray[i]], gameManager.getImage(blockType[blockNumberArray[i]]));
		}
	}
	
	// SendStatus 스레드를 생성하여 시작하는 함수
	public void makeSendStatusThread() {
		SendStatus ss = new SendStatus();
		ss.start();
	}
	
	// 1초마다 자신 상태를 상대방에게 보내는 스레드
	class SendStatus extends Thread {
		// 게임 메니저에게 게임 상태를 보낸다.
		@Override
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

				gameManager.sendStatusToRival(sendItemNumber);

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
	
	// GameProcessThread를 생성하고 시작하는 함수
	public void makeGameProcessThread() {
		GameProcessThread gpt = new GameProcessThread();
		gpt.start();
	}

	// 랜덤으로 블록을 생성하고 떨어트리는 스레드. 바닥에 떨어지면 반복이 종료되고 다시 생성하고 떨어트림
	class GameProcessThread extends Thread {
		int count = 0;
		Random random;
		
		public GameProcessThread() {
			random = new Random();
		}
		
		@Override
		public void run() {
			while (true) {
				if (!tetrisGame.gameStart)
					return;
				if (tetrisGame.isDead)
					break;
				int tmp;

				if (count == 7) { // 7종류의 블록을 모두 생성하였다면
					for (int i = 0; i < 9; i++)
						randomNumberHelper[i] = 0; // Helper 배열을 초기화
					count = 0;
				}
				while (true) {
					tmp = random.nextInt(9); // 랜덤 수 생성
					if (randomNumberHelper[tmp] == 0) { // 생성하지 않았던 블록이면
						randomNumberHelper[tmp] = 1; // 생성 표시
						count++; // 블록 생성 횟수 증가
						break;
					}
				}
				
				currentBlockNumber = blockNumberArray[0]; // 현재 블록 번호 가져옴
				
				// 현재 블록을 블록 저장 배열에서 제거
				for (int i = 0; i < blockNumberArray.length - 1; i++) {
					blockNumberArray[i] = blockNumberArray[i + 1];
				}
				// 랜덤으로 생성한 블록 번호를 가장 끝에다 저장
				blockNumberArray[blockNumberArray.length - 1] = tmp;
				
				// 다음에 생성될 5개의 블록을 힌트 보드에 그림
				for (int i = 0; i < blockNumberArray.length; i++) {
					gameManager.drawhintBoard(i, blockType[blockNumberArray[i]],
							gameManager.getImage(blockType[blockNumberArray[i]]));
				}
				
				createBlock(); // 현재 블록을 보드에 생성
				fallBlock(); // 블록을 떨어 뜨린다.
			}
		}

		public void createBlock() {
			centerX = 4; // 블록의 중심 좌표 설정
			centerY = 20;
			if (currentBlockNumber == 0 || currentBlockNumber == 7)
				centerY = 19; // O 블록이나 V 블록인 경우 y 좌표 수정 
			
			// 설정한 좌표와 현태 블록의 타입, 블록 색, 상태를 보드에 저장
			gameManager.drawTetrisBlock(centerX, centerY, blockType[currentBlockNumber],
					gameManager.getImage(blockType[currentBlockNumber]), "FallingBlock");
		}

		// 정해진 speed에 따라ㅏ 블록을 떨어뜨린다. 
		public void fallBlock() {
			boolean flag = false;
			if (tetrisGame.isDead || !tetrisGame.gameStart)
				return;
			while (true) {
				try {
					sleep(gameManager.speed);
				} catch (InterruptedException e) {
					return;
				}
				if (tetrisGame.isDead || !tetrisGame.gameStart)
					return;
				synchronized (this) {
					fallBlockLength = 0;
					int[][] fallBlocks = tetrisGame.getFallBlocks();
					
					// Falling 상태인 블록을 찾아 좌표 설정
					for (int i = 0; i < 23; i++) {
						for (int j = 0; j < 10; j++) {
							if (gamePanel.getBlockStatus(j, i).equals("FallingBlock")) {
								fallBlocks[fallBlockLength][0] = j;
								fallBlocks[fallBlockLength][1] = i;
								fallBlockLength++;
							}
						}
					}
					tetrisGame.setFallBlocks(fallBlocks);
					if (fallBlockLength <= 0) { // 떨어지는 블록을 찾지 못했다면
						gameManager.checkLine(); // 라인확인
						break;
					}
					
					fallBlocks = tetrisGame.getFallBlocks();
					
					// 떨어지는 블록이 더이상 떨어질 수 없는 상태인지 확인
					for (int i = 0; i < fallBlockLength; i++) {
						if (fallBlocks[i][1] == 0) {
							for (int k = 0; k < fallBlockLength; k++) {
								gamePanel.board[fallBlocks[k][0]][fallBlocks[k][1]].setStatus("StopBlock");
							}
							flag = true;
							break;
						} else if (gamePanel.getBlockStatus(fallBlocks[i][0], fallBlocks[i][1] - 1)
								.equals("StopBlock")) {
							for (int k = 0; k < fallBlockLength; k++) {
								gamePanel.board[fallBlocks[k][0]][fallBlocks[k][1]].setStatus("StopBlock");
							}
							flag = true;
							break;
						}
					}
					if (flag == true) {
						gameManager.checkLine(); // 블록이 멈추면 라인체크
						break;
					}
					fallBlocks = tetrisGame.getFallBlocks();
					// 기존에 블록이 있던 자리는 보드 색으로 변경
					for (int k = 0; k < fallBlockLength; k++) {
						gameManager.drawBlock(fallBlocks[k][0], fallBlocks[k][1], ' ', null, "Empty");
					}
					// 떨어질 블록을 다시 그림
					for (int k = 0; k < fallBlockLength; k++) {
						gameManager.drawBlock(fallBlocks[k][0], fallBlocks[k][1] - 1, blockType[currentBlockNumber],
								gameManager.getImage(blockType[currentBlockNumber]), "FallingBlock");
					}
					centerY--;

					gamePanel.repaint();

				}
			}
			// 라이벌이 공격한 경우
			if (tetrisGame.countAttackFromRival > 0) {
				// 공격 수 만큼 공격을 당한다.
				gameManager.attackFromRival(tetrisGame.countAttackFromRival);
				tetrisGame.countAttackFromRival = 0;
			}

		}

	}
	
	// MoveThread 스레드를 생성하고 시작하는 함수
	public void makeMoveThread(int key) {
		MoveThread mt = new MoveThread(key);
		mt.start();
	}
	
	// 키 입력을 처리하는 스레드
	class MoveThread extends Thread {
		int key;
		private static final int UP = 0;
		private static final int DOWN = 1;
		private static final int LEFT = 2;
		private static final int RIGHT = 3;
		private static final int SPACE = 4;
		
		public MoveThread(int key) {
			this.key = key; // 생성 시 인자로 들어온 값이 입력 키 값
		}

		public void run() {
			if (!tetrisGame.gameStart)
				return;

			synchronized (this) {
				tetrisGame.keySleep = true; // 스레드가 종료되기 전까지 키 입력을 받지 않도록 플레그 설정 (보드 좌표 문제)
				moveBlock(); // 키 값에 따라 블록을 움직임
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
			
			// 떨어지는 블록을 찾아 좌표 설정
			for (int i = 0; i < 23; i++) {
				for (int j = 0; j < 10; j++) {
					if (gamePanel.getBlockStatus(j, i).equals("FallingBlock")) {
						fallBlocks[fallBlockLength][0] = j;
						fallBlocks[fallBlockLength][1] = i;
						fallBlockLength++;
					}
				}
			}
			
			tetrisGame.setFallBlocks(fallBlocks);
			fallBlocks = tetrisGame.getFallBlocks();
			
			if (key == SPACE) { // 스페이스 키를 누른 경우 (하드 드랍) 
				// 기존 위치는 원래 보드 색으로 칠함
				for (int k = 0; k < fallBlockLength; k++) {
					gameManager.drawBlock(fallBlocks[k][0], fallBlocks[k][1], ' ', null, "Empty");
				}
				while (true) {
					boolean flag = false;
					// 블록이 끝까지 떨어졌다면 반복 종료
					for (int i = 0; i < fallBlockLength; i++) {
						if (fallBlocks[i][1] == 0) {
							flag = true;
							break;
						}
						if (gamePanel.getBlockStatus(fallBlocks[i][0], fallBlocks[i][1] - 1).equals("StopBlock")) {
							flag = true;
							break;
						}
					}
					if (flag)
						break;
					// 블록 좌표를 내린다.
					for (int j = 0; j < fallBlockLength; j++) {
						fallBlocks[j][1]--;
					}
					centerY--;
				}
				// 떨어지고 있는 블록을 그린다.
				for (int k = 0; k < fallBlockLength; k++) {
					gameManager.drawBlock(fallBlocks[k][0], fallBlocks[k][1], blockType[currentBlockNumber],
							gameManager.getImage(blockType[currentBlockNumber]), "FallingBlock");
				}
				gameManager.checkLine(); // 라인 검사
			}

			if (key == DOWN) { // 아래 방향키를 누른 경우 (소프트 드랍)
				// 블록이 끝까지 떨어졌다면 함수 종료
				for (int i = 0; i < fallBlockLength; i++) {
					if (fallBlocks[i][1] == 0) {
						for (int k = 0; k < fallBlockLength; k++) {
							gamePanel.board[fallBlocks[k][0]][fallBlocks[k][1]].setStatus("StopBlock");
						}
						return;
					}
					if (gamePanel.getBlockStatus(fallBlocks[i][0], fallBlocks[i][1] - 1).equals("StopBlock")) {
						for (int k = 0; k < fallBlockLength; k++) {
							gamePanel.board[fallBlocks[k][0]][fallBlocks[k][1]].setStatus("StopBlock");
						}
						return;
					}
				}
				// 기존에 블록이 있던 위치는 보드 색으로 바꿈
				for (int k = 0; k < fallBlockLength; k++) {
					gameManager.drawBlock(fallBlocks[k][0], fallBlocks[k][1], ' ', null, "Empty");
				}
				// 블록을 한칸 내린다.
				for (int k = 0; k < fallBlockLength; k++) {
					gameManager.drawBlock(fallBlocks[k][0], fallBlocks[k][1] - 1, blockType[currentBlockNumber],
							gameManager.getImage(blockType[currentBlockNumber]), "FallingBlock");
				}
				centerY--;
			}
			
			if (key == LEFT) { // 왼쪽 방향키를 누른 경우 (왼쪽 이동)
				for (int i = 0; i < fallBlockLength; i++) {
					// 블록이 멈췄다면 함수 종료
					if (fallBlocks[i][0] - 1 < 0)
						return;
					if (gamePanel.getBlockStatus(fallBlocks[i][0] - 1, fallBlocks[i][1]).equals("StopBlock"))
						return;
				}
				// 기존에 블록이 있던 위치는 보드 색으로 변경
				for (int k = 0; k < fallBlockLength; k++) {
					gameManager.drawBlock(fallBlocks[k][0], fallBlocks[k][1], ' ', null, "Empty");
				}
				// 블록을 왼쪽으로 한칸 옮긴다
				for (int k = 0; k < fallBlockLength; k++) {
					gameManager.drawBlock(fallBlocks[k][0] - 1, fallBlocks[k][1], blockType[currentBlockNumber],
							gameManager.getImage(blockType[currentBlockNumber]), "FallingBlock");
				}
				centerX--;
			}

			else if (key == RIGHT) { // 오른쪽 방향키를 누른 경우 (오른쪽 이동)
				// 블록이 멈췄다면 함수 종료
				for (int i = 0; i < fallBlockLength; i++) {
					if (fallBlocks[i][0] + 1 > 9)
						return;
					if (gamePanel.getBlockStatus(fallBlocks[i][0] + 1, fallBlocks[i][1]).equals("StopBlock"))
						return;
				}
				// 기존에 블록이 있던 위치는 보드 색으로 변경
				for (int k = 0; k < fallBlockLength; k++) {
					gameManager.drawBlock(fallBlocks[k][0], fallBlocks[k][1], ' ', null, "Empty");
				}
				// 블록을 오른쪽으로 한칸 옮긴다
				for (int k = 0; k < fallBlockLength; k++) {
					gameManager.drawBlock(fallBlocks[k][0] + 1, fallBlocks[k][1], blockType[currentBlockNumber],
							gameManager.getImage(blockType[currentBlockNumber]), "FallingBlock");
				}
				centerX++;
			}

			else if (key == UP) { // 위쪽 방향키를 누른 경우 (회전)
				// 블록을 회전 시킨다
				int f = gameManager.rotateBlock(centerX, centerY, blockType[currentBlockNumber],
						gameManager.getImage(blockType[currentBlockNumber]));
				
				// 바뀐 회전축에 따라 중심 좌표 변경
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

	// ItemFromRival 스레드를 생성하고 시작하는 함수
	public void makeItemFromRival(int n) {
		ItemFromRival ifr = new ItemFromRival(n);
		ifr.start();
	}
	
	// 방해받은 아이템 효과를 발생 시키는 스레드
	class ItemFromRival extends Thread {
		int n;
		public ItemFromRival(int n) {
			this.n = n; // 생성 시 전달되는 인자가 아이템의 타입
			sendItemNumber = n;
		}

		public void run() {
			if (n == 1) { 
				gamePanel.attackFromRival.setIcon(Settings.Item1ImgIcon);
			} else if (n == 2) { // 떨어지는 속도를 증가 시킨다
				gameManager.speed = 200;
				gamePanel.attackFromRival.setIcon(Settings.Item2ImgIcon);
			} else if (n == 3) { // 블록을 회전하지 못하게 함
				tetrisGame.spinable = false;
				gamePanel.attackFromRival.setIcon(Settings.Item3ImgIcon);
			}

			if (tetrisGame.isDead || !tetrisGame.gameStart)
				return;
			try {
				sleep(5000); // 5초간 효과 지속
			} catch (InterruptedException e) {
				return;
			}
			
			if (tetrisGame.isDead || !tetrisGame.gameStart) return;
			 
			 tetrisGame.attackCount--; if (tetrisGame.attackCount > 0) return;
			  
			 // 변경시킨 상태 값을 원래대로 변경
			 if (n == 2) { gameManager.speed = 1000; } else if (n == 3) { tetrisGame.spinable = true; }
			 
			gamePanel.attackFromRival.setIcon(null);
			sendItemNumber = 0;
		}
	}
}
