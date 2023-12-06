package WaitingRoom;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import TetrisGame.TetrisGame;
import utility.Settings;

public class GameInitPanel extends JPanel {
    private RoundJTextField userName; // 변경된 부분
    private Image backImg;
    private JLabel startBtn;
    private JLabel exitBtn;

    public GameInitPanel() {
        setLayout(null);
        setOpaque(true);
        System.out.println("GameInit");
        backImg = Settings.intro_background_img.getImage();

        startBtn = new JLabel(Settings.btn_start);
        startBtn.addMouseListener(new ImageButtonEvent());

        exitBtn = new JLabel(Settings.btn_exit);
        exitBtn.addMouseListener(new ImageButtonEvent());

        startBtn.setSize(100, 100);
        startBtn.setLocation(130, 430);
        exitBtn.setSize(100, 100);
        exitBtn.setLocation(490, 430);
        add(startBtn);
        add(exitBtn);

        Font font = new Font("HBIOS-SYS", Font.PLAIN, 30);

        JLabel nameLabel = new JLabel("UserName");
        nameLabel.setBounds(180, 320, 180, 30);
        nameLabel.setFont(new Font("Rockwell", Font.BOLD, 25));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setHorizontalAlignment(JTextField.CENTER);
        add(nameLabel);

        userName = new RoundJTextField(10); // 변경된 부분
        userName.setBounds(360, 320, 200, 30);
        userName.setHorizontalAlignment(JTextField.CENTER);
        userName.setBorder(BorderFactory.createCompoundBorder(null, null));
        add(userName);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backImg != null)
            g.drawImage(backImg, 0, 0, null);
    }

    public String getUserName() {
        return userName.getText();
    }

    class ImageButtonEvent extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            JLabel btn = (JLabel) e.getSource();
            if (btn == startBtn) {
                if (!userName.getText().isEmpty()) {
                    TetrisGame.isChange = true;
                    TetrisGame.isWaitingRoom = true;
                    GameInitPanel.this.setVisible(false);
                }
            }
            if (btn == exitBtn) {
                btn.setIcon(Settings.btn_exit);
                int answer = JOptionPane.showConfirmDialog(getParent(), "종료하시겠습니까?", "confirm",
                        JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) { // 사용자가 yes를 눌렀을 경우
                    System.exit(1);
                } else { // 사용자가 Yes 이외의 값을 눌렀을 경우
                    System.out.println("종료를 취소합니다.");
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            JLabel btn = (JLabel) e.getSource();
            if (btn == startBtn)
                btn.setIcon(Settings.hover_btn_start);
            if (btn == exitBtn)
                btn.setIcon(Settings.hover_btn_exit);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            JLabel btn = (JLabel) e.getSource();
            if (btn == startBtn)
                btn.setIcon(Settings.btn_start);
            if (btn == exitBtn)
                btn.setIcon(Settings.btn_exit);
        }
    }

    class RoundJTextField extends JTextField {
        private Shape shape;

        public RoundJTextField(int size) {
            super(size);
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            super.paintComponent(g);
        }

        protected void paintBorder(Graphics g) {
            g.setColor(getForeground());
            g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
        }

        public boolean contains(int x, int y) {
            if (shape == null || !shape.getBounds().equals(getBounds())) {
                shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
            return shape.contains(x, y);
        }
    }
}
