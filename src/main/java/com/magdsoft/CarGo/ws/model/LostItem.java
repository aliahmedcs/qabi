package com.magdsoft.CarGo.ws.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class LostItem {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private boolean isActive;
    @ManyToOne(targetEntity=User.class,cascade=CascadeType.ALL)
    User user_id;
    

	@OneToOne
    private UserTrip trip_Id;
	@CreationTimestamp
    private Date createdAt;
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    public User getUser_id() {
		return user_id;
	}
	public void setUser_id(User user_id) {
		this.user_id = user_id;
	}
	
	public UserTrip getTrip_Id() {
		return trip_Id;
	}
	public void setTrip_Id(UserTrip trip_Id) {
		this.trip_Id = trip_Id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public Date getUpdatedAt() {
		return updatedAt;
	}
	
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
    
}
