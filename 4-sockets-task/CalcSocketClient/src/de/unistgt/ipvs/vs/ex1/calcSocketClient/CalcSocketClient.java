package de.unistgt.ipvs.vs.ex1.calcSocketClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import de.unistgt.ipvs.vs.ex1.calcSocketClient.MessageModel.Operators;

/**
 * Implement the connectTo-, disconnect-, and calculate-method of this class as
 * necessary to complete the assignment. You may also add some fields or
 * methods.
 */
public class CalcSocketClient {
	private int rcvdOKs; // --> Number of valid message contents
	private int rcvdErs; // --> Number of invalid message contents
	private int calcRes; // --> Calculation result (cf. 'RES')
	private Socket socket = null;
	private PrintWriter writer = null;
	private BufferedReader reader = null;

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
		try {
			socket = new Socket(srvIP, srvPort);
			System.out.println("CLIENT: Connected to "+srvIP+":"+srvPort );
			writer = new PrintWriter(socket.getOutputStream(),true);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			MessageModel connectionMessage = new MessageModel(reader.readLine());
			System.out.println("CLIENT: "+ connectionMessage+" Received.");
			if(connectionMessage.getParams().get(0).toString().equals(Operators.Ready.toString())){
				System.out.println("CLIENT: returning true");
				return true;
			}else{
				System.out.println("CLIENT: returning false");
				return false;
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean disconnect() {
		try {
			socket.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}

	public boolean calculate(String request) {
		writer.println(request);
		
		try {
			String msg = reader.readLine();
			System.out.println("CLIENT: String received is "+msg);
			MessageModel receivedMsg = new MessageModel(msg);
			
			if(receivedMsg.getParams().get(0).toString().equals(Operators.Error.toString())){
				
				rcvdErs+= receivedMsg.getInvalidParams().size();
				receivedMsg = new MessageModel(reader.readLine());
				System.out.println("CLIENT: String received is "+receivedMsg);
				if(receivedMsg.getParams().get(0).toString().equals(Operators.Ok.toString()) )
					rcvdOKs+=receivedMsg.getParams().size()-1;
			}			
			else if(receivedMsg.getParams().get(0).toString().equals(Operators.Ok.toString())){
				if(receivedMsg.getParams().size()==3 && receivedMsg.getParams().get(1).toString().equals(Operators.Result.toString()))
					calcRes= Integer.parseInt(receivedMsg.getParams().get(2).toString());
				else{
					rcvdOKs+=receivedMsg.getParams().size()-1;
					if(request.toUpperCase().contains(Operators.Result.toString())){
						receivedMsg = new MessageModel(reader.readLine());
						System.out.println("CLIENT: String received is "+receivedMsg);
						if(receivedMsg.getParams().size()==3 && receivedMsg.getParams().get(1).toString().equals(Operators.Result.toString()))
							calcRes= Integer.parseInt(receivedMsg.getParams().get(2).toString());
					}
				}
			}
			
			System.out.println("CLIENT: Ers "+rcvdErs+", oks: "+rcvdOKs+", res: "+calcRes);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
}
