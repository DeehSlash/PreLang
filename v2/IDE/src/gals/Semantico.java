package gals;

import gals.Symbol.Type;
import ide.IDE;
import java.util.Stack;

/**
 * Semantico
 */
public class Semantico {

  /**
   * Mode Enum to help control the flow
   */
  private enum Mode {
    NONE,
    DECLARING_FUNCTION
  }
  
  // Symbols Table
  public SymbolTable symbolTable = new SymbolTable();

  // Semantic Table
  public Stack<Integer> semanticTable = new Stack<>();

  // Assembler
  private final Assembler assembler = new Assembler();

  // Scope
  private Stack<String> scopeStack = new Stack<>();
  private int scopeCount = 0;

  // Temp variables
  private Mode currentMode = Mode.NONE;
  private String lastFunction;
  private Type lastType = Type.UNDEFINED;
  private boolean isArray = false;
  
  /**
   * ACTION MANUAL
   * This comment describes the range of values availables for each action
   *
   * 0 - 99
   * Symbols declaration
   * 
   * 100 - 199
   * Types
   * 
   * 900 - 999
   * Misc (scope, etc.)
   */
  
  
  /**
   * Executes the current semantic action
   *
   * @param action Semantic action
   * @param token Current token
   * @throws SemanticError
   */
  public void executeAction(int action, Token token) throws SemanticError {
    IDE.mainWindow.debug("#" + action + " - " + token.getLexeme());
    
    switch (action) {
      // Function name (declaration)
      case 10:
        lastFunction = token.getLexeme();
        currentMode = Mode.DECLARING_FUNCTION;
        break;
      // -----------------------------------------------------------------------
        
      // Primitive type
      case 100:
        lastType = parseType(token.getLexeme());
        break;
      // -----------------------------------------------------------------------
        
      // Primitive type as array
      case 101:
        lastType = parseType(token.getLexeme());
        isArray = true;
        break;
      // -----------------------------------------------------------------------
        
      // Scope open
      case 900:
        // In any case, generate scope
        generateScope(token.getLexeme());
        
        // If it's declaring a function, add it to the symbols table and reset
        // the current mode to None
        if (currentMode == Mode.DECLARING_FUNCTION) {
          symbolTable.addFunction(lastFunction, lastType, isArray);
          resetState();
        }
        
        break;
      // -----------------------------------------------------------------------
        
      // Scope close
      case 901:
        // Removes the top scope
        scopeStack.pop();
        
        // If empty, reset the counter
        if (scopeStack.empty())
          scopeCount = 0;
        
        break;
      // -----------------------------------------------------------------------
    }
  }

  /**
   * Generates and push a new scope to the scope stack
   * @param scope Scope to generate
   */
  private void generateScope (String scope) {
    scopeStack.push(scope + Integer.toString(scopeCount));
    scopeCount++;
  } 
  
  /**
   * Gets the generated assembly from Assembler class
   * @return The generated assembly code
   */
  public String getAssembly() {
    return this.assembler.getCode();
  }
  
  /**
   * Parses Type from String
   * @param type String to be parsed
   * @return Type The parsed type
   * @throws SemanticError If the type couldn't be parsed
   */
  private Symbol.Type parseType(String type) throws SemanticError {
    switch (type) {
      case "void":
        return Type.VOID;
      case "int":
        return Type.INT;
      case "float":
        return Type.FLOAT;
      case "double":
        return Type.DOUBLE;
      case "string":
        return Type.STRING;
      case "char":
        return Type.CHAR;
      case "boolean":
        return Type.BOOLEAN;
      case "binary":
        return Type.BINARY;
      case "hexadecimal":
        return Type.HEXADECIMAL;
    }
    
    throw new SemanticError("Expected type, found " + type);
  }
  
  /**
   * Reset mode and temp variables to its default state
   */
  private void resetState () {
    this.currentMode = Mode.NONE;
    this.lastFunction = "";
    this.lastType = Type.UNDEFINED;
    this.isArray = false;
  }
}
