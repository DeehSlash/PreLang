package gals;

import gals.Symbol.Type;
import ide.IDE;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Symbol Table
 */
public class SymbolTable {
  
  // Symbol table
  public ArrayList<Symbol> symbolTable = new ArrayList<>();
  
  // Temp variables
  private List<String> paramNames;
  private List<Type> paramTypes;
  private List<Boolean> paramArray;
  
  /**
   * Default constructor
   */
  public SymbolTable () {
    paramNames = new ArrayList<>();
    paramTypes = new ArrayList<>();
    paramArray = new ArrayList<>();
  }
  
  /**
   * Adds a new function to the Symbol Table
   * @param name Function name
   * @param type Function return type
   * @param returnsArray Function returns array
   * @throws SemanticError Throws an error if the function is already
   * defined
   */
  public void addFunction(String name, Type type, boolean returnsArray) throws SemanticError {
    // First checks if the function has already been declared
    if (functionExists(name))
      throw new SemanticError("Function " + name + " is already defined");
    
    // Adds the function to the Symbol Table
    this.symbolTable.add(new Symbol(name, type, false, "global", false, 0,
            returnsArray, 0, false, false));
    
    // Then adds its parameters (if any)
    for (int i = 0; i < paramNames.size(); i++)
      this.symbolTable.add(new Symbol(paramNames.get(i), paramTypes.get(i),
              false, name, true, i, paramArray.get(i), 0, false, false));
    
    // In any case, reset the temp variables
    resetTempVariables();
  }
  
  /**
   * Adds a new temp parameter
   * @param name Parameter name
   * @param type Parameter type
   * @param isArray If parameter is array
   * @throws SemanticError  Throws an error if the parameter is already declared
   */
  public void addParameter (String name, Type type, boolean isArray) throws SemanticError {
    // First checks if the parameter has already been declared
    if (parameterExists(name))
      throw new SemanticError("Parameter " + name + " is already declared");
    
    // Then adds it to the temp arrays
    paramNames.add(name);
    paramTypes.add(type);
    paramArray.add(isArray);
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
   * Checks if the given parameter exists
   * @param name Parameter name to check
   * @return True if the parameter is already declared
   */
  private boolean parameterExists(String name) {
    for (String paramName : paramNames) {
      if (paramName.equals(name))
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
  
  /**
   * Reset temp variables
   */
  private void resetTempVariables () {
    paramNames = new ArrayList<>();
    paramTypes = new ArrayList<>();
    paramArray = new ArrayList<>();
  }
}
