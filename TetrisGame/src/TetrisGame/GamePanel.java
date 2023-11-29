package TetrisGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.AbstractBorder;
import javax.swing.border.LineBorder;

import TetrisGame.GameObejct.Blocks;
import TetrisGame.GameObejct.Player;
import utility.Settings;
import WaitingRoom.WaitingPanel;
import utility.PlayerKeySetting;

public class GamePanel extends JPanel {
	GamePanel fpanel;

	private Player player1;
	private Player player2;
	
	private Player controlPlayer;
	private String userName;
	private int controlPlayerNum;
	

	public GamePanel() {
		setLayout(null);

		setBackground(new Color(30, 160, 255));

		player1 = new Player(1);
		player2 = new Player(2);
		player1.setLocation(370, 0);
		add(player1);

		player2.setLocation(0, 0);
		add(player2);
		
		userName = WaitingPanel.userName;
		controlPlayerNum = WaitingPanel.getPlayerNum();
		System.out.println(userName+" "+controlPlayerNum);
		if(WaitingPanel.getPlayerNum() == 1) 
			controlPlayer = player1;
		else
			controlPlayer = player2;
		
		MyKey(controlPlayer);
		
	}

	/*public void remakePanel() {
		fpanel.highestScore.setBounds(255, 200, 205, 30);
		fpanel.time.setBounds(255, 240, 205, 30);
	}*/

	public void MyKey(JPanel panel) {
		System.out.println(panel);
		panel.setFocusable(true);
		panel.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				System.out.println(e.getKeyCode());
				if (((Player) panel).gameRun && ((Player) panel).Gaming) {
					if (e.getKeyCode() == PlayerKeySetting.FKeyType[0]) {
						((Player) panel).move_down();
						((Player) panel).setArray();
						((Player) panel).drawTetris();
					} else if (e.getKeyCode() == PlayerKeySetting.FKeyType[1]) {
						((Player) panel).move_left();
						((Player) panel).setArray();
						((Player) panel).drawTetris();
					} else if (e.getKeyCode() == PlayerKeySetting.FKeyType[2]) {
						((Player) panel).move_right();
						((Player) panel).setArray();
						((Player) panel).drawTetris();
					} else if (e.getKeyCode() == PlayerKeySetting.FKeyType[3]) {
						((Player) panel).move_drop();
						((Player) panel).setArray();
						((Player) panel).drawTetris();
					} else if (e.getKeyCode() == PlayerKeySetting.FKeyType[4]) {
						((Player) panel).move_turn();
						((Player) panel).setArray();
						((Player) panel).drawTetris();
					}
				}
			}
		});
	}

}
