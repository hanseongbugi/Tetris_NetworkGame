package TetrisGame.GameObejct;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

// 게임 보드 클래스
public class Board {
	
	private String status; //Empty (빈 상태) FallingBlock (떨어지고 있는 블록) StopBlock (블록이 닿아 멈춘 상태)
	private char type; //'O' 'L' 'J' 'I' 'Z' 'S' 'T' 'V' '-' '.' ' '
	public JLabel labelbox; // 이미지(블록)나 색(블록이 아닌 경우)을 담을 때 사용
	
	public Board() {
		this.labelbox = new JLabel();
		setBoard(' ', null, "Empty");
	}
	
	// 하나의 보드를 채우는 함수
	// Color 인 경우는 블록이 없는 경우 (빈 보드 상태를 나타냄)
	// ImageIcon 인 경우 보드에 블록이 있어 블록을 그려야 하는 경우
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
