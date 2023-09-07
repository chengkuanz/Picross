package Picross;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * GameClient.java
 * This is the game client side class with a client UI
 * 
 * @author Chengkuan Zhao, Chang Liu
 *
 */
@SuppressWarnings("serial")
public class GameClient extends JFrame {

	private int port = GameConfig.DEFAULT_PORT;
	private String ip = GameConfig.DEFAULT_ADDR;
	private String gameSolution = GameConfig.DEFAULT_GAME_SOLUTION;
	private boolean isConnected = false;
	private Integer clientID = -1;
	private String userName = GameConfig.DEFAULT_USER;
	private Socket socket;

	private Client client;

	//panel at the top contains imgLabel
	private JPanel topPanel;
	//contains all buttons for the client control
	private JPanel controlPanel;
	//client log text field
	private JPanel logPanel;

	//JButton to connect to server
	private JButton connectButton = new JButton("Connect");
	//JButton to close client
	private JButton endButton = new JButton("End");
	//JButton to start a new game
	private JButton newGameButton = new JButton("New Game");
	//JButton to send game
	private JButton sendGameButton = new JButton("Send Game");
	//JButton to receive game from server
	private JButton receiveGameButton = new JButton("Recevie Game");
	//JButton to send data
	private JButton sendDataButton = new JButton("Send Data");
	//JButton to play game
	private JButton playButton = new JButton("Play");

	// JLabel contains the game image 
	private JLabel imgLabel = new JLabel();
	//JLabel to show user for userTextField
	private JLabel userLabel = new JLabel("User: ");
	//user JTextField
	private JTextField userTextField = new JTextField(10);
	//JLabel to show server IP for serverIPTextField
	private JLabel serverIPLabel = new JLabel("Server IP: ");
	//server IP JTextField
	private JTextField serverIPTextField = new JTextField(10);
	//JLabel to show port for portTextField
	private JLabel portLabel = new JLabel("Port: ");
	//port JTextField
	private JTextField portTextField = new JTextField(5);

	//JScrollPane for adding scrollable functionality to the JTextArea.
	private JScrollPane loggerScrollPane;
	//JTextArea for holding the client-side log messages.
	private JTextArea loggerTextArea;

	private GameModel model;
	private GameView view;

	/**
	 * GameClinet constructor to call initClientFrame for client UI
	 */
	public GameClient(){
		this.initClientFrame();
	}


	/**
	 * To Create the client UI
	 */
	public void initClientFrame() {
		// Set the title of the frame
		setTitle("Picross Client");

		// Set the layout of the content pane to BorderLayout
		getContentPane().setLayout(new BorderLayout());

		// Set default value to JTextField
		userTextField.setText(GameConfig.DEFAULT_USER);
		portTextField.setText(GameConfig.DEFAULT_PORT.toString());
		serverIPTextField.setText(GameConfig.DEFAULT_ADDR);

		// Create the top panel to hold the client info labels
		topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension(800,200));
		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		imgLabel.setIcon(new ImageIcon("image/gameclient.png"));
		topPanel.add(imgLabel);
		getContentPane().add(topPanel, BorderLayout.NORTH);



		// Create the control panel to hold the client control buttons
		controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		controlPanel.add(userLabel);
		controlPanel.add(userTextField);
		controlPanel.add(serverIPLabel);
		controlPanel.add(serverIPTextField);
		controlPanel.add(portLabel);
		controlPanel.add(portTextField);
		controlPanel.add(connectButton).setEnabled(true);
		controlPanel.add(endButton).setEnabled(false);
		controlPanel.add(newGameButton).setEnabled(false);
		controlPanel.add(sendGameButton).setEnabled(false);
		controlPanel.add(receiveGameButton).setEnabled(false);
		controlPanel.add(sendDataButton).setEnabled(false);
		controlPanel.add(playButton).setEnabled(false);
		getContentPane().add(controlPanel, BorderLayout.CENTER);

		// Create the log panel to hold the client log text field
		logPanel = new JPanel();
		logPanel.setPreferredSize(new Dimension(800, 200));
		logPanel.setLayout(new BorderLayout());
		loggerTextArea = new JTextArea();
		loggerTextArea.setEditable(false);
		loggerScrollPane = new JScrollPane(loggerTextArea);
		loggerScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		logPanel.add(loggerScrollPane, BorderLayout.CENTER);
		getContentPane().add(logPanel, BorderLayout.SOUTH);


