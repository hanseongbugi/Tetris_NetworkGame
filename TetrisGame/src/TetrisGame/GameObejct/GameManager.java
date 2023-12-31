package TetrisGame.GameObejct;

import java.awt.Color;
import java.util.Random;

import javax.swing.ImageIcon;

import TetrisGame.GamePanel;
import TetrisGame.TetrisGame;
import WaitingRoom.UserMessage;
import WaitingRoom.WaitingPanel;
import utility.Settings;

// 게임을 관리하는 클래스 (나의 보드 관리, 블록 움직임 관리, 라이벌 보드 관리)
public class GameManager {
	private TetrisGame tetrisGame;
	private GamePanel gamePanel;
	private WaitingPanel waitingPanel;
	
	public int countAttackLine = 0; // 공격할 라인 수
	public int removeLine = 0; // 제거한 줄 수 - 일정줄 제거시 공격/아이템생성
	public int speed = 1000; // 떨어지는 속도
	
	// 보드들의 기본 배경색
	public Color defaultColor1 = new Color(250, 210, 250);
	public Color defaultColor2 = new Color(250, 205, 185);
	public Color defaultColor3 = new Color(250, 250, 210);

	public GameManager(TetrisGame tetrisGame, WaitingPanel waitingPanel) {
		this.tetrisGame = tetrisGame;
		this.waitingPanel = waitingPanel;
	}
	
	// 기본 배경 색을 반환
	public Color getDefaultColor(int type) {
		switch (type) {
		case 1:
			return defaultColor1;
		case 2:
			return defaultColor2;
		case 3:
			return defaultColor3;
		default:
			return null;
		}
	}
	
	// 블록 이미지를 반환
	public ImageIcon getImage(char type) {
		switch (type) {
		case 'O':
			return Settings.blockYellow;
		case 'L':
			return Settings.blockGreen;
		case 'J':
			return Settings.blockOrange;
		case 'I':
			return Settings.blockCyan;
		case 'Z':
			return Settings.blockPuple;
		case 'S':
			return Settings.blockRed;
		case 'T':
			return Settings.blockLime;
		case '.':
			return Settings.blockGray;
		default:
			return null;
		}
	}
	
	public void setGamePanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	// 블록 한 칸을 변경하는 함수 null이 넘어오면 빈 블록이 되며 배경색으로 변경
	public void drawBlock(int x, int y, char type, Object obj, String status) {
		if (obj == null)
			obj = defaultColor1;
		gamePanel.board[x][y].setBoard(type, obj, status);
	}

	// 상대방의 블록을 그리는 함수
	public void drawRivalBlock(int x, int y, char type, Object obj) {
		if (obj == null) {
			obj = defaultColor3;
		}
		gamePanel.rivalBoard[x][y].setBoard(type, obj);
	}
	
	// Item의 이미지를 반환하는 함수
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

	// 아이템을 라이벌에게 보내는 함수
	public void sendItem(int n) {
		UserMessage msg = new UserMessage(WaitingPanel.userName, "403"); // 아이템 메시지 생성
		msg.setItem(n); // 아이템 번호 저장
		WaitingPanel.SendMessage(msg); // 서버에 메시지 전송
	}

