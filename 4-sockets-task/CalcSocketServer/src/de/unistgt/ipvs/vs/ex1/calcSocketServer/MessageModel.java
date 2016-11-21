package de.unistgt.ipvs.vs.ex1.calcSocketServer;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MessageModel {

	// enum for all valid operators
	public enum Operators {
		Ready("RDY"), Add("ADD"), Subtract("SUB"), Multiply("MUL"), Result("RES"), Ok("OK"), Error("ERR"), Finish(
				"FIN");
		private final String value;

		private Operators(String val) {
			value = val;
		}

		public String toString() {
			return value;
		}
	}

	// list of all params in message
	private ArrayList<Object> params;

	// Just a constructor to create an empty Message object
	public MessageModel() {

	}

	// Message constructor with received Message as string parse it and
	// convert to
	// a our message format
	public MessageModel(String message) {
		if (message.isEmpty())
			return;
		Pattern messagePattern = Pattern.compile("<[0-9][0-9]:.+>");
		Matcher m = messagePattern.matcher(message);
		if (m.find()) {
			String filteredMessage = m.group(0);
			String length = filteredMessage.substring(message.indexOf("<") + 1, 3);
			if (!isNumber(length))
				throw new RuntimeException("invalid length portion: " + length);
			int l = Integer.valueOf(length);
			if (l != filteredMessage.length())
				throw new RuntimeException("Not consistent length of Message(" + filteredMessage + "): received is "
						+ length + " while actual is " + filteredMessage.length());
			Pattern ParamsPattern = Pattern.compile("\\s+");
			String msgContent = filteredMessage
					.substring(filteredMessage.indexOf(":") + 1, filteredMessage.length() - 1).trim();
			String[] extractedParams = ParamsPattern.split(msgContent);
			for (int i = 0; i < extractedParams.length; i++) {
				try {
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
		// check if passed param is valid else throw exception
		if (!isvalidParam(param.toString()))
			throw new RuntimeException("not valid param " + param.toString());
		// add the param
		this.params.add(param);
	}

	private boolean isvalidParam(String param) {
		// Check if param is number
		if (isNumber(param))
			return true;
		// Check if param is operator
		for (Operators val : Operators.values()) {
			if (param.equals(val.toString()))
				return true;
		}
		// else it is invalid
		return false;
	}

	// Check if a value is number by converting to double if failed then not
	// number
	public static boolean isNumber(String value) {
		try {
			Double.valueOf(value);
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
		if (msgContent.length() > 99)
			return null;
		// create the message String
		return "<" + String.format("%02d", msgContent.length() + 5) + ":" + msgContent + ">";
	}

	public static void main(String[] args) {
		MessageModel m = new MessageModel("<13: SUB 10 >");
		System.out.println(m.toString() == null ? "invalid" : m.toString());
		m = new MessageModel("<16:ADD 23 9 -1>");
		System.out.println(m.toString() == null ? "invalid" : m.toString());
		m = new MessageModel("<18:MUL 2 SUB 13 >");
		System.out.println(m.toString() == null ? "invalid" : m.toString());
		// for (int i = 0; i < params.length; i++) {
		// System.out.println(params[i]);
		// }

	}

}

