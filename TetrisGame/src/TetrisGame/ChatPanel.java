package TetrisGame;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import WaitingRoom.WaitingPanel;
import utility.Settings;

// 체팅을 담당하는 패널
public class ChatPanel extends JPanel{
	private GamePanel gamePanel;
	
	public JTextArea chatTextArea;
	private JTextField textChat;
	private JLabel chatBtn;
	private WaitingPanel waitingPanel;
	
	public ChatPanel(WaitingPanel waitingPanel, GamePanel gamePanel) {
		this.waitingPanel = waitingPanel;
		this.gamePanel = gamePanel;
		
		setBorder(new EmptyBorder(5,5,5,5));
		setLayout(null);
		setBackground(new Color(173, 216, 250)); //배경색 하늘색 지정
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 10, 230, 540);
		add(scrollPane);
		
		chatTextArea = new JTextArea();
		chatTextArea.setEditable(false);
		scrollPane.setViewportView(chatTextArea);
		
		chatBtn = new JLabel();
		chatBtn.setIcon(Settings.btn_send);
		chatBtn.setBounds(180, 550, 35, 35);
		chatBtn.setOpaque(true);
		chatBtn.addMouseListener(new MouseChatAction());
		add(chatBtn);
		
		textChat = new JTextField("");
		textChat.setBounds(0,550,175,35);
		add(textChat);
		
		textChat.addActionListener(new ChatAction());
		textChat.addMouseListener(new ChatFocusListener());
		
		JLabel gameGround = new JLabel();
		gameGround.setIcon(Settings.gameBackground);
		gameGround.setBounds(0, 480, 250, 190);
		add(gameGround);;
	}
	public void setChatMessage(String username, String chatMessage) {
		chatTextArea.append("[" + username + "] " + chatMessage + "\n");
		chatTextArea.setCaretPosition(chatTextArea.getText().length());
	}
	public void setConnectedFocus(boolean flag) {
		textChat.setFocusable(flag);
		if(flag) textChat.requestFocus();
	}
	
	// TextField에 엔터키가 입력시 서버에게 내용 전달
	class ChatAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JTextField textField = (JTextField) e.getSource();
			String text = textField.getText();
			if (!text.equals(""))
				waitingPanel.sendChat(text);
			textField.setText("");
		}
	}
	class MouseChatAction extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			String text = textChat.getText();
			if (!text.equals(""))
				waitingPanel.sendChat(text);
			textChat.setText("");
		}
	}
	class ChatFocusListener extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			setConnectedFocus(true);
			gamePanel.setFocusable(false);
			
		}
	}
}
