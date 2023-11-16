import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.*;

public class GameFrame extends JFrame
{
	GamePanel fgp;
	static int gameMode = 1;
	static JFrame jf;

	GameFrame()
	{
		setTitle("Tetris");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(null);
		setSize(360, 640);
		setResizable(false);

		jf = this;
		Container c = this.getContentPane();

		c.setBackground(new Color(30, 160, 255));

		switch (gameMode)
		{
			case 1:
				fgp = new GamePanel();
				fgp.setBounds(0, 0, 360, 640);
				add(fgp);
				MyKey1(fgp);
				break;
		}

		setLocationRelativeTo(getParent());

		setVisible(true);
	}

	public void remakePanel()
	{
		fgp.highestScore.setBounds(255, 200, 205, 30);
		fgp.time.setBounds(255, 240, 205, 30);
	}

	public void MyKey1(JPanel gp)
	{
		this.setFocusable(true);
		this.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				if (((GamePanel) gp).gameRun && ((GamePanel) gp).Gaming)
				{
//					if (e.getKeyText(e.getKeyCode()).equals(FirstPlayerKeySetting.FKeyType[0]))
					if (e.getKeyCode() == FirstPlayerKeySetting.FKeyType[0])
					{
						((GamePanel) gp).move_down();
						((GamePanel) gp).checkArray();
						((GamePanel) gp).drawTetris();
					}
					else if (e.getKeyCode() == FirstPlayerKeySetting.FKeyType[1])
					{
						((GamePanel) gp).move_left();
						((GamePanel) gp).checkArray();
						((GamePanel) gp).drawTetris();
					}
					else if (e.getKeyCode() == FirstPlayerKeySetting.FKeyType[2])
					{
						((GamePanel) gp).move_right();
						((GamePanel) gp).checkArray();
						((GamePanel) gp).drawTetris();
					}
					else if (e.getKeyCode() == FirstPlayerKeySetting.FKeyType[3])
					{
						// 내게 쓰기
						((GamePanel) gp).ogp = fgp;

						JLabel labelNum = ((GamePanel) gp).itemLabel.get(0);
//						if (labelNum.getName().equals("6"))
//							((GamePanel) gp).ogp = sgp;

						((GamePanel) gp).useItem();
					}
					else if (e.getKeyCode() == FirstPlayerKeySetting.FKeyType[4])
					{
						// 적에게 쓰기
//						((GamePanel) gp).ogp = sgp;
						((GamePanel) gp).attackItem();
					}
					else if (e.getKeyCode() == FirstPlayerKeySetting.FKeyType[5])
					{
						// 아이템 지우기
						((GamePanel) gp).deleteItem();
					}
					else if (e.getKeyCode() == FirstPlayerKeySetting.FKeyType[6])
					{
						((GamePanel) gp).move_drop();
						((GamePanel) gp).checkArray();
						((GamePanel) gp).drawTetris();
					}
					else if (e.getKeyCode() == FirstPlayerKeySetting.FKeyType[7])
					{
						((GamePanel) gp).move_turn();
						((GamePanel) gp).checkArray();
						((GamePanel) gp).drawTetris();
					}
				}
			}
		});
	}

	public static void main(String[] args)
	{
		new ImageSource();
		new GameFrame();
	}
}