import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.IOException;
import java.util.Vector;

public class Main extends JFrame {
	private JPanel introPanel;
    public Main() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setTitle("TETRIS");
        setSize(720, 640);
        
        JLabel background = new JLabel(ImageSource.intro_background_img);

        JLabel startBtn = new JLabel(ImageSource.btn_start);
        
        startBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("CLICK START!!");
                introPanel.setVisible(false);
            }
        });
        
        JLabel exitBtn = new JLabel(ImageSource.btn_exit);
        exitBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });
        
        background.setSize(720,640);
        background.setLocation(0, 0);
        startBtn.setSize(100, 100);
        startBtn.setLocation(130,400);
        exitBtn.setSize(100,100);
        exitBtn.setLocation(490,400);
        
        introPanel = new JPanel();
        introPanel.setLayout(null);
        
        introPanel.add(startBtn);
        introPanel.add(exitBtn);
        introPanel.add(background);
        add(introPanel);

        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) throws IOException {
    	new ImageSource();
        new Main();
    }
}
	

