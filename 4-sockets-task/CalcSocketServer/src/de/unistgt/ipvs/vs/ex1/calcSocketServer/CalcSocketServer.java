package de.unistgt.ipvs.vs.ex1.calcSocketServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import de.unistgt.ipvs.vs.ex1.calcSocketServer.MessageModel.Operators;
import de.unistgt.ipvs.vs.ex1.calculationImpl.CalculationImpl;

/**
 * Extend the run-method of this class as necessary to complete the assignment.
 * You may also add some fields, methods, or further classes.
 */
public class CalcSocketServer extends Thread {
	private ServerSocket srvSocket;
	private int port;

	public CalcSocketServer(int port) {
		this.srvSocket = null;
		this.port = port;
	}

	@Override
	public void interrupt() {
		try {
			if (srvSocket != null)
				srvSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		if (port <= 0) {
			System.err.println("SocketServer listen port not specified!");
			System.exit(-1);
		}

		// TODO
		// Start listening server socket ..
		try {

			// start listening
			srvSocket = new ServerSocket(port);
			Operators operation = null;
			
			// initialize calculator
			CalculationImpl calculator = new CalculationImpl();			
			System.out.println("SERVER: "+"Server running on port "+ port);
			
			// accept client Connection
			Socket clientSocket = srvSocket.accept();
			System.out.println("SERVER: "+"Client connected");
			
			// create writer to send to client
			PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

			// create reader to read from Client
			BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			// Send Readymessage
			MessageModel readyMessage = new MessageModel();
			readyMessage.addParam(Operators.Ready);
			writer.println(readyMessage);
			System.out.println("SERVER: "+readyMessage+" Sent.");
			
			String read = "";
			while ((read = reader.readLine()) != null) {
				System.out.println("SERVER: "+read+" Received.");
				// parse incoming message
				MessageModel msg = new MessageModel(read);

				if (msg.getInvalidParams()!= null && msg.getInvalidParams().size() > 0) {
					// send error message with invalid params
					MessageModel errMsg = new MessageModel();
					errMsg.setParams(msg.getInvalidParams());
					errMsg.getParams().add(0, Operators.Error.toString());
					writer.println(errMsg);
					System.out.println("SERVER: "+errMsg+" Sent.");
				}

				// send ok message with valid params
				int Res= -1;
				//search for Res parameter to not include in the ok Message
				for (int i = msg.getParams().size()-1; i >=0 ; i--) {
					if(msg.getParams().get(i).toString().equalsIgnoreCase(Operators.Result.toString())){
						Res = i;
						break;
					}
				}
				//check if the only parameter is RES then do not send Ack
				if(!(msg.getParams().get(0).toString().equalsIgnoreCase(Operators.Result.toString()) && msg.getParams().size()==1)){
					MessageModel okMsg = new MessageModel();
					okMsg.setParams(msg.getParams());
					if(Res!=-1)
						okMsg.getParams().remove(Res);
					okMsg.getParams().add(0, Operators.Ok);
					writer.println(okMsg);
					System.out.println("SERVER: "+okMsg+" Sent.");
				}
				
			
				// execute
				
				for (int i = 0; i < msg.getParams().size(); i++) {
					String param = msg.getParams().get(i).toString();
					// check if parameter is an operation
					if (!MessageModel.isNumber(param)) {
						Operators operatorParam = Operators.fromString(param);
						// check if it is an add mul sub operation
						if (operatorParam == Operators.Add || operatorParam == Operators.Subtract
								|| operatorParam == Operators.Multiply)
							operation = operatorParam;
						// if it is result, send the result message
						else if (operatorParam == Operators.Result) {
							MessageModel resultMessage = new MessageModel();
							resultMessage.addParam(Operators.Ok.toString());
							resultMessage.addParam(Operators.Result.toString());
							resultMessage.addParam(String.valueOf(calculator.getResult()));
							writer.println(resultMessage);
							System.out.println("SERVER: "+resultMessage+" Sent.");
						}
						// else it is an integer value
					} else {

						int value = Integer.parseInt(param);
					
						// perform the current operation
						if (operation == Operators.Add) {
							calculator.add(value);
						} else if (operation == Operators.Multiply) {
							calculator.multiply(value);
						} else if (operation == Operators.Subtract) {
							calculator.subtract(value);
						}
						System.out.println("SERVER: "+operation.toString()+" "+value +" = "+calculator.getResult() );
					}

				}
				//Send FIN Message
				MessageModel finMsg= new MessageModel();
				finMsg.addParam(Operators.Finish.toString());
				writer.println(finMsg);
			}
			// close the socket when the connection is closed by client
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
