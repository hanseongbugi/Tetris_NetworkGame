package TetrisGame;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import TetrisGame.GameObejct.Board;
import TetrisGame.GameObejct.SmallBoard;

// 테트리스 게임을 그리는 패널
public class GamePanel extends JPanel {
	
	private LineBorder border = new LineBorder(Color.WHITE);
	private LineBorder border2 = new LineBorder(Color.LIGHT_GRAY, 1);
	public Board [][] board = new Board[10][23]; //자신 플레이 판
	public SmallBoard [][] rivalBoard = new SmallBoard[10][20]; //상대방 판
	public SmallBoard [][][] hintBoard = new SmallBoard[5][4][5]; //미리보기
	
	public JLabel itemBox; //나의 아이템 박스
	public JLabel networkStatusBox; //상대방 연결상태 박스
	public JLabel nameBox; //상대방 이름 박스
	public JLabel [] rivalItemBox = new JLabel[2]; //상대방이 받은 아이템 박스
	public JLabel attackFromRival; //방해받은 아이템 박스
	
	public JLabel myEmoticon; //내 이모티콘
	public JLabel rivalEmoticon; //상대방 이모티콘
	
	// 상자들의 기본 배경색
	public Color defaultColor1 = new Color(250, 210, 250);
	public Color defaultColor2 = new Color(250, 205, 185);
	public Color defaultColor3 = new Color(250, 250, 210);
	public Color defaultColor4 = new Color(210, 250, 220);
	public Color defaultColor5 = new Color(210, 210, 250);

	public GamePanel() {
		setLayout(null);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		createBoard();
	}

