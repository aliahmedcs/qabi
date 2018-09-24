package com.magdsoft.CarGo.ws.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.jpamodelgen.xml.jaxb.CascadeType;

@Entity
public class Promotion {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	@ManyToMany (cascade=javax.persistence.CascadeType.ALL,targetEntity=User.class)
//	@JoinTable(name="user_promotion",joinColumns={@JoinColumn(name="user_id")},inverseJoinColumns={@JoinColumn(name="promotion_id")})
	 
	private List<User> user_id = new ArrayList<>();
	private String code;
	private Date expDate;
	private Integer intialCount;
	private Integer point;
	private Integer countNow;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Date getExpDate() {
		return expDate;
	}
	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}
	public Integer getIntialCount() {
		return intialCount;
	}
	public void setIntialCount(Integer intialCount) {
		this.intialCount = intialCount;
	}
	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}
	public Integer getCountNow() {
		return countNow;
	}
	public void setCountNow(Integer countNow) {
		this.countNow = countNow;
	}
	public List<User> getUser_id() {
		return user_id;
	}
	public void setUser_id(List<User> user_id) {
		this.user_id = user_id;
	}
	
}
