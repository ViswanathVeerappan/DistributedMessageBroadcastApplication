package com.project.server;

import java.io.Serializable;

public class MessageBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 389409945879934058L;

	private String message;
	private String senderId;
	private Integer priority;
	private boolean isDeliverable;

	public MessageBean(String message, String senderId, Integer priority,
			boolean isDeliverable) {
		this.message = message;
		this.senderId = senderId;
		this.priority = priority;
		this.isDeliverable = isDeliverable;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public boolean isDeliverable() {
		return isDeliverable;
	}

	public void setDeliverable(boolean isDeliverable) {
		this.isDeliverable = isDeliverable;
	}

}
