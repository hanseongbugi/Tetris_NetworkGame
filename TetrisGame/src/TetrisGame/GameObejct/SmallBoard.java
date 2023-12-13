package TetrisGame.GameObejct;

import java.awt.Color;
import javax.swing.JLabel;

public class SmallBoard {
	public char type;
	public JLabel labelbox;
	
	public SmallBoard() {
		this.labelbox = new JLabel();
		setBoard(' ', null);
	}
	
	public void setBoard(char type, Color color) {
		this.type = type;
		labelbox.setBackground(color);
	}
}