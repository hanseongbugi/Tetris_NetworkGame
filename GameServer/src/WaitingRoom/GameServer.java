package WaitingRoom;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.net.*;
import java.util.*;

// 채팅 서버를 관리하는 GUI를 구현하는 클래스
public class GameServer extends JFrame{


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JTextArea textArea;

	private ServerSocket socket; // 서버소켓
	private ArrayList userList = new ArrayList(); // 연결된 사용자를 저장할 배열
	
	// 메인 메서드
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameServer frame = new GameServer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// 생성자
	public GameServer() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 338, 420);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// textarea를 스크롤 가능하도록 설정
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 300, 298);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		// 서버 시작 버튼
		JButton btnServerStart = new JButton("Server Start");
		btnServerStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					socket = new ServerSocket(30000);
				} catch (NumberFormatException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				AppendText("Chat Server Running..");
				btnServerStart.setText("Chat Server Running..");
				btnServerStart.setEnabled(false); // 서버를 더이상 실행시키지 못 하게 막는다
				AcceptServer acceptServer = new AcceptServer(GameServer.this, socket, userList);
				acceptServer.start();
			}
		});
		btnServerStart.setBounds(12, 326, 300, 35);
		contentPane.add(btnServerStart);
	}
	
	// textarea에 문자열을 추가하는 메서드
	public void AppendText(String str) {
		textArea.append(str + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	// textarea에 UserMessage 객체 정보를 추가하는 메서드
	public void AppendObject(UserMessage msg) {
		textArea.append("code = " + msg.getCode() + "\n");
		textArea.append("name = " + msg.getUserName() + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}
	
	// 서버 소켓 객체 반환하는 메서드
	public ServerSocket getServerSocket() {
		return socket;
	}
}
