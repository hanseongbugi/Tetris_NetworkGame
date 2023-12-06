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
import WaitingRoom.UserMessage;
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
	public void movePlayer(String[] playerInformation) {
		String keyType = playerInformation[1];
		
		Player otherPlayer;
		if(playerInformation[0].equals("1"))
			otherPlayer = player1;
		else
			otherPlayer = player2;
		if (keyType.equals(PlayerKeySetting.StringKeyType[0])) {
			otherPlayer.moveDown();
			otherPlayer.setArray();
			otherPlayer.drawTetris();
		} else if (keyType.equals(PlayerKeySetting.StringKeyType[1])) {
			otherPlayer.moveLeft();
			otherPlayer.setArray();
			otherPlayer.drawTetris();
		} else if (keyType.equals(PlayerKeySetting.StringKeyType[2])) {
			otherPlayer.moveRight();
			otherPlayer.setArray();
			otherPlayer.drawTetris();
		} else if (keyType.equals(PlayerKeySetting.StringKeyType[3])) {
			otherPlayer.moveDrop();
			otherPlayer.setArray();
			otherPlayer.drawTetris();
		} else if (keyType.equals(PlayerKeySetting.StringKeyType[4])) {
			otherPlayer.moveTurn();
			otherPlayer.setArray();
			otherPlayer.drawTetris();
		}
	}

	public void MyKey(JPanel panel) {
		panel.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (((Player) panel).gameRun && ((Player) panel).Gaming) {
					if (e.getKeyCode() == PlayerKeySetting.FKeyType[0]) {
						WaitingPanel.SendObject(new UserMessage(
								userName,"401",controlPlayerNum+"@@"+PlayerKeySetting.FKeyType[0]));
					} else if (e.getKeyCode() == PlayerKeySetting.FKeyType[1]) {
						WaitingPanel.SendObject(new UserMessage(
								userName,"401",controlPlayerNum+"@@"+PlayerKeySetting.FKeyType[1]));
					} else if (e.getKeyCode() == PlayerKeySetting.FKeyType[2]) {
						WaitingPanel.SendObject(new UserMessage(
								userName,"401",controlPlayerNum+"@@"+PlayerKeySetting.FKeyType[2]));

					} else if (e.getKeyCode() == PlayerKeySetting.FKeyType[3]) {
						WaitingPanel.SendObject(new UserMessage(
								userName,"401",controlPlayerNum+"@@"+PlayerKeySetting.FKeyType[3]));

					} else if (e.getKeyCode() == PlayerKeySetting.FKeyType[4]) {
						WaitingPanel.SendObject(new UserMessage(
								userName,"401",controlPlayerNum+"@@"+PlayerKeySetting.FKeyType[4]));
					}
				}
			}
		});
		panel.setFocusable(true);
		panel.requestFocus();
	}

}
