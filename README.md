# PreLang

 + É necessario executar no galsModificado
 + Configurar o semantico para string.
 + Escolher opcoes de tabela compactada
 + Alterar os caracteres utf8 para sem acento.

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
