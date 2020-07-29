package adadr;

import distributed.plugin.runtime.IMessage;
import distributed.plugin.runtime.engine.Entity;

public class One extends Entity {

	// all possible states
	public static final int STATE_SLEEP = 0;
	public static final int STATE_IDLE = 1;
	public static final int STATE_WAITING_ACK = 2;
	public static final int STATE_DONE = 3;

	// distributed settings for WRITE_CONSISTENCY_LEVEL = "ONE";
	private final int REPLICATION_FACTOR = 2;

	// all message label will be used in the protocol
	private static final String MSG_LABEL_REQUEST = "Request";
	private static final String MSG_LABEL_ACK = "ACK";

	public One() {
		super(STATE_SLEEP);
	}

	@Override
	public void init() {
		String request = "test query";
		boolean done = false;

		String myId = this.getName();
		String[] toIds = Utils.getNodes(request, this.REPLICATION_FACTOR);

		printToConsole(myId + ": Processing request. Handler node is: " + toIds[0]);
		for (String toId : toIds) {
			Message msg = new Message(myId, toId, request);

			if (myId.equals(toId)) {
				printToConsole(myId + ": Handling request.");
				Utils.handleRequest(msg, myId);
				printToConsole(myId + ": Returning data to client");

				// become done after sending all messages
				done = true;
			} else {
				printToConsole(myId + ": Sending request to next node. Waiting for response.");
				this.sendTo(MSG_LABEL_REQUEST, "right", msg);
			}

		}

		if (done) {
			this.become(STATE_DONE);
			return;
		}
		this.become(STATE_WAITING_ACK);
	}

	@Override
	public void receive(String incomingPort, IMessage message) {
		String myId = this.getName();
		String msgLabel = message.getLabel();
		Message msg = (Message) message.getContent();

		if (this.getState() == STATE_SLEEP || this.getState() == STATE_IDLE) {
			if (msgLabel.equals(MSG_LABEL_REQUEST)) {

				if (myId.equals(msg.getDestination())) {
					printToConsole(myId + ": Handling request.");

					String data = Utils.handleRequest(msg, myId);
					Message ackMessage = new Message(myId, "", data);
					this.sendTo(MSG_LABEL_ACK, "left", ackMessage);

				} else {
					printToConsole(myId + ": Passing request to next node.");
					this.sendTo(MSG_LABEL_REQUEST, "right", msg);
				}

			} else if (msgLabel.equals(MSG_LABEL_ACK)) {
				printToConsole(myId + ": Passing ack/data from " + msg.getSender() + " to previous node.");
				this.sendTo(MSG_LABEL_ACK, "left", msg);
			}

			this.become(STATE_IDLE);
		} else if (this.getState() == STATE_WAITING_ACK) {
			if (msgLabel.equals(MSG_LABEL_ACK)) {
				printToConsole(myId + ": Ack/Data received from: " + msg.getSender());
				printToConsole(myId + ": Returning data to client");
				this.become(STATE_DONE);
			}
		}
	}

	@Override
	public void alarmRing() {
		// this protocol does not need this function
	}

}
