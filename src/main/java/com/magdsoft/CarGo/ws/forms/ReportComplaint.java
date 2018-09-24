package com.magdsoft.CarGo.ws.forms;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class ReportComplaint {
    String apiToken;
    int tripId;
    int complaintTypeId;
    
   
	String complaintDescription;
     List<MultipartFile> image;
     String Title;
	

	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	
	
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
	public int getComplaintTypeId() {
		return complaintTypeId;
	}
	public void setComplaintTypeId(int complaintTypeId) {
		this.complaintTypeId = complaintTypeId;
	}
	public String getComplaintDescription() {
		return complaintDescription;
	}
	public void setComplaintDescription(String complaintDescription) {
		this.complaintDescription = complaintDescription;
	}
	public List getImage() {
		return image;
	}
	public void setImage(List image) {
		this.image = image;
	}
}
