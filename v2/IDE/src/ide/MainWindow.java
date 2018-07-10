package ide;

import gals.LexicalError;
import gals.Lexico;
import gals.SemanticError;
import gals.Semantico;
import gals.Sintatico;
import gals.Symbol;
import gals.SyntaticError;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * IDE MainWindow
 */
public class MainWindow extends javax.swing.JFrame {

  private File openedFile = null;
  private Lexico lexico;
  private Sintatico sintatico;
  private Semantico semantico;

  private ArrayList<Symbol> symbolTable;
  private String assemblyCode;
  
  private final boolean DEBUG = true;

  /**
   * Creates new form MainWindow
   */
  public MainWindow() {
    initComponents();
    addListeners();
  }

  /**
   * Adds listeners to the respective buttons / menus
   */
  private void addListeners() {
    this.mniOpen.addActionListener(e -> openFile());
    this.mniSave.addActionListener(e -> saveFile());
    this.mniExit.addActionListener(e -> exit());
    this.mniSymbolTable.addActionListener(e -> openSymbolTable());
    this.mniAssemblyCode.addActionListener(e -> openAssemblyCode());
  }

  /**
   * Opens the Symbol Table window
   */
  private void openSymbolTable() {
    SymbolTableWindow symbolTableWindow = new SymbolTableWindow();
    symbolTableWindow.setVisible(true);
    symbolTableWindow.loadTable(symbolTable);
  }

  /**
   * Opens the Assembly Code window
   */
  private void openAssemblyCode() {
    AssemblyCodeWindow assemblyCodeWindow = new AssemblyCodeWindow();
    assemblyCodeWindow.setVisible(true);
    assemblyCodeWindow.loadCode(assemblyCode);
  }

  /**
   * Sets a File as opened
   * @param file File to set as opened
   */
  private void setOpenedFile(File file) {
    openedFile = file;
    this.setTitle("PreIDE - " + openedFile.getAbsolutePath());
  }

