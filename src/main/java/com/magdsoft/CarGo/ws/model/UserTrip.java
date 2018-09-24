package com.magdsoft.CarGo.ws.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class UserTrip {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
	@ManyToOne(cascade=CascadeType.ALL,targetEntity=User.class)
    private User user_id;
	@ManyToOne(cascade=CascadeType.ALL,targetEntity=Driver.class)
    private Driver driver_id;
	@ManyToOne(cascade=CascadeType.ALL,targetEntity=Car.class)
    private Car car_id;
	@ManyToOne(cascade=CascadeType.ALL,targetEntity=CarType.class)
    private CarType car_type;
    private String startAtLatitude;
    private String startAtLongitude;
    private String endAtLatitude;
    private String endAtLongitude;
    private String currentLatitude;
    private String currentLongitude;
    private Date startAt;
    private Date endAt;
    private int distance;
    private String startAtAddress;
    private String endAtAddress;
    private String estimatedCost;
    private String realCost;
    private String tripeCostRate;
    private int rate;
    public enum Status{
    	noDrivers,driverAccepted,driverArriving,canceledByUser,canceledByDriver,
    	oppened,waiting,inProgress,finished
    };
    private Status status; 
    private int estimatedDistance;
    private int estimatedEndLatitude;
    private int estimatedEndLongtude;
    private String comment;
    @CreationTimestamp
    private Date createdAt;
    private Date endedAt;
    private Float user_rate;
    private Float driverRate;
    private String user_comment;
    private String driver_comment;
    
    
	public Float getUser_rate() {
		return user_rate;
	}
	public void setUser_rate(Float user_rate) {
		this.user_rate = user_rate;
	}
	public Float getDriverRate() {
		return driverRate;
	}
	public void setDriverRate(Float driverRate) {
		this.driverRate = driverRate;
	}
	public String getUser_comment() {
		return user_comment;
	}
	public void setUser_comment(String user_comment) {
		this.user_comment = user_comment;
	}
	public String getDriver_comment() {
		return driver_comment;
	}
	public void setDriver_comment(String driver_comment) {
		this.driver_comment = driver_comment;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public Car getCar_id() {
		return car_id;
	}
	public void setCar_id(Car car_id) {
		this.car_id = car_id;
	}
	public CarType getCar_type() {
		return car_type;
	}
	public void setCar_type(CarType car_type) {
		this.car_type = car_type;
	}
	public String getStartAtLatitude() {
		return startAtLatitude;
	}
	public void setStartAtLatitude(String startAtLatitude) {
		this.startAtLatitude = startAtLatitude;
	}
	
	public String getStartAtLongitude() {
		return startAtLongitude;
	}
	public void setStartAtLongitude(String startAtLongitude) {
		this.startAtLongitude = startAtLongitude;
	}
	public String getEndAtLatitude() {
		return endAtLatitude;
	}
	public void setEndAtLatitude(String endAtLatitude) {
		this.endAtLatitude = endAtLatitude;
	}
	public String getEndAtLongitude() {
		return endAtLongitude;
	}
	public void setEndAtLongitude(String endAtLongitude) {
		this.endAtLongitude = endAtLongitude;
	}
	public String getCurrentLongitude() {
		return currentLongitude;
	}
	public void setCurrentLongitude(String currentLongitude) {
		this.currentLongitude = currentLongitude;
	}
	public String getCurrentLatitude() {
		return currentLatitude;
	}
	public void setCurrentLatitude(String currentLatitude) {
		this.currentLatitude = currentLatitude;
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
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public String getStartAtAddress() {
		return startAtAddress;
	}
	public void setStartAtAddress(String startAtAddress) {
		this.startAtAddress = startAtAddress;
	}
	public String getEndAtAddress() {
		return endAtAddress;
	}
	public void setEndAtAddress(String endAtAddress) {
		this.endAtAddress = endAtAddress;
	}
	public String getEstimatedCost() {
		return estimatedCost;
	}
	public void setEstimatedCost(String estimatedCost) {
		this.estimatedCost = estimatedCost;
	}
	public String getRealCost() {
		return realCost;
	}
	public void setRealCost(String realCost) {
		this.realCost = realCost;
	}
	public String getTripeCostRate() {
		return tripeCostRate;
	}
	public void setTripeCostRate(String tripeCostRate) {
		this.tripeCostRate = tripeCostRate;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	
	public int getEstimatedDistance() {
		return estimatedDistance;
	}
	public void setEstimatedDistance(int estimatedDistance) {
		this.estimatedDistance = estimatedDistance;
	}
	public int getEstimatedEndLatitude() {
		return estimatedEndLatitude;
	}
	public void setEstimatedEndLatitude(int estimatedEndLatitude) {
		this.estimatedEndLatitude = estimatedEndLatitude;
	}
	public int getEstimatedEndLongtude() {
		return estimatedEndLongtude;
	}
	public void setEstimatedEndLongtude(int estimatedEndLongtude) {
		this.estimatedEndLongtude = estimatedEndLongtude;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getEndedAt() {
		return endedAt;
	}
	public void setEndedAt(Date endedAt) {
		this.endedAt = endedAt;
	}
    
}
