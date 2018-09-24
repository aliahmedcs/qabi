package com.magdsoft.CarGo.ws.forms;

public class RequestTrip {
	private String apiToken;
	private String currentLatitude;
	private String currentLongitude;
	private String startLatitude;
	private String startLongitude;
	private String estimatedEndLatitude;
	private String estimatedEndLongitude;
	private Integer estimatedDistance;
	private String startAtAddress;
	private String endAtAddress;
	private String estimatedCost;
	private String tripCostRate;
	private Integer carTypeId;
	private Integer paymentMethodId;
	
	
	public String getApiToken() {
		return apiToken;
	}
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}
	public String getCurrentLatitude() {
		return currentLatitude;
	}
	public void setCurrentLatitude(String currentLatitude) {
		this.currentLatitude = currentLatitude;
	}
	public String getCurrentLongitude() {
		return currentLongitude;
	}
	public void setCurrentLongitude(String currentLongitude) {
		this.currentLongitude = currentLongitude;
	}
	public String getStartLatitude() {
		return startLatitude;
	}
	public void setStartLatitude(String startLatitude) {
		this.startLatitude = startLatitude;
	}
	public String getStartLongitude() {
		return startLongitude;
	}
	public void setStartLongitude(String startLongitude) {
		this.startLongitude = startLongitude;
	}
	public String getEstimatedEndLatitude() {
		return estimatedEndLatitude;
	}
	public void setEstimatedEndLatitude(String estimatedEndLatitude) {
		this.estimatedEndLatitude = estimatedEndLatitude;
	}
	public String getEstimatedEndLongitude() {
		return estimatedEndLongitude;
	}
	public void setEstimatedEndLongitude(String estimatedEndLongitude) {
		this.estimatedEndLongitude = estimatedEndLongitude;
	}
	
	public Integer getEstimatedDistance() {
		return estimatedDistance;
	}
	public void setEstimatedDistance(Integer estimatedDistance) {
		this.estimatedDistance = estimatedDistance;
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
	public String getTripCostRate() {
		return tripCostRate;
	}
	public void setTripCostRate(String tripCostRate) {
		this.tripCostRate = tripCostRate;
	}
	public Integer getCarTypeId() {
		return carTypeId;
	}
	public void setCarTypeId(Integer carTypeId) {
		this.carTypeId = carTypeId;
	}
	public Integer getPaymentMethodId() {
		return paymentMethodId;
	}
	public void setPaymentMethodId(Integer paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}
	

}
