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

// 채팅을 담당하는 패널
public class ChatPanel extends JPanel{
	private GamePanel gamePanel; //게임 패널 객체
	
	public JTextArea chatTextArea; // 채팅 내용을 표시하는 textarea
	private JTextField textChat; // 채팅 입력을 받는 텍스트필드
	private JLabel chatBtn; // 채팅 전송 버튼
	private WaitingPanel waitingPanel; // 대기실 패널
	
	//생성자
	public ChatPanel(WaitingPanel waitingPanel, GamePanel gamePanel) {
		this.waitingPanel = waitingPanel;
		this.gamePanel = gamePanel;
		
		setBorder(new EmptyBorder(5,5,5,5));
		setLayout(null);
		setBackground(new Color(173, 216, 250)); //배경색 하늘색 지정
		
		// 스크롤 가능한 textarea 초기화
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 10, 230, 540);
		add(scrollPane);
		
		chatTextArea = new JTextArea();
		chatTextArea.setEditable(false);
		scrollPane.setViewportView(chatTextArea);
		
		// 채팅 전송 버튼 초기화
		chatBtn = new JLabel();
		chatBtn.setIcon(Settings.btn_send);
		chatBtn.setBounds(180, 550, 35, 35);
		chatBtn.setOpaque(true);
		chatBtn.addMouseListener(new MouseChatAction());
		add(chatBtn);
		
		// 채팅 입력을 받는 텍스트 필드 초기화
		textChat = new JTextField("");
		textChat.setBounds(0,550,175,35);
		add(textChat);
		
		// 엔터 키 입력 이벤트 처리 
		textChat.addActionListener(new ChatAction());
		textChat.addMouseListener(new ChatFocusListener());
		
		// 게임 판 배경 이미지 초기화
		JLabel gameGround = new JLabel();
		gameGround.setIcon(Settings.gameBackground);
		gameGround.setBounds(0, 480, 250, 190);
		add(gameGround);;
	}
	
	// 채팅 메시지 설정 메서드
	public void setChatMessage(String username, String chatMessage) {
		chatTextArea.append("[" + username + "] " + chatMessage + "\n");
		chatTextArea.setCaretPosition(chatTextArea.getText().length());
	}
	
	// 텍스트 필드 포커스 설정 메서드
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
	
	// 마우스 클릭으로 채팅 전송
	class MouseChatAction extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			String text = textChat.getText();
			if (!text.equals(""))
				waitingPanel.sendChat(text);
			textChat.setText("");
		}
	}
	
	// 채팅 입력을 위해 JTextField를 클릭한 경우 발생하는 이벤트 
	class ChatFocusListener extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			// TextField에 포커스를 적용
			setConnectedFocus(true);
			gamePanel.setFocusable(false);
			
		}
	}
}
