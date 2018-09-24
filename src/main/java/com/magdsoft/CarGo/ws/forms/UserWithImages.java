package com.magdsoft.CarGo.ws.forms;

import javax.validation.constraints.Digits;

import org.hibernate.validator.constraints.Email;
import org.springframework.web.multipart.MultipartFile;

public class UserWithImages {
	private String apiToken;
	private String name;
	
	private MultipartFile image;
	
	@Email
	private String email;
	
	@Digits(integer=35, fraction=0)
	private String phone;

	
	public String getApiToken() {
		return apiToken;
	}

	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
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
	
	
	
}
