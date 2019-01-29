// package typecheck;

import java.util.Stack;
import syntaxtree.*;

public class Typecheck {
    public static void main(String[] args) {
        try {
            new MiniJavaParser(System.in);
            Goal program = MiniJavaParser.Goal();
            FirstVisitor visitor = new FirstVisitor();
            Stack<Scope> symbolTable = new Stack<Scope>();
            // Scope test = new Scope("TestSuccess");
            // symbolTable.push(test);
            program.accept(visitor, symbolTable);
            // program.accept(visitor);
        } catch (ParseException e) {
            System.out.println("ParseException in Typecheck main");
        }
        return;
    }
}