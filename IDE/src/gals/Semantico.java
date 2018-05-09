package gals;

import java.util.ArrayList;

public class Semantico implements Constants
{
    public ArrayList<Symbol> symbolTable = new ArrayList<>();
  
    /**
     * #1 = FUNCTION
     * #2 = RETURN (->)
     * #3 = TYPE
     * #4 = SCOPE
     */
    
    public void executeAction(int action, Token token) throws SemanticError
    {
        System.out.println("Ação #"+action+", Token: "+token);
    }	
}
