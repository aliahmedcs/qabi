package com.magdsoft.CarGo.ws.model;

import java.util.Date;

import javax.persistence.CascadeType;
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

@Entity
public class UserComplaintImage {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    
    @ManyToOne(cascade=CascadeType.ALL,targetEntity=UserComplaint.class)
    private UserComplaint user_complaint;
    
    
    @CreationTimestamp
    private Date createdAt;
    private Date updatedAt;
    private String image;
    
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	
	public UserComplaint getUser_complaint() {
		return user_complaint;
	}
	public void setUser_complaint(UserComplaint user_complaint) {
		this.user_complaint = user_complaint;
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
