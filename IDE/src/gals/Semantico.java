package gals;

import gals.Symbol.Type;
import java.util.ArrayList;
import java.util.Stack;

public class Semantico implements Constants
{
  
  private enum Mode {
    NONE,
    VARIABLE,
    CONSTANT,
    DECLARING_FUNCTION,
    DECLARING_FUNCTION_PARAMETERS,
    ATTRIBUTE_ASSIGNMENT
  }
  
  // Symbols Table
  public ArrayList<Symbol> symbolTable = new ArrayList<>();
  
  // Current mode
  private Mode mode = Mode.NONE;
  
  // Scope Stack
  Stack<String> scopeStack = new Stack<>();
  
  // Temp variables
  private String function;
  private String variableOrConstant;
  private Symbol.Type type;
  private int position = 0;
  private boolean array = false;
  private ArrayList<Symbol> parametersToBeAdded = new ArrayList<>();
  private int innerScopeCount = 0;
    
  /**
   * #1   =   FUNCTION
   * #2   =   TYPE
   * #3   =   
   * #4   =   FUNCTION PARAMETERS (START)
   * #5   =   FUNCTION PARAMETERS (END)
   * #6   =   VARIABLE / CONSTANT 
   * #7   =   COMMA
   * #8   =   INDEX OPEN
   * #9   =   INDEX CLOSE
   * #10  =   SCOPE_OPEN
   * #11  =   SCOPE_CLOSE
   * #12  =   VARIABLE / CONSTANT (ATTRIBUTE)
   * #13  =   COMMAND (AFTER)
   * #14  =   ATTRIBUTES ASSIGNMENT COMMAND
   * #15  =   ARRAY INDEX
   */
    
  /**
   * Executes the current semantic action
   * @param action Semantic action
   * @param token Current token
   * @throws SemanticError 
   */
  public void executeAction(int action, Token token) throws SemanticError
  {
    System.out.println("#" + action + " - " + token.getLexeme());
    
    switch (action) {
      
      // FUNCTION
      case 1:
        if(this.scopeStack.empty()) {
          this.function = token.getLexeme();
          this.mode = Mode.DECLARING_FUNCTION;
        }
        break;
      
      // TYPE
      case 2:
        if (this.mode == Mode.DECLARING_FUNCTION) {
          this.type = parseType(token.getLexeme());
          this.addFunction();
          this.addParameters();
        } else if (this.mode == Mode.DECLARING_FUNCTION_PARAMETERS) {
          this.type = parseType(token.getLexeme());
        }
        break;

      // FUNCTION PARAMETERS (START)
      case 4:
        this.mode = Mode.DECLARING_FUNCTION_PARAMETERS;
        
        break;
        
      // FUNCTION PARAMETERS (END)
      case 5:
        this.addParameter();
        this.mode = Mode.DECLARING_FUNCTION;
        this.position = 0;
        break;
        
      // VARIABLE / CONSTANT
      case 6:
        if (this.mode == Mode.DECLARING_FUNCTION_PARAMETERS) {
          this.variableOrConstant = token.getLexeme();
        }
        break;
        
      // COMMA
      case 7:
        if (this.mode == Mode.DECLARING_FUNCTION_PARAMETERS) {
          this.addParameter();
          this.array = false;
        }
        
        break;
        
      // INDEX CLOSE
      case 9:
        if (this.mode == Mode.DECLARING_FUNCTION_PARAMETERS) {
          this.array = true;
        }
        break;
      
      // SCOPE OPEN
      case 10:
        if (scopeStack.empty()) {
          scopeStack.push(this.function);
        } else {
          scopeStack.push(this.function + "-" + Integer.toString(innerScopeCount));
          this.innerScopeCount++;
        }
        break;
      
      // SCOPE CLOSE
      case 11:
        scopeStack.pop();
        if (scopeStack.empty()) {
          this.innerScopeCount = 0;
          this.mode = Mode.NONE;
        }
        break;
        
      // VARIABLE / CONSTANT (ATTRIBUTE)
      case 12:
        this.variableOrConstant = token.getLexeme();
        this.type = Type.UNDEFINED;
        break;
        
      // COMMAND (AFTER)
      case 13:
        if(this.mode == Mode.ATTRIBUTE_ASSIGNMENT) {
          if(this.isConstant(this.variableOrConstant))
            addConstant();
          else
            addVariable(); 
        }
   
        // Reset temp variables
        this.mode = Mode.NONE;
        this.array = false;
        this.type = Type.UNDEFINED;
        break;
        
      // ATTRIBUTES ASSIGNMENT COMMAND
      case 14:
        this.mode = Mode.ATTRIBUTE_ASSIGNMENT;
        break;
        
      // ARRAY INDEX
      case 15:
        if (this.mode == Mode.ATTRIBUTE_ASSIGNMENT)
          this.array = true;
        break;
    }
  }	
  
