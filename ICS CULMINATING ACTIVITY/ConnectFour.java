/**
* The ConnectFour class.
*
* This class represents a Connect Four (TM)
* game, which allows two players to drop
* checkers into a grid until one achieves
* four checkers in a straight line.
*/

import java.io.*;
import java.util.*;

public class ConnectFour {
   
   ConnectFourGUI gui;
   
   // declare all constants here
   final int EMPTY = 0;
   final int NUMPLAYER = 2;
   final int NUMROW = 6;
   final int NUMCOL = 7; 
   final int WINCOUNT = 4;
   final String GAMEFILEFOLDER = "gamefiles";
   
   final int TERMINATE = -1;
   // declare all "global" variables here
   int curPlayer;
   int[][] board = new int [NUMROW][NUMCOL];

   /*
   Method name: resetBoard
   */
   public void resetBoard () {
      for (int i = 0; i < NUMROW; i++) {
         for (int j = 0; j < NUMCOL; j++) {
            board[i][j] = EMPTY;
            //Sets every slot of the board array to 0
         }
      }
      curPlayer = 1;
      gui.setNextPlayer(curPlayer);
      //Sets the current player back to 1
   }
   
   /*
   Method name: locateEmptySlot
   */
   public int locateEmptySlot (int input) {
      int posY = 0;
      //Checks if the column is full or not
      for (int i = NUMROW-1; i >= 0; i--) {
         if (board[i][input] == EMPTY) {
            posY = i;
            i = TERMINATE;
            //if not, set the Y position to the current i and end the loop
         } else {
            //otherwise set to -1 to indicate that the column is full
            posY = TERMINATE;
         }
      }
      return posY;
   }
   
   /*
   Method name: boardFull
   */
   public boolean boardFull () {
      boolean filled = true;
      //Checks top of board to see if anything is full
      for (int i = 0; i < NUMROW; i++) {
         if (board[EMPTY][i] == EMPTY) {
            filled = false;
         }
      }
      return filled;
   } 
   
   /*
   Method name: verticalConnect
   */
   public int verticalConnect (int row, int col) {
      int counter = EMPTY;
      int player = board[row][col];
      //Checks for vertical going down from current Y position
      for (int i = row; i < NUMROW; i++) {
         if (board[i][col] == player) {
            counter++;
         } else {
         //Once the next slot is not the same piece anymore then end loop
            i = NUMROW;
         }
      }      
      //This one checks going up
      for (int i = row; i >= 0; i--) {
         if (board[i][col] == player) {
            counter++;
         } else {
            i = TERMINATE;
         }
      }
      //-1 because both loops start at piece position and that will overlap
      counter-=1;
      return counter;
   }
    
   /*
   Method name: horizontalConnect
   */
   public int horizontalConnect (int row, int col) {
      int counter = EMPTY;
      int player = board[row][col];
      
      //Checks from point going right
      for (int i = col; i < NUMCOL; i++) {
         if (board[row][i] == player) {
            counter++;
         } else {
         //loop ends when next slot is a different piece
            i = NUMCOL;
         }
      }      
      //Checks going left now
      for (int i = col; i >= 0; i--) {
         if (board[row][i] == player) {
            counter++;
         } else {
            i = TERMINATE;
         }
      }
      //-1 because both loops overlap for the first check
      counter-=1;
      return counter;
   }
   
   /*
   Method name: diagonalConnect1
   */
   public int diagonalConnect1 (int row, int col) {
      int counter = EMPTY;
      int player = board[row][col];
      //Sets piece to current player that it is checking
      int rowCheck = row;
      int colCheck = col;
      
      //checks going from top left to bottom right
      while (colCheck < NUMCOL && rowCheck < NUMROW && board[rowCheck][colCheck] == player) {
         counter++;
         rowCheck++;
         colCheck++;
      }
      rowCheck = row;
      colCheck = col;
      
      //Now checks from bottom right to top left
      while (colCheck >= 0 && rowCheck >= 0 && board[rowCheck][colCheck] == player) {
         counter++;
         colCheck--;
         rowCheck--;
      }
      //-1 to counter overlapping numbers
      counter-=1;
      return counter;
   }
   
