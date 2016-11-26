package de.unistgt.ipvs.vs.ex1.calcRMIserver;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import de.unistgt.ipvs.vs.ex1.calculationImpl.CalculationImplFactory;

/**
 * Implement the run-method of this class to complete
 * the assignment. You may also add some fields or methods.
 */
public class CalcRmiServer extends Thread {
	private String regHost;
	private String objName;

	public CalcRmiServer(String regHost, String objName) {
		this.regHost = regHost;
		this.objName = objName;
	}

	@Override
	public void run() {
		if (regHost == null || objName == null) {
			System.err.println("<registryHost> and/or <objectName> not set!");
			return;
		}
		// TODO
		try {
			//create an object of CalculationImplFactory
			CalculationImplFactory factory = new CalculationImplFactory();
			//Register this host as the rmi server
			System.setProperty("java.rmi.server.hostname",regHost);
			//start registry on default port
			Registry registry = LocateRegistry.createRegistry(1099); 
			//rebind the object in the registry with the specified name
			registry.rebind (objName, factory); 
			System.out.println("RMI Server is ready!");
		} catch(Exception e){
			System.err.println("Error setting up CalculationImplFactory: " + e.getMessage());
			//e.printStackTrace();
		}

	}
/** 
 * optional main method to independently start the rmi server 
 */
	public static void main(String args[]) {
		new CalcRmiServer("127.0.0.1","calfactory").start();
	}

}
