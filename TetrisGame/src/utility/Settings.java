package utility;

import javax.swing.ImageIcon;
import java.util.Objects;

public class Settings
{
	static ImageIcon img_field;
	static ImageIcon img_logo;				// 360x640
	static ImageIcon img_logo2;				// 320x480
	static ImageIcon img_button;         
	static ImageIcon img_buttonP;    
	static ImageIcon img_KNUT;
	
	public static ImageIcon bg_ground;
	
	public static ImageIcon block_I;
	public static ImageIcon block_J;
	public static ImageIcon block_L;
	public static ImageIcon block_O;
	public static ImageIcon block_S;
	public static ImageIcon block_T;
	public static ImageIcon block_Z;
	
	public static ImageIcon block_blue;
	public static ImageIcon block_cyan;
	public static ImageIcon block_gray;
	public static ImageIcon block_green;
	public static ImageIcon block_lime;
	public static ImageIcon block_orange;
	public static ImageIcon block_puple;
	public static ImageIcon block_red;
	public static ImageIcon block_yellow;
	
	static ImageIcon info_i;
	static ImageIcon info_q;
	static ImageIcon btn_replay;
	static ImageIcon img_pencil;
	
	public static ImageIcon intro_background_img;
	public static ImageIcon btn_start;
	public static ImageIcon btn_exit;
	public static ImageIcon hover_btn_start;
	public static ImageIcon hover_btn_exit;
	
	public Settings()
	{
		img_field = new ImageIcon("images/field.png");
		img_logo = new ImageIcon("images/logo.png");				// 360x640
		img_logo2 = new ImageIcon("images/logo2.png");			// 320x480
		img_button = new ImageIcon("images/button.png");         // 
		img_buttonP = new ImageIcon("images/button_push.png");    // 
		img_KNUT = new ImageIcon("images/KNUT.png");
		
		bg_ground = new ImageIcon("images/bg_ground.png");
		
		block_I = new ImageIcon("images/block/block_I.png");
		block_J = new ImageIcon("images/block/block_J.png");
		block_L = new ImageIcon("images/block/block_L.png");
		block_O = new ImageIcon("images/block/block_O.png");
		block_S = new ImageIcon("images/block/block_S.png");
		block_T = new ImageIcon("images/block/block_T.png");
		block_Z = new ImageIcon("images/block/block_Z.png");
		
		block_blue = new ImageIcon("images/block/blue.png");
		block_cyan = new ImageIcon("images/block/cyan.png");
		block_gray = new ImageIcon("images/block/gray.png");
		block_green = new ImageIcon("images/block/green.png");
		block_lime = new ImageIcon("images/block/lime.png");
		block_orange = new ImageIcon("images/block/orange.png");
		block_puple = new ImageIcon("images/block/puple.png");
		block_red = new ImageIcon("images/block/red.png");
		block_yellow = new ImageIcon("images/block/yellow.png");
		
		info_i = new ImageIcon("images/info_i.png");
		info_q = new ImageIcon( "images/info_q.png");
		btn_replay = new ImageIcon( "images/replay.png");
		img_pencil = new ImageIcon( "images/pencil.png");
		
		intro_background_img = new ImageIcon("images/introBackground.png");
		btn_start = new ImageIcon("images/start.png");
		btn_exit = new ImageIcon("images/exit.png");
		
		hover_btn_start = new ImageIcon("images/hoverStart.png");
		hover_btn_exit = new ImageIcon("images/hoverExit.png");
	}
}
