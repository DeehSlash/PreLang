/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gals;

import ide.PreLangWindow;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author croisebeck
 */
public class GeracaoDeCodigo extends Semantico{

    String asb_data = ".data" + "\n";
    String asb_text = ".text \n";
    String varIO = "";
    String nome_id_atrib = "";
    String opr = "",oprl="",numExp=null,idExp=null;
    String temporarios[] = {null,null,null,null};
    boolean flagExp = false,enquanto=false;
    LinkedList escopoAninhamento = new LinkedList();
    

    public GeracaoDeCodigo() {
    }

    public void gera_data(List<Simbolo> TabelaSimbolos) {
        for (Simbolo T : TabelaSimbolos) {
            if (!T.isFunc()) {
                if (T.vet) {
                    asb_data += "\t" + T.getId() + " : 0";
                    for (int i = 0; i < T.getTamanho() - 1; i++) {
                        asb_data += ",0";
                    }
                    asb_data += "\n";
                } else {
                    asb_data += "\t" + T.getId() + " : 0\n";
                }

            }
        }
        asb_data += "\n";
        asb_text += "\tHLT\t0";
        PreLangWindow.editor.setText("");
        PreLangWindow.editor.append(asb_data + asb_text);

    }

    public void gera_cod(String a, String b) {
        asb_text += "\t" + a + "\t" + b + "\n";
    }
        
    public String verify_temp(Boolean alocar,String var) throws SemanticError
    {
        if(alocar)
        {
         for(int i=0;i<temporarios.length;i++)
         {
             if(temporarios[i]==null) temporarios[i]=var;
             return "temp"+(i+1);
         }
         throw new SemanticError("Erro ao alocar variavel em registrador temporário");
        }
        else
        {
            for (int i=0;i<temporarios.length;i++) {
                temporarios[i]=null;
            }
            return "";
        }
         
    }

