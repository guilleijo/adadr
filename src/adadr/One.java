package adadr;

import distributed.plugin.runtime.IMessage;
import distributed.plugin.runtime.engine.Entity;

public class One extends Entity {

	// all possible states
	public static final int STATE_SLEEP = 0;
	public static final int STATE_PASSIVE = 1;
	public static final int STATE_WAITING_ACK = 2;
	public static final int STATE_DONE = 3;

	// distributed settings
	// private final String WRITE_CONSISTENCY_LEVEL = "ONE";
	private final int TABLE_REPLICATION_FACTOR = 3;

	// all message label will be used in the protocol
	private static final String MSG_LABEL_REQUEST = "Read/Write request";
	private static final String MSG_LABEL_ACK = "ACK";

	public One() {
		super(STATE_SLEEP);
	}

	@Override
	public void init() {
		String data = "test data";

		String myId = this.getName();
		String[] toIds = Utils.getNodes(data, this.TABLE_REPLICATION_FACTOR);
		for (String toId : toIds) {
			Message msg = new Message(myId, toId, data);

			if (myId.equals(toId)) {
				Utils.handleData(msg);
				this.become(STATE_DONE);
				return;
			}
			this.sendTo(MSG_LABEL_REQUEST, "right", msg);
		}

		this.become(STATE_WAITING_ACK);
	}

	@Override
	public void receive(String incomingPort, IMessage message) {
		printToConsole("en el receive " + this.getName() + " " + incomingPort);

		String myId = this.getName();

		String msgLabel = message.getLabel();
		Message msg = (Message) message.getContent();

		if (this.getState() == STATE_SLEEP) {
			if (myId.equals(msg.getHandler())) {
				Utils.handleData(msg);
				msg.setData("ack from " + msg.getHandler());
				this.sendTo("left", msg);
			} else {
				this.sendTo("right", msg);
			}
			this.become(STATE_PASSIVE);
		} else if (this.getState() == STATE_PASSIVE) {
			if (msgLabel.equals(MSG_LABEL_REQUEST)) {
				this.sendTo(MSG_LABEL_REQUEST, "right", msg);
			} else {
				this.sendTo(MSG_LABEL_ACK, "left", msg);
			}
		} else if (this.getState() == STATE_WAITING_ACK) {
			if (msgLabel.equals(MSG_LABEL_ACK)) {
				printToConsole("Received: " + msg.getData());
				printToConsole("Returning data to client");
				this.become(STATE_DONE);
			} else {
				// should not happen!!
			}
		}
	}

	@Override
	public void alarmRing() {
		// this protocol does not need this function
	}

}
