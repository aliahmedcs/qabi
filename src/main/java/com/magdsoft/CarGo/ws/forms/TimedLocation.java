package com.magdsoft.CarGo.ws.forms;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class TimedLocation {
	private Date time;
	
	@NotNull
	private Double longitude;
	
	@NotNull
	private Double latitude;

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
}
