package WaitingRoom;

import java.io.*;
public class UserMessage implements Serializable{
	private static final long serialVersionID = 1L;
	private String userID;
	private String code; // 100: 로그인, 400: 로그아웃, 200: 채팅메시지
	private String data;
	
	public UserMessage(String id, String code, String msg) {
		this.userID= id;
		this.code =code;
		this.data = msg;
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

	public String getUserID() {
		return userID;
	}

	public void setUserID(String id) {
		this.userID = id;
	}

	public void setData(String data) {
		this.data = data;
	}
}
