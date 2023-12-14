package WaitingRoom;

import java.io.*;

// 사용자 간에 주고받는 메시지를 표현하는 클래스
public class UserMessage implements Serializable{
	private static final long serialVersionID = 1L;
	private int userID; // 사용자 고유 ID
	private String code; // 메시지 코드 (100: 로그인, 400: 로그아웃, 200: 채팅메시지)
	private String userName; // 사용자 이름
	private int userNum; // 사용자 번호
	private String data; // 메시지에 포함된 데이터
	private boolean isDead; // 사용자의 승리 또는 패배와 같은 상태
	
	private String[] userList = new String[2]; // 게임 중인 사용자 목록
	private char[][] blockStatus = new char[10][20]; //블록 상태 배열
	private boolean[] itemStatus = new boolean[2]; //아이템 사용 여부 배열
	private int attackLines; //공격받은 라인 수
	private int item;// 현재 아이템 종류
	private int emoji;//플레이어가 선택한 이모티콘
	
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
	public String getData() {
		return data;
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
	public void setData(String data) {
		this.data = data;
	}
	
	//사용자 번호 반환 메서드
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
	
	public boolean[] getItemStatus(){
		return itemStatus;
	}
	public void setItemStatus(boolean[] itemStatus) {
		this.itemStatus = itemStatus;
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