	// 기본 틀을 그리는 함수
	// 자신의 게임판, 힌트판, 아이템상자, 이모티콘 상자, 상대방의 게임판, 상대방의 상태판, 상대방의 이모티콘 등
	public void createBoard() {
		for(int i=0; i<10; i++) {
			for(int j=0; j<23; j++) {
				board[i][j] = new Board();
				if(j>19) board[i][j].labelbox.setBounds(60 + 20*i, 478 - 20*j, 20, 20);
				else board[i][j].labelbox.setBounds(60 + 20*i, 480 - 20*j, 20, 20);
				board[i][j].labelbox.setBackground(defaultColor1);
				board[i][j].labelbox.setOpaque(true);
				if(j>19) board[i][j].labelbox.setBorder(border2);	
				else board[i][j].labelbox.setBorder(border);
				this.add(board[i][j].labelbox);
			}
		}
		
		for(int i=0; i<10; i++) {
			for(int j=0; j<20; j++) {
				rivalBoard[i][j] = new SmallBoard();
				rivalBoard[i][j].labelbox.setBounds(400 + 10*i, 260 - 10 * j, 10, 10);
				
				rivalBoard[i][j].labelbox.setBackground(defaultColor3);
				
				rivalBoard[i][j].labelbox.setOpaque(true);
				rivalBoard[i][j].labelbox.setBorder(border);
				this.add(rivalBoard[i][j].labelbox);
							
			}
		}
			
		for(int i=0; i<5; i++) {
			for(int j=0; j<4; j++) {
				for(int k=0; k<5; k++) {
					hintBoard[i][j][k] = new SmallBoard();
					hintBoard[i][j][k].labelbox.setBounds(270 + 10*j, 140 - 10*k + i*50, 10, 10);
					hintBoard[i][j][k].labelbox.setBackground(defaultColor2);
					hintBoard[i][j][k].labelbox.setOpaque(true);
					hintBoard[i][j][k].labelbox.setBorder(border);
					this.add(hintBoard[i][j][k].labelbox);
				}
			}
		}
		
		JTextArea explainBox = new JTextArea();
		explainBox.setBounds(400, 390, 300, 110);
		explainBox.setBackground(defaultColor2);
		explainBox.setOpaque(true);
		explainBox.setBorder(border);
		explainBox.setText(" ← → : 좌우 이동\n ↑ : 하드드랍\n ↓ : 소프트드랍\n SPACE : 회전\n SHIFT : 아이템 사용\n Z X C : 이모티콘");
		explainBox.setEditable(false);
		explainBox.setFocusable(false);
		this.add(explainBox);
		
		JLabel textLabel3 = new JLabel("내 아이템");
		textLabel3.setBounds(0, 20, 60, 20);
		textLabel3.setHorizontalAlignment(JLabel.CENTER);
		this.add(textLabel3);
		
		itemBox = new JLabel();
		itemBox.setBounds(10, 40, 40, 40);
		itemBox.setBackground(defaultColor1);
		itemBox.setOpaque(true);
		itemBox.setBorder(border);
		this.add(itemBox);
		
		JLabel textLabel2 = new JLabel("받은 공격");
		textLabel2.setBounds(260, 380, 60, 20);
		textLabel2.setHorizontalAlignment(JLabel.CENTER);
		this.add(textLabel2);
		
		attackFromRival = new JLabel();
		attackFromRival.setBounds(270, 400, 40, 40);
		attackFromRival.setBackground(defaultColor2);
		attackFromRival.setOpaque(true);
		attackFromRival.setBorder(border);
		this.add(attackFromRival);
		
		JLabel textLabel1 = new JLabel("이모티콘");
		textLabel1.setBounds(260, 440, 60, 20);
		textLabel1.setHorizontalAlignment(JLabel.CENTER);
		this.add(textLabel1);
		
		myEmoticon = new JLabel();
		myEmoticon.setBounds(270, 460, 40, 40);
		myEmoticon.setBackground(defaultColor2);
		myEmoticon.setOpaque(true);
		myEmoticon.setBorder(border);
		this.add(myEmoticon);
		
		networkStatusBox = new JLabel();
		networkStatusBox.setBounds(390, 40, 20, 20);
		networkStatusBox.setBackground(defaultColor3);
	
		networkStatusBox.setOpaque(true);
		networkStatusBox.setHorizontalAlignment(JLabel.CENTER);
		networkStatusBox.setBorder(border);
		this.add(networkStatusBox);

		
		nameBox = new JLabel();
		nameBox.setBounds(410, 40, 60, 20);
		nameBox.setBackground(defaultColor3);
			

		nameBox.setOpaque(true);
		nameBox.setHorizontalAlignment(JLabel.CENTER);
		nameBox.setBorder(border);
		this.add(nameBox);
		
		
		for(int i=0; i<2; i++) {
			rivalItemBox[i] = new JLabel();
			rivalItemBox[i].setBounds(470 + 20*i, 40, 20, 20);
			rivalItemBox[i].setBackground(defaultColor3);
			

			rivalItemBox[i].setOpaque(true);
			rivalItemBox[i].setBorder(border);
			this.add(rivalItemBox[i]);
		}
		
		
		rivalEmoticon = new JLabel();
		rivalEmoticon.setBounds(460, 280, 40, 40);
		rivalEmoticon.setBackground(defaultColor3);
		rivalEmoticon.setOpaque(true);
		rivalEmoticon.setBorder(border);
		this.add(rivalEmoticon);
		
	}
	
	// 블록 한 칸을 변경하는 함수 color값으로 null이 넘어오면 빈 블록이 되며 배경색으로 변경한다
	public void drawBlock(int x, int y, char type, Color color, String status) {
		if(color == null) color = defaultColor1;
		board[x][y].setBoard(type, color, status);
	}
	
	// 상대방의 블록을 그리는 함수 n-상대방 번호
	public void drawRivalBlock(int x, int y, char type, Color color) {
		if(color == null) {
			color = defaultColor3;
		}
		rivalBoard[x][y].setBoard(type, color);
	}
	
