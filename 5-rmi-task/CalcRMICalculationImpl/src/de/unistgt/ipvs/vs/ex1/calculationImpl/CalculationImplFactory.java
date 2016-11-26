package de.unistgt.ipvs.vs.ex1.calculationImpl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import de.unistgt.ipvs.vs.ex1.calculation.ICalculation;
import de.unistgt.ipvs.vs.ex1.calculation.ICalculationFactory;

/**
 * Change this class (implementation/signature/...) as necessary to complete the assignment.
 * You may also add some fields or methods.
 */
public class CalculationImplFactory extends UnicastRemoteObject implements ICalculationFactory{

	private static final long serialVersionUID = 1L;
	// TODO
	public CalculationImplFactory() throws RemoteException {
		super();
	}
	//get an instance of ICalculation object
	public ICalculation getSession() throws RemoteException {
		return new CalculationImpl();
	}
}
