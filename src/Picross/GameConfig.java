package Picross;

/**
 * GameConfig.java
 * This is the configuration class with basic game proeprties and network protocols
 * 
 * @author Chengkuan Zhao, Chang Liu
 *
 */
public class GameConfig {
	//protocol properties
	static final String PROTOCOL_SEPARATOR = "#";
	static final String PROTOCOL_DIMENTION_SOLUTION_SEPARATOR = ";";
	static final String FIELD_SEPARATOR = ",";
	static final String PROTOCOL_END = "0";
	static final String PROTOCOL_SENDGAME = "1";
	static final String PROTOCOL_RECVGAME = "2";
	static final String PROTOCOL_DATA = "3";
	//image for the game
	static final String IMAGE_GAME = "";
	//network properties
	static String DEFAULT_USER = "Chang vs Chengkuan";
	static String DEFAULT_ADDR = "127.0.0.1";
	static Integer DEFAULT_PORT = 12345;
	static final String DEFAULT_GAME_SOLUTION = "5;00110,10110,01100,01111,11011";
}