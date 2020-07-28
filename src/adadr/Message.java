package adadr;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
	private String coordinator;
	private String handler;
	private String data;
	private Date timestamp;

	private static final long serialVersionUID = 0;

	public Message(String coordinator, String handler, String data) {
		this.coordinator = coordinator;
		this.handler = handler;
		this.data = data;
		this.timestamp = new Date();
	}

	public Message() {
		// TODO Auto-generated constructor stub
	}

	public String getCoordinator() {
		return coordinator;
	}

	public void setCoordinator(String coordinator) {
		this.coordinator = coordinator;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
