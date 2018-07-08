package gals;

public class Assembler {

  String data = ".data\n";
  String text = ".text\n";

  public Assembler() {}

  public String getCode() {
    return data + "\n" + text;
  }

  public void addToData(String a, String b) {
    this.data += "    " + a + " : " + b + "\n";
  }
  
  public void addToText(String a, String b) {
    this.text += "    " + a + "\t" + b + "\n";
  }
}
