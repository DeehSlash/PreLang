/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gals;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author croisebeck
 */
public class Simbolo {
    
    String id;
    int escopo;
    int tipo;
    int tamanho;
    
    boolean usada;
    boolean ini;
    
    int funcao;
    boolean param;
    int pos;
    List<Parametro> parametro;
    
    boolean vet;
    boolean ref;
    boolean func;
    boolean biblio;

    public int getFuncao() {
        return funcao;
    }

    public void setFuncao(int funcao) {
        this.funcao = funcao;
    }

    public List<Parametro> getParametro() {
        return parametro;
    }

    public void setParametro(List<Parametro> parametro) {
        this.parametro = parametro;
    }

    public boolean isIni() {
        return ini;
    }

    public void setIni(boolean ini) {
        this.ini = ini;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }
    
    
    public Simbolo(String id,int escopo) {
        this.id =id; 
        this.escopo = escopo;
        this.usada = false;
        this.param =false;
        this.vet = false;
        this.ref = false;
        this.func = false;
        this.biblio=false;
        this.funcao =0;
        this.ini = false;
        this.parametro = new ArrayList<>();
        
    }

    public String getId() {
        return id;
    }

    public boolean isBiblio() {
        return biblio;
    }

    public void setBiblio(boolean biblio) {
        this.biblio = biblio;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public boolean isUsada() {
        return usada;
    }

    public void setUsada(boolean usada) {
        this.usada = usada;
    }

    public int getEscopo() {
        return escopo;
    }

    public void setEscopo(int escopo) {
        this.escopo = escopo;
    }

    public boolean isParam() {
        return param;
    }

    public void setParam(boolean param) {
        this.param = param;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public boolean isVet() {
        return vet;
    }

    public void setVet(boolean vet) {
        this.vet = vet;
    }

    public boolean isRef() {
        return ref;
    }

    public void setRef(boolean ref) {
        this.ref = ref;
    }

    public boolean isFunc() {
        return func;
    }

    public void setFunc(boolean func) {
        this.func = func;
    }
    
    
    
}
