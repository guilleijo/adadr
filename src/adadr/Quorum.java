package adadr;

import distributed.plugin.runtime.IMessage;
import distributed.plugin.runtime.engine.Entity;

public class Quorum extends Entity {

	// all possible states
	public static final int STATE_SLEEP = 0;
	public static final int STATE_IDLE = 1;
	public static final int STATE_WAITING_ACK = 2;
	public static final int STATE_DONE = 3;

	// distributed settings for WRITE_CONSISTENCY_LEVEL = "QUORUM";
	private final int REPLICATION_FACTOR = 3;
	private int quorum;
	private int acks;

	// all message label will be used in the protocol
	private static final String MSG_LABEL_REQUEST = "Request";
	private static final String MSG_LABEL_REPLICATE = "Replicate";
	private static final String MSG_LABEL_ACK = "ACK";

	public Quorum() {
		super(STATE_SLEEP);
	}

	@Override
	public void init() {
		String request = "test query";
		quorum = (this.REPLICATION_FACTOR / 2) + 1;
		acks = 0;

		String myId = this.getName();
		String toId = Utils.getNode(request);
		Message msg = new Message(myId, toId, request, this.REPLICATION_FACTOR);

		printToConsole(myId + ": Processing reqiest. Handler node is: " + toId);
		if (myId.equals(toId)) {
			printToConsole(myId + ": Handling request.");
			Utils.handleRequest(msg, myId);
			this.become(STATE_DONE);
			return;
		}

		printToConsole(myId + ": Sending request to next node. Waiting for response.");
		this.sendTo(MSG_LABEL_REQUEST, "right", msg);
		this.become(STATE_WAITING_ACK);
	}

	@Override
	public void receive(String incomingPort, IMessage message) {
		String myId = this.getName();
		String msgLabel = message.getLabel();
		Message msg = (Message) message.getContent();
		int replicationFactor = msg.getReplicationFactor();

		if (this.getState() == STATE_SLEEP || this.getState() == STATE_IDLE) {
			if (msgLabel.equals(MSG_LABEL_REQUEST)) {

				if (myId.equals(msg.getDestination())) {
					printToConsole(myId + ": Handling request.");

					String data = Utils.handleRequest(msg, myId);
					Message ackMessage = new Message(myId, "", data, replicationFactor);
					this.sendTo(MSG_LABEL_ACK, "left", ackMessage);

					if (replicationFactor > 1) {
						msg.setReplicationFactor(replicationFactor - 1);
						this.sendTo(MSG_LABEL_REPLICATE, "right", msg);
					}
				} else {
					printToConsole(myId + ": Passing request to next node.");
					this.sendTo(MSG_LABEL_REQUEST, "right", msg);
				}

			} else if (msgLabel.equals(MSG_LABEL_REPLICATE)) {
				printToConsole(myId + ": Handling request (REPLICATE).");

				String data = Utils.handleRequest(msg, myId);
				Message ackMessage = new Message(myId, "", data, replicationFactor);
				this.sendTo(MSG_LABEL_ACK, "left", ackMessage);

				if (replicationFactor > 1) {
					msg.setReplicationFactor(replicationFactor - 1);
					this.sendTo(MSG_LABEL_REPLICATE, "right", msg);
				}
			} else if (msgLabel.equals(MSG_LABEL_ACK)) {
				printToConsole(myId + ": Passing ack/data from " + msg.getSender() + " to previous node.");
				this.sendTo(MSG_LABEL_ACK, "left", msg);
			}

			this.become(STATE_IDLE);
		} else if (this.getState() == STATE_WAITING_ACK) {
			acks++;
			if (msgLabel.equals(MSG_LABEL_ACK)) {
				printToConsole(myId + ": Ack/Data received from: " + msg.getSender());
			} else if (msgLabel.equals(MSG_LABEL_REPLICATE)) {
				printToConsole(myId + ": Handling request (REPLICATE).");

				if (replicationFactor > 1) {
					msg.setReplicationFactor(replicationFactor - 1);
					this.sendTo(MSG_LABEL_REPLICATE, "right", msg);
				}
			}

			if (acks == quorum) {
				printToConsole(myId + ": QUORUM achieved! Returning data to client.");
				this.become(STATE_DONE);
			} else {
				printToConsole(myId + ": Waiting for more acks.");
			}
		}
	}

	@Override
	public void alarmRing() {
		// this protocol does not need this function
	}

}
