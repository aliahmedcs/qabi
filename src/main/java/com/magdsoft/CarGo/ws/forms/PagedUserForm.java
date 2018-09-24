package com.magdsoft.CarGo.ws.forms;

public class PagedUserForm {
	private String apiToken;
	private Integer page;
	
	
	public String getApiToken() {
		return apiToken;
	}
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}
	public Integer getPage() {
		if (page == null || page == 0) {
			return 1;
		}
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	
}
