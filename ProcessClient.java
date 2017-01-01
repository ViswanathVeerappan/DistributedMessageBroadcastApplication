package com.project.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.project.server.MessageBean;
import com.project.server.Process;

/**
 * @author Viswanath Veerappan(vveera5 - 671492285)
 */

public class ProcessClient {

	private static int MESSAGE_ONE_ID = 1;

	private static int MESSAGE_TWO_ID = 1;

	private static int MESSAGE_THREE_ID = 1;

	private static int MESSAGE_FOUR_ID = 1;

	public static void main(String[] args) {
		JFrame frame = new JFrame("Three phase total ordering algorithm");

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JLabel labelOne = new JLabel(
				"Press the button to send message from Process One");
		JButton buttonOne = new JButton();
		buttonOne.setText("1");
		buttonOne.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Thread threadOne = new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Process process = (Process) Naming
									.lookup("rmi://127.0.0.1:1900"
											+ "/PROCESS_ONE");
							String message = "MESSAGE_1_";
							message = message.concat(String
									.valueOf(MESSAGE_ONE_ID));
							MESSAGE_ONE_ID++;
							process.initiateMessageTransfer(message);
						} catch (MalformedURLException e) {
							e.printStackTrace();
						} catch (RemoteException e) {
							e.printStackTrace();
						} catch (NotBoundException e) {
							e.printStackTrace();
						}

					}
				});
				threadOne.start();
			}
		});
		panel.add(labelOne);
		panel.add(buttonOne);

		JLabel labelTwo = new JLabel(
				"Press the button to send message from Process Two");
		JButton buttonTwo = new JButton();
		buttonTwo.setText("2");
		buttonTwo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Thread threadTwo = new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Process process = (Process) Naming
									.lookup("rmi://127.0.0.1:2900"
											+ "/PROCESS_TWO");
							String message = "MESSAGE_2_";
							message = message.concat(String
									.valueOf(MESSAGE_TWO_ID));
							MESSAGE_TWO_ID++;
							process.initiateMessageTransfer(message);
						} catch (MalformedURLException e) {
							e.printStackTrace();
						} catch (RemoteException e) {
							e.printStackTrace();
						} catch (NotBoundException e) {
							e.printStackTrace();
						}

					}
				});
				threadTwo.start();
			}
		});
		panel.add(labelTwo);
		panel.add(buttonTwo);

		JLabel labelThree = new JLabel(
				"Press the button to send message from Process Three");
		JButton buttonThree = new JButton();
		buttonThree.setText("3");
		buttonThree.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Thread threadThree = new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Process process = (Process) Naming
									.lookup("rmi://127.0.0.1:3900"
											+ "/PROCESS_THREE");
							String message = "MESSAGE_3_";
							message = message.concat(String
									.valueOf(MESSAGE_THREE_ID));
							MESSAGE_THREE_ID++;
							process.initiateMessageTransfer(message);
						} catch (MalformedURLException e) {
							e.printStackTrace();
						} catch (RemoteException e) {
							e.printStackTrace();
						} catch (NotBoundException e) {
							e.printStackTrace();
						}

					}
				});
				threadThree.start();
			}
		});
		panel.add(labelThree);
		panel.add(buttonThree);

		JLabel labelFour = new JLabel(
				"Press the button to send message from Process Four");
		JButton buttonFour = new JButton();
		buttonFour.setText("4");
		buttonFour.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Thread threadFour = new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Process process = (Process) Naming
									.lookup("rmi://127.0.0.1:4900"
											+ "/PROCESS_FOUR");
							String message = "MESSAGE_4_";
							message = message.concat(String
									.valueOf(MESSAGE_FOUR_ID));
							MESSAGE_FOUR_ID++;
							process.initiateMessageTransfer(message);
						} catch (MalformedURLException e) {
							e.printStackTrace();
						} catch (RemoteException e) {
							e.printStackTrace();
						} catch (NotBoundException e) {
							e.printStackTrace();
						}

					}
				});
				threadFour.start();
			}
		});
		panel.add(labelFour);
		panel.add(buttonFour);

		JLabel labelFive = new JLabel(
				"Press the button to view all messages which are delivered");
		JButton buttonFive = new JButton();
		buttonFive.setText("Show Messages");
		buttonFive.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Thread threadFive = new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							JFrame tableFrame = new JFrame("Results");
							Process processOne = (Process) Naming
									.lookup("rmi://127.0.0.1:1900"
											+ "/PROCESS_ONE");
							Map<String, MessageBean> processOneMessages = processOne
									.getDeliveredMessageQueue();

							Process processTwo = (Process) Naming
									.lookup("rmi://127.0.0.1:2900"
											+ "/PROCESS_TWO");
							Map<String, MessageBean> processTwoMessages = processTwo
									.getDeliveredMessageQueue();

							Process processThree = (Process) Naming
									.lookup("rmi://127.0.0.1:3900"
											+ "/PROCESS_THREE");
							Map<String, MessageBean> processThreeMessages = processThree
									.getDeliveredMessageQueue();

							Process processFour = (Process) Naming
									.lookup("rmi://127.0.0.1:4900"
											+ "/PROCESS_FOUR");
							Map<String, MessageBean> processFourMessages = processFour
									.getDeliveredMessageQueue();

							int numberOfRows = processOneMessages.size()
									+ processTwoMessages.size()
									+ processThreeMessages.size()
									+ processFourMessages.size() + 4;
							Object[] columnName = { "Messages delivered" };
							Object[][] rowData = new Object[numberOfRows][1];
							int rowNumber = 0;
							rowNumber = displayMessages(processOneMessages,
									"PROCESS_ONE", rowNumber, rowData);
							rowNumber = displayMessages(processTwoMessages,
									"PROCESS_TWO", rowNumber, rowData);
							rowNumber = displayMessages(processThreeMessages,
									"PROCESS_THREE", rowNumber, rowData);
							rowNumber = displayMessages(processFourMessages,
									"PROCESS_FOUR", rowNumber, rowData);
							JTable table = new JTable(rowData, columnName);

							table.setDefaultRenderer(Object.class,
									new MyTableCellRender());
							JScrollPane scrollPane = new JScrollPane(table);
							tableFrame.add(scrollPane, BorderLayout.CENTER);
							tableFrame.setSize(600, 600);
							tableFrame.setVisible(true);
							tableFrame
									.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						} catch (MalformedURLException | RemoteException
								| NotBoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
				threadFive.start();
			}
		});

		panel.add(labelFive);
		panel.add(buttonFive);

		JLabel labelQuit = new JLabel(
				"Press the button to stop sending messages and close the client");
		JButton buttonQuit = new JButton();
		buttonQuit.setText("Q");
		buttonQuit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		panel.add(labelQuit);
		panel.add(buttonQuit);

		frame.add(panel);
		frame.setSize(600, 600);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private static int displayMessages(
			Map<String, MessageBean> messagesDelivered, String processId,
			int rowNumber, Object[][] rowData) {
		Iterator<Entry<String, MessageBean>> iterator = messagesDelivered
				.entrySet().iterator();
		rowData[rowNumber][0] = processId;
		rowNumber++;
		while (iterator.hasNext()) {
			Map.Entry<String, MessageBean> entry = (Map.Entry<String, MessageBean>) iterator
					.next();
			rowData[rowNumber][0] = messagesDelivered.get(entry.getKey())
					.getMessage();
			rowNumber++;
		}
		return rowNumber;
	}

	public static class MyTableCellRender extends DefaultTableCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3004794419585488495L;

		public MyTableCellRender() {
			setOpaque(true);
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			String stringValue = (String) value;
			if (stringValue.startsWith("PROCESS")) {
				setForeground(Color.black);
				setBackground(Color.blue);
			} else {
				setForeground(Color.black);
				setBackground(Color.white);
			}
			setText(stringValue);
			return this;
		}
	}
}
