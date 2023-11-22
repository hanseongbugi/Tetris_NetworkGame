import javax.swing.ImageIcon;
import java.util.Objects;

public class ImageSource
{
	static ImageIcon img_field;
	static ImageIcon img_logo;				// 360x640
	static ImageIcon img_logo2;				// 320x480
	static ImageIcon img_button;         
	static ImageIcon img_buttonP;    
	static ImageIcon img_KNUT;
	
	static ImageIcon bg_ground;
	static ImageIcon kakao_tube;
	
	static ImageIcon block_I;
	static ImageIcon block_J;
	static ImageIcon block_L;
	static ImageIcon block_O;
	static ImageIcon block_S;
	static ImageIcon block_T;
	static ImageIcon block_Z;
	
	static ImageIcon block_blue;
	static ImageIcon block_cyan;
	static ImageIcon block_gray;
	static ImageIcon block_green;
	static ImageIcon block_lime;
	static ImageIcon block_orange;
	static ImageIcon block_puple;
	static ImageIcon block_red;
	static ImageIcon block_yellow;
	
	static ImageIcon block_blackout;
	static ImageIcon block_fast;
	static ImageIcon block_lineup_1;
	static ImageIcon block_lineup_3;
	static ImageIcon block_zigzag;
	static ImageIcon block_bomb;
	static ImageIcon block_change;
	static ImageIcon block_linedown_1;
	static ImageIcon block_linedown_3;
	static ImageIcon block_slow;
	
	static ImageIcon item_blackout;
	static ImageIcon item_fast;
	static ImageIcon item_lineup_1;
	static ImageIcon item_lineup_3;
	static ImageIcon item_zigzag;
	static ImageIcon item_bomb;
	static ImageIcon item_change;
	static ImageIcon item_linedown_1;
	static ImageIcon item_linedown_3;
	static ImageIcon item_slow;
	
	static ImageIcon info_i;
	static ImageIcon info_q;
	static ImageIcon btn_replay;
	static ImageIcon img_pencil;
	
	public ImageSource()
	{
		img_field = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/field.png")));
		img_logo = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/logo.png")));				// 360x640
		img_logo2 = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/logo2.png")));			// 320x480
		img_button = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/button.png")));         // 
		img_buttonP = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/button_push.png")));    // 
		img_KNUT = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/KNUT.png")));
		
		bg_ground = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/bg_ground.png")));
		kakao_tube = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/kakao_tube.png")));
		
		block_I = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/block/block_I.png")));
		block_J = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/block/block_J.png")));
		block_L = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/block/block_L.png")));
		block_O = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/block/block_O.png")));
		block_S = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/block/block_S.png")));
		block_T = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/block/block_T.png")));
		block_Z = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/block/block_Z.png")));
		
		block_blue = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/block/blue.png")));
		block_cyan = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/block/cyan.png")));
		block_gray = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/block/gray.png")));
		block_green = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/block/green.png")));
		block_lime = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/block/lime.png")));
		block_orange = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/block/orange.png")));
		block_puple = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/block/puple.png")));
		block_red = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/block/red.png")));
		block_yellow = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/block/yellow.png")));
		
		block_blackout = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/block/a_blackout.png")));
		block_fast = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/block/a_fast.png")));
		block_lineup_1 = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/block/a_lineup_1.png")));
		block_lineup_3 = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/block/a_lineup_3.png")));
		block_zigzag = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/block/a_zigzag.png")));
		block_bomb = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/block/p_bomb.png")));
		block_change = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/block/p_change.png")));
		block_linedown_1 = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/block/p_linedown_1.png")));
		block_linedown_3 = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/block/p_linedown_3.png")));
		block_slow = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/block/p_slow.png")));
		
		item_blackout = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/item/a_blackout.png")));
		item_fast = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/item/a_fast.png")));
		item_lineup_1 = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/item/a_lineup_1.png")));
		item_lineup_3 = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/item/a_lineup_3.png")));
		item_zigzag = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/item/a_zigzag.png")));
		item_bomb = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/item/p_bomb.png")));
		item_change = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/item/p_change.png")));
		item_linedown_1 = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/item/p_linedown_1.png")));
		item_linedown_3 = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/item/p_linedown_3.png")));
		item_slow = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/item/p_slow.png")));
		
		info_i = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/info_i.png")));
		info_q = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/info_q.png")));
		btn_replay = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/replay.png")));
		img_pencil = new ImageIcon(Objects.requireNonNull(ImageSource.class.getClassLoader().getResource("images/pencil.png")));	
	}
}