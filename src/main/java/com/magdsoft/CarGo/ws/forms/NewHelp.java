package com.magdsoft.CarGo.ws.forms;

public class NewHelp {
	String apiToken;
	String questionTitle;
	String question;
	
	public String getQuestion() {
		return question;
	}

	

	public String getApiToken() {
		return apiToken;
	}



	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}



	public String getQuestionTitle() {
		return questionTitle;
	}

	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
	
}
