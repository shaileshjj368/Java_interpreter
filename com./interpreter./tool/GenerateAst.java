package com.interpreter.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

class GenerateAst{
    public static void main(String args[]) throws IOException{
        if(args.length != 1){
            System.out.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }

        String outputDir = args[0];
        defineAst(outputDir, "Expr", Arrays.asList(
            "Binary: Expr left, Token operator, Expr right",
            "Grouping: Expr expression",
            "Literal: Object value",
            "Unary: Token operator, Expr right"
        ));
    }

    private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException{
        String path = outputDir+ "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package com.interpreter.lox;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");
        defineVisitor(writer, baseName, types);
        for(String t :  types){
            String className = t.split(":")[0].trim();
            String fields = t.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }
        writer.println("}");
        writer.println("    abstract <R> R accept(Visitor<R> visitor);");
        writer.close();
    }

    private static void defineType(PrintWriter writer, String baseName, String className, String FieldList){
        writer.println("static class "+ className+ " extends "+ baseName + " {");
        writer.println("    " + className+ " (" + FieldList+" ) {");
        String[] fields = FieldList.split(" ")[1];
        for(String f:fields){
            String name = f.split(" ")[1];
            writer.println("    this." + name + " = " + name+ "; ");
        }
        writer.println("    }");

        writer.println();
        writer.println("    @Override");
        writer.println("    <R> R accept(Visitor<R> visitor){");
        writer.println("        return  visitor.visit"+ className+ baseName+ "(this);");
        writer.println("    }");

        writer.println();
        for(String f:fields){
            writer.println("    final "+ f + " ;");
        }


        writer.println("    }");
    }

    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types){
        writer.println(" interface Visitor<R> {");

        for(String t: types){
            String typeName = t.split(":")[0].trim();
            writer.println("    R visit"+ typeName+ baseName+"("+typeName+ " "+ baseName.toLowerCase()+");");
        }
        writer.println("    }");
    }
}