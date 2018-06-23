/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ide;

import static ide.PreLangWindow.editor;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class AssemblyCodeWindow extends javax.swing.JFrame {

  private File arquivoAberto = null;
  
    public AssemblyCodeWindow() {
        initComponents();
    }
    
    public void loadCode(String code) {
      this.txtCode.setText(code);
    }
   
    @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    pnlCode = new javax.swing.JPanel();
    scrCode = new javax.swing.JScrollPane();
    txtCode = new javax.swing.JTextArea();
    menu = new javax.swing.JMenuBar();
    mnuSave = new javax.swing.JMenu();
    jMenuItem1 = new javax.swing.JMenuItem();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setMaximumSize(new java.awt.Dimension(700, 600));
    setMinimumSize(new java.awt.Dimension(700, 600));
    getContentPane().setLayout(new java.awt.GridBagLayout());

    pnlCode.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Código Gerado", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12))); // NOI18N
    pnlCode.setLayout(new java.awt.GridBagLayout());

    txtCode.setEditable(false);
    txtCode.setColumns(20);
    txtCode.setRows(5);
    scrCode.setViewportView(txtCode);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    pnlCode.add(scrCode, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    getContentPane().add(pnlCode, gridBagConstraints);

    mnuSave.setText("Arquivo");

    jMenuItem1.setText("Salvar");
    jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jMenuItem1ActionPerformed(evt);
      }
    });
    mnuSave.add(jMenuItem1);

    menu.add(mnuSave);

    setJMenuBar(menu);

    pack();
    setLocationRelativeTo(null);
  }// </editor-fold>//GEN-END:initComponents

  private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        if (arquivoAberto == null){
            JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));
            fileChooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    if (f.isDirectory()) return true;
                    return f.getName().endsWith(".asm");
                }

                @Override public String getDescription() { return "Arquivo Assembly"; }
            });
            fileChooser.showSaveDialog(this);
            setArquivoAberto(fileChooser.getSelectedFile());
        }
        try {
            OutputStream fos = new FileOutputStream(arquivoAberto);
            fos = new BufferedOutputStream(fos);
            String text = txtCode.getText();
            for (int i = 0; i < text.length(); ++i){
                fos.write(text.charAt(i));
            }
            fos.close();
            JOptionPane.showMessageDialog(this, "O arquivo foi salvo.");
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PreLangWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PreLangWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
  }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void setArquivoAberto(File arquivo){
        arquivoAberto = arquivo;
        this.setTitle("PreLang - "+arquivoAberto.getAbsolutePath());
    }
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JMenuItem jMenuItem1;
  private javax.swing.JMenuBar menu;
  private javax.swing.JMenu mnuSave;
  private javax.swing.JPanel pnlCode;
  private javax.swing.JScrollPane scrCode;
  private javax.swing.JTextArea txtCode;
  // End of variables declaration//GEN-END:variables

}
