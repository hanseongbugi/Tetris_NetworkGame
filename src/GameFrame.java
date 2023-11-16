import java.awt.*;
import java.awt.event.*;
import java.io.*;
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
	GameThread gameThread;

	GameFrame() throws IOException{
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
		
		gameThread = new GameThread();
		gameThread.start();
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e){
				try {
					gameThread.sendGameOver();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
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
						((GamePanel) gp).setArray();
						((GamePanel) gp).drawTetris();
					}
					else if (e.getKeyCode() == FirstPlayerKeySetting.FKeyType[1])
					{
						((GamePanel) gp).move_left();
						((GamePanel) gp).setArray();
						((GamePanel) gp).drawTetris();
					}
					else if (e.getKeyCode() == FirstPlayerKeySetting.FKeyType[2])
					{
						((GamePanel) gp).move_right();
						((GamePanel) gp).setArray();
						((GamePanel) gp).drawTetris();
					}

					else if (e.getKeyCode() == FirstPlayerKeySetting.FKeyType[3])
					{
						((GamePanel) gp).move_drop();
						((GamePanel) gp).setArray();
						((GamePanel) gp).drawTetris();
					}
					else if (e.getKeyCode() == FirstPlayerKeySetting.FKeyType[4])
					{
						((GamePanel) gp).move_turn();
						((GamePanel) gp).setArray();
						((GamePanel) gp).drawTetris();
					}
				}
			}
		});
	}

	public static void main(String[] args) throws IOException{
		new ImageSource();
		new GameFrame();
	}
}