package gals;

import gals.Symbol.Type;
import ide.IDE;
import java.util.Stack;

/**
 * Semantico
 */
public class Semantico {
  
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
  private String lastAttribute;
  private String lastFunction;
  private String lastParameter;
  private Type lastType = Type.UNDEFINED;
  private boolean isArray = false;
  private int arraySize = 0;
  private boolean resolvingExpression = false;
  
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
   * 800 - 899
   * Expressions
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
        break;
      // -----------------------------------------------------------------------
        
      // After function
      case 11:
        symbolTable.addFunction(lastFunction, lastType, isArray);
        resetState();
        break;
      // -----------------------------------------------------------------------
        
      // Parameter name (declaration)
      case 20:
        lastParameter = token.getLexeme();
        break;
      // -----------------------------------------------------------------------
        
      // After parameter
      case 21:
        symbolTable.addParameter(lastParameter, lastType, isArray, arraySize);
        arraySize = 0;
        break;
      // -----------------------------------------------------------------------
        
      // Primitive type
      case 100:
        lastType = parseType(token.getLexeme());
        isArray = false;
        break;
      // -----------------------------------------------------------------------
        
      // Primitive type as array
      case 101:
        lastType = parseType(token.getLexeme());
        isArray = true;
        break;
      // -----------------------------------------------------------------------
        
      // Primitive type array size
      case 102:
        arraySize = Integer.parseInt(token.getLexeme());
        break;
      // -----------------------------------------------------------------------
        
      // Expression - INT
      case 800:
        if (resolvingExpression)
          semanticTable.push(0);
        break;
      // -----------------------------------------------------------------------
        
      // Expression - FLOAT
      case 801:
        if (resolvingExpression)
          semanticTable.push(1);
        break;
      // -----------------------------------------------------------------------
        
      // Expression - BOOLEAN
      case 802:
        if (resolvingExpression)
          semanticTable.push(4);
        break;
      // -----------------------------------------------------------------------
        
      // Expression - STRING
      case 803:
        if (resolvingExpression)
          semanticTable.push(3);
        break;
      // -----------------------------------------------------------------------
        
      // Expression - CHAR
      case 804:
        if (resolvingExpression)
          semanticTable.push(2);
        break;
      // -----------------------------------------------------------------------
        
      // Expression - VARIABLE
      case 810:
        lastAttribute = token.getLexeme();
        
        if (!resolvingExpression)
          symbolTable.addAttribute(lastAttribute, Type.UNDEFINED,
                  scopeStack.peek(), isArray, arraySize);
        else
          semanticTable.push(symbolTable.getExpressionType(lastAttribute));
        break;
      // -----------------------------------------------------------------------
        
      // Expression - CONSTANT
      case 811:
        lastAttribute = token.getLexeme();
        symbolTable.addAttribute(lastAttribute, Type.UNDEFINED,
                scopeStack.peek(), isArray, arraySize);
        
        if (!resolvingExpression)
          symbolTable.addAttribute(lastAttribute, Type.UNDEFINED,
                  scopeStack.peek(), isArray, arraySize);
        else
          semanticTable.push(symbolTable.getExpressionType(lastAttribute));
        break;
      // -----------------------------------------------------------------------
      
      // Expression - ADD operator
      case 820:
        semanticTable.push(0);
        break;
      // -----------------------------------------------------------------------
        
      // Expression - SUB operator
      case 821:
        semanticTable.push(1);
        break;
      // -----------------------------------------------------------------------
        
      // Expression - MULT operator
      case 822:
        semanticTable.push(2);
        break;
      // -----------------------------------------------------------------------
        
      // Expression - DIV operator
      case 823:
        semanticTable.push(3);
        break;
      // -----------------------------------------------------------------------
        
      // Expression - RELATIONAL operators
      case 824:
        semanticTable.push(4);
        break;
      // -----------------------------------------------------------------------
        
      // Expression - START
      case 850:
        resolvingExpression = true;
        break;
      // -----------------------------------------------------------------------
        
      // Expression - END
      case 851:
        lastType = resolveExpression();
        resolvingExpression = false;
        symbolTable.updateAttribute(lastAttribute, lastType);
        break;
      // -----------------------------------------------------------------------
        
      // Scope open
      case 900:
        // Generate scope
        generateScope(lastFunction);   
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
   * Resolves an expression and returns the resulting type
   * @return The resulting type
   * @throws SemanticError 
   */
  private Type resolveExpression() throws SemanticError {
    // While there's something to resolve
    while (semanticTable.size() > 1) {
      // Get the operating and the operator
      int type1 = semanticTable.pop();
      int operator = semanticTable.pop();
      int type2 = semanticTable.pop();
      
      // Calculate the result
      int result = SemanticTable.resultType(type1, type2, operator);
      
      // If the result is correct, insert in the table again
      if (result != SemanticTable.ERR)
        semanticTable.push(result);
      else
        throw new SemanticError("Invalid expression");
    }
      
    // Get the resulting type
    int resultingType = semanticTable.pop();
    
    switch(resultingType) {
      case 0:
        return Type.INT;
      case 1:
        return Type.FLOAT;
      case 2:
        return Type.CHAR;
      case 3:
        return Type.STRING;
      case 4:
        return Type.BOOLEAN;
    }
    
    return Type.UNDEFINED;
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
    lastAttribute = "";
    lastParameter = "";
    lastType = Type.UNDEFINED;
    isArray = false;
    arraySize = 0;
    resolvingExpression = false;
  }
}
