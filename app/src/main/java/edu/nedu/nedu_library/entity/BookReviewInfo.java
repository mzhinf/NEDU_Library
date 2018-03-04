package edu.nedu.nedu_library.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;

public class BookReviewInfo {
    private int id;                     //书评id
    private int u_id;					//UserInfo id
    private int b_id;					//BookInfo id
    private Timestamp reviewTime;       //评价时间
    private int grade;                  //评分
    private String bookReviewContent;   //书评内容

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

    public Timestamp getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Timestamp reviewTime) {
        this.reviewTime = reviewTime;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getBookReviewContent() {
        return bookReviewContent;
    }

    public void setBookReviewContent(String bookReviewContent) {
        this.bookReviewContent = bookReviewContent;
    }

    @Override
    public String toString() {
        JSONObject res = new JSONObject();
        try {
            res.put("id", getId());
            res.put("u_id", getU_id());
            res.put("b_id", getB_id());
            res.put("reviewTime", getReviewTime());
            res.put("grade", getGrade());
            res.put("bookReviewContent", getBookReviewContent());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res.toString();
    }

    public BookReviewInfo(){
        // TODO Auto-generated constructor stub
    }

    public BookReviewInfo(JSONObject bookReviewJson) {
        try {
            id = bookReviewJson.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            u_id = bookReviewJson.getInt("u_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            b_id = bookReviewJson.getInt("b_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            reviewTime = Timestamp.valueOf(bookReviewJson.getString("reviewTime"));
        } catch (JSONException e) {
            e.printStackTrace();
        }try {
            grade = bookReviewJson.getInt("grade");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            bookReviewContent = bookReviewJson.getString("bookReviewContent");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