	// 블록을 회전 시킨 후 바뀐 회전축을 리턴하는 함수
	public int rotateBlock(int x, int y, char type, Color color) {
		
		int flag = -1;
		if(type == 'O') {
			return flag;
		}
		
		else if(type == 'I') {
			if(x==-2) {
				flag = 5;
				x+=3;
			}
			if(x==-1) {
				flag = 3;
				x+=2;
			}
			else if(x==0) {
				flag = 0;
				x+=1;
			}
			else if(x==8) {
				flag = 1;
				x=x-1;
			}
			else if(x==9) {
				flag = 4;
				x=x-2;
			}
			else if(x==10) {
				flag = 6;
				x=x-3;
			}
			else if(y<=1) {
				y++;
			}
			
			Board [] temp = new Board[16];
			temp[0] = board[x][y+1];
			temp[1] = board[x+1][y+1];
			temp[2] = board[x+1][y];
			temp[3] = board[x][y];
			
			temp[4] = board[x][y+2];
			temp[5] = board[x+1][y+2];
			temp[6] = board[x+2][y+2];
			temp[7] = board[x+2][y+1];
			temp[8] = board[x+2][y];
			temp[9] = board[x+2][y-1];
			temp[10] = board[x+1][y-1];
			temp[11] = board[x][y-1];
			temp[12] = board[x-1][y-1];
			temp[13] = board[x-1][y];
			temp[14] = board[x-1][y+1];
			temp[15] = board[x-1][y+2];
			
			String [] beforeStatus = new String[16];
			beforeStatus[0] = board[x][y].getStatus();
			beforeStatus[1] = board[x][y+1].getStatus();
			beforeStatus[2] = board[x+1][y+1].getStatus();
			beforeStatus[3] = board[x+1][y].getStatus();
			
			beforeStatus[4] = board[x-1][y].getStatus();
			beforeStatus[5] = board[x-1][y+1].getStatus();
			beforeStatus[6] = board[x-1][y+2].getStatus();
			beforeStatus[7] = board[x][y+2].getStatus();
			beforeStatus[8] = board[x+1][y+2].getStatus();
			beforeStatus[9] = board[x+2][y+2].getStatus();
			beforeStatus[10] = board[x+2][y+1].getStatus();
			beforeStatus[11] = board[x+2][y].getStatus();
			beforeStatus[12] = board[x+2][y-1].getStatus();
			beforeStatus[13] = board[x+1][y-1].getStatus();
			beforeStatus[14] = board[x][y-1].getStatus();
			beforeStatus[15] = board[x-1][y-1].getStatus();
			
			char [] beforeType = new char[16];
			beforeType[0] = board[x][y].getType();
			beforeType[1] = board[x][y+1].getType();
			beforeType[2] = board[x+1][y+1].getType();
			beforeType[3] = board[x+1][y].getType();
			
			beforeType[4] = board[x-1][y].getType();
			beforeType[5] = board[x-1][y+1].getType();
			beforeType[6] = board[x-1][y+2].getType();
			beforeType[7] = board[x][y+2].getType();
			beforeType[8] = board[x+1][y+2].getType();
			beforeType[9] = board[x+2][y+2].getType();
			beforeType[10] = board[x+2][y+1].getType();
			beforeType[11] = board[x+2][y].getType();
			beforeType[12] = board[x+2][y-1].getType();
			beforeType[13] = board[x+1][y-1].getType();
			beforeType[14] = board[x][y-1].getType();
			beforeType[15] = board[x-1][y-1].getType();
			
			String [] afterStatus = new String[16];
			afterStatus[0] = board[x][y+1].getStatus();
			afterStatus[1] = board[x+1][y+1].getStatus();
			afterStatus[2] = board[x+1][y].getStatus();
			afterStatus[3] = board[x][y].getStatus();
			
			afterStatus[4] = board[x][y+2].getStatus();
			afterStatus[5] = board[x+1][y+2].getStatus();
			afterStatus[6] = board[x+2][y+2].getStatus();
			afterStatus[7] = board[x+2][y+1].getStatus();
			afterStatus[8] = board[x+2][y].getStatus();
			afterStatus[9] = board[x+2][y-1].getStatus();
			afterStatus[10] = board[x+1][y-1].getStatus();
			afterStatus[11] = board[x][y-1].getStatus();
			afterStatus[12] = board[x-1][y-1].getStatus();
			afterStatus[13] = board[x-1][y].getStatus();
			afterStatus[14] = board[x-1][y+1].getStatus();
			afterStatus[15] = board[x-1][y+2].getStatus();
			
			char [] afterType = new char[16];
			afterType[0] = board[x][y+1].getType();
			afterType[1] = board[x+1][y+1].getType();
			afterType[2] = board[x+1][y].getType();
			afterType[3] = board[x][y].getType();
			
			afterType[4] = board[x][y+2].getType();
			afterType[5] = board[x+1][y+2].getType();
			afterType[6] = board[x+2][y+2].getType();
			afterType[7] = board[x+2][y+1].getType();
			afterType[8] = board[x+2][y].getType();
			afterType[9] = board[x+2][y-1].getType();
			afterType[10] = board[x+1][y-1].getType();
			afterType[11] = board[x][y-1].getType();
			afterType[12] = board[x-1][y-1].getType();
			afterType[13] = board[x-1][y].getType();
			afterType[14] = board[x-1][y+1].getType();
			afterType[15] = board[x-1][y+2].getType();
			
			for(int i=0; i<16; i++) {
				if(beforeStatus[i].equals("AlreadySet")) {
					return flag;
				}
			}
			
			for(int i=0; i<16; i++) {
				if(!beforeStatus[i].equals("AlreadySet")) {
					if(beforeType[i] == ' ') {
						temp[i].setBoard(beforeType[i], defaultColor1, beforeStatus[i]);
					}
					else
						temp[i].setBoard(beforeType[i], color, beforeStatus[i]);
				}
			}
			
			return flag;
		}
		
		else if(type == 'V') {
			Board [] temp = new Board[4];
			temp[0] = board[x][y+1];
			temp[1] = board[x+1][y+1];
			temp[2] = board[x+1][y];
			temp[3] = board[x][y];
			
			String [] beforeStatus = new String[4];
			beforeStatus[0] = board[x][y].getStatus();
			beforeStatus[1] = board[x][y+1].getStatus();
			beforeStatus[2] = board[x+1][y+1].getStatus();
			beforeStatus[3] = board[x+1][y].getStatus();
			
			char [] beforeType = new char[4];
			beforeType[0] = board[x][y].getType();
			beforeType[1] = board[x][y+1].getType();
			beforeType[2] = board[x+1][y+1].getType();
			beforeType[3] = board[x+1][y].getType();
			
			String [] afterStatus = new String[8];
			afterStatus[0] = board[x][y+1].getStatus();
			afterStatus[1] = board[x+1][y+1].getStatus();
			afterStatus[2] = board[x+1][y].getStatus();
			afterStatus[3] = board[x][y].getStatus();
			
			char [] afterType = new char[8];
			afterType[0] = board[x][y+1].getType();
			afterType[1] = board[x+1][y+1].getType();
			afterType[2] = board[x+1][y].getType();
			afterType[3] = board[x][y].getType();
			
			for(int i=0; i<4; i++) {
				if(beforeStatus[i].equals("AlreadySet")) {
					return flag;
				}
			}
			
			for(int i=0; i<4; i++) {
				if(!beforeStatus[i].equals("AlreadySet")) {
					if(beforeType[i] == ' ')
						temp[i].setBoard(beforeType[i], defaultColor1, beforeStatus[i]);
					else
						temp[i].setBoard(beforeType[i], color, beforeStatus[i]);
				}
			}		
			return flag;
		}
		
		else {
			if(x<=0) {
				flag = 0;
				x++;
			}
			else if(x>=9) {
				flag = 1;
				x--;
			}
			else if(y<=1) {
				flag = 2;
				y++;
			}
			
			Board [] temp = new Board[8];
			temp[0] = board[x+1][y+1];
			temp[1] = board[x+1][y];
			temp[2] = board[x+1][y-1];
			temp[3] = board[x][y-1];
			temp[4] = board[x-1][y-1];
			temp[5] = board[x-1][y];
			temp[6] = board[x-1][y+1];
			temp[7] = board[x][y+1];
			
			String [] beforeStatus = new String[8];
			beforeStatus[0] = board[x-1][y+1].getStatus();
			beforeStatus[1] = board[x][y+1].getStatus();
			beforeStatus[2] = board[x+1][y+1].getStatus();
			beforeStatus[3] = board[x+1][y].getStatus();
			beforeStatus[4] = board[x+1][y-1].getStatus();
			beforeStatus[5] = board[x][y-1].getStatus();
			beforeStatus[6] = board[x-1][y-1].getStatus();
			beforeStatus[7] = board[x-1][y].getStatus();
			
			char [] beforeType = new char[8];
			beforeType[0] = board[x-1][y+1].getType();
			beforeType[1] = board[x][y+1].getType();
			beforeType[2] = board[x+1][y+1].getType();
			beforeType[3] = board[x+1][y].getType();
			beforeType[4] = board[x+1][y-1].getType();
			beforeType[5] = board[x][y-1].getType();
			beforeType[6] = board[x-1][y-1].getType();
			beforeType[7] = board[x-1][y].getType();
			
			String [] afterStatus = new String[8];
			afterStatus[0] = board[x+1][y+1].getStatus();
			afterStatus[1] = board[x+1][y].getStatus();
			afterStatus[2] = board[x+1][y-1].getStatus();
			afterStatus[3] = board[x][y-1].getStatus();
			afterStatus[4] = board[x-1][y-1].getStatus();
			afterStatus[5] = board[x-1][y].getStatus();
			afterStatus[6] = board[x-1][y+1].getStatus();
			afterStatus[7] = board[x][y+1].getStatus();
			
			char [] afterType = new char[8];
			afterType[0] = board[x+1][y+1].getType();
			afterType[1] = board[x+1][y].getType();
			afterType[2] = board[x+1][y-1].getType();
			afterType[3] = board[x][y-1].getType();
			afterType[4] = board[x-1][y-1].getType();
			afterType[5] = board[x-1][y].getType();
			afterType[6] = board[x-1][y+1].getType();
			afterType[7] = board[x][y+1].getType();
			
			for(int i=0; i<8; i++) {
				if(beforeStatus[i].equals("AlreadySet")) {
					return -1;
				}
			}
			
			for(int i=0; i<8; i++) {
				if(!beforeStatus[i].equals("AlreadySet")) {
					if(beforeType[i] == ' ')
						temp[i].setBoard(beforeType[i], defaultColor1, beforeStatus[i]);
					else
						temp[i].setBoard(beforeType[i], color, beforeStatus[i]);
				}
			}
			return flag;
		}		
	}
	
