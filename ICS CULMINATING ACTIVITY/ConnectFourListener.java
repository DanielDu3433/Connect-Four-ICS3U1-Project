import javax.swing.*;
import java.awt.event.*;
public class ConnectFourListener implements MouseListener {
   private ConnectFourGUI gui;
   private ConnectFour game;
   public ConnectFourListener (ConnectFour game, ConnectFourGUI gui) {
      this.game = game;
      this.gui = gui;
      gui.addListener (this);
   }
   
   public void mouseClicked (MouseEvent event) {
      JLabel label = (JLabel) event.getComponent ();
      int column = gui.getColumn (label);
      game.play(column);   
   }

   public void mousePressed (MouseEvent event) {
   }

   public void mouseReleased (MouseEvent event) {
   }


   public void mouseEntered (MouseEvent event) {
   }

   public void mouseExited (MouseEvent event) {
   }
}

class ButtonListener implements ActionListener {
   final String SAVEGAMEBUTTON = "Save Game";
   final String LOADGAMEBUTTON = "Load Game";
   final String RESTARTGAMEBUTTON = "Restart Game";
   final String EXITBUTTON = "Exit";
   
   private ConnectFourGUI gui;
   private ConnectFour game;
   
   public ButtonListener (ConnectFour game, ConnectFourGUI gui) {
      this.game = game;
      this.gui = gui;
      gui.addListener (this);
   }

   public void actionPerformed(ActionEvent e) {
      // detect which button is clicked
      String button = e.getActionCommand();
      
      if (button.equals(EXITBUTTON)) {
         System.exit(0);
      } else if (button.equals(RESTARTGAMEBUTTON)) {
         game.resetBoard();
         gui.resetGameBoard();
      } else {
         // if save or load game, prompt user for the file name
         String fileName = JOptionPane.showInputDialog(null, "File Name: ", "File Name", JOptionPane.QUESTION_MESSAGE);   
         if (fileName != null) {
            if (button.equals(SAVEGAMEBUTTON)) {
               if (!game.saveToFile(fileName)) {
                  JOptionPane.showMessageDialog(null, "Problem Saving Game!", "Error", JOptionPane.PLAIN_MESSAGE, null); 
               }
            } else if (button.equals(LOADGAMEBUTTON)) {
               if (!game.loadFromFile(fileName)) {
                  JOptionPane.showMessageDialog(null, "Problem Loading Game!", "Error", JOptionPane.PLAIN_MESSAGE, null);           
               } else {
               // update the game board from the loaded game
                  gui.resetGameBoard();
                  game.updateGameBoard();
               }
            }
         }
      }
   }

}
