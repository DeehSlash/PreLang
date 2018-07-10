package gals;

import ide.IDE;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 * Semantico
 */
public class Semantico {

  // Symbols Table
  public ArrayList<Symbol> symbolTable = new ArrayList<>();

  // Semantic Table
  public Stack<Integer> semanticTable = new Stack<>();

  // Assembler
  private final Assembler assembler = new Assembler();

  // Scope Stack
  Stack<String> scopeStack = new Stack<>();

  
  /**
   * ACTION MANUAL
   * This comment describes the range of values availables for each action
   *
   * 
   */
  
  
  /**
   * Executes the current semantic action
   *
   * @param action Semantic action
   * @param token Current token
   * @throws SemanticError
   */
  public void executeAction(int action, Token token) throws SemanticError {

  }

  /**
   * Checks if the given identifier exists in the current scope or in
   * the upper scopes
   * @param name Identifier to check
   * @return True if the identifier exists in any of the current scopes
   */
  private boolean identifierExists(String id) {
    Iterator iterator = scopeStack.iterator();

    while (iterator.hasNext()) {
      String scope = (String) iterator.next();
      for (Symbol symbol : symbolTable) {
        if(symbol.getIdentifier() != null) {
          if (symbol.getIdentifier().equals(id) && symbol.getScope().startsWith(scope))
            return true;
        }
      }
    }
    
    return false;
  }
  
  /**
   * Checks if the given function exists
   * @param name Function name to check
   * @return True if the function exists in the global scope
   */
  private boolean functionExists(String name) {
    for (Symbol symbol : symbolTable) {
      if (symbol.getIdentifier().equals(name) && symbol.isFunction())
        return true;
    }
    
    return false;
  }
  
  /**
   * Checks for symbols that haven't been used
   *
   * @param scope Scope to check symbols
   */
  private void checkForNotUsed(String scope) {
    for (Symbol symbol : symbolTable) {
      if (symbol.getScope().startsWith(scope) && !symbol.hasBeenUsed()) {
        IDE.mainWindow.warn("Symbol " + symbol.getIdentifier() + " not used in scope " + scope);
      }
    }
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
        return Symbol.Type.VOID;
      case "int":
        return Symbol.Type.INT;
      case "float":
        return Symbol.Type.FLOAT;
      case "double":
        return Symbol.Type.DOUBLE;
      case "string":
        return Symbol.Type.STRING;
      case "char":
        return Symbol.Type.CHAR;
      case "boolean":
        return Symbol.Type.BOOLEAN;
      case "binary":
        return Symbol.Type.BINARY;
      case "hexadecimal":
        return Symbol.Type.HEXADECIMAL;
    }
    
    throw new SemanticError("Expected type, found " + type);
  }
}
