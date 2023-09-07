package Picross;

import java.awt.event.ActionListener;

/**
 * GameController.java
 * Gamecontroller interface implemented by GameView
 *
 * @author Chengkuan Zhao, Chang Liu
 */
public interface GameController extends ActionListener {

    /**
     * Initialize the popup dialog.
     */
    void initPopupDialog();

    /**
     * Initialize the popup splash window.
     */
    void initPopupDialogSpalsh();

    /**
     * initial the game
     *
     * @param gameDimension - game dimension
     * @param reset - reset the game
     */
    void initGame(int gameDimension, int reset);

    /**
     * Method to save the game
     *
     * @param currentStatus - current game status
     */
    void saveGame(Integer[][] currentStatus);

    /**
     * Method to load the saved game
     */
    void loadSavedGame();

    /**
     * Method to start a new game
     */
    void startNewGame();

    /**
     * Method to reset the game
     */
    void resetGame();

    /**
     * Method to exit the game
     */
    void exitGame();

    /**
     * Method to show the solution
     */
    void showSolution();

    /**
     * Method to generate a solution - popup dialog
     */
    void solutionPopupDialog();

    /**
     * Method to show the user menu
     */
    void showUserMenu();

    /**
     * Method to change the default box color
     */
    void changeDefaultBoxColor();


    /**
     * Generate a random solution matrix when starting the game
     * @return tempSolution - game solution
     */
    Integer[][] generateSolution();

    /**
     * Method to update the button matrix
     * @param i - row
     * @param j - column
     */
    void updateButtonMatrix(int i, int j);
}
