package de.unistgt.ipvs.vs.ex1.testCalc;

import java.io.IOException;
import java.util.Random;

import de.unistgt.ipvs.vs.ex1.calcSocketClient.CalcSocketClient;

// This is the main test program.
public class CalcTestScktClient extends Thread {
	private int cliIdx;
	
	private CalcSocketClient csCli;
	private String srvIP;
	private int    srvPort;
	
	private boolean success;
	private boolean finished;

	public CalcTestScktClient(int cliIdx, CalcSocketClient csCli, String srvIP, int srvPort) {
		this.cliIdx = cliIdx;
		
		this.csCli   = csCli;
		this.srvIP   = srvIP;
		this.srvPort = srvPort;
		
		this.success = false;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	@Override
	public void run() {
		csCli.connectTo(srvIP, srvPort);
		
		success = true;
		
		Random r = new Random();

		int testNo = r.nextInt(2) + 1;		// Adapt maximal no of known tests ..
		switch (testNo) {
		case 1: success = runTest1(); break;
		case 2: success = runTest2(); break;
		case 3: success = runTest3(); break;
		}
		
		csCli.disconnect();
		
		finished = true;
	}
	
	private boolean runTest1() {
		System.out.println(cliIdx + "> Test (1) ..");
		
		try {
			String req1 = "ADD 1 2 3 SUB 3 2 1";
			if (!csCli.calculate("<" + (req1.length() + 5) + ":" + req1 + ">"))
				throw new IOException(cliIdx + "> IOException11!");
			if (!csCli.calculate("<08:rEs>"))
				throw new IOException(cliIdx + "> IOException12!");
			
			if (csCli.getCalcRes() != 0 || csCli.getRcvdOKs() != 8 || csCli.getRcvdErs() != 0)
				throw new IOException(cliIdx + "> Wrong result!");
		} catch (IOException e) {
			System.err.println(cliIdx + "> Test (1): FAILED");
			e.printStackTrace();
			return false;
		}
		
		System.out.println(cliIdx + "> Test (1): SUCCESS");
		return true;
	}
	
	private boolean runTest2() {
		System.out.println(cliIdx + "> Test (2) ..");
		
		try {
			String req2 = "Add -3 ASM -2 ABC -1 SUB -1 ASM10 -2 ABC09 -3";
			if (!csCli.calculate("<" + (req2.length() + 5) + ":" + req2 + ">"))
				throw new IOException(cliIdx + "> IOException21!");
			if (!csCli.calculate("<08:RES>"))
				throw new IOException(cliIdx + "> IOException22!");
			
			if (csCli.getCalcRes() != 0 || csCli.getRcvdOKs() != 8 || csCli.getRcvdErs() != 4)
				throw new IOException(cliIdx + "> Wrong result!");
		} catch (IOException e) {
			System.err.println(cliIdx + "> Test (2): FAILED");
			e.printStackTrace();
			return false;
		}
		
		System.out.println(cliIdx + "> Test (2): SUCCESS");
		return true;
	}
	
	private boolean runTest3() {
		System.out.println(cliIdx + "> Test (3) ..");
		
		try {
			String req31 = "  MUL  1   ASM  ADD ABC 10    5  SUB 100 ADD10   ADD";
			if (!csCli.calculate("24 foo 42 <" + (req31.length() + 5) + ":" + req31 + ">"))
					throw new IOException(cliIdx + "> IOException31!");
			String req32 = "60 4 MUL -2 RES  ";
			if (!csCli.calculate("a faq 23 <" + (req32.length() + 5) + ":" + req32 + "> bla 42 "))
					throw new IOException(cliIdx + "> IOException32!");
			
			if (csCli.getCalcRes() != 42 || csCli.getRcvdOKs() != 12 || csCli.getRcvdErs() != 3)
				throw new IOException(cliIdx + "> Wrong result!");
		} catch (IOException e) {
			System.err.println(cliIdx + "> Test (3): FAILED");
			e.printStackTrace();
			return false;
		}
		
		System.out.println(cliIdx + "> Test (3): SUCCESS");
		return true;
	}
	
	// Further tests:
	//private boolean runTest4() { ..
}
