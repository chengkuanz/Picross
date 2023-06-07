package Picross;

import java.util.Scanner;
/**
 * Game.java
 * This is the game launcher for the server and client
 * 
 * @author Chengkuan Zhao, Chang Liu
 *
 */
public class Game {

    /**
     * main method
     * This is the entry point of the main line logic for the application
     *
     * @param args - main line arguments
     */
    public static void main(String[] args) {
    	
    	while(true) {
    		Game.GameLauncher launcher = new Game.GameLauncher();
    	}
    }
    
    /**
     * print the user menu and listening to user selection
     *
     */
private static class GameLauncher {
    	
    	/**
    	 * GameLauncher constructor will call initLauncher
    	 */
    	public GameLauncher() {
    		this.initLauncher();
    	}
		
    	/**
    	 * To print the menu and listen to user selection
    	 */
    	public void initLauncher() {
    		//LauncherBoard board = new LauncherBoard();
    		System.out.println("Game Launcher started");
    		Scanner userInput = new Scanner(System.in);
    		System.out.println("1 for server, 2 for client, 3 for ending the launcher");
    		int option = 0;
    		if(userInput.hasNextInt()) option = userInput.nextInt();
    		switch(option){
    		case 1:
    			GameServer server = new GameServer();
    			break;
    		case 2:
    			GameClient clinet = new GameClient();
    			break;
    		case 3:
    			System.exit(0);
    		default:
    			this.dumbUser();
    			break;
    		}
    	}
		
		/**
		 * print error message for input mismatch
		 */
		private void dumbUser() {
			System.out.println("Please choose a number from the menu");
		}
    }
}
