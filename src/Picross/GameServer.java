package Picross;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * GameServer.java
 * This is the game server side class with a server UI
 * 
 * @author Chengkuan Zhao, Chang Liu
 *
 */
@SuppressWarnings("serial")
public class GameServer extends JFrame {
	// Server socket
	private ServerSocket serverSocket = null;
	// Client socket
	private Socket clientSocket = null;
	// Boolean to track the running status of server
	private boolean isRunning = false;
	// Server running attributes as default
	private int port = GameConfig.DEFAULT_PORT;
	private String gameSolution = GameConfig.DEFAULT_GAME_SOLUTION;
	
	private Integer clientID = -1;

	// panel at the top contains imgLabel
	private JPanel topPanel;
	// contains all buttons for the server control
	private JPanel controlPanel;
	// Server log text field
	private JPanel logPanel;

	// JLabel contains the game image 
	private JLabel imgLabel = new JLabel();

	// JButton to execute the server
	private JButton executeButton = new JButton("Execute");
	// JButton to show the result of client sent
	private JButton resultButton = new JButton("Results");
	// JButton to close server
	private JButton endButton = new JButton("End");

	// JLabel to show port for portTextField
	private JLabel portLabel = new JLabel("Port: ");
	// port JTextField with max char number of 5
	private JTextField portTextField = new JTextField(10);


	// JScrollPane for adding JTextArea and JTextArea for the server logger.
	private JScrollPane loggerScrollPane;
	private JTextArea loggerTextArea = new JTextArea();
	
	/**
	 * GameServer constructor to call the initServerFrame
	 */
	public GameServer() {
		this.initServerFrame();
	}

	/**
	 * To create the server frame
	 */
	public void initServerFrame() {
		// Set the title of the frame
		setTitle("Picross Server");

		// Set the layout of the content pane to BorderLayout
		getContentPane().setLayout(new BorderLayout());

		// Create the top panel to hold the image label
		topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension(800,200));
		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		imgLabel.setIcon(new ImageIcon("image/gameserver.png"));
		topPanel.add(imgLabel);
		getContentPane().add(topPanel, BorderLayout.NORTH);

		// Create the controlPanel to hold the server control buttons
		controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		controlPanel.add(executeButton).setEnabled(true);
		controlPanel.add(resultButton).setEnabled(false);
		controlPanel.add(endButton);
		getContentPane().add(controlPanel, BorderLayout.CENTER);

		// Create the log panel to hold the server log text field
		logPanel = new JPanel();
		logPanel.setPreferredSize(new Dimension(800,200));
		logPanel.setLayout(new BorderLayout());
		loggerTextArea.setEditable(false);
		loggerScrollPane = new JScrollPane(loggerTextArea);
		loggerScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		logPanel.add(loggerScrollPane, BorderLayout.CENTER);
		getContentPane().add(logPanel, BorderLayout.SOUTH);

		// Set default value for portTextField
		portTextField.setText(GameConfig.DEFAULT_PORT.toString());
		
		// Create the label and text field for the server port
		JPanel portPanel = new JPanel();
		portPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		portPanel.add(portLabel);
		portPanel.add(portTextField);
		getContentPane().add(portPanel, BorderLayout.EAST);
		
		//Create the server controller from serve class
		new ServerController(new Server());
		
