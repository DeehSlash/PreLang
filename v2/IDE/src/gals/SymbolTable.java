package gals;

import gals.Symbol.Type;
import ide.IDE;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 * Symbol Table
 */
public class SymbolTable {
  
  public ArrayList<Symbol> symbolTable = new ArrayList<>();
  
  /**
   * Adds a new function to the Symbol Table
   * @param name
   * @param type
   * @param returnsArray
   * @throws gals.SemanticError Throws an error if the function is already
   * defined
   */
  public void addFunction(String name, Type type, boolean returnsArray) throws SemanticError {
    // First checks if function has already been declared
    if (functionExists(name))
      throw new SemanticError("Function " + name + " is already defined");
    
    this.symbolTable.add(new Symbol(name, type, false, "global", false, 0,
            returnsArray, 0, false, false));
  }
  
  /**
   * Checks if the given identifier exists in the current scope or in
   * the upper scopes
   * @param id Identifier to check
   * @param scopeStack Scope Stack to check
   * @return True if the identifier exists in any of the current scopes
   */
  public boolean identifierExists(String id, Stack<String> scopeStack) {
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
  public void checkForNotUsed(String scope) {
    for (Symbol symbol : symbolTable) {
      if (symbol.getScope().startsWith(scope) && !symbol.hasBeenUsed()) {
        IDE.mainWindow.warn("Symbol " + symbol.getIdentifier() + " not used in scope " + scope);
      }
    }
  }
  
  /**
   * Gets the Symbol Table
   * @return The Symbol Table
   */
  public ArrayList<Symbol> getSymbolTable () {
    return symbolTable;
  }
}
