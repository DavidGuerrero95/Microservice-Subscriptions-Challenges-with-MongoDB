package com.retos.app.subscripciones.models;

public class Ideas {

	private String username;

	private String ideas;

	public Ideas(String username, String ideas) {
		super();
		this.username = username;
		this.ideas = ideas;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIdeas() {
		return ideas;
	}

	public void setIdeas(String ideas) {
		this.ideas = ideas;
	}

}
