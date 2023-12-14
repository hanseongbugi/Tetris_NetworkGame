package WaitingRoom;

import java.io.*;

public class UserMessage implements Serializable{
	private static final long serialVersionID = 1L;
	private int userID;
	private String code; // 100: 로그인, 400: 로그아웃, 200: 채팅메시지
	private String userName;
	private int userNum;
	private String data;
	private boolean isDead;
	
	private String[] userList = new String[2];
	private char[][] blockStatus = new char[10][20];
	private int attackLines;
	private int item;
	private int emoji;
	
	private boolean isReady;
	
	public UserMessage(String userName, String code) {
		this.userName= userName;
		this.code =code;
	}
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getData() {
		return data;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int id) {
		this.userID = id;
	}

	public void setData(String data) {
		this.data = data;
	}
	public int getUserNum() {
		return userNum;
	}
	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}
	public boolean getIsDead() {
		return isDead;
	}
	public void setIsDead(boolean isDead) {
		this.isDead = isDead;
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
