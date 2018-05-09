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
  private String variable;
  private String constant;
  private Symbol.Type type;
  private String scope = "global";
    
  /**
   * #1 = FUNCTION
   * #2 = TYPE
   * #3 = SCOPE END
   * #4 = FUNCTION PARAMETERS (START)
   * #5 = FUNCTION PARAMETERS (END)
   * #6 = VARIABLE / CONSTANT 
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
        if (mode == Mode.DECLARING_FUNCTION) {
          this.type = parseType(token.getLexeme());
          this.addFunction();
        }
        break;
        
      // SCOPE END
      case 3:
        mode = Mode.NONE;
        this.scope = "global";
        break;
        
      // FUNCTION PARAMETERS (START)
      case 4:
        this.mode = Mode.DECLARING_FUNCTION_PARAMETERS;
        break;
        
      // FUNCTION PARAMETERS (END)
      case 5:
        this.mode = Mode.DECLARING_FUNCTION;
        break;
    }
  }	
  
  private boolean addFunction() throws SemanticError {
    identifierExists(this.function);
    System.out.println("TIPO DA FUNÇÃO: " + this.type.toString());
    this.symbolTable.add(new Symbol(this.function, this.type, false,
            "global", false, 0, false, false, false));
    
    return true;
  }
  
  private boolean identifierExists(String identifier) throws SemanticError {
    for (Symbol symbol : symbolTable) {
      if(symbol.getIdentifier().equals(identifier))
        throw new SemanticError("Identifier " + identifier + 
                " had already been declared before");
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
