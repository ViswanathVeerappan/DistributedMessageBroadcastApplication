package com.project.server;

import java.net.MalformedURLException;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Viswanath Veerappan(vveera5 - 671492285)
 */

public class ProcessOneImpl extends UnicastRemoteObject implements Process {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3151207310103394210L;

	private static final Map<String, List<Integer>> TIMESTAMP_UPDATION_MAP = new HashMap<String, List<Integer>>();

	private static final Map<String, MessageBean> UNDELIVERED_MESSAGE_QUEUE = new LinkedHashMap<String, MessageBean>();

	private static final Map<String, MessageBean> DELIVERED_MESSAGE_QUEUE = new LinkedHashMap<String, MessageBean>();

	private static int CLOCK = 0;

	private static int PRIORITY = 0;

	ProcessOneImpl() throws RemoteException {
		super();
	}

	public void initiateMessageTransfer(final String message)
			throws RemoteException {
		System.out.println("initiateMessageTransfer() of " + PROCESS_ONE_ID
				+ " is called");
		CLOCK = CLOCK + 1;
		final String messageTag = generateMessageTag();
		if (getProcessObject("2900", PROCESS_TWO_ID) != null) {
			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						getProcessObject("2900", PROCESS_TWO_ID)
								.reviseTimestamp(message, PROCESS_ONE_ID,
										messageTag, CLOCK);
					} catch (RemoteException e) {
						System.out.println("Exception occurred in "
								+ PROCESS_ONE_ID + " : " + e.getMessage());
					}

				}
			});
			thread.start();
		}
		if (getProcessObject("3900", PROCESS_THREE_ID) != null) {
			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						getProcessObject("3900", PROCESS_THREE_ID)
								.reviseTimestamp(message, PROCESS_ONE_ID,
										messageTag, CLOCK);
					} catch (RemoteException e) {
						System.out.println("Exception occurred in "
								+ PROCESS_ONE_ID + " : " + e.getMessage());
					}

				}
			});
			thread.start();
		}

		if (getProcessObject("4900", PROCESS_FOUR_ID) != null) {
			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						getProcessObject("4900", PROCESS_FOUR_ID)
								.reviseTimestamp(message, PROCESS_ONE_ID,
										messageTag, CLOCK);
					} catch (RemoteException e) {
						System.out.println("Exception occurred in "
								+ PROCESS_ONE_ID + " : " + e.getMessage());
					}

				}
			});
			thread.start();
		}

	}

	public void reviseTimestamp(String message, final String senderId,
			final String messageTag, int timestamp) throws RemoteException {
		System.out.println("reviseTimestamp() of " + PROCESS_ONE_ID
				+ " is called");
		PRIORITY = Math.max(PRIORITY + 1, timestamp);
		MessageBean messageBean = new MessageBean(message, senderId, PRIORITY,
				false);
		UNDELIVERED_MESSAGE_QUEUE.put(messageTag, messageBean);
		if (senderId.equals(PROCESS_TWO_ID)) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						getProcessObject("2900", PROCESS_TWO_ID)
								.proposeTimestamp(PROCESS_ONE_ID, senderId,
										messageTag, PRIORITY);
					} catch (RemoteException e) {
						System.out.println("Exception occurred in "
								+ PROCESS_ONE_ID + " : " + e.getMessage());
					}

				}
			});
			thread.start();
		} else if (senderId.equals(PROCESS_THREE_ID)) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						getProcessObject("3900", PROCESS_THREE_ID)
								.proposeTimestamp(PROCESS_ONE_ID, senderId,
										messageTag, PRIORITY);
					} catch (RemoteException e) {
						System.out.println("Exception occurred in "
								+ PROCESS_ONE_ID + " : " + e.getMessage());
					}

				}
			});
			thread.start();
		} else if (senderId.equals(PROCESS_FOUR_ID)) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						getProcessObject("4900", PROCESS_FOUR_ID)
								.proposeTimestamp(PROCESS_ONE_ID, senderId,
										messageTag, PRIORITY);
					} catch (RemoteException e) {
						System.out.println("Exception occurred in "
								+ PROCESS_ONE_ID + " : " + e.getMessage());
					}

				}
			});
			thread.start();
		}
	}

	public void proposeTimestamp(String senderId, String receiverId,
			final String messageTag, int proposedTimestamp)
			throws RemoteException {
		System.out.println("proposeTimestamp() of " + PROCESS_ONE_ID
				+ " is called");
		if (TIMESTAMP_UPDATION_MAP.containsKey(messageTag)) {
			TIMESTAMP_UPDATION_MAP.get(messageTag).add(proposedTimestamp);
		} else {
			List<Integer> proposedTimestampList = new ArrayList<Integer>();
			proposedTimestampList.add(proposedTimestamp);
			TIMESTAMP_UPDATION_MAP.put(messageTag, proposedTimestampList);
		}
		if (TIMESTAMP_UPDATION_MAP.get(messageTag).size() == NO_OF_PROCESSES - 1) {
			final List<Integer> proposedTimestampList = TIMESTAMP_UPDATION_MAP
					.get(messageTag);
			Collections.reverse(proposedTimestampList);
			CLOCK = Math.max(CLOCK, proposedTimestampList.get(0));

			if (getProcessObject("2900", PROCESS_TWO_ID) != null) {
				Thread threadTwo = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							getProcessObject("2900", PROCESS_TWO_ID)
									.finalTimestamp(PROCESS_ONE_ID, messageTag,
											proposedTimestampList.get(0));
						} catch (RemoteException e) {
							System.out.println("Exception occurred in "
									+ PROCESS_ONE_ID + " : " + e.getMessage());
						}
					}
				});
				threadTwo.start();
			}
			if (getProcessObject("3900", PROCESS_THREE_ID) != null) {
				Thread threadThree = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							getProcessObject("3900", PROCESS_THREE_ID)
									.finalTimestamp(PROCESS_ONE_ID, messageTag,
											proposedTimestampList.get(0));
						} catch (RemoteException e) {
							System.out.println("Exception occurred in "
									+ PROCESS_ONE_ID + " : " + e.getMessage());
						}
					}
				});
				threadThree.start();
			}

			if (getProcessObject("4900", PROCESS_FOUR_ID) != null) {
				Thread threadFour = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							getProcessObject("4900", PROCESS_FOUR_ID)
									.finalTimestamp(PROCESS_ONE_ID, messageTag,
											proposedTimestampList.get(0));
						} catch (RemoteException e) {
							System.out.println("Exception occurred in "
									+ PROCESS_ONE_ID + " : " + e.getMessage());
						}
					}
				});
				threadFour.start();
			}
		}
	}

	public void finalTimestamp(String senderId, String messageTag,
			int finalTimestamp) throws RemoteException {
		System.out.println("finalTimestamp() of " + PROCESS_ONE_ID
				+ " is called");
		UNDELIVERED_MESSAGE_QUEUE.get(messageTag).setDeliverable(true);
		UNDELIVERED_MESSAGE_QUEUE.get(messageTag).setPriority(finalTimestamp);
		resortMap();
		Set<String> messageTagsSet = UNDELIVERED_MESSAGE_QUEUE.keySet();
		int messageTagPostion = new ArrayList<String>(messageTagsSet)
				.indexOf(messageTag);
		if (messageTagPostion == 0) {
			Iterator<Entry<String, MessageBean>> iterator = UNDELIVERED_MESSAGE_QUEUE
					.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, MessageBean> entry = (Map.Entry<String, MessageBean>) iterator
						.next();
				if (entry.getValue().isDeliverable()) {
					DELIVERED_MESSAGE_QUEUE.put(entry.getKey(),
							entry.getValue());
					UNDELIVERED_MESSAGE_QUEUE.remove(entry.getKey());
					CLOCK = Math.max(CLOCK, entry.getValue().getPriority()) + 1;
				} else {
					break;
				}
			}
		}
	}

	public Map<String, MessageBean> getDeliveredMessageQueue()
			throws RemoteException {
		return DELIVERED_MESSAGE_QUEUE;
	}

	private static String generateMessageTag() {
		return PROCESS_ONE_ID + "_" + String.valueOf(CLOCK);
	}

	private static void resortMap() {
		List<Map.Entry<String, MessageBean>> entries = new ArrayList<>(
				UNDELIVERED_MESSAGE_QUEUE.entrySet());
		Collections.sort(entries,
				new Comparator<Map.Entry<String, MessageBean>>() {
					public int compare(Map.Entry<String, MessageBean> o1,
							Map.Entry<String, MessageBean> o2) {
						return (o1.getValue().getPriority()).compareTo(o2
								.getValue().getPriority());
					}
				});
		synchronized (UNDELIVERED_MESSAGE_QUEUE) {
			UNDELIVERED_MESSAGE_QUEUE.clear();
			for (Map.Entry<String, MessageBean> entry : entries) {
				UNDELIVERED_MESSAGE_QUEUE.put(entry.getKey(), entry.getValue());
			}
		}

	}

	private static Process getProcessObject(String portNumber, String processId) {
		Process process = null;
		try {
			process = (Process) Naming.lookup("rmi://127.0.0.1:" + portNumber
					+ "/" + processId);
		} catch (MalformedURLException e) {
			System.out.println("Exception occurred in " + PROCESS_ONE_ID
					+ " : " + e.getMessage());
		} catch (RemoteException e) {
			System.out.println("Exception occurred in " + PROCESS_ONE_ID
					+ " : " + e.getMessage());
		} catch (NotBoundException e) {
			System.out.println("Exception occurred in " + PROCESS_ONE_ID
					+ " : " + e.getMessage());
		}
		return process;
	}

	public static void main(String args[]) throws Exception {
		ProcessOneImpl processImpl = new ProcessOneImpl();
		LocateRegistry.createRegistry(1900);
		Naming.rebind("rmi://127.0.0.1:1900/" + PROCESS_ONE_ID, processImpl);
		System.out.println(PROCESS_ONE_ID + " is up and running....");
	}

}