    public void executacaGeracaoAssembly(int a, String token) throws SemanticError {
        switch (a) {
            case 35:
                gera_cod("LD", "$in_port");
                gera_cod("STO", token);
                break;
            case 36:
                flagExp = true;
                opr = token;
                break;
            case 37:
                if (!flagExp) {
                    gera_cod("LD", token);
                } else {
                    if ("+".equals(opr)) {
                        gera_cod("ADD", token);
                    } else if ("-".equals(opr)) {
                        gera_cod("SUB", token);
                    } else if ("<<".equals(opr)) {
                        gera_cod("SLL", token);

                    } else if (">>".equals(opr)) {
                        gera_cod("SRL", token);
                    }
                    flagExp = false;
                }
                idExp=token;
                break;
            case 38:
                if (!flagExp) {
                    gera_cod("LDI", token);
                } else {
                    if ("+".equals(opr)) {
                        gera_cod("ADDI", token);
                    } else if ("-".equals(opr)) {
                        gera_cod("SUBI", token);
                    } else if ("<<".equals(opr)) {
                        gera_cod("SLL", token);

                    } else if (">>".equals(opr)) {
                        gera_cod("SRL", token);
                    }
                    flagExp = false;
                }
                numExp=token;
                break;
            case 39:
                if(numExp!=null && idExp==null)
                {
                    gera_cod("LDI", numExp);
                    gera_cod("STO", verify_temp(true,numExp));
                    numExp=null;
                }
                else if(numExp == null && idExp!=null)
                {
                    gera_cod("LD", idExp);
                    gera_cod("STO", verify_temp(true,idExp));
                    idExp=null;
                }
                oprl=token;
                break;
            case 40:                 
                gera_cod("STO", nome_id_atrib);
                nome_id_atrib=null;
                break;
            case 41:
                gera_cod("LD", token);
                gera_cod("STO", "$out_port");
                break;
            case 42:
                gera_cod("LDI", token);
                gera_cod("STO", "$out_port");
                break;
            case 43:
                varIO = token;
                detectaVariavelAtribuicaoDireta(token);
                break;
            case 44:
                gera_cod("LDI", token);
                gera_cod("STO", "$indr");
                gera_cod("LD", "$in_port");
                gera_cod("STOV", varIO);
                break;
            case 45:
                varIO = token;
                break;
            case 46:
                gera_cod("LDI", token);
                gera_cod("STO", "$indr");
                gera_cod("LDV", varIO);
                gera_cod("STO", "$out_port");
                break;
            case 47:
                int temp=0;
                gera_cod("STO", verify_temp(true, token));
                for (String t : temporarios) {
                    if(t!=null)
                    {
                        gera_cod("LD", t);
                        gera_cod("SUB", temporarios[temp+1]);
                        continue;
                    }
                    temp++;
                }
                break;
            case 48:
                if(!enquanto)
                {
                escopoAninhamento.push((String)("R"+( escopoAninhamento.size()+1)+":"));
                if(oprl=="<")gera_cod("BGE", (String)escopoAninhamento.peek());
                else if(oprl==">") gera_cod("BLE", (String)escopoAninhamento.peek());
                else if(oprl=="==") gera_cod("BNE", (String)escopoAninhamento.peek());
                else if(oprl=="!=") gera_cod("BEQ", (String)escopoAninhamento.peek());
                else if(oprl==">=") gera_cod("BLT", (String)escopoAninhamento.peek());
                else if(oprl=="<=") gera_cod("BGT", (String)escopoAninhamento.peek());
                }
                else
                {
                    escopoAninhamento.push((String)("R"+( escopoAninhamento.size()+1)+":"));
                    if(oprl=="<")gera_cod("BLT", (String)escopoAninhamento.peekLast());
                    else if(oprl==">") gera_cod("BGT", (String)escopoAninhamento.peekLast());
                    else if(oprl=="==") gera_cod("BEQ", (String)escopoAninhamento.peekLast());
                    else if(oprl=="!=") gera_cod("BNE", (String)escopoAninhamento.peekLast());
                    else if(oprl==">=") gera_cod("BGE", (String)escopoAninhamento.peekLast());
                    else if(oprl=="<=") gera_cod("BLE", (String)escopoAninhamento.peekLast());
                }
                break;
            case 49:
                gera_cod("", "\n"+(String)escopoAninhamento.poll());
                break;
            case 50:
                escopoAninhamento.push((String)("R"+( escopoAninhamento.size()+1)+":"));
                gera_cod("JMP", (String)escopoAninhamento.peekLast());
                gera_cod("", "\n"+(String)escopoAninhamento.poll()); 
                break;
            case 51:
               gera_cod("", "\n"+(String)escopoAninhamento.poll());
                break;
            case 52:
                escopoAninhamento.push((String)("R"+( escopoAninhamento.size()+1)+":"));
                gera_cod("","\n"+(String)escopoAninhamento.peekLast());
                enquanto=true;
                break;
            case 53:
                gera_cod("JMP", (String)escopoAninhamento.poll());
                gera_cod("", "\n"+(String)escopoAninhamento.poll());
                enquanto=false;
                break;
            case 54:
                escopoAninhamento.push((String)("R"+( escopoAninhamento.size()+1)+":"));
                gera_cod("","\n"+(String)escopoAninhamento.peekFirst());
                enquanto=true;
                break;
            case 55:
                    if(oprl=="<")gera_cod("BLT", (String)escopoAninhamento.peekLast());
                    else if(oprl==">") gera_cod("BGT", (String)escopoAninhamento.peekLast());
                    else if(oprl=="==") gera_cod("BEQ", (String)escopoAninhamento.peekLast());
                    else if(oprl=="!=") gera_cod("BNE", (String)escopoAninhamento.peekLast());
                    else if(oprl==">=") gera_cod("BGE", (String)escopoAninhamento.peekLast());
                    else if(oprl=="<=") gera_cod("BLE", (String)escopoAninhamento.peekLast());
                break;
        }
    }

}
