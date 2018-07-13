package gals;

/**
 * Assembler Class
 * Used to construct BIP assembly code from PreLang code
 */
public class Assembler {

  // Data section (variables declaration)
  private String data;
  
  // Text section (commands)
  private String text;

  /**
   * Default constructor
   */
  public Assembler() {
    this.data = ".data\n";
    this.text = ".text\n";
  }

  /**
   * Joins and returns the full assemly code (data + text)
   * @return The full assembly code
   */
  public String getCode() {
    return data + "\n" + text;
  }

  /**
   * Adds a line to the data section
   * @param a Left side of the command (variable name)
   * @param b  Right side of the command (initial value)
   */
  public void addToData(String a, String b) {
    this.data += "    " + removePrefixes(a) + " : " + b + "\n";
  }
  
  /**
   * Adds a line to the text section
   * @param a Left side of the command (mnemonic)
   * @param b Right side of the command
   */
  public void addToText(String a, String b) {
    this.text += "    " + a + "\t" + removePrefixes(b) + "\n";
  }
  
  /**
   * Removes @ and $ prefixes from a string
   * @param string String to remove prefixes
   * @return The formatted string
   */
  private String removePrefixes(String string) {
    return string.replaceAll("[$|@]", "");
  }
}
