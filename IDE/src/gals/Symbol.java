package gals;

public class Symbol {

  public enum Type {
    UNDEFINED,
    VOID,
    INT,
    FLOAT,
    DOUBLE,
    STRING,
    CHAR,
    BOOLEAN,
    BINARY,
    HEXADECIMAL
  }
  
  private String identifier;
  private Type type;
  private boolean used;
  private String scope;
  private boolean parameter;
  private int position;
  private boolean array;
  private boolean matrix;
  private boolean reference;
  
  public Symbol(String id, Type type, boolean used, String scope, boolean param,
          int pos, boolean array, boolean matrix, boolean ref) {
    this.identifier = id;
    this.type = type;
    this.used = used;
    this.scope = scope;
    this.parameter = param;
    this.position = pos;
    this.array = array;
    this.matrix = matrix;
    this.reference = ref;
  }
  
  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public boolean hasBeenUsed() {
    return used;
  }

  public void setUsed(boolean used) {
    this.used = used;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public boolean isParameter() {
    return parameter;
  }

  public void setParameter(boolean parameter) {
    this.parameter = parameter;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public boolean isArray() {
    return array;
  }

  public void setArray(boolean array) {
    this.array = array;
  }

  public boolean isMatrix() {
    return matrix;
  }

  public void setMatrix(boolean matrix) {
    this.matrix = matrix;
  }

  public boolean isReference() {
    return reference;
  }

  public void setReference(boolean reference) {
    this.reference = reference;
  }
  
  public boolean isVariable() {
    return this.identifier.startsWith("$");
  }
  
  public boolean isConstant() {
    return this.identifier.startsWith("&");
  }
  
  public boolean isFunction() {
    return this.identifier.startsWith("@");
  }
}
