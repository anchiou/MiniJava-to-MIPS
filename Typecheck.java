// package typecheck;

import java.util.Stack;
import syntaxtree.*;

public class Typecheck {
    public static void main(String[] args) {
        try {
            new MiniJavaParser(System.in);
            Goal program = MiniJavaParser.Goal();
            VisitorPhaseOne visitor = new VisitorPhaseOne();
            // Stack symbolTable = new Stack<Scope>();
            // program.accept(visitor, symbolTable);
            program.accept(visitor);
        } catch (ParseException e) {
            System.out.println("ParseException in Typecheck main");
        }
        return;
    }
}