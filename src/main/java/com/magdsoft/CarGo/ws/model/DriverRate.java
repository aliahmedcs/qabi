package com.magdsoft.CarGo.ws.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class DriverRate {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
	@ManyToOne(cascade = CascadeType.ALL,targetEntity = User.class)
	private User user_id;
	@OneToOne
    private UserTrip trip_id;
	@ManyToOne(cascade = CascadeType.ALL,targetEntity = Driver.class)
	private Driver driver_id;//=new ArrayList<Driver>();
    private int rateValue;
    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;
    
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public UserTrip getTrip_id() {
		return trip_id;
	}
	public void setTrip_id(UserTrip trip_id) {
		this.trip_id = trip_id;
	}
	public User getUser_id() {
		return user_id;
	}
	public void setUser_id(User user_id) {
		this.user_id = user_id;
	}
	public Driver getDriver_id() {
		return driver_id;
	}
	public void setDriver_id(Driver driver_id) {
		this.driver_id = driver_id;
	}
	public int getRateValue() {
		return rateValue;
	}
	public void setRateValue(int rateValue) {
		this.rateValue = rateValue;
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