   /*
   Method name: diagonalConnect2
   */
   public int diagonalConnect2 (int row, int col) {
      int counter = EMPTY;
      int player = board[row][col];
      
      int rowCheck = row;
      int colCheck = col;
      
      //Checks from top right to bottom left
      while (colCheck >= 0 && rowCheck < NUMROW && board[rowCheck][colCheck] == player) {
         counter++;
         rowCheck++;
         colCheck--;
      }
      //resets
      rowCheck = row;
      colCheck = col;
      
      //Checks the other way around
      while (colCheck < NUMCOL && rowCheck >= 0 && board[rowCheck][colCheck] == player) {
         counter++;
         rowCheck--;
         colCheck++;
      }
      //remove 1 to counter overlapping
      counter-=1;
      
      return counter;
   }
   
   /*
   Method name: saveToFile
   */
   public boolean saveToFile (String save) {
      boolean valid;
      try {
      //Writes game to a texr file in gamefile
         BufferedWriter out = new BufferedWriter (new FileWriter (GAMEFILEFOLDER + "/" + save));
         //Loops array to write everything to txt file
         for (int i = 0; i < NUMROW; i++) {
            for (int j = 0; j < NUMCOL; j++) {
               out.write(board[i][j] + " ");
            }
            out.newLine();
         }
         //Once array ends, write current player's turn to txt file
         out.write(curPlayer + "");
         out.close();
         valid = true;
      } catch (IOException iox) {
      //If failed to write then return false
         valid = false;
      }
      return valid;
   }
   
   /*
   Method name: loadFromFile
   */
   public boolean loadFromFile (String save) {
      boolean valid;
      String input;
      //Array for reading off the txt file
      String[] loadGame = new String[NUMCOL];
      try {
         BufferedReader in = new BufferedReader (new FileReader (GAMEFILEFOLDER + "/" + save));
         for (int i = 0; i < NUMROW; i++) {
            input = in.readLine();
            //gets input first then splits the strings based on a space " "
            loadGame = input.split(" ");
            //Once split, take the loadGame and parse it into the board array
            for (int j = 0; j < NUMCOL; j++) {
               board[i][j] = Integer.parseInt(loadGame[j]);
            }
         }
         //Lastly reads out the current player
         curPlayer = Integer.parseInt(in.readLine());
         gui.setNextPlayer(curPlayer);
         valid = true;
         in.close();
      } catch (IOException iox) {
         //If fails, then return false
         valid = false;
      }
      return valid;
   }
   
   public ConnectFour (ConnectFourGUI gui) {
      this.gui = gui;
      start();
   }
   
   /*
   Method name: play
   */
   public void play (int col) {
      Scanner sc = new Scanner(System.in);
      //Set variables for position X and Y. Also the checking variables
      int positionX;
      int positionY;
      int vert, hori, diag1, diag2;
      
      //Once click is registered, locate the Y position of current click
      positionX = col;
      positionY = locateEmptySlot(col);
   
      if (positionY != TERMINATE) { 
         //If not -1 then set the board to the current X and Y. Then set it on the graphics game board
            board[positionY][positionX] = curPlayer;
            gui.setPiece(positionY, positionX, curPlayer);
      
      
         //Variables for all checkers
         vert = verticalConnect(positionY, positionX);
         hori = horizontalConnect(positionY, positionX);
         diag1 = diagonalConnect1(positionY, positionX);
         diag2 = diagonalConnect2(positionY, positionX);
      
         //Checks if any checkers add up to 4, if so then display game message of current player. Resets the board and then set current player to 1 again
         if    (vert >= WINCOUNT || hori >= WINCOUNT || diag1 >= WINCOUNT || diag2 >= WINCOUNT) {
            gui.showWinnerMessage(curPlayer);
            gui.resetGameBoard();
            resetBoard();
            curPlayer = 1;
            //If tied, then reset game board and display tie message. Also set player back to 1
         } else if (boardFull() == true) {
            gui.showTieGameMessage();
            gui.resetGameBoard();
            resetBoard();
            curPlayer = 1;
            //If none of those apply, continue the game and change players
         } else {
            if (curPlayer == 1) {
               curPlayer = NUMPLAYER;
            } else {
               curPlayer = 1;
            }
         }
         //Change player on graphics game board
         gui.setNextPlayer(curPlayer);
      }
   }
   
   /*
   Method name: start
   */
   public void start() {
      board = new int[NUMROW][NUMCOL];
      //Starts connect four game
      curPlayer = 1;
      gui.setNextPlayer(curPlayer);
   }  
   
   /*
   Method name: updateGameBoard 
   */
   
   public void updateGameBoard() {
      for (int i = 0; i < NUMROW; i++) {
         for (int j = 0; j < NUMCOL; j++) {
         //Updates actual graphics game board
            if (board[i][j] != EMPTY) {
               gui.setPiece (i, j, board[i][j]);
            }
         } 
      }
   }
}