  /**
   * Opens a file to edit
   */
  private void openFile() {
    JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));
    fileChooser.setFileFilter(new FileFilter() {
      @Override
      public boolean accept(File f) {
        if (f.isDirectory()) {
          return true;
        }
        return f.getName().endsWith(".pre");
      }

      @Override
      public String getDescription() {
        return "Arquivos PreLang";
      }
    });
    fileChooser.showOpenDialog(this);
    File file = fileChooser.getSelectedFile();
    try {
      InputStream fis = new FileInputStream(file);
      fis = new BufferedInputStream(fis);
      int l;
      String content = "";
      while ((l = fis.read()) != -1) {
        content += (char) l;
      }
      fis.close();
      txtEditor.setText(content);
      setOpenedFile(file);

    } catch (FileNotFoundException ex) {
      Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Exits from the applcation
   */
  private void exit() {
    System.exit(0);
  }

  /**
   * Saves a file
   */
  private void saveFile() {
    if (openedFile == null) {
      JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));
      fileChooser.setFileFilter(new FileFilter() {
        @Override
        public boolean accept(File f) {
          if (f.isDirectory()) {
            return true;
          }
          return f.getName().endsWith(".plg");
        }

        @Override
        public String getDescription() {
          return "Arquivos PreLang";
        }
      });
      fileChooser.showSaveDialog(this);
      setOpenedFile(fileChooser.getSelectedFile());
    }
    try {
      OutputStream fos = new FileOutputStream(openedFile);
      fos = new BufferedOutputStream(fos);
      String text = txtEditor.getText();
      for (int i = 0; i < text.length(); ++i) {
        fos.write(text.charAt(i));
      }
      fos.close();

    } catch (FileNotFoundException ex) {
      Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Run the code
   */
  private void run() {
    lexico = new Lexico(txtEditor.getText());
    sintatico = new Sintatico();
    semantico = new Semantico();
    try {
      txtOutput.setText("");
      sintatico.parse(lexico, semantico);
      txtOutput.append("SUCCESS\n");
    } catch (LexicalError | SyntaticError | SemanticError ex) {
      txtOutput.append(ex.getMessage());
    } finally {
      this.symbolTable = semantico.symbolTable;
      this.assemblyCode = semantico.getAssemblyCode();
    }
  }
  
  /**
   * Appends a warning into the output
   * @param warning Warning message to display
   */
  public void warn(String warning) {
    this.txtOutput.append("WARNING: " + warning + "\n");
  }
  
  /**
   * Appends a debug message into the output
   * @param debug Debug message to display
   */
  public void debug(String debug) {
    if(this.DEBUG)
      this.txtOutput.append("DEBUG: " + debug + "\n");
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    pnlCenter = new javax.swing.JPanel();
    scrEditor = new javax.swing.JScrollPane();
    txtEditor = new javax.swing.JTextArea();
    pnlActions = new javax.swing.JPanel();
    btnRun = new javax.swing.JButton();
    pnlOutput = new javax.swing.JPanel();
    pnlStatus = new javax.swing.JPanel();
    lblStatus = new javax.swing.JLabel();
    scrOutput = new javax.swing.JScrollPane();
    txtOutput = new javax.swing.JTextArea();
    mnbMain = new javax.swing.JMenuBar();
    mnuFile = new javax.swing.JMenu();
    mniOpen = new javax.swing.JMenuItem();
    mniSave = new javax.swing.JMenuItem();
    mniExit = new javax.swing.JMenuItem();
    mnuTools = new javax.swing.JMenu();
    mniSymbolTable = new javax.swing.JMenuItem();
    mniAssemblyCode = new javax.swing.JMenuItem();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("PreIDE");
    setBackground(new java.awt.Color(0, 0, 0));
    setLocationByPlatform(true);
    setName("frmMain"); // NOI18N
    getContentPane().setLayout(new java.awt.GridBagLayout());

    pnlCenter.setLayout(new java.awt.GridBagLayout());

    scrEditor.setBackground(new java.awt.Color(51, 51, 51));

    txtEditor.setBackground(new java.awt.Color(51, 51, 51));
    txtEditor.setColumns(20);
    txtEditor.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
    txtEditor.setForeground(new java.awt.Color(238, 238, 238));
    txtEditor.setRows(5);
    txtEditor.setTabSize(2);
    txtEditor.setCaretColor(new java.awt.Color(255, 255, 255));
    txtEditor.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    txtEditor.setDisabledTextColor(new java.awt.Color(238, 238, 238));
    txtEditor.setInheritsPopupMenu(true);
    txtEditor.setMargin(new java.awt.Insets(10, 10, 10, 10));
    scrEditor.setViewportView(txtEditor);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    pnlCenter.add(scrEditor, gridBagConstraints);

    pnlActions.setBackground(java.awt.Color.darkGray);
    pnlActions.setLayout(new java.awt.GridBagLayout());

    btnRun.setForeground(new java.awt.Color(18, 47, 60));
    btnRun.setText("Run");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    pnlActions.add(btnRun, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    pnlCenter.add(pnlActions, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 0.85;
    getContentPane().add(pnlCenter, gridBagConstraints);

    pnlOutput.setForeground(java.awt.Color.darkGray);
    pnlOutput.setLayout(new java.awt.GridBagLayout());

    pnlStatus.setBackground(java.awt.Color.darkGray);
    pnlStatus.setLayout(new java.awt.GridBagLayout());

    lblStatus.setBackground(new java.awt.Color(151, 149, 149));
    lblStatus.setForeground(new java.awt.Color(255, 255, 255));
    lblStatus.setText("Output");
    lblStatus.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    pnlStatus.add(lblStatus, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    pnlOutput.add(pnlStatus, gridBagConstraints);

    txtOutput.setEditable(false);
    txtOutput.setBackground(new java.awt.Color(51, 51, 51));
    txtOutput.setColumns(20);
    txtOutput.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
    txtOutput.setForeground(new java.awt.Color(238, 238, 238));
    txtOutput.setRows(7);
    txtOutput.setCaretColor(new java.awt.Color(255, 255, 255));
    txtOutput.setMargin(new java.awt.Insets(10, 10, 10, 10));
    scrOutput.setViewportView(txtOutput);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    pnlOutput.add(scrOutput, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 0.15;
    getContentPane().add(pnlOutput, gridBagConstraints);

    mnbMain.setBackground(new java.awt.Color(51, 51, 51));
    mnbMain.setForeground(java.awt.Color.darkGray);
    mnbMain.setBorderPainted(false);

    mnuFile.setBackground(new java.awt.Color(51, 51, 51));
    mnuFile.setForeground(new java.awt.Color(255, 255, 255));
    mnuFile.setText("Arquivo");

    mniOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
    mniOpen.setText("Abrir");
    mnuFile.add(mniOpen);

    mniSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
    mniSave.setText("Salvar");
    mnuFile.add(mniSave);

    mniExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
    mniExit.setText("Sair");
    mnuFile.add(mniExit);

    mnbMain.add(mnuFile);

    mnuTools.setBackground(new java.awt.Color(51, 51, 51));
    mnuTools.setForeground(new java.awt.Color(255, 255, 255));
    mnuTools.setText("Ferramentas");

    mniSymbolTable.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
    mniSymbolTable.setText("Tabela de Símbolos");
    mnuTools.add(mniSymbolTable);

    mniAssemblyCode.setText("Código Assembly");
    mnuTools.add(mniAssemblyCode);

    mnbMain.add(mnuTools);

    setJMenuBar(mnbMain);

    pack();
  }// </editor-fold>//GEN-END:initComponents
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnRun;
  private javax.swing.JLabel lblStatus;
  private javax.swing.JMenuBar mnbMain;
  private javax.swing.JMenuItem mniAssemblyCode;
  private javax.swing.JMenuItem mniExit;
  private javax.swing.JMenuItem mniOpen;
  private javax.swing.JMenuItem mniSave;
  private javax.swing.JMenuItem mniSymbolTable;
  private javax.swing.JMenu mnuFile;
  private javax.swing.JMenu mnuTools;
  private javax.swing.JPanel pnlActions;
  private javax.swing.JPanel pnlCenter;
  private javax.swing.JPanel pnlOutput;
  private javax.swing.JPanel pnlStatus;
  private javax.swing.JScrollPane scrEditor;
  private javax.swing.JScrollPane scrOutput;
  public static javax.swing.JTextArea txtEditor;
  private javax.swing.JTextArea txtOutput;
  // End of variables declaration//GEN-END:variables
}
