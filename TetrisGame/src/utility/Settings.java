package utility;

import javax.swing.ImageIcon;
import java.util.Objects;

public class Settings
{		
	public static ImageIcon intro_background_img;
	public static ImageIcon btn_start;
	public static ImageIcon btn_exit;
	public static ImageIcon hover_btn_start;
	public static ImageIcon hover_btn_exit;
	
	public static ImageIcon emoji1;
	public static ImageIcon emoji2;
	public static ImageIcon emoji3;
	
	public static ImageIcon ready_icon;
	public static ImageIcon Item1ImgIcon;
	public static ImageIcon Item2ImgIcon;
	public static ImageIcon Item3ImgIcon;
	
	public static ImageIcon connectIcon;
	
	public Settings()
	{
		
		intro_background_img = new ImageIcon("images/introBackground.png");
		btn_start = new ImageIcon("images/start.png");
		btn_exit = new ImageIcon("images/exit.png");
		
		hover_btn_start = new ImageIcon("images/hoverStart.png");
		hover_btn_exit = new ImageIcon("images/hoverExit.png");
	
		emoji1 = new ImageIcon("images/emoticon1.png");
		emoji2 = new ImageIcon("images/emoticon2.png");
		emoji3 = new ImageIcon("images/emoticon3.png");
		ready_icon = new ImageIcon("images/ready.png");
		
		Item1ImgIcon = new ImageIcon("images/item1.png");
		Item2ImgIcon = new ImageIcon("images/item2.png");
		Item3ImgIcon = new ImageIcon("images/item3.png");
		
		connectIcon = new ImageIcon("images/disconnect.png");
	}
}
