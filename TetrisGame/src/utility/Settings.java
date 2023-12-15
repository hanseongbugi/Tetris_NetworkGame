package utility;

import javax.swing.ImageIcon;

// Tetris Game에 필요한 이미지들을 생성하는 Class 
public class Settings
{		
	public static ImageIcon intro_background_img;
	public static ImageIcon ending_background_img;
	public static ImageIcon btn_start;
	public static ImageIcon btn_exit;
	public static ImageIcon btn_back;
	public static ImageIcon hover_btn_start;
	public static ImageIcon hover_btn_exit;
	public static ImageIcon hover_btn_back;
	
	public static ImageIcon emoji1;
	public static ImageIcon emoji2;
	public static ImageIcon emoji3;
	
	public static ImageIcon ready_icon;
	public static ImageIcon Item1ImgIcon;
	public static ImageIcon Item2ImgIcon;
	public static ImageIcon Item3ImgIcon;
	
	public static ImageIcon connectIcon;
	
	public static ImageIcon blockBlue;
	public static ImageIcon blockCyan;
	public static ImageIcon blockGray;
	public static ImageIcon blockGreen;
	public static ImageIcon blockLime;
	public static ImageIcon blockOrange;
	public static ImageIcon blockPuple;
	public static ImageIcon blockRed;
	public static ImageIcon blockYellow;
	
	public static ImageIcon btn_send;
	
	public static ImageIcon gameBackground;
	
	public static ImageIcon playerImage1;
	public static ImageIcon playerImage2;
	public static ImageIcon winnerImage;
	public static ImageIcon loserImage;
	public Settings()
	{
		
		intro_background_img = new ImageIcon("images/introBackground.png");
		btn_start = new ImageIcon("images/start.png");
		btn_exit = new ImageIcon("images/exit.png");
		btn_back = new ImageIcon("images/back.png");
		
		hover_btn_start = new ImageIcon("images/hoverStart.png");
		hover_btn_exit = new ImageIcon("images/hoverExit.png");
		hover_btn_back = new ImageIcon("images/hoverBack.png");
	
		emoji1 = new ImageIcon("images/emoticon1.png");
		emoji2 = new ImageIcon("images/emoticon2.png");
		emoji3 = new ImageIcon("images/emoticon3.png");
		ready_icon = new ImageIcon("images/ready.png");
		
		Item1ImgIcon = new ImageIcon("images/item1.png");
		Item2ImgIcon = new ImageIcon("images/item2.png");
		Item3ImgIcon = new ImageIcon("images/item3.png");
		
		connectIcon = new ImageIcon("images/disconnect.png");
		
		blockBlue = new ImageIcon("images/block/blue.png");
		blockCyan = new ImageIcon("images/block/cyan.png");
		blockGray = new ImageIcon("images/block/gray.png");
		blockGreen = new ImageIcon("images/block/green.png");
		blockLime = new ImageIcon("images/block/lime.png");
		blockOrange = new ImageIcon("images/block/orange.png");
		blockPuple = new ImageIcon("images/block/puple.png");
		blockRed = new ImageIcon("images/block/red.png");
		blockYellow = new ImageIcon("images/block/yellow.png");
		
		btn_send =  new ImageIcon("images/send.png");
		
		gameBackground = new ImageIcon("images/bg_ground.png");
		
		playerImage1 = new ImageIcon("images/kurome.png");
		playerImage2 = new ImageIcon("images/mymelody.png");
		winnerImage = new ImageIcon("images/happy.png"); 
		loserImage =  new ImageIcon("images/sad.png"); 
	}
}
