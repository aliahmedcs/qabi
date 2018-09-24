package com.magdsoft.CarGo.ws.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.engine.internal.Cascade;


@Entity
public class Help {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private String question;
    @Column(columnDefinition="text")
    private String answer;
    private boolean isGlobal;
    @ManyToOne(cascade=CascadeType.ALL,targetEntity=User.class)
    private User user_id;
    @ManyToOne(cascade=CascadeType.ALL,targetEntity=Driver.class)
    private Driver driver_id;
    @CreationTimestamp
    private Date createdAt;
    private Date updatedAt;
    private String questionTitle;
    private String type;
    @ManyToOne
    private Admins admin_id;
    private Integer is_active;
   
    
	public Admins getAdmin_id() {
		return admin_id;
	}
	public void setAdmin_id(Admins admin_id) {
		this.admin_id = admin_id;
	}
	public Integer getIs_active() {
		return is_active;
	}
	public void setIs_active(Integer is_active) {
		this.is_active = is_active;
	}
	public Driver getDriver_id() {
		return driver_id;
	}
	public void setDriver_id(Driver driver_id) {
		this.driver_id = driver_id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getQuestionTitle() {
		return questionTitle;
	}
	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public boolean isGlobal() {
		return isGlobal;
	}
	public void setGlobal(boolean isGlobal) {
		this.isGlobal = isGlobal;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public User getUser_id() {
		return user_id;
	}
	public void setUser_id(User user_id) {
		this.user_id = user_id;
	}
    
}
