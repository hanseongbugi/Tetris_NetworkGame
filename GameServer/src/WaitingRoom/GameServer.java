package WaitingRoom;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.net.*;
import java.util.*;

public class GameServer extends JFrame{


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JTextArea textArea;

	private ServerSocket socket; // 서버소켓
	private ArrayList userList = new ArrayList(); // 연결된 사용자를 저장할 배열
	
	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the frame.
	 */
	public GameServer() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 338, 420);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 300, 298);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);


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
	
	public void AppendText(String str) {
		textArea.append(str + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	public void AppendObject(UserMessage msg) {
		textArea.append("code = " + msg.getCode() + "\n");
		textArea.append("name = " + msg.getUserName() + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}
	public ServerSocket getServerSocket() {
		return socket;
	}
}
