package com.magdsoft.CarGo.ws.forms;

public class StartTrip {
	private Integer userId;
	private Integer tripId;
	private String startAddress;
	private String startLatitude;
	private String startLongitude;
	private int carId;
    private int clientId;
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public int getCarId() {
		return carId;
	}
	public void setCarId(int carId) {
		this.carId = carId;
	}

	private String apiToken;

	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getTripId() {
		return tripId;
	}
	public void setTripId(Integer tripId) {
		this.tripId = tripId;
	}
	public String getStartAddress() {
		return startAddress;
	}
	public void setStartAddress(String startAddress) {
		this.startAddress = startAddress;
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

	/**
	 * @return the apiToken
	 */
	public String getApiToken() {
		return apiToken;
	}

	/**
	 * @param apiToken the apiToken to set
	 */
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}

}
