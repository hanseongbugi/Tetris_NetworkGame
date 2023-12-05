package TetrisGame.GameObejct;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.AbstractBorder;
import javax.swing.border.LineBorder;

import TetrisGame.GamePanel;
import WaitingRoom.UserMessage;
import WaitingRoom.WaitingPanel;
import utility.PlayerKeySetting;
import utility.Settings;

public class Player extends JPanel implements Runnable{
	JPanel tetrisArea = new JPanel();
	JLabel nextBlock = new JLabel();
	JLabel score = new JLabel();
	JLabel highestScore = new JLabel();
	JPanel item = new JPanel();
	JPanel item_using = new JPanel();
	JLabel time = new JLabel();
	AbstractBorder WhiteLineBorder;
	JLabel[][] fieldLabel = new JLabel[20][10];
	
	public final int playerNumber;
	Thread th;

	int gameSpeed = 500;
	int speedCount = 0;
	int preSpeed;

	JLabel tube;
	JLabel ground;
	JLabel textLabel = new JLabel();
	JPanel blackPanel = new JPanel();
	public boolean Gaming = false;

	String userName;

	public Player(int playerNumber)
	{
		this.playerNumber = playerNumber;
		userName = WaitingPanel.userName;
		setLayout(null);
		setSize(360, 640);
		
		makeComponent(0);
		makeTetrisArea(0);
		makeBackground();

		setArray(); // 데이터 배열 테두리 초기화

		addBlock(); // 블럭추가

		Thread textTh = new Thread()
		{
			public void run()
			{
				int textCount = 3;

				while (textCount > -1)
				{
					if (textCount > 0)
						textLabel.setText(Integer.toString(textCount));
					else
						textLabel.setText("START!!");
					try
					{
						sleep(1000);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					blackPanel.repaint();
					textCount--;
				}
				tetrisArea.setVisible(true);
				blackPanel.setVisible(false);

				synchronized (th)
				{
					th.notify();
				}
				Gaming = true;
			}
		};
		textTh.start();
	}


	public void makeBackground()
	{
		setOpaque(false);
		setBackground(new Color(30, 160, 255));

		ground = new JLabel(Settings.bg_ground);
		int groundWidth = Settings.bg_ground.getIconWidth();
		int groundHeight = Settings.bg_ground.getIconHeight();
		ground.setBounds(0, 640 - groundHeight, groundWidth, groundHeight);
		add(ground);
	}

	public void makeComponent(int n)
	{
		WhiteLineBorder = new LineBorder(Color.WHITE);

		tetrisArea = new JPanel();
		tetrisArea.setBounds(10, 60, 240, 480);
		tetrisArea.setBackground(Color.BLACK);
		blackPanel.setBounds(10, 60, 240, 480);
		blackPanel.setBackground(Color.BLACK);
		add(blackPanel);
		add(tetrisArea);
		tetrisArea.setVisible(false);

		nextBlock = new JLabel(Settings.block_L);
		nextBlock.setFont(new Font("verdana", 0, 12));
		nextBlock.setForeground(Color.WHITE);
		nextBlock.setBackground(Color.BLACK);
		nextBlock.setOpaque(true);
		nextBlock.setBorder(WhiteLineBorder);
		nextBlock.setBounds(255, 60, 90, 90);
		add(nextBlock);
		
		blackPanel.setLayout(null);
		blackPanel.add(textLabel);
		textLabel.setBounds(20, 50, 200, 50);
		textLabel.setHorizontalAlignment(SwingConstants.CENTER);
		textLabel.setFont(new Font("Verdana", 1, 30));
		textLabel.setOpaque(true);
		textLabel.setBackground(new Color(255, 255, 255, 0));
		textLabel.setForeground(new Color(-1));
		textLabel.setBorder(null);

	}
	public void makeTetrisArea(int n)
	{
		tetrisArea.setLayout(new GridLayout(20, 10));

		for (int i = 0; i < 20; i++)
		{
			for (int j = 0; j < 10; j++)
			{
				fieldLabel[i][j] = new JLabel();
				tetrisArea.add(fieldLabel[i][j]);
				fieldLabel[i][j].setBackground(Color.BLACK);
				fieldLabel[i][j].setOpaque(true);

				WhiteLineBorder = new LineBorder(new Color(15, 15, 15));
				fieldLabel[i][j].setBorder(WhiteLineBorder);
			}
		}
	}

	/******************** 테트리스 영역 메소드 ********************/

	/****************************************
	 * 표시 레이블, 데이터 배열, 배열 복사본, 아이템 객체
	 ****************************************/
	int[][] array = new int[21][12];
	int[][] field = new int[21][12];
	int[][] copy;


	/******************** 테트리스 동작 그리기 ********************/

	int preBlock = (int) (Math.random() * 7 + 1);

	int[][] block;
	int x; // 블럭 가로 위치 (점 0,0)
	int y; // 블럭 세로 위치 (점 0,0)
	int blockNum;
	int blockWSize; // 블럭의 너비, 용도 : 목표 범위 초과하여 데이터 손실 막기
	int blockHSize; // 블럭의 높이, 용도 : 목표 범위 초과하여 데이터 손실 막기
	public boolean gameRun = true;

	public void setArray()
	{
		/****************************************
		 * 안내 : 데이터 배열내에 테두리를 표현 단순 테두리 표현으로 실제 데이터에 영향을 안줌. 출력시에만 영향을 주므로 실제 초기
		 * 테두리 데이터는 0임.
		 ****************************************/
		for (int row = 0; row < array.length - 1; row++)
			for (int col = 1; col < array[0].length - 1; col++)
				if (array[row][col] == 9)
					array[row][col] = 0;

		for (int row = 0; row < array.length; row++)
		{
			array[row][0] = 9;
			array[row][array[0].length - 1] = 9;
		}

		for (int col = 0; col < array[0].length; col++)
			array[array.length - 1][col] = 9;
	}

	public void addBlock()
	{
		int[][] block;
		int start_x;
		int start_y;
		int blockNum;

		Blocks b = new Blocks();
		nextBlock(b);

		block = b.getBlock();
		blockNum = b.getBlockNum();

		// block = b.getBlock();
		start_x = b.getStart_x();
		start_y = b.getStart_y();
		
		this.block = block;
		this.blockNum = blockNum;
		x = start_x;
		y = start_y;
		blockWSize = block[0].length;
		blockHSize = block.length;

		/****************************************
		 * start_y 의 존재 : 시작위치 0값을 받아옴
		 ****************************************/

		for (int row = 0, h = 0 + start_y; row < blockHSize - start_y; row++, h++)
			for (int col = start_x, w = 0; col < blockWSize + start_x; col++, w++)
				if (block[h][w] > 0)
					array[row][col] = block[h][w];

		int count = 0;
		for (int row = y; row < y + blockHSize; row++)
			for (int col = x; col < x + blockWSize; col++)
				if (array[row][col] > 0 && field[row][col] > 0)
					count++; // 필드의 가장 윗족에 블록이 있는지 검사 (더이상 놓을 수 없는지 확인)
		
		if (count > 0) // 블록이 끝에 있다면 종료
		{
			gameRun = false;
			th.interrupt();
			/* if (GameFrame.gameMode == 2)
			{
				textLabel.setText("LOSE");
			}
			else if (GameFrame.gameMode == 1)
			{
				textLabel.setText("FAIL");
			}*/
			textLabel.setVisible(true);
			blackPanel.setVisible(true);
			tetrisArea.setVisible(false);
			
			System.exit(0);
		}
		else
		{
			drawTetris();
			setArray();
			th = new Thread(this);
			th.start();
		}

		b = null;
	}

	public void nextBlock(Blocks b)
	{
		int currentBlock;
		currentBlock = preBlock;
		b.blockNum = currentBlock;
		preBlock = (int) (Math.random() * 7 + 1);
		
		switch (preBlock)
		{
		/**********************
		 * I : red, J : lime, L : orange, O : purple S : cyan T : blue Z : green
		 **********************/
		case 1:
			nextBlock.setIcon(Settings.block_I);
			break;
		case 2:
			nextBlock.setIcon(Settings.block_J);
			break;
		case 3:
			nextBlock.setIcon(Settings.block_L);
			break;
		case 4:
			nextBlock.setIcon(Settings.block_O);
			break;
		case 5:
			nextBlock.setIcon(Settings.block_S);
			break;
		case 6:
			nextBlock.setIcon(Settings.block_T);
			break;
		case 7:
			nextBlock.setIcon(Settings.block_Z);
			break;
		}
	}

	public void drawEndTetris()
	{
		for (int row = 0; row < fieldLabel.length; row++)
			for (int col = 0; col < fieldLabel[0].length; col++)
			{
				fieldLabel[row][col].setIcon(Settings.block_gray);
			}
	}

	public void drawTetris()
	{
		for (int row = 0; row < fieldLabel.length; row++)
			for (int col = 0; col < fieldLabel[0].length; col++)
			{
				if (array[row][col + 1] > 0 && array[row][col + 1] < 8)
					switch (array[row][col + 1])
					{
					/**********************
					 * I : red, J : lime, L : orange, O : purple S : cyan T :
					 * blue Z : green
					 **********************/
					case 1:
						fieldLabel[row][col].setIcon(Settings.block_red);
						break;
					case 2:
						fieldLabel[row][col].setIcon(Settings.block_lime);
						break;
					case 3:
						fieldLabel[row][col].setIcon(Settings.block_orange);
						break;
					case 4:
						fieldLabel[row][col].setIcon(Settings.block_puple);
						break;
					case 5:
						fieldLabel[row][col].setIcon(Settings.block_cyan);
						break;
					case 6:
						fieldLabel[row][col].setIcon(Settings.block_blue);
						break;
					case 7:
						fieldLabel[row][col].setIcon(Settings.block_green);
						break;
					}
				else if (field[row][col + 1] >= 80 && field[row][col + 1] < 90)
				{
					// 원래 아이템 배치하던 부분
				}
				else if (field[row][col + 1] >= 90 && field[row][col + 1] < 100)
				{
					switch (field[row][col + 1] - 90)
					{
					/**********************
					 * I : red, J : lime, L : orange, O : purple S : cyan T :
					 * blue Z : green
					 **********************/
					case 1:
						fieldLabel[row][col].setIcon(Settings.block_red);
						break;
					case 2:
						fieldLabel[row][col].setIcon(Settings.block_lime);
						break;
					case 3:
						fieldLabel[row][col].setIcon(Settings.block_orange);
						break;
					case 4:
						fieldLabel[row][col].setIcon(Settings.block_puple);
						break;
					case 5:
						fieldLabel[row][col].setIcon(Settings.block_cyan);
						break;
					case 6:
						fieldLabel[row][col].setIcon(Settings.block_blue);
						break;
					case 7:
						fieldLabel[row][col].setIcon(Settings.block_green);
						break;
					}
				}
				else if (field[row][col + 1] >= 100)
					fieldLabel[row][col].setIcon(Settings.block_gray);
				else
					fieldLabel[row][col].setIcon(null);
			}
	}

	public void drawTurn()
	{
		for (int row = y, h = 0; h < blockWSize; row++, h++)
			for (int col = x, w = 0; w < blockHSize; col++, w++)
				array[row][col] = 0;

		for (int row = y, h = 0; h < blockHSize; row++, h++)
			for (int col = x, w = 0; w < blockWSize; col++, w++)
				if (block[h][w] > 0)
					array[row][col] = block[h][w];

		/****************************************
		 * 조건 : 우측 벽에서 모형 손실에 대한 예방대책 세울것
		 ****************************************/
		if (x + blockWSize > array[0].length - 1)
			moveLeft();
	}

	public void fixBlock()
	{
		/****************************************
		 * 조건 : 1. move_drop, move_down의 인터럽트 발생시 바닥에 닿았다는 조건이므로 마지막 데이터 값을 변경해줘
		 * 고정됨을 표시한다. 2. 데이터 값 >> I:91 J:92 L:93 O:94 S:95 T:96 Z:97 3.
		 * checkArray()에서 고정된 블럭은 n으로 표시되게끔 코드를 수정한다.
		 *****************************************/

		for (int row = 0; row < array.length - 1; row++)
			for (int col = 1; col < array[0].length - 1; col++)
			{
				if (array[row][col] != 0 && array[row][col] != 8)
					field[row][col] = array[row][col] + 90; // 90: 고정을 의미
				array[row][col] = 0;
			}
		lineCheck();
	}

	public void lineCheck()
	{
		int deleteCount = 0;
		// 정지된 y값의 줄부터 블럭의 length까지 계산한다.
		
		for (int i = y; i < y + block.length; i++)
		{
			int count = 0;
			for (int j = 1; j < field[0].length - 1; j++)
				if (field[i][j] != 0)
					count++;

			if (count == 10)
			{
				lineDelete(i);
				deleteCount++;
			}
		}

	}

	public void lineDelete(int num)
	{
		copy = new int[field.length][field[0].length];
		
		for (int row = 0; row < field.length - 1; row++)
			for (int col = 0; col < field[0].length; col++)
				if (row < num)
					copy[row + 1][col] = field[row][col];
				else
					copy[row + 1][col] = field[row + 1][col];

		backArray(field, copy);

		copy = null;
	}

	/******************** 테트리스 키 이벤트 ********************/

	public void moveLeft()
	{
		copy = new int[array.length][array[0].length];
		for (int row = 0; row < array.length; row++)
			for (int col = 1; col < array[0].length; col++)
				copy[row][col - 1] = array[row][col];

		int count = 0;
		for (int row = y; row < y + blockHSize; row++)
			for (int col = x - 1; col < x + blockWSize; col++)
				if (copy[row][col] > 0 && field[row][col] > 0)
					count++;

		/****************************************
		 * 조건 : 좌측 데이터의 벽<9> 넘지 않기
		 ****************************************/
		if (!(count > 0))
			if (x > 1)
			{
				x--;
				backArray(array, copy);
			}

		copy = null;
	}

	public void moveRight()
	{
		copy = new int[array.length][array[0].length];
		
		for (int row = 0; row < array.length; row++)
			for (int col = 0; col < array[0].length - 1; col++)
				copy[row][col + 1] = array[row][col];

		int count = 0;
		
		for (int row = y; row < y + blockHSize; row++)
			for (int col = x; col < x + blockWSize + 1; col++)
				if (copy[row][col] > 0 && field[row][col] > 0)
					count++;

		/****************************************
		 * 조건 : 우측 데이터의 벽<9> 넘지 않기
		 ****************************************/
		
		if (!(count > 0))
			if (x < array[0].length - blockWSize - 1)
			{
				x++;
				backArray(array, copy);
			}

		copy = null;
	}

	public void moveDown()
	{
		copy = new int[array.length][array[0].length];
		for (int row = 0; row < array.length - 1; row++)
			for (int col = 0; col < array[0].length; col++)
				copy[row + 1][col] = array[row][col];

		int count = 0;
		for (int row = y; row < y + blockHSize + 1; row++)
			for (int col = x; col < x + blockWSize; col++)
				if (copy[row][col] > 0 && field[row][col] > 0)
					count++;

		/****************************************
		 * 조건 : 하단 데이터의 벽<9> 넘지 않기
		 ****************************************/
		if (count > 0)
		{
			th.interrupt();
			fixBlock();
			addBlock();
		}
		else if (y < (array.length - 1) - blockHSize)
		{
			y++;
			backArray(array, copy);
		}
		else
		{
			th.interrupt();
			fixBlock();
			addBlock();
		}

		copy = null;
	}

	public void move_up()
	{
		copy = new int[array.length][array[0].length];
		
		for (int row = 1; row < array.length - 1; row++)
			for (int col = 0; col < array[0].length; col++)
				copy[row - 1][col] = array[row][col];

		y--;
		backArray(array, copy);

		copy = null;
	}

	public void moveDrop()
	{
		while (true)
		{
			/* 전역: int[][] */copy = new int[array.length][array[0].length];
			for (int row = 0; row < array.length - 1; row++)
				for (int col = 0; col < array[0].length; col++)
					copy[row + 1][col] = array[row][col];

			int count = 0;
			for (int row = y; row < y + blockHSize + 1; row++)
				for (int col = x; col < x + blockWSize + 1; col++)
					if (copy[row][col] > 0 && field[row][col] > 0)
						count++;

			/****************************************
			 * 조건 : 하단 데이터의 벽<9> 넘지 않기 (move_down() 메소드와 동일함)
			 ****************************************/
			if (count > 0)
			{
				th.interrupt();
				fixBlock();
				addBlock();
				break;
			}
			else if (y < (array.length - 1) - blockHSize)
			{
				y++;
				backArray(array, copy);
			}
			else
			{
				/* 무한루프로 끝까지 내린후 바닥에 닿으면 break를 호출하여 루프 종료 */
				th.interrupt();
				fixBlock();
				addBlock();
				break;
			}
			copy = null;
		}
	}

	public void moveTurn()
	{
		int[][] turn = new int[blockWSize][blockHSize];
		for (int i = 0; i < blockWSize; i++)
			for (int j = 0; j < blockHSize; j++)
				turn[i][j] = block[j][(blockWSize - 1) - i];

		int count = 0;
		if (blockNum == 1 && x >= 9)
			count++;
		if (!(count > 0))
			for (int row = y, i = 0; i < blockWSize; row++, i++)
				for (int col = x, j = 0; j < blockHSize; col++, j++)
					if (turn[i][j] > 0 && field[row][col] > 0)
						count++;

		if (!(count > 0))
		{
			int temp = blockHSize;
			blockHSize = blockWSize;
			blockWSize = temp;

			block = null;
			block = new int[blockHSize][blockWSize];
			block = turn;

			drawTurn();
		}

		turn = null;
	}

	public void backArray(int[][] array, int[][] copy)
	{
		/****************************************
		 * 안내 : backArray(원래 데이터, 복사 데이터) 복사 데이터를 원래 데이터로 재복사
		 ****************************************/

		for (int row = 0; row < array.length; row++)
			for (int col = 0; col < array[0].length; col++)
				array[row][col] = copy[row][col];
	}

	/******************** 테트리스 쓰레드 메소드 ********************/

	public void run()
	{
		if (!Gaming)
		{
			synchronized (th)
			{
				try
				{
					th.wait();
					while (gameRun)
					{
						try
						{
							Thread.sleep(gameSpeed);
						}
						catch (InterruptedException e)
						{
							return;
						}
						moveDown();
						drawTetris();
					}
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
		else
		{
			while (gameRun)
			{
				try
				{
					Thread.sleep(gameSpeed);
				}
				catch (InterruptedException e)
				{
					return;
				}
				moveDown(); 
				drawTetris();
			}
		}
	}
}