	// 블록 생성시 그리는 함수 - 여러칸을 그린다는 뜻
	public void drawEntireBlock(int x, int y, char type, Color color, String status) {
		if(type == 'O') {
			board[x+1][y+1].setBoard(type, color, status);
			board[x][y+1].setBoard(type, color, status);
			board[x+1][y].setBoard(type, color, status);
			board[x][y].setBoard(type, color, status);
		}
		else if(type == 'L') {
			board[x][y+1].setBoard(type, color, status);
			board[x][y].setBoard(type, color, status);
			board[x][y-1].setBoard(type, color, status);
			board[x+1][y-1].setBoard(type, color, status);
		}
		else if(type == 'J') {
			board[x][y-1].setBoard(type, color, status);
			board[x][y+1].setBoard(type, color, status);
			board[x][y].setBoard(type, color, status);
			board[x+1][y+1].setBoard(type, color, status);
		}
		else if(type == 'I') {
			board[x][y-1].setBoard(type, color, status);
			board[x][y].setBoard(type, color, status);
			board[x][y+1].setBoard(type, color, status);
			board[x][y+2].setBoard(type, color, status);
		}
		else if(type == 'Z') {
			board[x+1][y+1].setBoard(type, color, status);
			board[x+1][y].setBoard(type, color, status);
			board[x][y].setBoard(type, color, status);
			board[x][y-1].setBoard(type, color, status);
			
		}
		else if(type == 'S') {
			board[x+1][y-1].setBoard(type, color, status);
			board[x+1][y].setBoard(type, color, status);
			board[x][y].setBoard(type, color, status);
			board[x][y+1].setBoard(type, color, status);
		}
		else if(type == 'T') {
			board[x][y-1].setBoard(type, color, status);
			board[x][y].setBoard(type, color, status);
			board[x][y+1].setBoard(type, color, status);
			board[x+1][y].setBoard(type, color, status);
		}
		else if(type == 'V') {
			board[x][y+1].setBoard(type, color, status);
			board[x][y].setBoard(type, color, status);
			board[x+1][y].setBoard(type, color, status);
		}
		else if(type == '-') {
			board[x][y-1].setBoard(type, color, status);
			board[x][y].setBoard(type, color, status);
			board[x][y+1].setBoard(type, color, status);
		}
	}
	
