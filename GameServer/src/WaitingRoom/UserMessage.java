package WaitingRoom;

import java.io.*;

// 사용자 간에 주고받는 메시지를 표현하는 클래스
public class UserMessage implements Serializable{
	private static final long serialVersionID = 1L;
	private int userID; // 사용자 고유 ID
	private String code; // 메시지 코드
	private String userName; // 사용자 이름
	private int userNum; // 사용자 번호
	private String chatMessage; // 메시지에 포함된 데이터
	private String[] userList = new String[2]; // 서버 접속 명단
	private char[][] blockStatus = new char[10][20]; // 테트리스 블록 상태
	private boolean[] itemStatus = new boolean[3]; // 라이벌이 받는 공격 종류
	private int attackLines; // 라인 공격 횟수
	private int item; // 아이템 번호
	private int emoji; // 이모티콘 번호
	private boolean isReady;//플레이어가 게임 준비가 되어있는 지 알 수 있는 상태
	
	// 생성자
	public UserMessage(String userName, String code) {
		this.userName= userName;
		this.code =code;
	}
	
	//코드 값 반환 메서드
	public String getCode() {
		return code;
	}

	//코드 값 설정 메서드
	public void setCode(String code) {
		this.code = code;
	}

	// 데이터 값 반환 메서드
	public String getChatMessage() {
		return chatMessage;
	}

	// 사용자 ID 반환 메서드
	public int getUserID() {
		return userID;
	}

	// 사용자 ID 설정 메서드
	public void setUserID(int id) {
		this.userID = id;
	}

	//데이터 값 설정 메서드
	public void setChatMessage(String chatMessage) {
		this.chatMessage = chatMessage;
	}
	
	//사용자 번호 반환 메서드
	public int getUserNum() {
		return userNum;
	}
	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}
	public String[] getUserList(){
		return userList;
	}
	public void setUserList(String[] userList) {
		this.userList = userList;
	}
	public char[][] getBlockStatus(){
		return blockStatus;
	}
	public void setBlockStatus(char[][] blockStatus) {
		this.blockStatus = blockStatus;
	}
	
	public int getAttackLines() {
		return attackLines;
	}
	public void setAttackLines(int attackLines) {
		this.attackLines = attackLines;
	}
	
	public boolean[] getItemStatus(){
		return itemStatus;
	}
	public void setItemStatus(boolean[] itemStatus) {
		this.itemStatus = itemStatus;
	}
	
	public int getItem() {
		return item;
	}
	public void setItem(int item) {
		this.item = item;
	}
	public int getEmoji() {
		return emoji;
	}
	public void setEmoji(int emoji) {
		this.emoji = emoji;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setIsReady(boolean isReady) {
		this.isReady = isReady;
	}
	public boolean getIsReady() {
		return isReady;
	}
}
