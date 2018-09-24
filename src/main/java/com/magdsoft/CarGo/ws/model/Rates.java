package com.magdsoft.CarGo.ws.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
@Entity
public class Rates {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private Float userRate;
	private Float driverRate;
	@Column(columnDefinition="text")
	private String userComment;
	@Column(columnDefinition="text")
	private String driverComment;
	@OneToOne
	private UserTrip trip_id;
	@CreationTimestamp
	private Date createdAt;
	@UpdateTimestamp
	private Date updatedAt;
	
	
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
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Float getUserRate() {
		return userRate;
	}
	public void setUserRate(Float userRate) {
		this.userRate = userRate;
	}
	public Float getDriverRate() {
		return driverRate;
	}
	public void setDriverRate(Float driverRate) {
		this.driverRate = driverRate;
	}
	public String getUserComment() {
		return userComment;
	}
	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}
	public String getDriverComment() {
		return driverComment;
	}
	public void setDriverComment(String driverComment) {
		this.driverComment = driverComment;
	}
	public UserTrip getTrip_id() {
		return trip_id;
	}
	public void setTrip_id(UserTrip trip_id) {
		this.trip_id = trip_id;
	} 
	
	

}
