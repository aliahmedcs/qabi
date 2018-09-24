package com.magdsoft.CarGo.ws.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class AdminPermission {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	@ManyToOne
	private Admins admin_id ;
	@ManyToOne
	private Permissions permission_id ;
	@CreationTimestamp
	private Date created_at ;
    @UpdateTimestamp
	private Date updated_at ;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Admins getAdmin_id() {
		return admin_id;
	}
	public void setAdmin_id(Admins admin_id) {
		this.admin_id = admin_id;
	}
	public Permissions getPermission_id() {
		return permission_id;
	}
	public void setPermission_id(Permissions permission_id) {
		this.permission_id = permission_id;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public Date getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

    
}
