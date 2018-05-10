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
    DECLARING_FUNCTION_PARAMETERS
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
        if(this.isConstant(this.variableOrConstant))
          this.mode = Mode.CONSTANT;
        else
          this.mode = Mode.VARIABLE;
        break;
        
      // COMMAND (AFTER)
      case 13:
        switch(this.mode) {
          case CONSTANT:
            addVariable();
          case VARIABLE:
            addConstant();
        }
        this.mode = Mode.NONE;
        break;
    }
  }	
  
  private boolean addFunction() throws SemanticError {
    if (identifierExists(this.function))
      throw new SemanticError("Function " + this.function + 
              " had already been declared before");
    
    this.symbolTable.add(new Symbol(this.function, this.type, false,
            "global", false, 0, false, false, false));
    
    return true;
  }
  
  private boolean addParameter() throws SemanticError {
    if (identifierExists(this.variableOrConstant))
      throw new SemanticError("Parameter " + this.variableOrConstant +
              " had already been declared before");
    
    this.parametersToBeAdded.add(new Symbol(this.variableOrConstant, this.type, false,
            this.function, true, this.position, this.array, false, false));
    
    this.position++;
    
    return true;
  }
  
  private void addParameters() {
    for (Symbol symbol : this.parametersToBeAdded) {
      this.symbolTable.add(symbol);
    }
    
    this.parametersToBeAdded.clear();
  }
  
  private void addConstant() throws SemanticError {
    if (identifierExists(this.variableOrConstant))
      throw new SemanticError("Constant " + this.variableOrConstant +
              " had already been declared before");
    
    this.symbolTable.add(new Symbol(this.variableOrConstant, this.type, false,
            this.scopeStack.peek(), false, 0, this.array, false, false));
  }
  
  private void addVariable() throws SemanticError {
    if (identifierExists(this.variableOrConstant))
      throw new SemanticError("Variable " + this.variableOrConstant +
              " had already been declared before");
    
    this.symbolTable.add(new Symbol(this.variableOrConstant, this.type, false,
            this.scopeStack.peek(), false, 0, this.array, false, false));
  }
  
  private boolean identifierExists(String identifier) throws SemanticError {
    for (Symbol symbol : symbolTable) {
      
      if (symbol.getIdentifier().equals(identifier)) {
        
        // REPEATED PARAMETERS
        if (this.mode == Mode.DECLARING_FUNCTION_PARAMETERS && symbol.isParameter()
                && symbol.getScope().equals(this.function))
          return true;
        
        // REPEATED FUNCTIONS
        else if (this.mode == Mode.DECLARING_FUNCTION && symbol.getScope().equals("global"))
          return true;
      }
      
    }
   
    return false;
  }
  
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
  
  private boolean isConstant(String id) {
    return id.startsWith("&");
  }
  
  private boolean isVariable(String id) {
    return id.startsWith("$");
  }
  
  private boolean isFunction(String id) {
    return id.startsWith("@");
  }
}
