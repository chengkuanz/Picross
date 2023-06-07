package Picross;

/**
 * Game.java
 * main class of picross game
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
    	// create new GameModel object
    	GameModel model = new GameModel();
     	//create new GameView instance
    	GameView view = GameView.getInstance(model);
    }

}
