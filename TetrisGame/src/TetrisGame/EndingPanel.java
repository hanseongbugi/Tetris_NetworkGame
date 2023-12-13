package TetrisGame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import WaitingRoom.WaitingPanel;

// 종료 화면
public class EndingPanel extends JPanel{
	private String loser;
	private JLabel state;
	public EndingPanel(TetrisGame tetris, String loser) {
		state = new JLabel();
		add(state);
		this.loser = loser;
		if(loser.equals(WaitingPanel.userName)) {
			state.setText(loser + "님은 패배하였습니다.");
		}else {
			state.setText(WaitingPanel.userName+"님은 승리하였습니다.");
		}
		
		
		JButton backBtn = new JButton("BACK");
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tetris.isChange = true;
				tetris.isInit = true;
				EndingPanel.this.setVisible(false);
			}
		});
		JButton exitBtn = new JButton("EXIT");
		exitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		add(backBtn);
		add(exitBtn);
	}

}