  /**
   * Adds a function to the Symbol Table
   * @return True if the operation was successful
   * @throws SemanticError If the function had already been declared
   */
  private boolean addFunction() throws SemanticError {
    if (functionExists(this.function))
      throw new SemanticError("Function " + this.function + 
              " had already been declared before");
    
    this.symbolTable.add(new Symbol(this.function, this.type, false,
            "global", false, 0, false, false, false));
    
    return true;
  }
  
  /**
   * Adds a parameter to the temporary array
   * @return True if the operation was successful
   * @throws SemanticError If the parameter had already been declared
   */
  private boolean addParameter() throws SemanticError {
    if (parameterExists(this.variableOrConstant))
      throw new SemanticError("Parameter " + this.variableOrConstant +
              " had already been declared before");
    
    this.parametersToBeAdded.add(new Symbol(this.variableOrConstant, this.type, false,
            this.function, true, this.position, this.array, false, false));
    
    this.position++;
    
    return true;
  }
  
  /**
   * Adds all parameters to the Symbol Table
   */
  private void addParameters() {
    for (Symbol symbol : this.parametersToBeAdded) {
      this.symbolTable.add(symbol);
    }
    
    this.parametersToBeAdded.clear();
  }
  
  /**
   * Adds a constant to the Symbol Table
   */
  private void addConstant() {
    if (!identifierExists(this.variableOrConstant))
      this.symbolTable.add(new Symbol(this.variableOrConstant, this.type, false,
            this.scopeStack.peek(), false, 0, this.array, false, false));
  }
  
  /**
   * Adds a variable to the Symbol Table
   */
  private void addVariable() {
    if (!identifierExists(this.variableOrConstant))
      this.symbolTable.add(new Symbol(this.variableOrConstant, this.type, false,
            this.scopeStack.peek(), false, 0, this.array, false, false));
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
   * Checks if the given parameter had already been declared
   * @param name Parameter name to check
   * @return True if the parameter had already been declared
   */
  private boolean parameterExists(String name) {
    for (Symbol symbol : parametersToBeAdded) {
      if (symbol.getIdentifier().equals(name))
        return true;
    }
    
    return false;
  }

  /**
   * Checks if the given identifier exists in the current scope or in
   * the upper scopes
   * @param name Identifier to check
   * @return True if the identifier exists in any of the current scopes
   */
  private boolean identifierExists(String name) {
    for (int i = scopeStack.size() - 1; i >= 0; i--) {
      String scope = scopeStack.get(i);
      for (Symbol symbol : symbolTable) {
        if (symbol.getIdentifier().equals(name) && symbol.getScope().equals(scope))
          return true;
      }
    }
    
    return false;
  }
  
  /**
   * Parses type from string
   * @param type String to be parsed
   * @return Type The parsed type
   * @throws SemanticError If the type couldn't be parsed
   */
  private Type parseType(String type) throws SemanticError {
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
   * Checks if the given identifier is a constant
   * @param id Identifier to check
   * @return True if the identifier is a constant
   */
  private boolean isConstant(String id) {
    return id.startsWith("&");
  }
  
  /**
   * Checks if the given identifier is a variable
   * @param id Identifier to check
   * @return True if the identifier is a variable
   */
  private boolean isVariable(String id) {
    return id.startsWith("$");
  }
  
  /**
   * Checks if the given identifier is a function
   * @param id Identifier to check
   * @return True if the identifier is a function
   */
  private boolean isFunction(String id) {
    return id.startsWith("@");
  }
}
