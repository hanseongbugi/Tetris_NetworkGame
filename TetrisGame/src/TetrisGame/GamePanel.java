package TetrisGame;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import TetrisGame.GameObejct.Board;
import TetrisGame.GameObejct.GameManager;
import TetrisGame.GameObejct.SmallBoard;
import utility.Settings;

// 테트리스 게임을 그리는 패널
public class GamePanel extends JPanel {
	private GameManager gameManager; 
	private LineBorder border = new LineBorder(Color.WHITE);
	
	public Board [][] board = new Board[10][23]; //자신 플레이 판
	public SmallBoard [][] rivalBoard = new SmallBoard[10][20]; //상대방 판
	public SmallBoard [][][] hintBoard = new SmallBoard[5][4][5]; //미리보기
	
	public JLabel itemBox; //나의 아이템 박스
	public JLabel networkStatusBox; //상대방 연결상태 박스
	public JLabel nameBox; //상대방 이름 박스
	public JLabel attackFromRival; //방해받은 아이템 박스
	public JLabel rivalItemBox;
	
	public JLabel myEmoji; //내 이모티콘
	public JLabel rivalEmoji; //상대방 이모티콘

	// GamePanel 생성자 
	public GamePanel(GameManager gameManager) {
		this.gameManager = gameManager;
		setLayout(null);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		createBoard();
		setBackground(new Color(173, 216, 250)); //배경색 하늘색 지정
		
		JLabel gameGround = new JLabel();
		gameGround.setIcon(Settings.gameBackground);
		gameGround.setBounds(0, 480, 630, 190);
		add(gameGround);
	}
	
	// 기본 틀을 그리는 함수
	// 자신의 게임판과 힌트판, 아이템상자, 이모티콘 상자, 상대방의 게임판, 상대방의 상태판, 상대방의 이모티콘 상자를 생성
	public void createBoard() {
		for(int i=0; i<10; i++) {
			for(int j=0; j<23; j++) {
				board[i][j] = new Board();
				if(j<20) {
					board[i][j].labelbox.setBounds(60 + 20*i, 430 - 20*j, 20, 20);
					board[i][j].labelbox.setBackground(gameManager.getDefaultColor(1));
					board[i][j].labelbox.setOpaque(true);
					board[i][j].labelbox.setBorder(border);
					this.add(board[i][j].labelbox);
				}
				
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
					hintBoard[i][j][k].labelbox.setBounds(270 + 10*j, 90 - 10*k + i*50, 10, 10);
					hintBoard[i][j][k].labelbox.setBackground(gameManager.getDefaultColor(2));
					hintBoard[i][j][k].labelbox.setOpaque(true);
					hintBoard[i][j][k].labelbox.setBorder(border);
					this.add(hintBoard[i][j][k].labelbox);
				}
			}
		}
		Font font = new Font("맑은 고딕", Font.BOLD, 11);
		JTextArea explainBox = new JTextArea();
		explainBox.setBounds(360, 380, 180, 95);
		explainBox.setBackground(gameManager.getDefaultColor(2));
		explainBox.setOpaque(true);
		explainBox.setBorder(border);
		explainBox.setText(" ← → : 좌우 이동\n ↑ : 회전\n ↓ : 소프트드랍\n SPACE : 하드드랍\n SHIFT : 아이템 사용\n Z X C : 이모티콘");
		explainBox.setEditable(false);
		explainBox.setFocusable(false);
		explainBox.setFont(font);
		add(explainBox);
		
		JLabel textLabel3 = new JLabel("내 아이템");
		textLabel3.setBounds(0, 30, 60, 20);
		textLabel3.setHorizontalAlignment(JLabel.CENTER);
		textLabel3.setFont(font);
		add(textLabel3);
		
		itemBox = new JLabel();
		itemBox.setBounds(10, 50, 40, 40);
		itemBox.setBackground(gameManager.getDefaultColor(1));
		itemBox.setOpaque(true);
		itemBox.setBorder(border);
		add(itemBox);
		
		JLabel textLabel2 = new JLabel("받은 공격");
		textLabel2.setBounds(260, 310, 60, 20);
		textLabel2.setHorizontalAlignment(JLabel.CENTER);
		textLabel2.setFont(font);
		add(textLabel2);
		
		attackFromRival = new JLabel();
		attackFromRival.setBounds(270, 330, 40, 40);
		attackFromRival.setBackground(gameManager.getDefaultColor(2));
		attackFromRival.setOpaque(true);
		attackFromRival.setBorder(border);
		add(attackFromRival);
		
		JLabel textLabel1 = new JLabel("이모티콘");
		textLabel1.setBounds(260, 380, 60, 20);
		textLabel1.setHorizontalAlignment(JLabel.CENTER);
		textLabel1.setFont(font);
		add(textLabel1);
		
		myEmoji = new JLabel();
		myEmoji.setBounds(270, 400, 40, 40);
		myEmoji.setBackground(gameManager.getDefaultColor(2));
		myEmoji.setOpaque(true);
		myEmoji.setBorder(border);
		add(myEmoji);
		
		networkStatusBox = new JLabel();
		networkStatusBox.setBounds(390, 40, 20, 20);
		networkStatusBox.setBackground(gameManager.getDefaultColor(3));
	
		networkStatusBox.setOpaque(true);
		networkStatusBox.setHorizontalAlignment(JLabel.CENTER);
		networkStatusBox.setBorder(border);
		add(networkStatusBox);

		
		nameBox = new JLabel();
		nameBox.setBounds(410, 40, 60, 20);
		nameBox.setBackground(gameManager.getDefaultColor(3));
		nameBox.setOpaque(true);
		nameBox.setHorizontalAlignment(JLabel.CENTER);
		nameBox.setBorder(border);
		add(nameBox);
		
		rivalItemBox = new JLabel();
		rivalItemBox.setBounds(350, 70, 40, 40);
		rivalItemBox.setBackground(gameManager.getDefaultColor(2));
		rivalItemBox.setOpaque(true);
		rivalItemBox.setBorder(border);
		add(rivalItemBox);
		
		rivalEmoji = new JLabel();
		rivalEmoji.setBounds(460, 280, 40, 40);
		rivalEmoji.setBackground(gameManager.getDefaultColor(3));
		rivalEmoji.setOpaque(true);
		rivalEmoji.setBorder(border);
		add(rivalEmoji);
		
	}
	
	
	// 블록의 상태 가져오는 함수
	public String getBlockStatus(int x, int y) {
		return board[x][y].getStatus();
	}
}
