package de.unistgt.ipvs.vs.ex1.calcRMIclient;

import java.util.Collection;

import de.unistgt.ipvs.vs.ex1.calculation.ICalculation;

/**
 * Implement the getCalcRes-, init-, and calculate-method of this class as
 * necessary to complete the assignment. You may also add some fields or methods.
 */
public class CalcRmiClient {
	private ICalculation calc = null;

	public CalcRmiClient() {
		this.calc = null;
	}

	public int getCalcRes() {
		// TODO		
		return 0;
	}

	public boolean init(String url) {
		// TODO
		return false;
	}

	public boolean calculate(CalculationMode calcMode, Collection<Integer> numbers) {
		// TODO
		return false;
	}
}
