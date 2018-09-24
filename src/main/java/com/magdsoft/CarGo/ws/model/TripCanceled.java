package com.magdsoft.CarGo.ws.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class TripCanceled {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@OneToOne
	private UserTrip trip_id;
	private String reason;
	private boolean isCanceledByUser;
	@CreationTimestamp
	private Date createdAt;
	@UpdateTimestamp
	private Date updatedAt;
	private Float cutMoney;
	

	public UserTrip getTrip_id() {
		return trip_id;
	}

	public void setTrip_id(UserTrip trip_id) {
		this.trip_id = trip_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/*public UserTrip getTrip_id() {
		return trip_id;
	}

	public void setTrip_id(UserTrip trip_id) {
		this.trip_id = trip_id;
	}*/

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public boolean isCanceledByUser() {
		return isCanceledByUser;
	}

	public void setCanceledByUser(boolean isCanceledByUser) {
		this.isCanceledByUser = isCanceledByUser;
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

	public Float getCutMoney() {
		return cutMoney;
	}

	public void setCutMoney(Float cutMoney) {
		this.cutMoney = cutMoney;
	}

	

}
