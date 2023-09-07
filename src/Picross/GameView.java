package Picross;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * GameView.java
 * Hold the game interface and definition of methods from controller
 *
 * @author Chengkuan Zhao, Chang Liu
 */
@SuppressWarnings("serial")
public class GameView extends JFrame implements GameController {
    private static String POINT = "";
    /**
     * Singleton instance.
     */
    private static GameView instance = null;

    /**
     * Resource file name.
     */
    private static final String SYSTEMMESSAGES = "resources/texts";
    /**
     * Resource bundle. TITLE - for testing purpose only
     */
    private static String TITLE = "";
    /**
     * Resource bundle. GREETING - for testing purpose only
     */
    private static String GREETING = "";

    /**
     * points
     */
    private static String SHOWPOINTS = "";

    /**
     * seconds after game started
     */
    private static String STOPWATCH = "";

    /**
     * file path for logo icon for picross
     */
    static final String ICON_NAME = "image/logo.png";

    /**
     * file path for perfect result image for picross
     */
    static final String finalPerfect_PATH = "image/perfect.png";

    /**
     * file path for common result image for picross
     */
    static final String finalCommon_PATH = "image/common.png";

    /**
     * image file path for loading game for picross
     */
    static final String starting_PATH = "image/starting.png";

    /**
     * The model of the game.
     */
    private final GameModel model;

    /**
     * The saved file name extension.
     */
    private static final String EXTENSION = ".txt";

    /**
     * menu bar
     */
    private JMenuBar menuBar;

    /**
     * The game menu.
     */
    private JMenu gameMenu, helpMenu;

    /**
     * The game menu items.
     */
    private JMenuItem saveGame, loadSavedGame, newGameSameSize, newGameNewSize, resetGame, exitGame, showSolution, trigglerAbout, 
    		customizedColorChangedDefault, customizedColorChangedSelected, customizedColorChangedUnselected;

    /**
     * The game board dimension.
     */
    private JComboBox<String> dimensionOptions;

    /**
     * The button to confirm the game dimension.
     */
    private JButton okButton;

    /**
     * The game dimension options.
     */
    private static final String[] OPTIONS = {" ", "3", "4", "5", "6", "7", "8"};// cannot be 9 due to top and left panel length restriction

    /**
     * The popup dialog.
     */
    private JDialog popupDialog;

    /**
     * The dimension of the game.
     */
    private int gameDimension;

    /**
     * The frame for game.
     */
    private JFrame frame;

    /**
     * The constructor of the view.
     * @param model the model of the game
     */
    public GameView(GameModel model) {
        // the puzzle should be changed to the user-defined game settings, puzzle is just a generic express for implementing menuItems
        this.model = model;
        this.initGame(gameDimension, 1);
    }

//	  In order to have the ability for multiple clients run multiple games, singleton is not implemented in A32  
//    public static GameView getInstance(GameModel model) {
//        if (instance == null) {
//            instance = new GameView(model);
//        }
//        return instance;
//    }

    /**
     * Initialize the popup dialog -selet game size
     */
    @Override
    public void initPopupDialog() {
        //initPopupDialogSpalsh();
        popupDialog = new JDialog(this, "Select game dimensions", true);
        popupDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        popupDialog.setLayout(new FlowLayout());

        dimensionOptions = new JComboBox<>(OPTIONS);
        okButton = new JButton("OK");
        dimensionOptions.addActionListener(this);
        okButton.addActionListener(this);

        popupDialog.add(dimensionOptions);
        popupDialog.add(okButton);

        popupDialog.setLocationRelativeTo(null);
        popupDialog.setSize(300, 100);

        popupDialog.setVisible(true);
    }

    /**
     * Initialize the popup dialog for splash screen.
     */
    public void initPopupDialogSpalsh() {
        popupDialog = new JDialog(this, "Loading ", true);
        popupDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        popupDialog.setLayout(new BorderLayout());

        ImageIcon image = new ImageIcon(starting_PATH);
        JLabel imageLabel = new JLabel(image);

        popupDialog.add(imageLabel, BorderLayout.CENTER);

        popupDialog.setLocationRelativeTo(null);
        popupDialog.setSize(image.getIconWidth() + 50, image.getIconHeight());

        // set the timer to close the dialog
        //lambda expression
        Timer timer = new Timer(3000, e -> popupDialog.dispose());
        timer.setRepeats(false);
        timer.start();

        popupDialog.setVisible(true);
    }


