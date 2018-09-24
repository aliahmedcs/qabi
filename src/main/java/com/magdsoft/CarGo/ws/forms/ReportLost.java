package com.magdsoft.CarGo.ws.forms;

public class ReportLost {
	private String apiToken;
	private int tripId;
	
	
	public String getApiToken() {
		return apiToken;
	}
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}
	public int getTripId() {
		return tripId;
	}
	public void setTripId(int tripId) {
		this.tripId = tripId;
	}

}
