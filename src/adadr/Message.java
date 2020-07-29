package adadr;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
	private String sender;
	private String destination;
	private String data;
	private int replicationFactor;
	private Date timestamp;

	private static final long serialVersionUID = 0;

	public Message(String sender, String destination, String data, int replicationFactor) {
		this.sender = sender;
		this.destination = destination;
		this.data = data;
		this.replicationFactor = replicationFactor;
		this.timestamp = new Date();
	}

	public Message() {
		// TODO Auto-generated constructor stub
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getReplicationFactor() {
		return replicationFactor;
	}

	public void setReplicationFactor(int replicationFactor) {
		this.replicationFactor = replicationFactor;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
