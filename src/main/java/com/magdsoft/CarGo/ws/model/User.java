package com.magdsoft.CarGo.ws.model;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
//@Table(name="USER")
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	@ManyToMany (cascade=javax.persistence.CascadeType.ALL,targetEntity=Promotion.class)
//  @JoinTable(name="user_promotion",joinColumns={@JoinColumn(name="promotion_id")},inverseJoinColumns={@JoinColumn(name="user_id")})
	private List<Promotion> promotion_id = new ArrayList<>();
	private String name;
	private String apiToken; 
	@Column(unique=true)
	private String phone;
	private String password;
	private String userImage;
	
	private Boolean isActive;
	private String userCode;
	
	private String friendCode;
	private Integer activationCode;

	private Integer points;
	@Column(unique=true)
	private String email;
	private Date createdAt=new Date();
	private Date updatedAt=new Date();
	private Integer paymentMethod;
	private Integer rate;
	
	@Column(unique=true)
	public String getApiToken() {
		return apiToken;
	}
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setActivationCode(Integer activationCode) {
		this.activationCode = activationCode;
	}
	public void setPoints(Integer points) {
		this.points = points;
	}
	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	public List<Promotion> getPromotion_id() {
		return promotion_id;
	}
	public void setPromotion_id(List<Promotion> promotion_id) {
		this.promotion_id = promotion_id;
	}
	public Integer getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(int paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	@Column(unique=true)
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUserImage() {
		return userImage;
	}
	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}
	public Boolean isActive() {
		if(isActive==null)
			isActive=false;
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public String getFriendCode() {
		return friendCode;
	}
	public void setFriendCode(String friendCode) {
		this.friendCode = friendCode;
	}
	public Integer getActivationCode() {
		return activationCode;
	}
	public void setActivationCode(int activationCode) {
		this.activationCode = activationCode;
	}
	public Integer getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	@Column(unique=true)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@CreationTimestamp
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
	public Integer getRate() {
		return rate;
	}
	public void setRate(Integer rate) {
		this.rate = rate;
	}
	
}
