package TetrisGame.GameObejct;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
public class Board {
	
	private String status; //Empty CurrentFall AlreadySet
	private char type; //'O' 'L' 'J' 'I' 'Z' 'S' 'T' 'V' '-' '.' ' '
	public JLabel labelbox;
	
	public Board() {
		this.labelbox = new JLabel();
		setBoard(' ', null, "Empty");
	}
	
	public void setBoard(char type, Object color, String status) {
		this.type = type;
		if(color == null || color instanceof Color) {
			labelbox.setBackground((Color)color);
			labelbox.setIcon(null);
		}
		else {
			labelbox.setBackground(null);
			labelbox.setIcon((ImageIcon)color);
		}
		setStatus(status);
	}

	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public char getType() {
		return type;
	}
	
	public String getStatus() {
		return status;
	}
	
}
