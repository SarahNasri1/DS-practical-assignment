package de.unistgt.ipvs.vs.ex1.calcSocketServer;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *  
 *a model class for a message that creates new messages and parse 
 *string into messages.
 *
 */
public class MessageModel {

	// enum for all valid operators
	public enum Operators {
		Ready("RDY"), Add("ADD"), Subtract("SUB"), Multiply("MUL"), Result("RES"), Ok("OK"), Error("ERR"), Finish(
				"FIN");
		private final String value;

		private Operators(String val) {
			value = val;
		}
		//Get the string value of enum item
		public String toString() {
			return value;
		}
		//Get enum item from string value
		public static Operators fromString(String text) {
		    if (text != null) {
		      for (Operators b : Operators.values()) {
		        if (text.equalsIgnoreCase(b.toString())) {
		          return b;
		        }
		      }
		    }
		    return null;
		  }
	}

	// list of all valid params in message
	private ArrayList<Object> params = new ArrayList<Object>();
	// list of all invalid params in message
	private ArrayList<Object> invalidParams = new ArrayList<Object>();

	// Just a constructor to create an empty Message object
	public MessageModel() {

	}

	// Message constructor with received Message as string parse it and
	// convert to
	// a our message format
	public MessageModel(String message) {
		if (message.isEmpty())
			return;
		// extract the message between <>
		Pattern messagePattern = Pattern.compile("<[0-9][0-9]:.+>");
		Matcher m = messagePattern.matcher(message);
		if (m.find()) {
			String filteredMessage = m.group(0);
			//extract the message content
			String length = filteredMessage.substring(filteredMessage.indexOf("<")+1,filteredMessage.indexOf("<")+3);
			//check the length if not number throw exception
			if (!isNumber(length))
				throw new RuntimeException("invalid length portion: " + length);
			int l = Integer.valueOf(length);
			//Check the consistency of the length
			if (l != filteredMessage.length())
				throw new RuntimeException("Not consistent length of Message(" + filteredMessage + "): received is "
						+ length + " while actual is " + filteredMessage.length());
			//split the message contents by space to get array of all params
			Pattern ParamsPattern = Pattern.compile("\\s+");
			String msgContent = filteredMessage
					.substring(filteredMessage.indexOf(":") + 1, filteredMessage.length() - 1).trim();
			String[] extractedParams = ParamsPattern.split(msgContent);
			for (int i = 0; i < extractedParams.length; i++) {
				try {
					// check if passed param is valid else throw exception
					if (!isvalidParam(extractedParams[i].toString())) {
						addInvalidParam(extractedParams[i].toString());
						System.out.println("SERVER: invalid param "+ extractedParams[i].toString());
					} else
						//add the valid parameter to list of params
						addParam(extractedParams[i]);
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}

	// getter for message params
	public ArrayList<Object> getParams() {
		return params;
	}

	// add a param to the message
	public void addParam(Object param) throws RuntimeException {
		// check if params is still not initiated
		if (params == null)
			params = new ArrayList<Object>();
		
		// add the param
		this.params.add(param);
	}

	private boolean isvalidParam(String param) {
		// Check if param is number
		if (isNumber(param))
			return true;
		// Check if param is operator
		for (Operators val : Operators.values()) {
			if (param.equalsIgnoreCase(val.toString()))
				return true;
		}
		// else it is invalid
		return false;
	}

	// Check if a value is number by converting to double if failed then not
	// number
	public static boolean isNumber(String value) {
		try {
			Integer.valueOf(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String toString() {
		// check if params is not null and if size is full
		if (params == null || params.size() == 0)
			return null;
		// create the string of message content
		String msgContent = "";
		for (int i = 0; i < params.size(); i++) {
			msgContent += params.get(i).toString() + " ";
		}
		// remove the last space added by loop
		msgContent = msgContent.substring(0, msgContent.length() - 1);

		/*
		 * check if message size is greater than 100 Which means that the
		 * first two part containing length will be greater than two
		 * character which is invalid state
		 */
		if ((msgContent.length()+5) > 99)
			return null;
		// create the message String
		return "<" + String.format("%02d", msgContent.length() + 5) + ":" + msgContent + ">";
	}

	public ArrayList<Object> getInvalidParams() {		
		return invalidParams;
	}

	public void setParams(ArrayList<Object> params) {
		this.params = (ArrayList<Object>) params.clone();
	}

	public void addInvalidParam(Object invalidParam) {
		if (invalidParams == null)
			invalidParams = new ArrayList<Object>();
		this.invalidParams.add(invalidParam);
	}

}
