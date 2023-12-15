package TetrisGame;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import WaitingRoom.WaitingPanel;
import utility.Settings;

// 종료 화면
public class EndingPanel extends JPanel {
	private TetrisGame tetris; // 테트리스 게임 객체
	private Image backImg; // 배경 이미지
    private String loser; // 게임에서 진 플레이어
    private JLabel state; // 게임 승패 상태 표시 라벨
    
    private JLabel imageLabel3; // 승리 이미지 라벨
	private JLabel imageLabel4; // 패배 이미지 라벨
	
    private JButton backButton; // 재시작 버튼
    private JButton exitButton; // 종료 버튼

    public EndingPanel(TetrisGame tetris, String loser) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // 중앙 정렬된 Flow 레이아웃 설정
        setLayout(null); // 레이아웃 매니저를 사용자 정의로 설정
        
        this.tetris = tetris;
        this.loser = loser;
        
        imageLabel3 = new JLabel();
		imageLabel3.setBounds(300,90,300,250); // 이미지의 위치와 크기 설정
		imageLabel3.setIcon(Settings.winnerImage);
		
		imageLabel4 = new JLabel();
		imageLabel4.setBounds(300,90,300,250); // 이미지의 위치와 크기 설정
		imageLabel4.setIcon(Settings.loserImage);

		// 게임 승패 상태 메시지 라벨 초기화
        state = new JLabel();
        if (loser.equals(WaitingPanel.userName)) {
            state.setText(loser + " 님은 패배하였습니다 ㅠ3ㅠ"); // 패배 시 username과 함께 패배 하였다는 문구가 나오고
            add(imageLabel4, JLayeredPane.DEFAULT_LAYER); // 패배 시 설정한 이미지가 같이 출력됨
        } else {
            state.setText(WaitingPanel.userName + " 님은 승리하였습니다 \\^~^/"); // 승리 시 username과 함께 승리 하였다는 문구가 나오고
            add(imageLabel3, JLayeredPane.DEFAULT_LAYER); // 승리 시 설정한 이미지가 같이 출력됨
        }
        
        state.setBounds(200, 250, 400, 200); // 라벨 위치 및 크기 설정
        state.setFont(new Font("고딕", Font.BOLD, 25)); // 폰트 설정
        state.setForeground(Color.WHITE); // 글자색 설정
        add(state);

        backImg = new ImageIcon("images/endingImage.png").getImage(); // 배경 이미지 삽입

        // 재시작 버튼 초기화
        backButton = new JButton(Settings.btn_back);
        makeButton(backButton, 130, 430);
        backButton.addMouseListener(new BackButtonListener());
        add(backButton);

        // 종료 버튼 초기화
        exitButton = new JButton(Settings.btn_exit);
        makeButton(exitButton, 490, 430);
        exitButton.addMouseListener(new ExitButtonListener());
        add(exitButton);
    }
    
    // 배경 이미지 그리기
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backImg, 0, 0, getWidth(), getHeight(), this);
    }

    // 버튼 스타일 설정 매서드
    private void makeButton(JButton button, int x, int y) {
        button.setSize(100, 100);
        button.setLocation(x, y);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
    }
    
    // 재시작 버튼 이벤트 리스너
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
    
    // 종료 버튼 이벤트 리스너
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
