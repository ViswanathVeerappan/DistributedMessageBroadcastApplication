package com.project.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * The interface specifies the list of operations supported by each and every
 * process participating in the distributed execution
 * 
 * @author Viswanath Veerappan(vveera5 - 671492285)
 */

public interface Process extends Remote {

	public static final int NO_OF_PROCESSES = 4;

	public static final String PROCESS_ONE_ID = "PROCESS_ONE";

	public static final String PROCESS_TWO_ID = "PROCESS_TWO";

	public static final String PROCESS_THREE_ID = "PROCESS_THREE";

	public static final String PROCESS_FOUR_ID = "PROCESS_FOUR";

	// This method is called from the RMI client to initiate a multicast message
	// transfer across the system
	public void initiateMessageTransfer(String message) throws RemoteException;

	// This method will be called at all receiver processes by sender with
	// input message and receiver in turn returns the possible timestamp at the
	// receiver for message delivery
	public void reviseTimestamp(String message, String senderId,
			String messageTag, int timestamp) throws RemoteException;

	// This method is called from all the message receiver processes at the
	// sender which state the possible timing at which the input message can be
	// processed and the server in turn returns the final timestamp for message
	// delivery
	public void proposeTimestamp(String senderId, String receiverId,
			String messageTag, int proposedTimestamp) throws RemoteException;

	// This method is called at all receiver processes by the sender with the
	// final common timestamp for message delivery
	public void finalTimestamp(String senderId, String messageTag,
			int finalTimestamp) throws RemoteException;

	// This method exposes the messages that have been delivered at each of the
	// processes
	public Map<String, MessageBean> getDeliveredMessageQueue()
			throws RemoteException;

}
