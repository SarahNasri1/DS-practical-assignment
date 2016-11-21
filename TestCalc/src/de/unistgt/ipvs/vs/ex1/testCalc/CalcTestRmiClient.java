package de.unistgt.ipvs.vs.ex1.testCalc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import de.unistgt.ipvs.vs.ex1.calcRMIclient.CalcRmiClient;
import de.unistgt.ipvs.vs.ex1.calcRMIclient.CalculationMode;

// This is the main test program.
public class CalcTestRmiClient extends Thread {
	private int cliIdx;
	private String url;
	
	private CalcRmiClient csCli;
	
	private boolean success;
	private boolean finished;

	public CalcTestRmiClient(int cliIdx, CalcRmiClient csCli, String url) {
		this.cliIdx = cliIdx;
		this.url = url;
		
		this.csCli   = csCli;
		
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
		csCli.init(url);
		
		success = true;
		
		Random r = new Random();

		int testNo = r.nextInt(2) + 1;		// Adapt maximal no of known tests ..
		switch (testNo) {
		case 1: success = runTest1(); break;
		case 2: success = runTest2(); break;
		case 3: success = runTest3(); break;
		}
		
		finished = true;
	}
	
	private boolean runTest1() {
		System.out.println(cliIdx + "> Test (1) ..");
		
		try {
			Collection<Integer> numbers1_1 = new ArrayList<Integer>(3);
			numbers1_1.add(1); numbers1_1.add(2); numbers1_1.add(3);
			if (!csCli.calculate(CalculationMode.ADD, numbers1_1))
				throw new IOException(cliIdx + "> IOException11!");
			Collection<Integer> numbers1_2 = new ArrayList<Integer>(3);
			numbers1_2.add(3); numbers1_2.add(2); numbers1_2.add(1);
			if (!csCli.calculate(CalculationMode.SUB, numbers1_2))
				throw new IOException(cliIdx + "> IOException12!");
			
			if (csCli.getCalcRes() != 0)
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
			Collection<Integer> numbers2_1 = new ArrayList<Integer>(3);
			numbers2_1.add(-3); numbers2_1.add(-2); numbers2_1.add(-1);
			if (!csCli.calculate(CalculationMode.ADD, numbers2_1))
				throw new IOException(cliIdx + "> IOException11!");
			Collection<Integer> numbers2_2 = new ArrayList<Integer>(3);
			numbers2_2.add(-1); numbers2_2.add(-2); numbers2_2.add(-3);
			if (!csCli.calculate(CalculationMode.SUB, numbers2_2))
				throw new IOException(cliIdx + "> IOException12!");
			
			if (csCli.getCalcRes() != 0)
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
			Collection<Integer> numbers3_1 = new ArrayList<Integer>(1);
			numbers3_1.add(1);
			if (!csCli.calculate(CalculationMode.MUL, numbers3_1))
				throw new IOException(cliIdx + "> IOException11!");
			Collection<Integer> numbers3_2 = new ArrayList<Integer>(2);
			numbers3_2.add(10); numbers3_2.add(5);
			if (!csCli.calculate(CalculationMode.ADD, numbers3_2))
				throw new IOException(cliIdx + "> IOException12!");
			Collection<Integer> numbers3_3 = new ArrayList<Integer>(1);
			numbers3_3.add(100);
			if (!csCli.calculate(CalculationMode.SUB, numbers3_3))
				throw new IOException(cliIdx + "> IOException13!");
			Collection<Integer> numbers3_4 = new ArrayList<Integer>(2);
			numbers3_4.add(60); numbers3_4.add(4);
			if (!csCli.calculate(CalculationMode.ADD, numbers3_4))
				throw new IOException(cliIdx + "> IOException14!");
			
			if (csCli.getCalcRes() != 42)
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
