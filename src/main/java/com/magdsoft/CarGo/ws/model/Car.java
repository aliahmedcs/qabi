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
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Car {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String name;
	private String color;
	private String model;
	private String manufacturer;
	private int year;
	private String licenses;
	@OneToMany(mappedBy="car_id",cascade = CascadeType.ALL,targetEntity = DriverCar.class)
	private List<DriverCar> car_driver=new ArrayList<DriverCar>();
	@Column(unique=true)
	private String chassisNumber;
	private int carDriver;
	private boolean isActive;
	private Date startAt;
	private Date endAt;
	@CreationTimestamp
	private Date createAt;
	private Date updateAt;
	private Double latitude;
	private Double longitude;
//	@ManyToOne(cascade=CascadeType.ALL,targetEntity=CarType.class)
//	private CarType currentCarType;
	@ManyToOne(cascade=CascadeType.ALL,targetEntity=CarType.class)
	private CarType carType;
	private boolean isOnLine;
	private String licenseNumber;
	private String carNumber;
	private Integer bearing;
	
	public String getLicenseNumber() {
		return licenseNumber;
	}
	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}
	public String getCarNumber() {
		return carNumber;
	}
	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}
	public boolean isOnLine() {
		return isOnLine;
	}
	public void setOnLine(boolean isOnLine) {
		this.isOnLine = isOnLine;
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
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getLicenses() {
		return licenses;
	}
	public void setLicenses(String licenses) {
		this.licenses = licenses;
	}
	public String getChassisNumber() {
		return chassisNumber;
	}
	public void setChassisNumber(String chassisNumber) {
		this.chassisNumber = chassisNumber;
	}
	public int getCarDriver() {
		return carDriver;
	}
	public void setCarDriver(int carDriver) {
		this.carDriver = carDriver;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
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
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public List<DriverCar> getCar_driver() {
		return car_driver;
	}
	public void setCar_driver(List<DriverCar> car_driver) {
		this.car_driver = car_driver;
	}
	/*public CarType getCurrentCarType() {
		return currentCarType;
	}
	public void setCurrentCarType(CarType currentCarType) {
		this.currentCarType = currentCarType;
	}*/
	public CarType getCarType() {
		return carType;
	}
	public void setCarType(CarType carType) {
		this.carType = carType;
	}
	
	public Integer getBearing() {
		return bearing;
	}
	public void setBearing(Integer bearing) {
		this.bearing = bearing;
	}

}
