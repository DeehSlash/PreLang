package ide;

import gals.Symbol;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class SymbolTableWindow extends javax.swing.JFrame {

    public SymbolTableWindow() {
        initComponents();
    }

    public void loadTable(ArrayList<Symbol> symbolTable) {
      DefaultTableModel model = (DefaultTableModel) tblSymbol.getModel();
      int i = 0;
      
      for (Symbol symbol : symbolTable) {
        model.addRow(new Object[9]);
        model.setValueAt(symbol.getIdentifier(), i, 0);
        model.setValueAt(symbol.getType().toString(), i, 1);
        model.setValueAt(symbol.hasBeenUsed(), i, 2);
        model.setValueAt(symbol.getScope(), i, 3);
        model.setValueAt(symbol.isParameter(), i, 4);
        model.setValueAt(symbol.getPosition(), i, 5);
        model.setValueAt(symbol.isArray(), i, 6);
        model.setValueAt(symbol.isMatrix(), i, 7);
        model.setValueAt(symbol.isReference(), i, 8);
        i++;
      }
      
      tblSymbol.setModel(model);
    }
    
    @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    pnlTable = new javax.swing.JPanel();
    jScrollPane1 = new javax.swing.JScrollPane();
    tblSymbol = new javax.swing.JTable();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    getContentPane().setLayout(new java.awt.GridBagLayout());

    pnlTable.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tabela de Símbolos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12))); // NOI18N
    pnlTable.setLayout(new java.awt.GridBagLayout());

    tblSymbol.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {
        "ID", "Tipo", "Usado", "Escopo", "Parâmetro", "Posição", "Array", "Matriz", "Referência"
      }
    ) {
      Class[] types = new Class [] {
        java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
      };
      boolean[] canEdit = new boolean [] {
        false, false, false, false, false, false, false, false, false
      };

      public Class getColumnClass(int columnIndex) {
        return types [columnIndex];
      }

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
      }
    });
    jScrollPane1.setViewportView(tblSymbol);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    pnlTable.add(jScrollPane1, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    getContentPane().add(pnlTable, gridBagConstraints);

    pack();
  }// </editor-fold>//GEN-END:initComponents
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JPanel pnlTable;
  private javax.swing.JTable tblSymbol;
  // End of variables declaration//GEN-END:variables

}
