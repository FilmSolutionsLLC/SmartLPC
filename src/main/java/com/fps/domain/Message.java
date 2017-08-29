package com.fps.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Id;

import org.springframework.data.elasticsearch.annotations.Document;
@Document(indexName = "message")
public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	Long id;
	String user;
	String message;
	LocalDate time;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public LocalDate getTime() {
		return time;
	}
	public void setTime(LocalDate time) {
		this.time = time;
	}
	public Message(Long id, String user, String message, LocalDate time) {
		super();
		this.id = id;
		this.user = user;
		this.message = message;
		this.time = time;
	}
	public Message() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
