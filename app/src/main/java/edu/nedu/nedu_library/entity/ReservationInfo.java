package edu.nedu.nedu_library.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;

public class ReservationInfo {
	private int id;						//预约id
	private int b_id;					//BookInfo id
	private int u_id;					//UserInfo id
	private Timestamp reservationTime;	//预约时间

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

	public Timestamp getReservationTime() {
		return reservationTime;
	}

	public void setReservationTime(Timestamp reservationTime) {
		this.reservationTime = reservationTime;
	}

	public ReservationInfo() {
		// TODO Auto-generated constructor stub
	}

	public ReservationInfo(JSONObject reservationjson){

		try {
			id = reservationjson.getInt("id");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			b_id = reservationjson.getInt("b_id");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			u_id = reservationjson.getInt("u_id");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			reservationTime = Timestamp.valueOf(reservationjson.getString("reservationTime"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
