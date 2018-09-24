package com.magdsoft.CarGo.ws.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class CarType {
	 @Id
	 @GeneratedValue(strategy=GenerationType.AUTO)
	 private int id;
	 private String  name;
	 @CreationTimestamp
	 private Date createdAt;
	 private Date updatesAt;
	 private float KMPrice;
	 private float minutePrice;
	 private float basefare;
	 private Boolean is_active;
	 
	public Boolean getIs_active() {
		return is_active;
	}
	public void setIs_active(Boolean is_active) {
		this.is_active = is_active;
	}
	public float getBasefare() {
		return basefare;
	}
	public void setBasefare(float basefare) {
		this.basefare = basefare;
	}
	public float getMinutePrice() {
		return minutePrice;
	}
	public void setMinutePrice(float minutePrice) {
		this.minutePrice = minutePrice;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatesAt() {
		return updatesAt;
	}
	public void setUpdatesAt(Date updatesAt) {
		this.updatesAt = updatesAt;
	}
	public float getKMPrice() {
		return KMPrice;
	}
	public void setKMPrice(float kMPrice) {
		KMPrice = kMPrice;
	}
	 
}
