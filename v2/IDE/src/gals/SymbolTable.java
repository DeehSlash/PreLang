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
  private List<Integer> paramArraySize;
  
  /**
   * Default constructor
   */
  public SymbolTable () {
    paramNames = new ArrayList<>();
    paramTypes = new ArrayList<>();
    paramArray = new ArrayList<>();
    paramArraySize = new ArrayList<>();
  }
  
  /**
   * Adds a new attribute (var or const) to the Symbol Table
   * @param name Attribute name
   * @param type Attribute type
   * @param scope Attribute scope
   * @param isArray If attribute is array
   * @param arraySize Array size (if attribute is array)
   */
  public void addAttribute(String name, Type type, String scope,
          boolean isArray, int arraySize) {
    // Adds the attribute to the Symbol Table
    symbolTable.add(new Symbol(name, type, false, scope, false, 0, isArray,
            arraySize, false, false));
  }
  
  /**
   * Updates the type of an attribute
   * @param name Name of the attribute to be updated
   * @param type Type to set
   */
  public void updateAttribute(String name, Type type) {
    for (Symbol symbol : symbolTable) {
      if (symbol.getIdentifier().equals(name))
        symbol.setType(type);
    }
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
              false, name, true, i, paramArray.get(i), paramArraySize.get(i),
              false, false));
    
    // In any case, reset the temp variables
    resetTempVariables();
  }
  
  /**
   * Adds a new temp parameter
   * @param name Parameter name
   * @param type Parameter type
   * @param isArray If parameter is array
   * @param arraySize Parameter array size
   * @throws SemanticError  Throws an error if the parameter is already declared
   */
  public void addParameter (String name, Type type, boolean isArray, int arraySize) throws SemanticError {
    // First checks if the parameter has already been declared
    if (parameterExists(name))
      throw new SemanticError("Parameter " + name + " is already declared");
    
    // Then adds it to the temp arrays
    paramNames.add(name);
    paramTypes.add(type);
    paramArray.add(isArray);
    paramArraySize.add(arraySize);
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
   * Gets the type of the given identifier and converts it to an expression type
   * @param id Identifier to lookup
   * @return The expression type
   * @throws SemanticError Throws an error if the symbol has not been declared,
   * initialized or if the type is not supported in expressions
   */
  public int getExpressionType(String id) throws SemanticError {
    // Gets the type
    Type type = getType(id);
    
    // If the type is null or undefined, throw an exception
    if (type == null)
      throw new SemanticError("Symbol " + id + " has not been declared");
    else if (type == Type.UNDEFINED)
      throw new SemanticError("Symbol " + id + " has not been initialized");
    
    // Returns the expression type
    switch(type) {
      case INT:
        return 0;
      case FLOAT:
        return 1;
      case CHAR:
        return 2;
      case STRING:
        return 3;
      case BOOLEAN:
        return 4;
    }
    
    // If it's an unsupported expression type, throws an exception
    throw new SemanticError("Type " + type + " is not supported in expressions");
  }
  
  /**
   * Gets the type of the given identifier
   * @param id Identifier to lookup
   * @return The type of the found identifier, or null if not found
   */
  public Type getType(String id) {
    for (Symbol symbol : symbolTable) {
      if (symbol.getIdentifier().equals(id))
        return symbol.getType();
    }
    
    return null;
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
    paramArraySize = new ArrayList<>();
  }
}
