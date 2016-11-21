package de.unistgt.ipvs.vs.ex1.testCalc;

import java.util.ArrayList;
import java.util.List;

import de.unistgt.ipvs.vs.ex1.calcRMIclient.CalcRmiClient;
import de.unistgt.ipvs.vs.ex1.calcRMIserver.CalcRmiServer;
import de.unistgt.ipvs.vs.ex1.calcSocketClient.CalcSocketClient;
import de.unistgt.ipvs.vs.ex1.calcSocketServer.CalcSocketServer;

// This is the main test program.
// To start it properly, specify: -Djava.security.policy="${resource_loc:/TestCalc/src/policy.ini}"
public class CalcTest {
	
	/**
	 * @param args
	 */
	public static void main(String[] argv) throws Exception {
		if (argv.length != 1) {
			System.err.println("Wrong usage: CalcTest <'RMI'|'SCKT'>");
			return;
		}
		
		String srvIP   = "localhost";	// "127.0.0.1"
		int    srvPort = 12345;

		if (argv[0].equals("SCKT")) {
			socketTest(srvIP, srvPort);
		} else if (argv[0].equals("RMI")) {
			rmiTest(srvIP);
		} else {
			System.err.println("Wrong usage: CalcTest <'RMI'|'SCKT'>");
			return;
		}
	}
	
	private static void rmiTest(String srvIP) {
		System.out.println("Starting CalcRmiServer on " + srvIP);
		CalcRmiServer cSrv = new CalcRmiServer(srvIP, "sessionFactory");
		cSrv.start();

		try {
			Thread.sleep(750);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		String url = "//" + srvIP + "/sessionFactory";
		List<CalcTestRmiClient> ctcs = new ArrayList<CalcTestRmiClient>();
		for (int i=0; i<1; i++) {		// Adapt maximal no of clients ..
			System.out.println("Starting CalcRmiClient" + i + " to " + srvIP);
			CalcRmiClient cCli = new CalcRmiClient();
			CalcTestRmiClient ctc = new CalcTestRmiClient(i, cCli, url);
			ctcs.add(ctc);
			ctc.start();
		}

		int idx = 0;
		while (ctcs.size() > 0) {
			CalcTestRmiClient ctc = ctcs.get(idx);
			if (!ctc.isFinished()) {
				idx += 1;
				if (idx >= ctcs.size()) idx = 0;
			} else {
				System.out.println("CalcSocketClient" + idx + " finished with " + ctc.isSuccess());
				ctcs.remove(idx);
			}
			
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		cSrv.interrupt();
	}
	
	private static void socketTest(String srvIP, int srvPort) {
		System.out.println("Starting CalcSocketServer on " + srvIP + ":" + srvPort);
		CalcSocketServer cSrv = new CalcSocketServer(srvPort);
		cSrv.start();

		List<CalcTestScktClient> ctcs = new ArrayList<CalcTestScktClient>();
		for (int i=0; i<1; i++) {		// Adapt maximal no of clients ..
			System.out.println("Starting CalcSocketClient" + i + " to " + srvIP + ":" + srvPort);
			CalcSocketClient cCli = new CalcSocketClient();
			CalcTestScktClient ctc = new CalcTestScktClient(i, cCli, srvIP, srvPort);
			ctcs.add(ctc);
			ctc.start();
		}

		int idx = 0;
		while (ctcs.size() > 0) {
			CalcTestScktClient ctc = ctcs.get(idx);
			if (!ctc.isFinished()) {
				idx += 1;
				if (idx >= ctcs.size()) idx = 0;
			} else {
				System.out.println("CalcSocketClient" + idx + " finished with " + ctc.isSuccess());
				ctcs.remove(idx);
			}
			
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		cSrv.interrupt();
	}
}
