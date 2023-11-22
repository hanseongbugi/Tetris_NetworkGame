import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;


public class GameFrame extends JFrame {
    GamePanel fgp;
    static int gameMode = 1;
    static JFrame jf;
    GamePanel enemyPanel;
    GameThread gameThread;
    private JButton startButton; // "시작" 버튼을 추가

    GameFrame() throws IOException {
        setTitle("Tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(720, 640);
        setResizable(false);

        jf = this;
        Container c = this.getContentPane();
        c.setBackground(new Color(30, 160, 255));

        switch (gameMode) {
            case 1:
                fgp = new GamePanel();
                fgp.setBounds(370, 0, 360, 640);
                add(fgp);

                enemyPanel = new GamePanel();
                enemyPanel.setBounds(0, 0, 360, 640);
                add(enemyPanel);

                MyKey1(fgp);
                break;
        }

        // "시작" 버튼을 생성하고 설정
        startButton = new JButton("시작");
        startButton.setBounds(10, 10, 100, 30); // 버튼 위치와 크기 조정
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame(); // "시작" 버튼이 클릭되면 게임을 시작하는 메서드 호출
            }
        });
        add(startButton);

        setLocationRelativeTo(getParent());
        setVisible(true);

        // 게임 스레드를 생성만 하고 시작하지 않습니다
        gameThread = new GameThread();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    gameThread.sendGameOver();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public void remakePanel() {
        fgp.highestScore.setBounds(255, 200, 205, 30);
        fgp.time.setBounds(255, 240, 205, 30);
    }

    public void MyKey1(JPanel gp) {
        gp.setFocusable(true);
        gp.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (((GamePanel) gp).gameRun && ((GamePanel) gp).Gaming) {
                    if (e.getKeyCode() == FirstPlayerKeySetting.FKeyType[0]) {
                        ((GamePanel) gp).move_down();
                        ((GamePanel) gp).setArray();
                        ((GamePanel) gp).drawTetris();
                    } else if (e.getKeyCode() == FirstPlayerKeySetting.FKeyType[1]) {
                        ((GamePanel) gp).move_left();
                        ((GamePanel) gp).setArray();
                        ((GamePanel) gp).drawTetris();
                    } else if (e.getKeyCode() == FirstPlayerKeySetting.FKeyType[2]) {
                        ((GamePanel) gp).move_right();
                        ((GamePanel) gp).setArray();
                        ((GamePanel) gp).drawTetris();
                    } else if (e.getKeyCode() == FirstPlayerKeySetting.FKeyType[3]) {
                        ((GamePanel) gp).move_drop();
                        ((GamePanel) gp).setArray();
                        ((GamePanel) gp).drawTetris();
                    } else if (e.getKeyCode() == FirstPlayerKeySetting.FKeyType[4]) {
                        ((GamePanel) gp).move_turn();
                        ((GamePanel) gp).setArray();
                        ((GamePanel) gp).drawTetris();
                    }
                }
            }
        });
    }

    // "시작" 버튼이 클릭되면 호출되는 메서드
    private void startGame() {
        // 게임 스레드를 시작합니다
        gameThread.startGame();

        // 키 이벤트를 활성화하기 위해 게임 패널에 포커스를 설정합니다
        fgp.requestFocus();
        
        // "시작" 버튼 비활성화 (이 부분은 선택적으로 추가)
        startButton.setEnabled(false);
    }

    public static void main(String[] args) throws IOException {
        new ImageSource();
        new GameFrame();
    }
}