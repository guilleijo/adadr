package adadr;

public class Utils {
	// private String[] NODES = { "n0", "n1", "n2", "n3", "n4", "n5", "n6", "n7", "n8", "n9" };

	/*
	 * Helping function that simulates consistent hashing to return the nodes that
	 * will handle the request. In first place returns the handler node
	 * and the rest are the replicas.
	 * eg: node = n4 and replicationFactor = 3 then, the nodes will be: n4, n5, n6
	 */
	public static String[] getNodes(String data, int replicationFactor) {
		// get hash corresponding to data
		// find out correct node and replicas
		// return nodes
		// String[] nodes = { "n9", "n0", "n1"};
		// String[] nodes = { "n3", "n4", "n5" };
		String[] nodes = { "n1", "n2", "n3" };
		return nodes;
	}

	/*
	 * Helping function that handles the request. - Read: returns the data from disk
	 * - Write: writes to disk and returns success message
	 */
	public static String handleRequest(Message msg, String myId) {
		System.out.println("---- " + myId + ": Reading/Writing data ----");
		String data = "Success";
		return data;
	}

}
