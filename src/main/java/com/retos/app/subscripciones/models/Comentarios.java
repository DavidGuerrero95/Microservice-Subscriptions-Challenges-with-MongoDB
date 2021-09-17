package com.retos.app.subscripciones.models;

import java.util.List;

public class Comentarios {

	private String idea;

	private List<String> comentario;

	private String username;
	
	private Integer likes;

	public Comentarios() {
	}

	public Comentarios(String idea, List<String> comentario, String username, Integer like) {
		super();
		this.idea = idea;
		this.comentario = comentario;
		this.username = username;
		this.likes = like;
	}

	public String getIdea() {
		return idea;
	}

	public void setIdea(String idea) {
		this.idea = idea;
	}

	public List<String> getComentario() {
		return comentario;
	}

	public void setComentario(List<String> comentario) {
		this.comentario = comentario;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer like) {
		this.likes = like;
	}

	

}
