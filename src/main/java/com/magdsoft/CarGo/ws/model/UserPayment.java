package com.magdsoft.CarGo.ws.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class UserPayment {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@CreationTimestamp
	private Date createdAt;
	@UpdateTimestamp
	private Date updatedAt; 
	private String secretKey;
	@ManyToOne
	private User user_id ;
	@ManyToOne
	private PaymentMethod user_payment_id ;
	
	
	public PaymentMethod getUser_payment_id() {
		return user_payment_id;
	}
	public void setUser_payment_id(PaymentMethod user_payment_id) {
		this.user_payment_id = user_payment_id;
	}
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
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}
