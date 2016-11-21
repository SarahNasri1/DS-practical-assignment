package de.unistgt.ipvs.vs.ex1.calcSocketClient;

/**
 * Implement the connectTo-, disconnect-, and calculate-method of this class
 * as necessary to complete the assignment. You may also add some fields or methods.
 */
public class CalcSocketClient {
	private int    rcvdOKs;		// --> Number of   valid message contents
	private int    rcvdErs;		// --> Number of invalid message contents
	private int    calcRes;		// --> Calculation result (cf. 'RES')
	
	public CalcSocketClient() {
		this.rcvdOKs   = 0;
		this.rcvdErs   = 0;
		this.calcRes   = 0;
	}
	
	// Do not change this method ..
	public int getRcvdOKs() {
		return rcvdOKs;
	}

	// Do not change this method ..
	public int getRcvdErs() {
		return rcvdErs;
	}

	// Do not change this method ..
	public int getCalcRes() {
		return calcRes;
	}

	public boolean connectTo(String srvIP, int srvPort) {
		// TODO
		return false;
	}

	public boolean disconnect() {
		// TODO
		return false;
	}

	public boolean calculate(String request) {
		// TODO
		return false;
	}
}
