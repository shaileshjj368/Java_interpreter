package com.interpreter.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import javax.swing.text.html.parser.Parser;

public class Lox{
    static boolean hadError = false;
    public static void main(String args[]) throws IOException{
        if(args.length>1){
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        }else if(args.length ==1){
            runFile(args[0]);
        }else{
            runPrompt();
        }
    }

    private static void runFile(String path) throws IOException{
        byte[] bytes = Files.readAllBytes(Path.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        if(hadError)
            System.exit(65);
    }

    private static void runPrompt() throws IOException{
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for(;;){
            System.out.print("> ");
            String line = reader.readLine();
            if(line==null)
                break;
            run(line);
            hadError=false;
        }
    }

    private static void run(String source){
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        Parser parser = new Parser(tokens);
        Expr expression = parser.parse();

        if(hadError()) return;

        System.out.println(new AstPrinter().print(expression));
    }

    static void error(int line, String message){
        report(line, "", message);
    }

    private static void report(int line,String where, String message){
        System.err.println("[line "+line+"] Error"+ where+ ": "+message);
        hadError = true;
    }

    static void error(Token token, String msg){
        if(token.type ==TokenType.EOF){
            report(token.line, " at end " , msg);
        }else{
            report(token.line, " '" + token.lexeme + "' ", msg);
        }
    }
}