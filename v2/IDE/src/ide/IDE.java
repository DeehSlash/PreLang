package ide;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Main class
 */
public class IDE {
  
  public static MainWindow mainWindow;
  
  /**
   * Application entry point
   * @param args 
   */
  public static void main(String[] args) {
    // Tries to set the look and feel of the application
    try {
      UIManager.setLookAndFeel(
              UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
      Logger.getLogger(IDE.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    // Creates a new MainWindow and shows it
    mainWindow = new MainWindow();
    mainWindow.setLocationRelativeTo(null);
    mainWindow.setVisible(true);    
  }
}
