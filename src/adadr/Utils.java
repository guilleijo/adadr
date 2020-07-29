package adadr;

public class Utils {
	private String[] NODES = { "n0", "n1", "n2", "n3", "n4", "n5", "n6", "n7", "n8", "n9" };

	/*
	 * Helping function that simulates consistent hashing to return the node that
	 * will handle the request
	 */
	public static String getNode(String data) {
		// get hash corresponding to data
		// find out correct node
		// return node id
		String node = "n9";
		return node;
	}

	/*
	 * Helping function that handles the request. - Read: returns the data from disk
	 * - Write: writes to disk and returns success message
	 */
	public static String handleRequest(Message msg, String myId) {
		System.out.println("---- " + myId + ": Reading/Writing data from/to node ----");
		String data = "Success";
		return data;
	}

}
