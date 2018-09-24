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
public class Suggestion {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(columnDefinition = "TEXT")
	private String suggest;
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = User.class)
	private User user_id;
	@CreationTimestamp
	private Date createdAt;
	private Date updatedAt;
	@ManyToOne
	private Admins assigned_to ;
	@ManyToOne
	private Admins assigned_by; 
	
	
	public Admins getAssigned_to() {
		return assigned_to;
	}

	public void setAssigned_to(Admins assigned_to) {
		this.assigned_to = assigned_to;
	}

	public Admins getAssigned_by() {
		return assigned_by;
	}

	public void setAssigned_by(Admins assigned_by) {
		this.assigned_by = assigned_by;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}

	
	public User getUser_id() {
		return user_id;
	}

	public void setUser_id(User user_id) {
		this.user_id = user_id;
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

}
