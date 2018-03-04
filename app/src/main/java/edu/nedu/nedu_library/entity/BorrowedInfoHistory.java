package edu.nedu.nedu_library.entity;

import java.sql.Timestamp;

public class BorrowedInfoHistory {
	private int id;						//借阅历史id
	private int b_id;					//BookInfo id
	private int u_id;					//UserInfo id
	private Timestamp borrowedTime;		//借阅时间
	private Timestamp returnTime;		//归还时间

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getB_id() {
		return b_id;
	}

	public void setB_id(int b_id) {
		this.b_id = b_id;
	}

	public int getU_id() {
		return u_id;
	}

	public void setU_id(int u_id) {
		this.u_id = u_id;
	}

	public Timestamp getBorrowedTime() {
		return borrowedTime;
	}

	public void setBorrowedTime(Timestamp borrowedTime) {
		this.borrowedTime = borrowedTime;
	}

	public Timestamp getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(Timestamp returnTime) {
		this.returnTime = returnTime;
	}

	public BorrowedInfoHistory(BorrowedInfo borrowedInfo, Timestamp returnTime) {
		this.b_id = borrowedInfo.getB_id();
		this.u_id = borrowedInfo.getU_id();
		this.borrowedTime = borrowedInfo.getBorrowedTime();
		this.returnTime = returnTime;
	}

	public BorrowedInfoHistory() {
		// TODO Auto-generated constructor stub
	}

}
