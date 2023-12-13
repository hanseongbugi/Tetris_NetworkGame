package TetrisGame;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import WaitingRoom.WaitingPanel;

public class ChatPanel extends JPanel{
	
	public JTextArea chatTextArea;
	private String [] users = new String[2];
	private JTextField textChat;
	private JLabel chatBtn;
	private WaitingPanel waitingPanel;
	
	public ChatPanel(WaitingPanel waitingPanel) {
		this.waitingPanel = waitingPanel;
		
		setBorder(new EmptyBorder(5,5,5,5));
		setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 10, 200, 540);
		add(scrollPane);
		
		chatTextArea = new JTextArea();
		chatTextArea.setEditable(false);
		scrollPane.setViewportView(chatTextArea);
		
		JLabel chatBtn = new JLabel("SEND");
		chatBtn.setBounds(0, 205, 25, 25);
		chatBtn.setBackground(Color.WHITE);
		chatBtn.setOpaque(true);
		add(chatBtn);
		
		textChat = new JTextField("");
		textChat.setBounds(0,550,200,35);
		add(textChat);
		
		textChat.addActionListener(new ChatAction());
	}
	public void setChatMessage(String username, String chatMessage) {
		chatTextArea.append("[" + username + "] " + chatMessage + "\n");
		chatTextArea.setCaretPosition(chatTextArea.getText().length());
	}
	
	// TextField에 엔터키가 입력시 서버에게 내용 전달
	class ChatAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JTextField textField = (JTextField) e.getSource();
			String text = textField.getText();
			if (!text.equals(""))
				waitingPanel.sendChat(text);
			textField.setText("");
		}
	}
}
