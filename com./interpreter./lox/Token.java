package com.interpreter.lox;

enum TokenType{
    LEFT_PAREN,RIGHT_PAREN,LEFT_BRACE,RIGHT_BRACE,
    COMMA,DOT,SEMICOLON,STAR,PLUS,MINUS,SLASH,

    BANG,BANG_EQUAL,
    EQUAL,EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESSER,LESSER_EQUAL,

    IDENTIFIER,STRING,NUMBER,

    AND,OR,IF,ELSE,RETURN,NIL,PRINT,FOR,WHILE,
    CLASS,VAR,SUPER,THIS,FUN,

    EOF
}

class Token{
    final TokenType type;
    final String lexeme;
    final Object literal;
    final int line;

    Token(TokenType type, String lexeme, Object literal,int line){
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public String toString(){
        retunn (type+" "+ lexeme+ " "+ literal);
    }
}