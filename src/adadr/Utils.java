package adadr;

public class Utils {
	private String[] NODES = {"n0", "n1",  "n2",  "n3",  "n4",  "n5",  "n6",  "n7",  "n8",  "n9"};
	
	
	/*
	 * Helping function
	 */
	public static String[] getNodes(String data, int replication_factor) {
		String hash = getHash(data);
		String[] nodes = {"n3", "n4"};
		return nodes;
	}

	/*
	 * Helping function
	 */
	public static String getHash(String data) {
		String hash = "Hashed data" + data;
		return hash;
	}

	/*
	 * Helping function
	 */
	public static void handleData(Message msg) {
		System.out.println("Reading/Writing data from/to node " + msg.getHandler());
	}

}
