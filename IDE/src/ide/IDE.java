/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ide;

/**
 *
 * @author croisebeck
 */
public class IDE {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        PreLangWindow mainWindow = new PreLangWindow();
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setVisible(true);    
    }
    
}
