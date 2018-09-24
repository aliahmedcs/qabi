package com.magdsoft.CarGo.ws.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class UserComplaint {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@ManyToOne(cascade = CascadeType.ALL,targetEntity = UserTrip.class)
	private UserTrip trip_id;
	@ManyToOne(cascade = CascadeType.ALL,targetEntity = UserComplaintType.class)
	private UserComplaintType complaint_type;
	@Column(columnDefinition = "TEXT")
	private String description;
	
	private boolean isActive;
	@CreationTimestamp
	private Date createAt;
	private Date updateAt;
	private String type;
	
	
	public UserTrip getTrip_id() {
		return trip_id;
	}
	public void setTrip_id(UserTrip trip_id) {
		this.trip_id = trip_id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public UserComplaintType getComplaint_type() {
		return complaint_type;
	}
	public void setComplaint_type(UserComplaintType complaint_type) {
		this.complaint_type = complaint_type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	public Date getUpdateAt() {
		return updateAt;
	}
	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

}
