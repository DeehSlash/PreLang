package gals;

import gals.Symbol.Type;
import java.util.ArrayList;

public class Semantico implements Constants
{
  private enum Mode {
    NONE,
    DECLARING_VARIABLE,
    DECLARING_CONSTANT,
    DECLARING_FUNCTION,
    DECLARING_FUNCTION_PARAMETERS
  }
  
  // Symbols Table
  public ArrayList<Symbol> symbolTable = new ArrayList<>();
  
  // Current mode
  private Mode mode = Mode.NONE;
  
  // Temp variables
  private String function;
  private String variableOrConstant;
  private Symbol.Type type;
  private String scope = "global";
  private int position = 0;
  private boolean array = false;
  private ArrayList<Symbol> parametersToBeAdded = new ArrayList<>();
    
  /**
   * #1 = FUNCTION
   * #2 = TYPE
   * #3 = SCOPE END
   * #4 = FUNCTION PARAMETERS (START)
   * #5 = FUNCTION PARAMETERS (END)
   * #6 = VARIABLE / CONSTANT 
   * #7 = COMMA
   * #8 = INDEX OPEN
   * #9 = INDEX CLOSE
   */
    
  public void executeAction(int action, Token token) throws SemanticError
  {
    System.out.println("#" + action + " - " + token.getLexeme());
    
    switch (action) {
      
      // FUNCTION
      case 1:
        if(this.scope.equals("global")) {
          this.function = token.getLexeme();
          this.mode = Mode.DECLARING_FUNCTION;
          this.scope = this.function; 
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
        
      // SCOPE END
      case 3:
        this.mode = Mode.NONE;
        this.scope = "global";
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
            this.scope, true, this.position, this.array, false, false));
    
    this.position++;
    
    return true;
  }
  
  private void addParameters() {
    for (Symbol symbol : this.parametersToBeAdded) {
      this.symbolTable.add(symbol);
    }
    
    this.parametersToBeAdded.clear();
  }
  
  private boolean identifierExists(String identifier) throws SemanticError {
    for (Symbol symbol : symbolTable) {
      
      if (symbol.getIdentifier().equals(identifier)) {
        
        // REPEATED PARAMETERS
        if (this.mode == Mode.DECLARING_FUNCTION_PARAMETERS && symbol.isParameter()
                && symbol.getScope().equals(this.scope))
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
}
