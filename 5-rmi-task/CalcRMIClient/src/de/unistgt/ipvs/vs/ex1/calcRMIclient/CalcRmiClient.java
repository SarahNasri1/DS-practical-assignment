package de.unistgt.ipvs.vs.ex1.calcRMIclient;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.unistgt.ipvs.vs.ex1.calculation.ICalculation;
import de.unistgt.ipvs.vs.ex1.calculation.ICalculationFactory;

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
		try {
			//get the result of this Icalculation object
			return this.calc.getResult();
		} catch (RemoteException e) {
			System.err.println("Error performing requested operation on remote object: " + e.getMessage());
			e.printStackTrace();
		}
		return 0;
	}

	public boolean init(String url) {
		// TODO
		try{  
			//lookup the remote registry and return a stub of ICalculationFactory
			ICalculationFactory stub=(ICalculationFactory)Naming.lookup(url); 
			//get a new ICalculation session
			this.calc = stub.getSession();
			return true;
		}catch(Exception e){
			System.err.println("Error creating ICalculation stub: " + e.getMessage());
			//e.printStackTrace();
		}
		return false;
	}

	public boolean calculate(CalculationMode calcMode, Collection<Integer> numbers) {
		// TODO
		//perform requested operation on the collection object.
		try {
			switch(calcMode){
			case ADD:
				for (Integer value : numbers) {	
					this.calc.add(value);
				}
				break;
			case MUL:
				for (Integer value : numbers) {
					this.calc.multiply(value);
				}
				break;
			case SUB:
				for (Integer value : numbers) {
					this.calc.subtract(value);
				}
				break;
			default:
				System.out.println("Error:: Wrong calculation mode selected!!");
				break;

			}

		} catch (RemoteException e) {
			System.err.println("Error performing requested operation on remote object: " + e.getMessage());
			//e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * optional main method to independently test the rmi client application 
	 */
	public static void main(String args[]){ 

		CalcRmiClient client = new CalcRmiClient();
		String url = "rmi://localhost/calfactory";
		List<Integer> numbers = new ArrayList<Integer>();
		numbers.add(10);
		numbers.add(3);
		numbers.add(7);

		client.init(url);
		client.calculate(CalculationMode.ADD, numbers);
		System.out.println("ADD: "+ client.getCalcRes());

		client.calculate(CalculationMode.MUL, numbers);
		System.out.println("MUL: "+client.getCalcRes());

		client.calculate(CalculationMode.SUB, numbers);
		System.out.println("SUB: "+client.getCalcRes());		

	} 

}
