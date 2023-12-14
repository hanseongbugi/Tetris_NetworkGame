package TetrisGame.GameObejct;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

// 작은 보드 클래스 (라이벌의 게임 판과 다음 블록을 알려줄 때 사용)
public class SmallBoard {
	public char type;
	public JLabel labelbox;
	
	public SmallBoard() {
		this.labelbox = new JLabel();
		setBoard(' ', null);
	}
	
	// 하나의 작은 보드를 채우는 함수
	// Color 인 경우는 블록이 없는 경우 (빈 보드 상태를 나타냄)
	// ImageIcon 인 경우 보드에 블록이 있어 블록을 그려야 하는 경우
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