		// Set the size of the frame and make it visible
		setSize(800, 600);
		setVisible(true);
	}
	
	/**
	 * ServerController to handler ActionListener
	 *
	 */
	class ServerController implements ActionListener{
		
		private Server server; 
		
		/**
		 * ServerController constructor to add ActionListener to all buttons
		 * @param server server instance
		 */
		public ServerController(Server server) {
			// Add ActionListener for control panel
			executeButton.addActionListener(this);
			resultButton.addActionListener(this);
			endButton.addActionListener(this);
			this.server = server;
		}
		
		/**
		 * Override the ActionPerformed for buttons
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == executeButton) {
				//to protect the server from running twice
				if(!isRunning) {
					loggerTextArea.append("Exccute button clicked" + "\n");
					server.execute();
				}
			} else if (e.getSource() == resultButton) {
				loggerTextArea.append("Result button clicked" + "\n");
				server.result();
			} else if (e.getSource() == endButton) {
				loggerTextArea.append("End button clicked" + "\n");
				loggerTextArea.append("Disconnect all clients and close the server..." + "\n");
				server.end();
				executeButton.setEnabled(true);
				resultButton.setEnabled(false);
			}
		}
	}
	
	/**
	 * Server inner class
	 * To handle the server connection
	 *
	 */
	class Server implements Runnable{
		//arraylist of threads object for clients connected 
		private List<ClientHandler> clientLists = new ArrayList<>(5);
		//store the results of all clients 
		private List<String> resultLists = new ArrayList<String>();
		
		/**
		 * Override Run method in runnable
		 */
		@Override
		public void run() {
			this.start();
		}

		/**
		 * To start the client thread if a client is connected
		 */
		public void start() {

			while (isRunning) {

				try {
					clientSocket = serverSocket.accept();
					loggerTextArea.append("New client connected: " + clientSocket.getInetAddress() + "\n");
					ClientHandler clientHandler = new ClientHandler(clientSocket);
					clientHandler.start();
					clientLists.add(clientHandler);
					loggerTextArea.append(clientLists.size() + " clients connected!\n");
				} catch (Exception e) {
					loggerTextArea.append(e.getMessage());
				}
			}
		}


		/**
		 * To start the server connection
		 */
		public void execute() {
			
			try {
				port = Integer.parseInt(portTextField.getText());
			} catch (Exception e) {
				port = GameConfig.DEFAULT_PORT;
				loggerTextArea.append(e.getMessage() + "\n");
				loggerTextArea.append("port number will be set as " + port + "\n" );
			}
			
			try {
				serverSocket = new ServerSocket(port);
				isRunning = true;
				executeButton.setEnabled(false);
				resultButton.setEnabled(true);
				Thread serverThread = new Thread(this);
				serverThread.start();
				loggerTextArea.append("Server started on port " + port + "\n");
				loggerTextArea.append("Waiting for clients to connect...\n");
			}catch(Exception e) {
				loggerTextArea.append(e.getMessage());
			}
		}

		/**
		 * To show the result saved on server
		 */
		public void result() {
			if(resultLists.size() > 0) {
				String gameResult = "";
				for(String result : resultLists) {
					gameResult += result + "\n";
				}
				loggerTextArea.append(gameResult + "\n");
			}else{
				loggerTextArea.append("No result to show"+ "\n");
			}
		}

		/**
		 * To disconnect all clients
		 */
		public void end() {
			try {
				if (isRunning) {
					for (ClientHandler client : clientLists) {
						client.closeClientSocket();
					}
					serverSocket.close();
					isRunning = false;
					loggerTextArea.append("closed" + "\n");
				}
			} catch (Exception e) {
				loggerTextArea.append(e.getMessage());
			}
		}

		/**
		 * ClientHandler to handle the client connection threads
		 *
		 */
		class ClientHandler extends Thread{
			//BufferedReader and PrintWriter from Week11 lab code
			//input of server = output of client
			private BufferedReader input = null;
			//output of server = input of client
			private PrintWriter output = null;
			private Socket clientSocket;
			
			/**
			 * ClientHandler constructor to handle create input and output stream 
			 * @param socket - server socket
			 */
			public ClientHandler(Socket socket) {
				this.clientSocket = socket;
				clientID++;
				try {
					input = new BufferedReader(
							new InputStreamReader(clientSocket.getInputStream()));
					output = new PrintWriter(clientSocket.getOutputStream());
					output.print(clientID.toString());
					output.flush();
				} catch (Exception e) {
					loggerTextArea.append(e.getMessage());
				}
			}

			/**
			 * To close the thread
			 */
			public void closeClientSocket() {
				try {
					input.close();
					output.close();
					clientSocket.close();
				}catch(Exception e) {
					loggerTextArea.append(e.getMessage());
				}
			}

			/**
			 * Override the run method in runnable
			 */
			public void run() {
				String clientID = "";
				String protocol = "";
				String newData = "";
				try {
					
					String request = input.readLine();

					while (request != null) {
						loggerTextArea.append(request);
						String[] tokens = request.split(GameConfig.PROTOCOL_SEPARATOR);
						clientID = tokens[0];
						protocol = tokens[1];
						if(protocol.equals(GameConfig.PROTOCOL_END)) {
							closeClientSocket();
							return;
						} else if (protocol.equals(GameConfig.PROTOCOL_SENDGAME)) {
							//3;00001,10111,00110,11111,000111
							newData = tokens[2];
							receiveSolution(newData);
						} else if (protocol.equals(GameConfig.PROTOCOL_RECVGAME)) {
							//3;00001,10111,00110,11111,000111
							sendSolution(clientID);
						} else if(protocol.equals(GameConfig.PROTOCOL_DATA)){
							receiveData(clientID, newData);
						} else {
							loggerTextArea.append("Reqeust is not recogonized, ignore this request\n");
						}
						request = input.readLine();
					} 
				} catch (Exception e) {
					loggerTextArea.append(e.getMessage());
				}
			}

			/**
			 * To receive a new game solution from client
			 * @param newData - game solution
			 */
			private void receiveSolution(String newData) {
				try {
					gameSolution = newData;
					loggerTextArea.append("Received solution. from clientID:" + clientID + " with solution: " + newData);
					output.println("Server Received solution");
					output.flush();
				} catch (Exception e) {
					loggerTextArea.append(e.getMessage());
				}
			}
			
			/**
			 * To send a solution to client
			 * @param clientID - unique clinet id
			 */
			private void sendSolution(String clientID) {
				
				try {
					output.println(gameSolution);
					output.flush();
					loggerTextArea.append("Sending solution to client " + clientID + "\n");
				} catch (Exception e) {
					loggerTextArea.append(e.getMessage());
				}
			}

			/**
			 * To receive the game data from clients
			 * @param clientID - unique clientID
			 * @param newData - game data
			 */
			private void receiveData(String clientID, String newData) {
				try {
					output.println("SERVER: Received game data.");
					output.flush();
					
					String data = "Player[" + clientID + "]: " + newData;
					
					resultLists.add(data);
					
					loggerTextArea.append("Received data from client " + clientID + "\n");
				} catch (Exception e) {
					loggerTextArea.append(e.getMessage());
				}
			}
		}//end of Clienthandler
	}//end of server
}//end of GameServer


/*reference
* https://brightspace.algonquincollege.com/d2l/le/content/507919/viewContent/7840565/View
* https://howtodoinjava.com/java/string/split-tokenize-strings/
* https://docs.oracle.com/javase/7/docs/api/java/lang/Integer.html
* https://docs.oracle.com/javase/tutorial/networking/sockets/index.html
* https://docs.oracle.com/javase/tutorial/networking/sockets/readingWriting.html
* https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
*
 */