package com.magdsoft.CarGo.ws.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Driver {
	
	/*@Transient
	private String status;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}*/
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
	@Column(unique=true)
	private String apiToken;
    private String name;
    @Column(unique=true)
    private String email;
    @Column(unique=true)
    private String phone;
    private String password;
    private String profileImage;
    private boolean isActive;
    private String job;
    private String age;
    private String address;
    public enum MilitaryStatus{performed,Adjourned,excempted}
    @Column(columnDefinition="varchar")
//    @Enumerated(EnumType.STRING)
    private MilitaryStatus militaryStatus;
    public enum SocialStatus{single,married,divorced}
    @Column(columnDefinition="varchar")
//    @Enumerated(EnumType.STRING)
    public SocialStatus socialStatus;
    @Column(unique=true)
    private String idNumber;
    private Double rating;
	@OneToMany(mappedBy="driver_id",cascade = CascadeType.ALL,targetEntity = DriverCar.class)
	 private List<DriverCar> driver_car=new ArrayList<DriverCar>();
     @CreationTimestamp
    private Date createdAt;
     @UpdateTimestamp
    private Date updatedAt;
    private Date startAt;
    private Date endAt;
    private Integer activationCode;
    private String military_doc;
    
    
   /* @OneToMany(cascade = CascadeType.ALL,targetEntity = Profit.class)
   	@JoinColumn(name = "profit_id")
       private List<Profit> profit_id=new ArrayList<Profit>();*/
    
   
	public String getMilitary_doc() {
		return military_doc;
	}
	public String getApiToken() {
		return apiToken;
	}
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}
	public void setMilitary_doc(String military_doc) {
		this.military_doc = military_doc;
	}
	public Integer getActivationCode() {
		return activationCode;
	}
	public void setActivationCode(Integer activationCode) {
		this.activationCode = activationCode;
	}
	private String licenes;
    private String CriminalStatus;
    
  
  
	public MilitaryStatus getMilitaryStatus() {
		return militaryStatus;
	}
	public void setMilitaryStatus(MilitaryStatus militaryStatus) {
		this.militaryStatus = militaryStatus;
	}
	public SocialStatus getSocialStatus() {
		return socialStatus;
	}
	public void setSocialStatus(SocialStatus socialStatus) {
		this.socialStatus = socialStatus;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	
   
	public List<DriverCar> getDriver_car() {
		return driver_car;
	}
	public void setDriver_car(List<DriverCar> driver_car) {
		this.driver_car = driver_car;
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
	public Date getStartAt() {
		return startAt;
	}
	public void setStartAt(Date startAt) {
		this.startAt = startAt;
	}
	public Date getEndAt() {
		return endAt;
	}
	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}
	public String getLicenes() {
		return licenes;
	}
	public void setLicenes(String licenes) {
		this.licenes = licenes;
	}
	public String getCriminalStatus() {
		return CriminalStatus;
	}
	public void setCriminalStatus(String criminalStatus) {
		CriminalStatus = criminalStatus;
	} 
    
    /**
	 * @return the rating
	 */
	public Double getRating() {
		return rating;
	}
	
	/**
	 * @param rating the rating to set
	 */
	public void setRating(Double rating) {
		this.rating = rating;
	}
}
