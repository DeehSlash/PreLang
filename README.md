# PreLang

 + É necessario executar no galsModificado
 + Configurar o semantico para string.
 + Escolher opcoes de tabela compactada
 + Alterar os caracteres utf8 para sem acento.

# implementação expressoes com suporte a vetores

 **A[i + 1] = B[x - 1] + c[z - 2];**
exemplo proc bip
+ 1 LD I
    ADDI 1
+ 2 STO 1002
+ 3 LD X
    SUBI 1
+ 4 STO $INDR
+ 5 LDV B
+ 6 STO 1000
+ 7 LD Z
  SUBI 2
+ 8 STO $INDR
+ 9 LDV C
+ 10 STO 1001
+ 11 LD 1000
+ 12 ADD 1001
+ 13 STO 1000
+ 14 LD 1002
+ 15 STO $INDR
+ 16 LD 1000
+ 17 STOV A



## Descrição

Este projeto pertence à disciplina de **Compiladores** do curso de **Ciência da Computação**. Tem como objetivo implementar uma nova linguagem, criando a análise **léxica**, **sintática** e **semântica**.

## Linguagem

A linguagem criada é batizada de **PreLang**, com inspirações em JavaScript e Python. Ela possui tipagem implícita e possui os seguintes prefixos:

| Prefixo | Significado |
| --- | --- |
| & | Constantes |
| $ | Variáveis |
| @ | Funções |
| # | Comentários inline |
| ## | Comentários de múltipla linhas (com sufixo igual) |

Atualmente ela suporta os seguintes tipos primitivos:

| Tipo |
| --- |
| Int |
| Float |
| Boolean |
| Binary |
| Hexadecimal |
| String |

Os escopos são definidos atualmente com o uso de chaves **{** **}**

## To-do

Futuramente as chaves serão removidas como delimitadoras de escopo, onde a identação irá assumir o lugar de escopo, assim como no Python.
