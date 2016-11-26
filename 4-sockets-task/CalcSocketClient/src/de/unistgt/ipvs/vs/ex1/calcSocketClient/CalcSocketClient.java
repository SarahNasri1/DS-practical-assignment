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
			//connect to server
			socket = new Socket(srvIP, srvPort);
			System.out.println("CLIENT: Connected to "+srvIP+":"+srvPort );
			
			//initiate the reader and the writer to socket
			writer = new PrintWriter(socket.getOutputStream(),true);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			//receive the ready message from the server  
			MessageModel connectionMessage = new MessageModel(reader.readLine());
			System.out.println("CLIENT: "+ connectionMessage+" Received.");
			//if message is ready return true else return true
			if(connectionMessage.getParams().get(0).toString().equalsIgnoreCase(Operators.Ready.toString())){
				return true;
			}else{
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
			//Close the connection and return true
			socket.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			//return false if exception occurs
			return false;
		}
		
	}

	public boolean calculate(String request) {
		//Send the message to the server
		writer.println(request);
		
		try {
			//Receive messages from the server until a FIN messae is received
			while(true){
				//read the received message
				String msg = reader.readLine();
				System.out.println("CLIENT: String received is "+msg);
				
				//Parse the received message
				MessageModel receivedMsg = new MessageModel(msg);
				
				//Error message received so increment counter bu number of invalid params
				if(receivedMsg.getParams().get(0).toString().equals(Operators.Error.toString())){				
					rcvdErs+= receivedMsg.getInvalidParams().size();			
				}			
				//ok message is received then it is either acknowledgement or result
				else if(receivedMsg.getParams().get(0).toString().equals(Operators.Ok.toString())){
					//checking if RES is a param then set the result value
					if(receivedMsg.getParams().size()==3 && receivedMsg.getParams().get(1).toString().equals(Operators.Result.toString()))
						calcRes= Integer.parseInt(receivedMsg.getParams().get(2).toString());
					else
						//else increment the ok counter
						rcvdOKs+=receivedMsg.getParams().size()-1;
				//FIN message is received just break the loop
				}else if(receivedMsg.getParams().get(0).toString().equalsIgnoreCase(Operators.Finish.toString())){
					break;
				}
			}			
			//return true after loop break
			return true;
		} catch (IOException e) {
			//return false if any exception occurs
			e.printStackTrace();
			return false;
		}
		
	}
}
