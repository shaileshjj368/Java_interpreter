package com.interpreter.lox;

import java.util.ArrayList;

class Scanner{
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 0;
    private final static Map<String, TokenType> keywords;

    static{
        keywords = new HashMap<>();
        keywords.put("and", AND);
        keywords.put("or", OR);
        keywords.put("fun", FUN);
        keywords.put("class", CLASS);
        keywords.put("for", FOR);
        keywords.put("while", WHILE);
        keywords.put("for", FOR);
        keywords.put("if", IF);
        keywords.put("nil", NIL);
        keywords.put("else", ELSE);
        keywords.put("super", SUPER);
        keywords.put("true", TRUE);
        keywords.put("false", FALSE);
        keywords.put("or", OR);
        keywords.put("print", PRINT);
        keywords.put("this", THIS);
        keywords.put("var", VAR);
    }

    Scanner(String source){
        this.source = source;
    }

    List<Token> scanTokens(){
        while(!isAtEnd()){
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF,"", null, line));
        return tokens;
    }

    private void scanToken(){
        char c = advance();
        switch(c){
            case '(':addToken(LEFT_PAREN); break;
            case ')':addToken(RIGHT_PAREN); break;
            case '{':addToken(LEFT_BRACE); break;
            case '}':addToken(RIGHT_BRACE); break;
            case '+':addToken(PLUS); break;
            case '-':addToken(MINUS); break;
            case '*':addToken(STAR); break;
            case ',':addToken(COMMA); break;
            case ';':addToken(SEMICOLON); break;
            case '.':addToken(DOT); break;
            case '!':
                addToken(match('=') ? BANG_EQUAL: BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL: EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESSER_EQUAL: EQUAL);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL: EQUAL);
                break;
            case '/':
                if(match('/')){
                    while(peek() != '\n' && !isAtEnd())
                        advance();
                }
                else{
                    addToken(SLASH);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;
            case '"':
                string();
                break;
            case 'o':
                if(peek()=='r'){
                    addToken(OR);
                }
                break;
            default:
                if(isDigit(c)){
                    number();
                }else if(isAlpha(c)){
                    identifier();
                }else{
                Lox.error(line, "Unexpected character.");}
                break;
        }
    }

    private boolean isAtEnd(){
        return current>= source.length();
    }

    private char advance(){
        current++;
        return source.charAt(current-1);
    }

    private void addToken(TokenType type){
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal){
        String text = source.substring(start,current);
        tokens.add(new Token(type, text, literal, line));
    }

    private boolean match(char expected){
        if(isAtEnd()) return false;
        if(source.charAt(current)!= expected) return false;

        current++;
        return true;
    }

    private char peek(){
        if(isAtEnd()) return '\0';
        return source.charAt(current); 
    }

    private void string(){
        while(peek()!='"' && !isAtEnd()){
            if(peek() != '\n' )
                line++;
            advance();
        }

        while(isAtEnd()){
            Lox.error(line, "Unterminated String.");
            return;
        }

        advance();

        String value = source.substring(start+1, current-1);
        addToken(STRING, value);
    }

    private boolean isDigit(char c){
        return c>='0' && c<='9';
    }

    private void number(){
        while(isDigit(peek())) advance();

        if(peek()=='.' && isDigit(peekNext())){
            advance();
            while(isDigit(peek())) advance();
        }

        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private char peekNext(){
        if(current+1>=source.length()) return '\0';
        return source.charAt(current+1);
    }

    private boolean isAlpha(char c){
        return (c>='a' && c<='z') || (c == '_') || (c>='A' && c<='Z'); 
    }

    private void identifier(){
        while(isAlphaNumeric(peek())) advance();

        String text = source.substring(start,current);
        TokenType type = keywords.get(text);
        if(type==null) type = IDENTIFIER;
        addToken(type);
    }

    private boolean isAlphaNumeric(char c){
        return isAlpha(c) || isDigit(c);
    }



}