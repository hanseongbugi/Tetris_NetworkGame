package TetrisGame.GameObejct;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class SmallBoard {
	public char type;
	public JLabel labelbox;
	
	public SmallBoard() {
		this.labelbox = new JLabel();
		setBoard(' ', null);
	}
	
	public void setBoard(char type, Object color) {
		this.type = type;
		if(color == null || color instanceof Color) {
			labelbox.setBackground((Color)color);
			labelbox.setIcon(null);
		}
		else {
			labelbox.setBackground(null);
			labelbox.setIcon((ImageIcon)color);
		}
	}

}