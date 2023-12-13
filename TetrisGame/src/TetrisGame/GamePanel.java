package TetrisGame;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import TetrisGame.GameObejct.Board;
import TetrisGame.GameObejct.GameManager;
import TetrisGame.GameObejct.SmallBoard;

// 테트리스 게임을 그리는 패널
public class GamePanel extends JPanel {
	private GameManager gameManager;
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

	public GamePanel(GameManager gameManager) {
		this.gameManager = gameManager;
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
				board[i][j].labelbox.setBackground(gameManager.getDefaultColor(1));
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
				
				rivalBoard[i][j].labelbox.setBackground(gameManager.getDefaultColor(3));
				
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
					hintBoard[i][j][k].labelbox.setBackground(gameManager.getDefaultColor(2));
					hintBoard[i][j][k].labelbox.setOpaque(true);
					hintBoard[i][j][k].labelbox.setBorder(border);
					this.add(hintBoard[i][j][k].labelbox);
				}
			}
		}
		
		JTextArea explainBox = new JTextArea();
		explainBox.setBounds(400, 390, 300, 110);
		explainBox.setBackground(gameManager.getDefaultColor(2));
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
		itemBox.setBackground(gameManager.getDefaultColor(1));
		itemBox.setOpaque(true);
		itemBox.setBorder(border);
		this.add(itemBox);
		
		JLabel textLabel2 = new JLabel("받은 공격");
		textLabel2.setBounds(260, 380, 60, 20);
		textLabel2.setHorizontalAlignment(JLabel.CENTER);
		this.add(textLabel2);
		
		attackFromRival = new JLabel();
		attackFromRival.setBounds(270, 400, 40, 40);
		attackFromRival.setBackground(gameManager.getDefaultColor(2));
		attackFromRival.setOpaque(true);
		attackFromRival.setBorder(border);
		this.add(attackFromRival);
		
		JLabel textLabel1 = new JLabel("이모티콘");
		textLabel1.setBounds(260, 440, 60, 20);
		textLabel1.setHorizontalAlignment(JLabel.CENTER);
		this.add(textLabel1);
		
		myEmoticon = new JLabel();
		myEmoticon.setBounds(270, 460, 40, 40);
		myEmoticon.setBackground(gameManager.getDefaultColor(2));
		myEmoticon.setOpaque(true);
		myEmoticon.setBorder(border);
		this.add(myEmoticon);
		
		networkStatusBox = new JLabel();
		networkStatusBox.setBounds(390, 40, 20, 20);
		networkStatusBox.setBackground(gameManager.getDefaultColor(3));
	
		networkStatusBox.setOpaque(true);
		networkStatusBox.setHorizontalAlignment(JLabel.CENTER);
		networkStatusBox.setBorder(border);
		this.add(networkStatusBox);

		
		nameBox = new JLabel();
		nameBox.setBounds(410, 40, 60, 20);
		nameBox.setBackground(gameManager.getDefaultColor(3));
			

		nameBox.setOpaque(true);
		nameBox.setHorizontalAlignment(JLabel.CENTER);
		nameBox.setBorder(border);
		this.add(nameBox);
		
		
		for(int i=0; i<2; i++) {
			rivalItemBox[i] = new JLabel();
			rivalItemBox[i].setBounds(470 + 20*i, 40, 20, 20);
			rivalItemBox[i].setBackground(gameManager.getDefaultColor(3));
			

			rivalItemBox[i].setOpaque(true);
			rivalItemBox[i].setBorder(border);
			this.add(rivalItemBox[i]);
		}
		
		
		rivalEmoticon = new JLabel();
		rivalEmoticon.setBounds(460, 280, 40, 40);
		rivalEmoticon.setBackground(gameManager.getDefaultColor(3));
		rivalEmoticon.setOpaque(true);
		rivalEmoticon.setBorder(border);
		this.add(rivalEmoticon);
		
	}
	
	
	// 블록의 상태 가져오는 함수
	public String getBlockStatus(int x, int y) {
		return board[x][y].getStatus();
	}
}
