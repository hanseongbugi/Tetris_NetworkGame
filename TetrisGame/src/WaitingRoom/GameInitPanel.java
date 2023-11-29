package WaitingRoom;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import utility.Settings;
import TetrisGame.TetrisGame;

public class GameInitPanel extends JPanel{
	private JTextField userName;
	private Image backImg;
	public GameInitPanel() {
		setLayout(null);
		setOpaque(true);
		System.out.println("GameInit");
		backImg = Settings.intro_background_img.getImage();
		
        JLabel startBtn = new JLabel(Settings.btn_start);
        startBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // System.out.println("CLICK START!!");
                //introPanel.setVisible(false);
                if(!userName.getText().isEmpty()) {
                	TetrisGame.isChange = true;
                	TetrisGame.isWaitingRoom = true;
                	GameInitPanel.this.setVisible(false);
                	// System.out.println("TetrisGame " + TetrisGame.isChange);
                }
            }
        });
        
        JLabel exitBtn = new JLabel(Settings.btn_exit);
        exitBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });
        startBtn.setSize(100, 100);
        startBtn.setLocation(130,430);
        exitBtn.setSize(100,100);
        exitBtn.setLocation(490,430);
        add(startBtn);
        add(exitBtn);
		
        Font font = new Font ("HBIOS-SYS", Font.PLAIN, 20);
        
		JLabel nameLabel = new JLabel("UserName");
		nameLabel.setBounds(200, 320, 100, 30);
		nameLabel.setFont(font);
		nameLabel.setForeground(new Color(255,186,0));
		nameLabel.setHorizontalAlignment(JTextField.CENTER);
		add(nameLabel);
		
		userName = new JTextField(10);
		userName.setBounds(360, 320, 200, 30);
		userName.setHorizontalAlignment(JTextField.CENTER);
		userName.setBorder(BorderFactory.createCompoundBorder( null, null));
		add(userName);
	
		
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(backImg!=null)
			g.drawImage(backImg, 0, 0, null);
	}
	public String getUserName() {
		return userName.getText();
	}
}
