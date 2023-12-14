package TetrisGame;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import WaitingRoom.WaitingPanel;
import utility.Settings;

// 종료 화면
public class EndingPanel extends JPanel {
	private TetrisGame tetris;
    private String loser;
    private JLabel state;
    private JButton backButton;
    private JButton exitButton;

    public EndingPanel(TetrisGame tetris, String loser) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Centered flow layout with horizontal and vertical gaps
        setLayout(null);
        this.tetris = tetris;
        this.loser = loser;

        state = new JLabel();
        if (loser.equals(WaitingPanel.userName)) {
            state.setText(loser + "님은 패배하였습니다.");
        } else {
            state.setText(WaitingPanel.userName + "님은 승리하였습니다.");
        }
        
        state.setBounds(200, 150, 350, 150); 
        state.setFont(new Font("고딕", Font.BOLD, 20));
        state.setForeground(Color.BLACK); 
        add(state);

        setBackground(new Color(173, 216, 250)); // 배경색 하늘색 지정

        backButton = new JButton(Settings.btn_back);
        makeButton(backButton, 130, 430);
        backButton.addMouseListener(new BackButtonListener());
        add(backButton);

        exitButton = new JButton(Settings.btn_exit);
        makeButton(exitButton, 490, 430);
        exitButton.addMouseListener(new ExitButtonListener());
        add(exitButton);
    }

    private void makeButton(JButton button, int x, int y) {
        button.setSize(100, 100);
        button.setLocation(x, y);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
    }
    class BackButtonListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			tetris.isChange = true;
            tetris.isInit = true;
            EndingPanel.this.setVisible(false);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			backButton.setIcon(Settings.hover_btn_back);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			backButton.setIcon(Settings.btn_back);
		}

	}
    
	class ExitButtonListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			exitButton.setIcon(Settings.btn_exit);
            int answer = JOptionPane.showConfirmDialog(getParent(), "종료하시겠습니까?", "confirm",
                    JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) { // 사용자가 yes를 눌렀을 경우
                System.exit(0);
            } else { // 사용자가 Yes 이외의 값을 눌렀을 경우
                System.out.println("종료를 취소합니다.");
            }
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			exitButton.setIcon(Settings.hover_btn_exit);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			exitButton.setIcon(Settings.btn_exit);
		}

	}
}