    /**
     * initialize the game properties and load the UI
     * @param gameDimension the dimension of the game
     * @param reset the flag to reset the game or not, 0 for reset, 1 for not reset
     */
    @Override
    public void initGame(int gameDimension, int reset) {
        // generate solution if 0
        if (reset == 0) {
            this.model.setGameSolution(this.generateSolution());
            this.model.resetGame();
        }
        System.out.println("game dimension:" + gameDimension);

        // Dispose of the old frame and create a new one
        if (frame != null) {
            frame.dispose();
        }
        frame = new JFrame("Picross A12");
        menuBar = new JMenuBar();
        gameMenu = new JMenu("Game");
        helpMenu = new JMenu("Help");
        saveGame = new JMenuItem("Save Game");
        loadSavedGame = new JMenuItem("Load Saved Game");
        newGameNewSize = new JMenuItem("New Game (New Size)");
        newGameSameSize = new JMenuItem("New Game (Same Size)");
        resetGame = new JMenuItem("Reset Game");
        exitGame = new JMenuItem("Exit Game");
        showSolution = new JMenuItem("Show Solution");
        trigglerAbout = new JMenuItem("About");
        customizedColorChangedDefault = new JMenuItem("Custom cell default Color");
        customizedColorChangedSelected = new JMenuItem("Custom selected cell Color");
        customizedColorChangedUnselected = new JMenuItem("Custom unselected cell Color");
        
        // add action listeners to menu items
        saveGame.addActionListener(this);
        loadSavedGame.addActionListener(this);
        newGameSameSize.addActionListener(this);
        newGameNewSize.addActionListener(this);
        resetGame.addActionListener(this);
        exitGame.addActionListener(this);
        showSolution.addActionListener(this);
        trigglerAbout.addActionListener(this);
        customizedColorChangedDefault.addActionListener(this);
        customizedColorChangedSelected.addActionListener(this);
        customizedColorChangedUnselected.addActionListener(this);

        // add menu items to menus
        gameMenu.add(saveGame);
        gameMenu.add(loadSavedGame);
        gameMenu.add(newGameSameSize);
        gameMenu.add(newGameNewSize);
        gameMenu.add(resetGame);
        gameMenu.add(exitGame);
        helpMenu.add(showSolution);
        helpMenu.add(trigglerAbout);
        helpMenu.add(customizedColorChangedDefault);
        helpMenu.add(customizedColorChangedSelected);
        helpMenu.add(customizedColorChangedUnselected);

        // add menus to menu bar
        menuBar.add(gameMenu);
        menuBar.add(helpMenu);

        // set menu bar for this frame
        menuBar.setVisible(true);
        frame.setJMenuBar(menuBar);

        //create frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLayout(new GridBagLayout());
        frame.setResizable(false);
        frame.setVisible(true);

        //point panel below====================================================
        JPanel pointPanel = new JPanel();
        pointPanel.setLayout(new BoxLayout(pointPanel, BoxLayout.Y_AXIS));
        //pointPanel.setPreferredSize(new Dimension(100, 5));
        //add icon to point panel
        JPanel pointPanelIcon = new JPanel();
        JLabel AssgNumberLabel = new JLabel("A22");
        AssgNumberLabel.setIconTextGap(2);//set the gap between icon and text
        AssgNumberLabel.setVerticalTextPosition(JLabel.CENTER);//set the position of text
        //add icon to label
        ImageIcon picrossIcon = new ImageIcon(ICON_NAME);
        Image img = picrossIcon.getImage();//get image from icon
        Image newimg = img.getScaledInstance(40, 16, java.awt.Image.SCALE_SMOOTH);//scale image
        picrossIcon = new ImageIcon(newimg);
        AssgNumberLabel.setIcon(picrossIcon);//add icon to label
        pointPanelIcon.add(AssgNumberLabel);//add label to panel
        pointPanel.add(pointPanelIcon);
        //add point label to point panel
        JPanel pointPanelPoint = new JPanel();
        JLabel pointLabel = new JLabel("Picross");
        //JLabel pointLabel = new JLabel("point: " );
        pointPanelPoint.add(pointLabel);
        pointPanel.add(pointPanelPoint);
        GridBagConstraints gbcPoint = new GridBagConstraints();
        gbcPoint.gridx = 0;
        gbcPoint.gridy = 0;
        gbcPoint.gridwidth = 1;
        gbcPoint.gridheight = 1;
        gbcPoint.anchor = GridBagConstraints.LAST_LINE_END;
        frame.add(pointPanel, gbcPoint);

        //left panel below====================================================
        JPanel leftPanel = new JPanel();
        JLabel[][] labelMatrixLeft = new JLabel[model.getGridDimension()][model.getGridDimension()];
        System.out.println("infoForleftPanel");
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < model.getGridDimension(); j++) {
                labelMatrixLeft[i][j] = new JLabel();

                labelMatrixLeft[i][j].setText(infoForLeftPanel(j));
                labelMatrixLeft[i][j].setHorizontalAlignment(JLabel.CENTER);
                leftPanel.add(labelMatrixLeft[i][j]);
            }
        }
        leftPanel.setLayout(new GridLayout(model.getGridDimension(), 1));
        leftPanel.setBackground(Color.pink);
        //leftPanel.setPreferredSize(new Dimension(20, 200));
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 1;
        gbcLeft.gridwidth = 1;
        gbcLeft.gridheight = 5;
        gbcLeft.fill = GridBagConstraints.VERTICAL;
        gbcLeft.anchor = GridBagConstraints.LINE_END;
        frame.add(leftPanel, gbcLeft);

        //top panel below====================================================
        JPanel topPanel = new JPanel();
        JLabel[][] labelMatrixTop = new JLabel[model.getGridDimension()][model.getGridDimension()];
        //System.out.println("infoForTopPanel");
        for (int i = 0; i < model.getGridDimension(); i++) {
            for (int j = 0; j < 1; j++) {
                labelMatrixTop[i][j] = new JLabel();
                labelMatrixTop[i][j].setText(infoForTopPanel(i));
                //infoForTopPanel(i);
                labelMatrixTop[i][j].setVerticalAlignment(JLabel.CENTER);
                labelMatrixTop[i][j].setHorizontalAlignment(JLabel.CENTER);
                topPanel.add(labelMatrixTop[i][j]);
            }
        }
        topPanel.setLayout(new GridLayout(1, model.getGridDimension()));
        topPanel.setBackground(new Color(0xEF890D));
        //topPanel.setPreferredSize(new Dimension(20, 95));//adjust here
        GridBagConstraints gbcTop = new GridBagConstraints();
        gbcTop.gridx = 1;
        gbcTop.gridy = 0;
        gbcTop.gridwidth = 5;
        gbcTop.gridheight = 1;
        gbcTop.fill = GridBagConstraints.HORIZONTAL;
        // gbc.anchor = GridBagConstraints.PAGE_END;
        frame.add(topPanel, gbcTop);


        //board panel below====================================================
        JPanel panelBoard = new JPanel();

        panelBoard.setBackground(Color.black);
        //matrix of the button
        JButton[][] buttonMatrix = new JButton[model.getGridDimension()][model.getGridDimension()];
        //add button to panelBoard
        for (int i = 0; i < model.getGridDimension(); i++) {
            for (int j = 0; j < model.getGridDimension(); j++) {
                final int finalI = i;
                final int finalJ = j;
                buttonMatrix[i][j] = new JButton();
                buttonMatrix[i][j].setBackground(model.getColor2());
                switch (model.getCurrentStatus()[i][j]) {
                    case 2:
                        buttonMatrix[i][j].setBackground(model.getColor2());
                        break;
                    case 3:
                        buttonMatrix[i][j].setBackground(model.getColor3());
                        break;
                    case 4:
                        buttonMatrix[i][j].setBackground(model.getColor4());
                        break;
                    default:
                        buttonMatrix[i][j].setBackground(model.getColor2());
                        break;
                }
                //buttonMatrix[i][j].setBackground(new Color(0xEF890D));
                buttonMatrix[i][j].setOpaque(true);
                buttonMatrix[i][j].setBorderPainted(false);
                buttonMatrix[i][j].addActionListener(e -> {
                    // change the color of the button
                    JButton button = (JButton) e.getSource();
                    Color color = button.getBackground();
                    if (color.equals(model.getColor2())) {
                        button.setBackground(model.getColor3());
                    } else if (color.equals(model.getColor3())) {
                        button.setBackground(model.getColor4());
                        button.setOpaque(true);
                        button.setBorderPainted(false);
                    } else if (color.equals(model.getColor4())) {
                        button.setBackground(model.getColor3());
                        button.setOpaque(true);
                        button.setBorderPainted(false);
                    }
                    // update the state in the game model
                    updateButtonMatrix(finalI, finalJ);
                });
                panelBoard.add(buttonMatrix[i][j]);
            }
        }
        panelBoard.setLayout(new GridLayout(model.getGridDimension(), model.getGridDimension()));
        GridBagConstraints gbcboard = new GridBagConstraints();
        gbcboard.gridx = 1;
        gbcboard.gridy = 1;
        gbcboard.gridwidth = 5;
        gbcboard.gridheight = 5;
        gbcboard.fill = GridBagConstraints.BOTH;
        gbcboard.weightx = 1;
        gbcboard.weighty = 1;
        // gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        frame.add(panelBoard, gbcboard);


        //control panel below====================================================
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(Color.green);
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        //add text area
        JTextArea textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(110, 200));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        controlPanel.add(textArea);
        JTextArea textArea2 = new JTextArea();
        textArea2.setPreferredSize(new Dimension(110, 200));
        textArea2.setLineWrap(true);
        textArea2.setWrapStyleWord(true);
        textArea2.setEditable(false);
        JTextArea textArea3 = new JTextArea();
        textArea3.setPreferredSize(new Dimension(110, 200));
        textArea3.setLineWrap(true);
        textArea3.setWrapStyleWord(true);
        textArea3.setEditable(false);
        controlPanel.add(textArea3);
        JTextArea textArea4 = new JTextArea();
        textArea4.setPreferredSize(new Dimension(110, 200));
        textArea4.setLineWrap(true);
        textArea4.setWrapStyleWord(true);
        textArea4.setEditable(false);
        controlPanel.add(textArea4);
        //textArea4.setText("stopwatch: \n" );
        JTextArea textArea5 = new JTextArea();
        textArea5.setPreferredSize(new Dimension(110, 200));
        textArea5.setLineWrap(true);
        textArea5.setWrapStyleWord(true);
        textArea5.setEditable(false);
        controlPanel.add(textArea5);


        // Set the initial time of label creation
        Date creationTime = new Date();

        // Create a timer to update the label text every second
        // The timer calls the actionPerformed method of the ActionListener
        Timer timer = new Timer(1000, e -> {
            // Calculate the elapsed time since label creation
            long elapsedTime = new Date().getTime() - creationTime.getTime();

            // Convert the elapsed time to a string in seconds
            String elapsedSeconds = String.format("%.0f", elapsedTime / 1000.0);

            // Set the label's text to show the elapsed time in seconds
            textArea5.setText(elapsedSeconds + " seconds");
        });
        timer.start();


        //=====================================================================================
        //testing resources bundle below
        String SYSTEMMESSAGESTESTING = "resources/MessageBundle";
        ResourceBundle bundle = ResourceBundle.getBundle(SYSTEMMESSAGESTESTING, Locale.US);
        System.out.println("Message in " + Locale.US + ":" + bundle.getString("greeting"));
        //changing the default locale to indonasian
        Locale.setDefault(new Locale("in", "ID"));
        bundle = ResourceBundle.getBundle("MessageBundle");
        System.out.println("Message in " + Locale.getDefault() + ":" + bundle.getString("greeting"));
        //testing resources bundle above
        //=====================================================================================

        JPanel ControlPanelPoint = new JPanel();
        JButton showPointButtom = new JButton("Current Points");//default text before language selection
        showPointButtom.addActionListener(e -> textArea3.setText(" " + model.getPoints()));
        ControlPanelPoint.add(showPointButtom);//add button to panel


        //add language selection comboBox
        JPanel ControlPanelLanguage = new JPanel();
        String[] language = {"English", "French"};
        JComboBox<String> languageComboBox = new JComboBox<>(language);
        languageComboBox.addActionListener(e -> {
            String selectedOption = (String) languageComboBox.getSelectedItem();
            textArea.setText("EN / FR : \n" + selectedOption + "\n" + "\n");
            assert selectedOption != null;
            if (selectedOption.equals("English")) {
                updateInterface(1);
                //showPointButtom.setText("update points");
                showPointButtom.setText(SHOWPOINTS);
                //textArea4.setText("stopwatch: \n" );
                textArea4.setText(STOPWATCH);

            } else if (selectedOption.equals("French")) {
                updateInterface(2);
                //showPointButtom.setText("mettre à jour les points");
                showPointButtom.setText(SHOWPOINTS);
                textArea4.setText(STOPWATCH);
            }
            //those two lines cannot be put in before the if else statement
            textArea.append(GREETING + "\n");
            textArea.append(POINT);

        });

        controlPanel.add(ControlPanelPoint);
        ControlPanelLanguage.add(languageComboBox);
        controlPanel.add(ControlPanelLanguage);
        controlPanel.repaint();
        controlPanel.revalidate();
        GridBagConstraints gbcControlPanel = new GridBagConstraints();
        gbcControlPanel.gridx = 6;
        gbcControlPanel.gridy = 0;
        gbcControlPanel.gridwidth = 1;
        gbcControlPanel.gridheight = 6;
        frame.add(controlPanel, gbcControlPanel);
        System.out.println("loading finished");
        calculatePoints();

        frame.validate();
        frame.repaint();
        modifyJFrame(frame);
    }


    /**
     * Splash window for finishing the game
     *
     * @param a 1 for perfect score, 0 for not perfect score
     */
    public void finalizationPopupDialog(int a) {//1 for perfect, 0 for not perfect
        popupDialog = new JDialog(this, "finalization", true);
        popupDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        // Create a StringBuilder object to construct the paragraph
        StringBuilder paragraph = new StringBuilder();
        if (a == 1) {
            paragraph.append("Congratulations!\n\nYou have completed the game with a perfect score! \n\n");
        } else {
            paragraph.append("You have completed the game!\n\nSome selection are not correct \n\n ");
        }
        paragraph.append("Your final score is " + model.getPoints() + " points. \n\n");
        // Create a JTextArea to display the paragraph
        JTextArea textArea = new JTextArea(paragraph.toString());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Declare the ImageIcon object outside the if-else block
        ImageIcon image;
        ImageIcon tempIcon;

        // Create a JLabel to display the image
        if (a == 1) {

            tempIcon = new ImageIcon(finalPerfect_PATH);

        } else {
            // image = new ImageIcon(finalCommon_PATH);
            tempIcon = new ImageIcon(finalCommon_PATH);

        }
        Image img = tempIcon.getImage();//get image from icon
        Image newimg = img.getScaledInstance(500, 400, java.awt.Image.SCALE_SMOOTH);//scale image
        image = new ImageIcon(newimg);

        JLabel imageLabel = new JLabel(image);
        // Create a JPanel to hold the text area and the image label
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(imageLabel, BorderLayout.EAST);

        // Add the panel to the pop-up window
        popupDialog.add(panel);
        popupDialog.setSize(700, 500); // Set the size of the window
        popupDialog.setLocationRelativeTo(null);
        popupDialog.setVisible(true);
    }


    /**
     * this method is used to update the jframe
     *
     * @param frame the jframe to be updated
     */
    public static void modifyJFrame(JFrame frame) {
        // Validate and repaint the JFrame
        frame.revalidate();
        frame.repaint();
    }


    /**
     * this method is used to convert int array to string for top panel
     *
     * @param index the index of the column
     * @return the string of the column that is used to display on the top panel
     */
    public String infoForTopPanel(int index) {

        //create array with 0 size of 9
        int[] count = new int[9];
        //set array with 0
        Arrays.fill(count, 0);
        for (int i = 0; i < model.getGridDimension(); i++) {
            if (model.getCurrentStatus()[i + 9][index] == 3) {
                count[i] = 1;
                int tempI = i;
                while (model.getCurrentStatus()[tempI + 9 + 1][index] == 3) {
                    count[i] = count[i] + 1;
                    tempI++;
                }
            }
            i = i + count[i];
            if (count[i] > 0) {
                i--;
            }
        }
        String result = convertIntArrayToString(count);
        return result;

    }


    /**
     * this method is used to convert int array to string for left panel
     *
     * @param index the index of the row
     * @return the string of the row that is used to display on the left panel
     */
    public String infoForLeftPanel(int index) {

        //create array with 0 size of 9
        int[] count = new int[9];
        //set array with 0
        Arrays.fill(count, 0);
        for (int i = 0; i < model.getGridDimension(); i++) {
            if (model.getCurrentStatus()[index + 9][i] == 3) {
                count[i] = 1;
                int tempI = i;
                while (model.getCurrentStatus()[index + 9][tempI + 1] == 3) {
                    count[i] = count[i] + 1;
                    tempI++;
                }
            }
            i = i + count[i];
            if (count[i] > 0) {
                i--;
            }

        }

        System.out.println(Arrays.toString(count));
        String result = convertIntArrayToString(count);
        return result;
    }


    /**
     * this method is used to convert int array to string
     *
     * @param arr the array to be converted
     * @return the string of the array
     */
    public static String convertIntArrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != 0) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(arr[i]);
            }
        }
        return sb.toString();
    }


    /**
     * Method to calculate points for the game
     */
    public void calculatePoints() {
        model.resetPoints();
        System.out.println("(calculatePoints method)19*9 array: ");
        int countDefault = 0;
        for (int i = 0; i < model.getGridDimension(); i++) {
            for (int j = 0; j < model.getGridDimension(); j++) {
                if ((model.getCurrentStatus()[i][j] != 2) && (!model.getCurrentStatus()[i + 9][j].equals(model.getCurrentStatus()[i][j]))) {
                    model.minusOnePoint();
                }
                if (model.getCurrentStatus()[i + 9][j].equals(model.getCurrentStatus()[i][j])) {
                    model.addOnePoint();
                }
                if (model.getCurrentStatus()[i][j] == 2) {
                    countDefault++;
                }
            }
        }
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(model.getCurrentStatus()[i][j]);
            }
            System.out.println();
        }
        System.out.println("(calculatePoints method)points: " + model.getPoints());


        if (model.getPoints() == model.getGridDimension() * model.getGridDimension()) {
            finalizationPopupDialog(1);
        } else if (countDefault == 0) {
            finalizationPopupDialog(0);
        }


    }


    /**
     * This method is used to update the interface when the language is changed by the user
     *
     * @param langIndex: 1 for English, 2 for French
     */
    public void updateInterface(int langIndex) {
        String language = "";
        String country = "";
        switch (langIndex) {
            case 1:
                language = "en";
                country = "US";
                break;
            case 2:
                language = "fr";
                country = "FR";
                break;
            default:
                language = "en";
                country = "US";
                break;
        }
        try {
            Locale currentLocale = new Locale.Builder().setLanguage(language).setRegion(country).build();
            ResourceBundle texts = ResourceBundle.getBundle(SYSTEMMESSAGES, currentLocale);
            // Uploading properties
            TITLE = texts.getString("TITLE");
            GREETING = texts.getString("GREETING");
            POINT = texts.getString("POINT");
            SHOWPOINTS = texts.getString("SHOWPOINTS");
            STOPWATCH = texts.getString("STOPWATCH");
        } catch (Exception e) {
            System.out.println("Error loading properties");
        }
    }

    /**
     * Method to perform menu actions
     *
     * @param event the action event
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        // handle action events for menu items
        if (event.getSource() == saveGame) {
            saveGame(model.getCurrentStatus());
        } else if (event.getSource() == loadSavedGame) {
            loadSavedGame();
            this.initGame(model.getGridDimension(), 1);
            //modifyJFrame();
        } else if (event.getSource() == newGameSameSize) {
            startNewGame();
            initGame(model.getGridDimension(), 0);
        } else if (event.getSource() == newGameNewSize) {
            initPopupDialog();
        } else if (event.getSource() == resetGame) {
            resetGame();
            initGame(model.getGridDimension(), 1);
        } else if (event.getSource() == exitGame) {
            exitGame();
        } else if (event.getSource() == showSolution) {
            showSolution();
        } else if (event.getSource() == trigglerAbout) {
            showUserMenu();
        } else if (event.getSource() == customizedColorChangedDefault) {
            changeDefaultBoxColor();
            this.initGame(gameDimension, 1);
        } else if (event.getSource() == customizedColorChangedSelected) {
            changeSelectedBoxColor();
            this.initGame(gameDimension, 1);
        } else if (event.getSource() == customizedColorChangedUnselected) {
            changeUnselectedBoxColor();
            this.initGame(gameDimension, 1);
        } else if (event.getSource() == dimensionOptions) {
            //update the dimension in the model with user selection
            String userSelection = (String) dimensionOptions.getSelectedItem();
            gameDimension = Integer.parseInt(userSelection);
            //model.setDimension(Integer.parseInt(userSelection));
            model.setDimension(gameDimension);
        } else if (event.getSource() == okButton) {
            this.dispose();
            this.initGame(gameDimension, 0);


        }
    }


    /**
     * Method to save the game
     *
     * @param currentStatus the current status of the game
     */
    @Override
    public void saveGame(Integer[][] currentStatus) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save " + EXTENSION + " file");
        //fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                FileWriter writer = new FileWriter(fileChooser.getSelectedFile());
                for (int i = 0; i < 19; i++) {
                    for (int j = 0; j < 9; j++) {
                        writer.write(model.getCurrentStatus()[i][j] + " ");
                    }
                    writer.write("\n");
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Method to load the saved game
     */
    @Override
    public void loadSavedGame() {
        int[][] currentStatusFromFile = new int[19][9];
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose a " + EXTENSION + " file");
        //fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        // set the default directory to the current directory
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        // Filter to show only text files
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
        fileChooser.setFileFilter(filter);
        int userSelection = fileChooser.showOpenDialog(null); //or this
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            try {
                Scanner myScanner = new Scanner(fileToOpen);
                for (int i = 0; i < 19; i++) {
                    for (int j = 0; j < 9; j++) {
                        currentStatusFromFile[i][j] = myScanner.nextInt();
                    }
                }
                myScanner.close();
                System.out.println("load game success");
            } catch (FileNotFoundException fe) {
                System.out.println("error opening file");
                fe.printStackTrace();
            }
        }
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 9; j++) {
                model.setCurrentStatusAtOneCile(i, j, currentStatusFromFile[i][j]);
            }
            System.out.println();
        }

        System.out.println("content of the " + EXTENSION + " file shown below:");
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(currentStatusFromFile[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("content of the " + EXTENSION + " file shown above");
        calculatePoints();


    }

    /**
     * Method to start a new game
     */
    @Override
    public void startNewGame() {
        model.setGameSolution(this.generateSolution());
        calculatePoints();
    }

    /**
     * Method to reset the game
     */
    @Override
    public void resetGame() {
        model.resetGame();
        //updateButtonMatrix();
    }

    /**
     * Method to exit the game
     */
    @Override
    public void exitGame() {
        System.exit(0);
    }

    /**
     * Method to show the solution
     */
    @Override
    public void showSolution() {

        solutionPopupDialog();
    }


    /**
     * Method to generate a solution
     */
    @Override
    public void solutionPopupDialog() {
        popupDialog = new JDialog(this, "Solution", true);
        popupDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        //popupDialog.setSize(500, 500);
        popupDialog.setLayout(new GridLayout(9, 9)); // Use GridLayout to display the array
        String[][] arrayInString = new String[9][20];

        // Add the integers to the pop-up window as JLabels
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 20; j++) {


                if (i < model.getGridDimension() && j < model.getGridDimension()) {
                    if (model.getCurrentStatus()[i + 9][j] == 3) {
                        arrayInString[i][j] = "X";
                    } else {
                        arrayInString[i][j] = "O";
                    }
                    if (i >= model.getGridDimension() || j >= model.getGridDimension()) {
                        arrayInString[i][j] = " ";
                    }

                }

                JLabel label = new JLabel(arrayInString[i][j]);
                popupDialog.add(label);
            }
        }

        popupDialog.pack(); // Set the size of the window to fit its contents
        popupDialog.setLocationRelativeTo(null);
        popupDialog.setVisible(true);
    }

    /**
     * Method to show the about dialog
     */
    public void aboutPopupDialog() {
        popupDialog = new JDialog(this, "Solution", true);
        popupDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        popupDialog.setSize(500, 500);

        // Create a StringBuilder object to construct the paragraph
        StringBuilder paragraph = new StringBuilder();

        // Add an introductory paragraph about game
        paragraph.append("This is the second implementation in the JAP course. .\n");
        paragraph.append("The purpose is to implement the game using DP (Design Patterns), \n");
        paragraph.append("specially the MVC (Model-View-Controller) structure for the game.\n\n");
        // Create a JTextArea to display the paragraph
        JTextArea textArea = new JTextArea(paragraph.toString());
        textArea.setEditable(false);
        textArea.setLineWrap(false);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Add the JTextArea to the pop-up window
        popupDialog.add(scrollPane);

        popupDialog.pack(); // Set the size of the window to fit its contents
        popupDialog.setLocationRelativeTo(null);
        popupDialog.setVisible(true);
    }

    /**
     * Method to prompt out user menu and show current status matrix
     */
    @Override
    public void showUserMenu() {
        System.out.print("(from menu-help-about)content of the currentStatus matrix shown below: \n");
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(model.getCurrentStatus()[i][j]);
            }
            System.out.println();
        }
        aboutPopupDialog();
    }

    /**
     *
     */
    @Override
    public void changeDefaultBoxColor() {
        //TODO: trigger the color from model to set the color
    	
        model.setColor2(JColorChooser.showDialog(null, "Choose default cell color", Color.WHITE)); 
        //buttonMatrix[i][j].setBackground(color1);
    }
    
    public void changeSelectedBoxColor() {
        //TODO: trigger the color from model to set the color
    	
         
        model.setColor3(JColorChooser.showDialog(null, "Choose selected cell color", Color.WHITE));
        
        //buttonMatrix[i][j].setBackground(color1);
    }

    public void changeUnselectedBoxColor() {
        //TODO: trigger the color from model to set the color
    	
        model.setColor4(JColorChooser.showDialog(null, "Choose unselected cell color", Color.WHITE));
        //buttonMatrix[i][j].setBackground(color1);
    }
    
    //menu item stops here

    /**
     * Method to update the board button matrix
     */
    @Override
    public void updateButtonMatrix(int i, int j) {
        //change current status
        //model.getDimension -> current status matrix
        System.out.println(i);
        System.out.println(j);
        if (model.getCurrentStatus()[i][j] == 3) {
            model.setCurrentStatusAtOneCile(i, j, 4);

        } else {
            model.setCurrentStatusAtOneCile(i, j, 3);


        }
        calculatePoints();
        //When game ends: After selecting all squares, the points and timeout are shown. Eventually, a new game can be started.
    }


    /**
     * Generate a random solution matrix when starting the game
     *
     * @return tempSolution - game solution
     */
    @Override
    public Integer[][] generateSolution() {
        Integer[][] tempSolution = new Integer[model.getGridDimension()][model.getGridDimension()];
        Random random = new Random();
        int a = 3;
        int b = 4;
        for (int i = 0; i < model.getGridDimension(); i++) {
            for (int j = 0; j < model.getGridDimension(); j++) {
                tempSolution[i][j] = random.nextBoolean() ? a : b;
                //tempSolution[i][j] =3;
            }
        }
        return tempSolution;
    }
}