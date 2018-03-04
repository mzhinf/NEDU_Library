package edu.nedu.nedu_library.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInfo {
	private int id;					//用户id
	private String userid;			//用户名
	private String name;			//姓名
	private String password;		//密码
	private char sex;				//性别
	private String phone;			//电话
	private String email;			//Email
	private String department;		//院系

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public char getSex() {
		return sex;
	}

	public void setSex(char sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public UserInfo() {

	}

	public UserInfo(JSONObject userjson) {
		try {
			id = userjson.getInt("id");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			userid = userjson.getString("userid");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			password = userjson.getString("password");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			name = userjson.getString("name");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			sex = userjson.getString("sex").charAt(0);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			phone = userjson.getString("phone");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			email = userjson.getString("email");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			department = userjson.getString("department");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}