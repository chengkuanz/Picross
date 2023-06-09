package Picross;

import java.awt.Color;

/**
 * GameModel.java
 * Hold the solutions and other game properties
 *
 * @author Chengkuan Zhao, Chang Liu
 */
public class GameModel {


    private Integer[][] currentStatus = new Integer[19][9];
    private Integer[][] gameSolution;
    //color 2 - default color
    private Color color2 = Color.WHITE;
    //color 3 - selected color
    private Color color3 = Color.BLACK;
    //color 4 - unselected color
    private Color color4 = Color.RED;


    /**
     * Game model constructor, will init currentStatus matrix to 2 (defalut value)
     */
    public GameModel() {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 9; j++) {
                currentStatus[i][j] = 2;
            }
        }
    }

    /**
     * get the points
     *
     * @return points - points for the game
     */
    public int getPoints() {
        return currentStatus[18][1];
    }

    /**
     * set the points
     *
     * @param points - points for the game
     */
    public void setPoints(int points) {
        this.currentStatus[18][1] = points;
    }

    /**
     * get the default object
     * 
     * @return color2 - color object
     */
    public Color getColor2() {
		return color2;
	}

	/**
	 * set default color
	 * 
	 * @param color2 - default color
	 */
	public void setColor2(Color color2) {
		this.color2 = color2;
	}

	/**
	 * get the selected object
	 * 
	 * @return color3 - color object
	 */
	public Color getColor3() {
		return color3;
	}

	/**
	 * set selected color
	 * 
	 * @param color3 - selected color
	 */
	public void setColor3(Color color3) {
		this.color3 = color3;
	}

	/**
	 * get the unselected object
	 * 
	 * @return color4 - color object
	 */
	public Color getColor4() {
		return color4;
	}

	/**
	 * set unselected color
	 * @param color4 - unselected color
	 */
	public void setColor4(Color color4) {
		this.color4 = color4;
	}

	/**
     * set the dimension of the game
     *
     * @param dimension - dimension of the game
     */
    public void setDimension(int dimension) {
        this.currentStatus[18][0] = dimension;
    }

    /**
     * get the current status
     *
     * @return currentStatus - current game status
     */
    public Integer[][] getCurrentStatus() {
        return currentStatus;
    }

    /**
     * reset points
     */
    public void resetPoints() {
        this.currentStatus[18][1] = 0;
    }

    /**
     * add one point
     */
    public void addOnePoint() {
        this.currentStatus[18][1]++;
    }

    /**
     * minus one point
     */
    public void minusOnePoint() {
        this.currentStatus[18][1]--;
    }


    /**
     * get the solution
     * @return - game dimension
     */
    public int getGridDimension() {
        return currentStatus[18][0];
    }


    /**
     * @param a - row
     * @param b - col
     * @param c - value
     */
    public void setCurrentStatusAtOneCile(int a, int b, int c) {
        currentStatus[a][b] = c;
    }


    /**
     * @param currentStatus - current game status
     */
    public void setCurrentStatus(Integer[][] currentStatus) {
        this.currentStatus = currentStatus;
    }


    /**
     * @param solution - solution for the game
     */
    public void setGameSolution(Integer[][] solution) {
        this.gameSolution = solution;
        this.saveSolution(gameSolution);
    }


    /**
     * @param solution - solution for the game
     */
    public void saveSolution(Integer[][] solution) {
        int rowOffset = 9;
        int colOffset = 0;
        for (int i = 0; i < solution.length; i++) {
            for (int j = 0; j < solution[0].length; j++) {
                currentStatus[rowOffset + i][colOffset + j] = solution[i][j];
            }
        }
    }

    /**
     * reset the game
     */
    public void resetGame() {
        resetPoints();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                currentStatus[i][j] = 2;
            }
        }
    }


}
