package com.magdsoft.CarGo.ws.forms;

public class EndTrip {
	private String apiToken;
	private Integer tripId;
	private String endLatitude;
	private String endLongtude;
	
	private String endAtAddress;
	private Integer distance;
	private String realCost;
	
	
	public String getApiToken() {
		return apiToken;
	}
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}
	public Integer getDistance() {
		return distance;
	}
	public String getRealCost() {
		return realCost;
	}
	public void setRealCost(String realCost) {
		this.realCost = realCost;
	}
	public void setDistance(Integer distance) {
		this.distance = distance;
	}
	public String getEndAtAddress() {
		return endAtAddress;
	}
	public void setEndAtAddress(String endAtAddress) {
		this.endAtAddress = endAtAddress;
	}
	
	public Integer getTripId() {
		return tripId;
	}
	public void setTripId(Integer tripId) {
		this.tripId = tripId;
	}
	
	public String getEndLatitude() {
		return endLatitude;
	}
	public void setEndLatitude(String endLatitude) {
		this.endLatitude = endLatitude;
	}
	public String getEndLongtude() {
		return endLongtude;
	}
	public void setEndLongtude(String endLongtude) {
		this.endLongtude = endLongtude;
	}
	
	
}
