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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class DriverCar {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
   private int id;
   @ManyToOne(cascade=CascadeType.ALL,targetEntity=Car.class)
   private Car car_id;
   @ManyToOne(cascade=CascadeType.ALL,targetEntity=Driver.class)
   private Driver driver_id;
   private boolean isActive;
   private Date starteAt;
   private Date endAt;
   @CreationTimestamp
   private Date createdAt;
   private Date updatesAt;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public Car getCar_id() {
	return car_id;
}
public void setCar_id(Car car_id) {
	this.car_id = car_id;
}
public Driver getDriver_id() {
	return driver_id;
}
public void setDriver_id(Driver driver_id) {
	this.driver_id = driver_id;
}
public boolean isActive() {
	return isActive;
}
public void setActive(boolean isActive) {
	this.isActive = isActive;
}
public Date getStarteAt() {
	return starteAt;
}
public void setStarteAt(Date starteAt) {
	this.starteAt = starteAt;
}
public Date getEndAt() {
	return endAt;
}
public void setEndAt(Date endAt) {
	this.endAt = endAt;
}
public Date getCreatedAt() {
	return createdAt;
}
public void setCreatedAt(Date createdAt) {
	this.createdAt = createdAt;
}
@UpdateTimestamp
@Temporal(TemporalType.TIMESTAMP)
public Date getUpdatesAt() {
	return updatesAt;
}
public void setUpdatesAt(Date updatesAt) {
	this.updatesAt = updatesAt;
}
   
    
}
