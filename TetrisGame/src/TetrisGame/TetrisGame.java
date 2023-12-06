package TetrisGame;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

import WaitingRoom.GameInitPanel;
import WaitingRoom.WaitingPanel;
import utility.PlayerKeySetting;
import utility.Settings;

public class TetrisGame extends JFrame{
	
	private GamePanel gamePanel;
	private GameInitPanel initPanel;
	private WaitingPanel waitingPanel;
	
	private Timer loadingTimer = new Timer();
	private TimerTask loadingTask;
	
	public static boolean isMain;
	public static boolean isGame;
	public static boolean isChange;
	public static boolean isInit;
	public static boolean isWaitingRoom;
	
	private GameProcessThread gameThread;
	
	public static boolean player1Dead = false;
	public static boolean player2Dead = false;
	
	public TetrisGame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setTitle("TETRIS");
        setSize(720, 640);
        
        init();
        gameThread = new GameProcessThread();
        gameThread.start();
        
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
	}
	// 변수 초기화
	public void init() {
		new Settings();
		new PlayerKeySetting();
		isMain = false;
		isGame = false;
		isChange = true;
		isInit = true;
		isWaitingRoom = false;
	}
	
	// Frame에 Panel을 붙이기 위함
	public void setPanel(JPanel panel) {
		add(panel);
	}
	
	// Game Paenl 설정
	private void setGamePanel(GamePanel gamePanel) {
		Container con = getContentPane();
		con.setLayout(new BorderLayout());
		add(gamePanel, BorderLayout.CENTER);
	}
	
	public void gameModeChanged() {
		this.isChange = false;
		if(isMain) {
			isMain = false;
		}
		else if(isGame) {
			gamePanel = new GamePanel();
			setGamePanel(gamePanel);
			
			waitingPanel.setGamePanel(gamePanel);
			isGame = false;
		}else if(isInit) {
			this.isInit = false;
			initPanel = new GameInitPanel();
			
			add(initPanel);
			setVisible(true);
		}else if(isWaitingRoom) {
			this.isWaitingRoom = false;
			waitingPanel = new WaitingPanel(initPanel.getUserName());
			add(waitingPanel);
			this.setVisible(true);
		}
	}
	
	class GameProcessThread extends Thread{
		@Override
		public void run() {
			while(true) {
				if(isChange) {
					isChange = false;
					gameModeChanged();
				}
				if(player1Dead || player2Dead) {
					// 플레이어가 게임 종료 시 수행할 것
					
				}
				repaint();
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {
					return;
				}
			}
		}
	}
}