	// 미리보기 블록 그리는 함수
	public void drawhintBoard(int n, char type, Color color) {
		for(int i=0; i<4; i++) {
			for(int j=0; j<4; j++) {
				hintBoard[n][i][j].setBoard(' ', defaultColor2);
			}
		}
		if(type == 'O') {
			hintBoard[n][1][1].setBoard(type, color);
			hintBoard[n][2][1].setBoard(type, color);
			hintBoard[n][1][2].setBoard(type, color);
			hintBoard[n][2][2].setBoard(type, color);
		}
		else if(type == 'L') {
			hintBoard[n][1][3].setBoard(type, color);
			hintBoard[n][1][2].setBoard(type, color);
			hintBoard[n][2][1].setBoard(type, color);
			hintBoard[n][1][1].setBoard(type, color);
		}
		else if(type == 'J') {
			hintBoard[n][2][3].setBoard(type, color);
			hintBoard[n][1][3].setBoard(type, color);
			hintBoard[n][1][2].setBoard(type, color);
			hintBoard[n][1][1].setBoard(type, color);
		}
		else if(type == 'I') {
			hintBoard[n][2][0].setBoard(type, color);
			hintBoard[n][2][1].setBoard(type, color);
			hintBoard[n][2][2].setBoard(type, color);
			hintBoard[n][2][3].setBoard(type, color);
		}
		else if(type == 'Z') {
			hintBoard[n][2][3].setBoard(type, color);
			hintBoard[n][2][2].setBoard(type, color);
			hintBoard[n][1][2].setBoard(type, color);
			hintBoard[n][1][1].setBoard(type, color);
		}
		else if(type == 'S') {
			hintBoard[n][1][3].setBoard(type, color);
			hintBoard[n][1][2].setBoard(type, color);
			hintBoard[n][2][2].setBoard(type, color);
			hintBoard[n][2][1].setBoard(type, color);
		}
		else if(type == 'T') {
			hintBoard[n][1][3].setBoard(type, color);
			hintBoard[n][1][2].setBoard(type, color);
			hintBoard[n][2][2].setBoard(type, color);
			hintBoard[n][1][1].setBoard(type, color);
		}
		else if(type == 'V') {
			hintBoard[n][1][2].setBoard(type, color);
			hintBoard[n][2][1].setBoard(type, color);
			hintBoard[n][1][1].setBoard(type, color);
		}
		else if(type == '-') {
			hintBoard[n][1][3].setBoard(type, color);
			hintBoard[n][1][2].setBoard(type, color);
			hintBoard[n][1][1].setBoard(type, color);
		}
		repaint();
	}
	
	// 블록의 상태 가져오는 함수
	public String getBlockStatus(int x, int y) {
		return board[x][y].getStatus();
	}
	

}