	// 상대방의 아이템 상태 업데이트 하기
	public void updateRivalStatus(boolean item1, boolean item2, boolean item3) {
		int num = -1;
		tetrisGame.setRival(waitingPanel.getRival());
		for (int i = 0; i < 2; i++) {
			if (tetrisGame.getRival().equals(WaitingPanel.playerList[i]))
				num = i;
		}
		if (num > -1) {
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 20; j++) {
					drawRivalBlock(i, j, tetrisGame.rivalStatus[i][j], getImage(tetrisGame.rivalStatus[i][j]));
				}
			}
		}
		// 아이템이 아무 것도 보내지지 않았다면
		if(!item1&&!item2&&!item3)
			gamePanel.rivalItemBox.setIcon(null);
		else { // 3개의 아이템 중 하나가 보내졌다면
			if (item1)
				gamePanel.rivalItemBox.setIcon(Settings.Item1ImgIcon);
			
			if (item2)
				gamePanel.rivalItemBox.setIcon(Settings.Item2ImgIcon);
			
			if (item3)
				gamePanel.rivalItemBox.setIcon(Settings.Item3ImgIcon);
		}
		
		gamePanel.repaint();
	}

	// 제거 라인을 체크하는 함수
	public void checkLine() {
		// 멈춰있는 블록이 보드의 가장 위까지 존재한다면 서버에 게임이 종료되었다고 알린다
		for (int i = 0; i < 10; i++) {
			if (gamePanel.board[i][19].getStatus().equals("StopBlock")) {
				tetrisGame.isDead = true;
				WaitingPanel.SendMessage(new UserMessage(WaitingPanel.userName, "405"));
				for (int j = 0; j < 10; j++) { // 서버에 알린 후 보드에 존재하는 블록을 죽은 블록으로 바꿈
					for (int k = 0; k < 23; k++) {
						if (gamePanel.board[j][k].getStatus().equals("StopBlock")) {
							drawBlock(j, k, '.', getImage('.'), "StopBlock");
						}
					}
				}
				return;
			}
		}
		
		// 멈춰있는 블록이 x 방향으로 9개 존재한다면 라인을 지운다
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 10; j++) {
				if (!gamePanel.board[j][i].getStatus().equals("StopBlock"))
					break;
				if (j == 9) {
					clearLine(i--); 
					countAttackLine++;
					removeLine++;
				}
			}
		}

		if (removeLine >= 2) { // 2줄 이상 지웠다면 아이템을 얻는다
			if (tetrisGame.getCurrentItem() == 0)
				setItem();
			removeLine -= 2;
		}
		
		// 2줄 이상 지웠다면 라이벌에게 라인 공격을 보낸다.
		if (countAttackLine >= 2) {
			UserMessage msg = new UserMessage(WaitingPanel.userName, "402"); // 라인 공격 메시지 생성
			msg.setAttackLines(countAttackLine - 1); // 라인 공격 수 저장
			countAttackLine = 0;
			WaitingPanel.SendMessage(msg); // 서버에 메시지 전송
		}
		gamePanel.repaint();
	}

	// 라인 제거 함수
	public void clearLine(int line) {
		for (int i = line; i < 19; i++) {
			for (int j = 0; j < 10; j++) {
				drawBlock(j, i, gamePanel.board[j][i + 1].getType(),
						getImage(gamePanel.board[j][i + 1].getType()), gamePanel.board[j][i + 1].getStatus());
			}
		}
		for (int i = 0; i < 10; i++) {
			drawBlock(i, 19, ' ', null, "Empty");
		}

	}

	// 아이템을 얻는 함수
	public void setItem() {
		Random r = new Random();
		r.nextInt(3); // 3종류의 아이템 중 랜덤으로 하나를 얻는다.
		tetrisGame.setCurrentItem(r.nextInt(3) + 1);
		gamePanel.itemBox.setIcon(getItemIcon(tetrisGame.getCurrentItem()));
	}

	// 자신의 블록 상태 및 아이템 상태 보내기
	public void sendStatusToRival(int sendItemNumber) {
		UserMessage msg = new UserMessage(WaitingPanel.userName, "401"); // 메시지 생성
		char[][] blockStatus = msg.getBlockStatus(); 
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 20; j++) {
				blockStatus[i][j] = gamePanel.board[i][j].getType();
			}
		}
		msg.setBlockStatus(blockStatus); // 메시지에 나의 블록 상태 저장
		
		boolean[] itemStatus = msg.getItemStatus();
	
		for(int i = 0;i<itemStatus.length;i++) {
			itemStatus[i] = false;
		}
		if(sendItemNumber!=0)
			itemStatus[sendItemNumber-1] = true;
	
		msg.setItemStatus(itemStatus); // 메시지에 나의 아이템 상태 저장
		WaitingPanel.SendMessage(msg); // 서버에 전송
	}

	// 상대방에게 공격 받는 함수 공격 받은 만큼 블럭들이 위로 올라간다
	public void attackFromRival(int lines) {
		for (int i = 19; i >= lines; i--) {
			for (int j = 0; j < 10; j++) {
				if (!gamePanel.board[j][i - lines].getStatus().equals("FallingBlock")
						|| !gamePanel.board[j][i].getStatus().equals("FallingBlock"))
					drawBlock(j, i, gamePanel.board[j][i - lines].getType(),
							getImage(gamePanel.board[j][i - lines].getType()),
							gamePanel.board[j][i - lines].getStatus());
			}
		}

		for (int i = 0; i < lines; i++) {
			Random r = new Random();
			int n = r.nextInt(10);
			for (int j = 0; j < 10; j++) {
				if (n != j)
					drawBlock(j, i, '.', getImage('.'), "StopBlock");
				else
					drawBlock(j, i, ' ', null, "Empty");
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



	// 자신 혹은 상대방의 이모티콘 업데이트
	public void showEmoji(int player, int type) {
		if (player == -1) { // 나의 경우
			gamePanel.myEmoji.setIcon(getEmoji(type));
		} else { 
			gamePanel.rivalEmoji.setIcon(getEmoji(type));
		}
		gamePanel.repaint();
	}

	// 이모티콘 보내기
	public void sendEmoji(int n) {
		UserMessage msg = new UserMessage(WaitingPanel.userName, "404"); // 이모티콘 메시지 생성
		msg.setEmoji(n); // 이모티콘 설정
		WaitingPanel.SendMessage(msg); // 메시지 전송

		showEmoji(-1, n); // 나의 이모티콘을 띄운다
	}

	// 블록을 회전 시킨 후 바뀐 회전축을 리턴하는 함수
	public int rotateBlock(int x, int y, char type, Object color) {

		int flag = -1;
		if (type == 'O') { // O 블록의 경우 회전 없음
			return flag;
		}

		else if (type == 'I') { // I 블록의 경우
			if (x == -2) {
				flag = 5;
				x += 3;
			}
			if (x == -1) {
				flag = 3;
				x += 2;
			} else if (x == 0) {
				flag = 0;
				x += 1;
			} else if (x == 8) {
				flag = 1;
				x = x - 1;
			} else if (x == 9) {
				flag = 4;
				x = x - 2;
			} else if (x == 10) {
				flag = 6;
				x = x - 3;
			} else if (y <= 1) {
				y++;
			}

			Board[] temp = new Board[16];
			temp[0] = gamePanel.board[x][y + 1];
			temp[1] = gamePanel.board[x + 1][y + 1];
			temp[2] = gamePanel.board[x + 1][y];
			temp[3] = gamePanel.board[x][y];

			temp[4] = gamePanel.board[x][y + 2];
			temp[5] = gamePanel.board[x + 1][y + 2];
			temp[6] = gamePanel.board[x + 2][y + 2];
			temp[7] = gamePanel.board[x + 2][y + 1];
			temp[8] = gamePanel.board[x + 2][y];
			temp[9] = gamePanel.board[x + 2][y - 1];
			temp[10] = gamePanel.board[x + 1][y - 1];
			temp[11] = gamePanel.board[x][y - 1];
			temp[12] = gamePanel.board[x - 1][y - 1];
			temp[13] = gamePanel.board[x - 1][y];
			temp[14] = gamePanel.board[x - 1][y + 1];
			temp[15] = gamePanel.board[x - 1][y + 2];

			String[] beforeStatus = new String[16]; // 블록의 상태를 저장
			beforeStatus[0] = gamePanel.board[x][y].getStatus();
			beforeStatus[1] = gamePanel.board[x][y + 1].getStatus();
			beforeStatus[2] = gamePanel.board[x + 1][y + 1].getStatus();
			beforeStatus[3] = gamePanel.board[x + 1][y].getStatus();

			beforeStatus[4] = gamePanel.board[x - 1][y].getStatus();
			beforeStatus[5] = gamePanel.board[x - 1][y + 1].getStatus();
			beforeStatus[6] = gamePanel.board[x - 1][y + 2].getStatus();
			beforeStatus[7] = gamePanel.board[x][y + 2].getStatus();
			beforeStatus[8] = gamePanel.board[x + 1][y + 2].getStatus();
			beforeStatus[9] = gamePanel.board[x + 2][y + 2].getStatus();
			beforeStatus[10] = gamePanel.board[x + 2][y + 1].getStatus();
			beforeStatus[11] = gamePanel.board[x + 2][y].getStatus();
			beforeStatus[12] = gamePanel.board[x + 2][y - 1].getStatus();
			beforeStatus[13] = gamePanel.board[x + 1][y - 1].getStatus();
			beforeStatus[14] = gamePanel.board[x][y - 1].getStatus();
			beforeStatus[15] = gamePanel.board[x - 1][y - 1].getStatus();

			char[] beforeType = new char[16]; // 블록의 Type을 저장
			beforeType[0] = gamePanel.board[x][y].getType();
			beforeType[1] = gamePanel.board[x][y + 1].getType();
			beforeType[2] = gamePanel.board[x + 1][y + 1].getType();
			beforeType[3] = gamePanel.board[x + 1][y].getType();

			beforeType[4] = gamePanel.board[x - 1][y].getType();
			beforeType[5] = gamePanel.board[x - 1][y + 1].getType();
			beforeType[6] = gamePanel.board[x - 1][y + 2].getType();
			beforeType[7] = gamePanel.board[x][y + 2].getType();
			beforeType[8] = gamePanel.board[x + 1][y + 2].getType();
			beforeType[9] = gamePanel.board[x + 2][y + 2].getType();
			beforeType[10] = gamePanel.board[x + 2][y + 1].getType();
			beforeType[11] = gamePanel.board[x + 2][y].getType();
			beforeType[12] = gamePanel.board[x + 2][y - 1].getType();
			beforeType[13] = gamePanel.board[x + 1][y - 1].getType();
			beforeType[14] = gamePanel.board[x][y - 1].getType();
			beforeType[15] = gamePanel.board[x - 1][y - 1].getType();

			for (int i = 0; i < 16; i++) {
				if (beforeStatus[i].equals("StopBlock")) {
					return flag;
				}
			}

			for (int i = 0; i < 16; i++) {
				if (!beforeStatus[i].equals("StopBlock")) {
					if (beforeType[i] == ' ') {
						temp[i].setBoard(beforeType[i], defaultColor1, beforeStatus[i]);
					} else
						temp[i].setBoard(beforeType[i], color, beforeStatus[i]);
				}
			}

			return flag;
		}
		else { // 이외 모든 블록의 경우
			if (x <= 0) {
				flag = 0;
				x++;
			} else if (x >= 9) {
				flag = 1;
				x--;
			} else if (y <= 1) {
				flag = 2;
				y++;
			}

			Board[] temp = new Board[8];
			temp[0] = gamePanel.board[x + 1][y + 1];
			temp[1] = gamePanel.board[x + 1][y];
			temp[2] = gamePanel.board[x + 1][y - 1];
			temp[3] = gamePanel.board[x][y - 1];
			temp[4] = gamePanel.board[x - 1][y - 1];
			temp[5] = gamePanel.board[x - 1][y];
			temp[6] = gamePanel.board[x - 1][y + 1];
			temp[7] = gamePanel.board[x][y + 1];

			String[] beforeStatus = new String[8];
			beforeStatus[0] = gamePanel.board[x - 1][y + 1].getStatus();
			beforeStatus[1] = gamePanel.board[x][y + 1].getStatus();
			beforeStatus[2] = gamePanel.board[x + 1][y + 1].getStatus();
			beforeStatus[3] = gamePanel.board[x + 1][y].getStatus();
			beforeStatus[4] = gamePanel.board[x + 1][y - 1].getStatus();
			beforeStatus[5] = gamePanel.board[x][y - 1].getStatus();
			beforeStatus[6] = gamePanel.board[x - 1][y - 1].getStatus();
			beforeStatus[7] = gamePanel.board[x - 1][y].getStatus();

			char[] beforeType = new char[8];
			beforeType[0] = gamePanel.board[x - 1][y + 1].getType();
			beforeType[1] = gamePanel.board[x][y + 1].getType();
			beforeType[2] = gamePanel.board[x + 1][y + 1].getType();
			beforeType[3] = gamePanel.board[x + 1][y].getType();
			beforeType[4] = gamePanel.board[x + 1][y - 1].getType();
			beforeType[5] = gamePanel.board[x][y - 1].getType();
			beforeType[6] = gamePanel.board[x - 1][y - 1].getType();
			beforeType[7] = gamePanel.board[x - 1][y].getType();

			for (int i = 0; i < 8; i++) {
				if (beforeStatus[i].equals("StopBlock")) {
					return -1;
				}
			}

			for (int i = 0; i < 8; i++) {
				if (!beforeStatus[i].equals("StopBlock")) {
					if (beforeType[i] == ' ')
						temp[i].setBoard(beforeType[i], defaultColor1, beforeStatus[i]);
					else
						temp[i].setBoard(beforeType[i], color, beforeStatus[i]);
				}
			}
			return flag;
		}
	}

	// 블록 생성시 그리는 함수 (테트리스 블록의 전체 형태를 보드에 저장)
	public void drawTetrisBlock(int x, int y, char type, Object color, String status) {
		if (type == 'O') {
			gamePanel.board[x + 1][y + 1].setBoard(type, color, status);
			gamePanel.board[x][y + 1].setBoard(type, color, status);
			gamePanel.board[x + 1][y].setBoard(type, color, status);
			gamePanel.board[x][y].setBoard(type, color, status);
		} else if (type == 'L') {
			gamePanel.board[x][y + 1].setBoard(type, color, status);
			gamePanel.board[x][y].setBoard(type, color, status);
			gamePanel.board[x][y - 1].setBoard(type, color, status);
			gamePanel.board[x + 1][y - 1].setBoard(type, color, status);
		} else if (type == 'J') {
			gamePanel.board[x][y - 1].setBoard(type, color, status);
			gamePanel.board[x][y + 1].setBoard(type, color, status);
			gamePanel.board[x][y].setBoard(type, color, status);
			gamePanel.board[x + 1][y + 1].setBoard(type, color, status);
		} else if (type == 'I') {
			gamePanel.board[x][y - 1].setBoard(type, color, status);
			gamePanel.board[x][y].setBoard(type, color, status);
			gamePanel.board[x][y + 1].setBoard(type, color, status);
			gamePanel.board[x][y + 2].setBoard(type, color, status);
		} else if (type == 'Z') {
			gamePanel.board[x + 1][y + 1].setBoard(type, color, status);
			gamePanel.board[x + 1][y].setBoard(type, color, status);
			gamePanel.board[x][y].setBoard(type, color, status);
			gamePanel.board[x][y - 1].setBoard(type, color, status);

		} else if (type == 'S') {
			gamePanel.board[x + 1][y - 1].setBoard(type, color, status);
			gamePanel.board[x + 1][y].setBoard(type, color, status);
			gamePanel.board[x][y].setBoard(type, color, status);
			gamePanel.board[x][y + 1].setBoard(type, color, status);
		} else if (type == 'T') {
			gamePanel.board[x][y - 1].setBoard(type, color, status);
			gamePanel.board[x][y].setBoard(type, color, status);
			gamePanel.board[x][y + 1].setBoard(type, color, status);
			gamePanel.board[x + 1][y].setBoard(type, color, status);
		} 
	}

	// 미리보기 블록 그리는 함수
	public void drawhintBoard(int n, char type, Object color) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				gamePanel.hintBoard[n][i][j].setBoard(' ', defaultColor2);
			}
		}
		if (type == 'O') {
			gamePanel.hintBoard[n][1][1].setBoard(type, color);
			gamePanel.hintBoard[n][2][1].setBoard(type, color);
			gamePanel.hintBoard[n][1][2].setBoard(type, color);
			gamePanel.hintBoard[n][2][2].setBoard(type, color);
		} else if (type == 'L') {
			gamePanel.hintBoard[n][1][3].setBoard(type, color);
			gamePanel.hintBoard[n][1][2].setBoard(type, color);
			gamePanel.hintBoard[n][2][1].setBoard(type, color);
			gamePanel.hintBoard[n][1][1].setBoard(type, color);
		} else if (type == 'J') {
			gamePanel.hintBoard[n][2][3].setBoard(type, color);
			gamePanel.hintBoard[n][1][3].setBoard(type, color);
			gamePanel.hintBoard[n][1][2].setBoard(type, color);
			gamePanel.hintBoard[n][1][1].setBoard(type, color);
		} else if (type == 'I') {
			gamePanel.hintBoard[n][2][0].setBoard(type, color);
			gamePanel.hintBoard[n][2][1].setBoard(type, color);
			gamePanel.hintBoard[n][2][2].setBoard(type, color);
			gamePanel.hintBoard[n][2][3].setBoard(type, color);
		} else if (type == 'Z') {
			gamePanel.hintBoard[n][2][3].setBoard(type, color);
			gamePanel.hintBoard[n][2][2].setBoard(type, color);
			gamePanel.hintBoard[n][1][2].setBoard(type, color);
			gamePanel.hintBoard[n][1][1].setBoard(type, color);
		} else if (type == 'S') {
			gamePanel.hintBoard[n][1][3].setBoard(type, color);
			gamePanel.hintBoard[n][1][2].setBoard(type, color);
			gamePanel.hintBoard[n][2][2].setBoard(type, color);
			gamePanel.hintBoard[n][2][1].setBoard(type, color);
		} else if (type == 'T') {
			gamePanel.hintBoard[n][1][3].setBoard(type, color);
			gamePanel.hintBoard[n][1][2].setBoard(type, color);
			gamePanel.hintBoard[n][2][2].setBoard(type, color);
			gamePanel.hintBoard[n][1][1].setBoard(type, color);
		} 
		gamePanel.repaint();
	}
}