		ClientController cController = new ClientController(new Client());
		// Set the size of the frame and make it visible
		setSize(800, 500);
		setVisible(true);
	}


	/**
	 * ClientController inner class
	 * 
	 * Has the ActionListener for buttons in control panel
	 *
	 */
	class ClientController implements ActionListener{
		private Client client;

		/**
		 * The constructor for the contorller
		 * To add the ActionListener for the buttons
		 * @param client - client instance
		 */
		public ClientController(Client client) {
			this.client = client;
			connectButton.addActionListener(this);
			endButton.addActionListener(this);
			newGameButton.addActionListener(this);
			sendGameButton.addActionListener(this);
			receiveGameButton.addActionListener(this);
			sendDataButton.addActionListener(this);
			playButton.addActionListener(this);
		}
		/**
		 * To link the button to the function to proform the functionality
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == connectButton) {
				if(!isConnected) { 
					client.connectToServer(ip, port);
				}
			}else if(e.getSource() == endButton) {
				if(!isConnected) return;
				else client.close(); 
			}else if(e.getSource() == newGameButton) {
				client.new PopUpDialog();
			}else if(e.getSource() == receiveGameButton) {
				client.receiveGame();
			}else if(e.getSource() == sendGameButton) {
				client.sendGame();
			}else if(e.getSource() == sendDataButton) {
				client.sendData();
			}else if(e.getSource() == playButton) {
				client.playGame();
			}
		}
	}


	/**
	 * Client class for handling the server and client connections
	 *
	 */
	class Client {
		//BufferedReader and PrintWriter from Week11 lab code
		private BufferedReader input = null;
		private PrintWriter output = null;
		private String ID = "";

		/**
		 * To connect to the server with the ip address and port number user provide
		 * @param ipAddress - Server ip address
		 * @param port - Server port number
		 */
		public void connectToServer(String ipAddress, int port) {
			try {
				ip = serverIPTextField.getText();
				port = Integer.parseInt(portTextField.getText());
			} catch (Exception e) {
				port = GameConfig.DEFAULT_PORT;
				ip = GameConfig.DEFAULT_ADDR;
				loggerTextArea.append(e.getMessage() + "\n");
				loggerTextArea.append("port number will be set as " + port + " IP address will be set as: " + ip + "\n" );
			}
			try {
				// create a socket and connect to the server
				socket = new Socket(ipAddress, port);
				isConnected = true;
				// create bufferReader and PrintWriter for the socket
				input = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				output = new PrintWriter(socket.getOutputStream(), true);

				clientID = input.read() - 48;
				

				isConnected = true;

				connectButton.setEnabled(false);
				endButton.setEnabled(true);
				newGameButton.setEnabled(true);
				sendGameButton.setEnabled(true);
				receiveGameButton.setEnabled(true);
				sendDataButton.setEnabled(true);
				playButton.setEnabled(true);
				loggerTextArea.append("Connected! clientID is "+clientID+"\n");

			} catch (IOException e) {
				loggerTextArea.append("Error connecting to server: " + e.getMessage());
			}
		}

		/**
		 * To disconnect from server
		 */
		public void close() {
			if(isConnected == true) {
				try {
					input.close();
					output.close();
					socket.close();

					isConnected = false;

					newGameButton.setEnabled(false);
					sendGameButton.setEnabled(false);
					receiveGameButton.setEnabled(false);
					sendDataButton.setEnabled(false);
					playButton.setEnabled(false);
					connectButton.setEnabled(true);

					loggerTextArea.append("DisConnected from server\n");
				} catch (IOException e) {
					loggerTextArea.append(e.getMessage());
				}
			}
		}

		/**
		 * To handle the protocol to send the new game solution to server
		 */
		private void sendGame() {
			if(!isConnected) {
				loggerTextArea.append("Server not Connected\n");
				return;
			}
			try {
				loggerTextArea.append("Sending solution" + gameSolution + " to the server...\n");
				//ID#PROTOCOL#gameSolution
				String request = clientID.toString() + GameConfig.PROTOCOL_SEPARATOR + GameConfig.PROTOCOL_SENDGAME + GameConfig.PROTOCOL_SEPARATOR + gameSolution; 
				output.println(request);
				output.flush();
				String response = input.readLine();
				loggerTextArea.append(response + "\n");
				loggerTextArea.append("Solution send successful.\n");
				//loggerTextArea.append(response + "\n");
			} catch (Exception e) {
				loggerTextArea.append("Connection failed\n");
				System.out.println(e);
				isConnected = false;
			}
		}


		/**
		 * To handle the protocol for sending data to server
		 */
		private void sendData() {

			if(!isConnected) {
				loggerTextArea.append("Didn't connect to server\n");
				return;
			}
			if(model == null) {
				loggerTextArea.append("None data to send...\n");
				return;
			}
			//Get user name from text field
			userName = userTextField.getText();
			//Add result
			String data = userName + " " + model.getPoints();

			
			try {
				//ClinetID#PROTOCOL_DATA#data
				//Send the request to server to send data
				String request = clientID + GameConfig.PROTOCOL_SEPARATOR +
						GameConfig.PROTOCOL_DATA + GameConfig.PROTOCOL_SEPARATOR +
						data;
				output.println(request);
				output.flush();
				loggerTextArea.append("Sending data...\n");
				
				//Receive the response from server
				String response = input.readLine();
				loggerTextArea.append(response + "\n");
			} catch (IOException e) {
				loggerTextArea.append(e.getMessage());
			}
		}


		/**
		 * To handle the protocol for receiving the game solution from server
		 */
		private void receiveGame() {
			if(!isConnected) {
				loggerTextArea.append("Didn't connect to server\n");			
			}
			
			try {
				//clientID#PROTOCOL_RECVGAME
				//Sending the request to receive a game
				String request = clientID + GameConfig.PROTOCOL_SEPARATOR +
						GameConfig.PROTOCOL_RECVGAME;
				output.println(request);
				output.flush();
				
				loggerTextArea.append("Receiving solution...\n");
				//Get the response from server as a solution
				String response = input.readLine();
				gameSolution = response;

				loggerTextArea.append("Receive successful, new solution: " + gameSolution +"\n");
			} catch (IOException e) {
				loggerTextArea.append(e.getMessage());
			}
		}


		/**
		 * To play the game with the existing solution
		 */
		private void playGame() {
			//create new GameModel object
			model = new GameModel(gameSolution);
			//create new GameView instance
			view = new GameView(model);
		}

		/**
		 * PopUpDialog inner class
		 * Create a popup dialog for a new random game
		 *
		 */
		class PopUpDialog extends JFrame implements ActionListener {

			final String[] OPTIONS = {" ", "3", "4", "5", "6", "7", "8"};// cannot be 9 due to top and left panel length restriction
			final int[][] DIRECTIONS = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}}; // right, down, left, up

			JDialog popupDialog = new JDialog(this, "Select game dimensions", true);
			JComboBox<String>dimensionOptions = new JComboBox<>(OPTIONS);
			JButton okButton = new JButton("OK");

			/**
			 * inner class constructor to create the popup dialog
			 */
			public PopUpDialog() {
				// set up dialog components
				popupDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				popupDialog.setLayout(new FlowLayout());
				dimensionOptions.addActionListener(this);
				okButton.addActionListener(this);
				popupDialog.add(dimensionOptions);
				popupDialog.add(okButton);
				popupDialog.setLocationRelativeTo(null);
				popupDialog.setSize(300, 100);

				// display the dialog
				popupDialog.setVisible(true);
			}

			/**
			 * To add action listener to for the dialog
			 */
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == okButton) {
					try {
						// get the value of the selected option and convert to integer
						int size = Integer.parseInt((String) dimensionOptions.getSelectedItem());

						// check if size is within range
						if (size < 3 || size > 8) {
							JOptionPane.showMessageDialog(this, "Matrix size must be between 3 and 8.");
						} else {
							// generate a random matrix of size x size
							int[][] matrix = new int[size][size];
							for (int i = 0; i < size; i++) {
								for (int j = 0; j < size; j++) {
									matrix[i][j] = (int) (Math.random() * 2);
								}
							}

							// Convert matrix to string in the desired format
							StringBuilder sb = new StringBuilder();
							sb.append(size);
							sb.append(GameConfig.PROTOCOL_DIMENTION_SOLUTION_SEPARATOR);
							for (int i = 0; i < size; i++) {
								for (int j = 0; j < size; j++) {
									sb.append(matrix[i][j]);
								}
								if (i < size - 1) {
									sb.append(GameConfig.FIELD_SEPARATOR);
								}
							}

							// assign the new solution to gameSolution
							gameSolution = sb.toString();
							// display the result in the loggerTextArea
							loggerTextArea.append("New solution created: " + gameSolution + "\n");
						}
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(this, "Please select a valid integer option.");
					} finally {
						// close the dialog
						popupDialog.dispose();
					}
				}
			}
		}

	}
}
