package com.magdsoft.CarGo.ws.forms;

public class CancelTrip {
    private String apiToken;
    private Integer tripId;
    private String reason;
   private float cutMoney;
    public float getCutMoney() {
	return cutMoney;
}
public void setCutMoney(float cutMoney) {
	this.cutMoney = cutMoney;
}
	private Boolean isCanceledByUser;
	 
    
	
	public String getApiToken() {
		return apiToken;
	}
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}
	public Integer getTripId() {
		return tripId;
	}
	public void setTripId(Integer tripId) {
		this.tripId = tripId;
	}
	
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	/*public Integer getCutMoney() {
		return cutMoney;
	}
	public void setCutMoney(Integer cutMoney) {
		this.cutMoney = cutMoney;
	}*/
	public Boolean getIsCanceledByUser() {
		return isCanceledByUser;
	}
	public void setIsCanceledByUser(Boolean isCanceledByUser) {
		this.isCanceledByUser = isCanceledByUser;
	}
    
}
