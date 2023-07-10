# Picross
Created by Chengkuan Zhao and Chang liu

Welcome to the Java Picross repository! This project is an implementation of the popular puzzle game Picross, also known as Nonograms, using Java programming language.

What is Picross?

Picross is a logic-based puzzle game that challenges players to reveal a hidden picture by filling in the correct cells on a grid. The numbers on the edges of the grid indicate how many consecutive cells should be filled in a row or column, while leaving at least one empty cell between each group. By applying deductive reasoning and careful analysis, players can gradually unveil the image and solve the puzzle.

Getting Started
To run the Java Picross game locally on your machine, follow these steps:
1.	Clone the repository to your local system.
2.	Ensure you have Java Development Kit (JDK) installed.
3.	Compile the Java source files using the command line or an integrated development environment (IDE) of your choice.
4.	Run the main Java class to start the game.
5.	Follow the on-screen instructions to play the game and solve Picross puzzles!

Contributing

We welcome contributions to the Java Picross project. If you'd like to contribute, please fork the repository, make your changes, and submit a pull request. Our team will review your contribution and work with you to incorporate it into the project.

License

The Java Picross project is licensed under the GNU Affero General Public License v3.0 (GNU AGPL v3.0).  

Feedback and Support

If you encounter any issues, have suggestions, or need assistance with the Java Picross game, please open an issue in the repository. We value your feedback and will strive to address any concerns promptly.

Enjoy the challenging world of Picross with Java Picross! Happy puzzling!


Picross Game in Java
This is a Java implementation of the Picross game, also known as Nonograms, Griddlers, or Hanjie. It's a puzzle game where the goal is to fill in some cells in a grid based on the numbers given at the side of the grid to reveal a hidden picture.

Project Structure
The project is organized using the Model-View-Controller (MVC) design pattern. Here are the main classes:

Game.java: The entry point of the application. It creates an instance of the GameModel and GameView.

GameModel.java: The model class for the game. It maintains the game's current state, points, and other properties.

GameController.java: An interface that defines the methods for controlling the game. These methods are implemented by the GameView class.

GameView.java: The view class for the game. It is responsible for the user interface of the game.

How to Run
(Add instructions on how to compile and run the game.)

Features
This implementation of the Picross game includes the following features:

Start a new game
Save and load game state
Show the solution
Change the color of the game cells
Change the language of the game interface

