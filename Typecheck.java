// package typecheck;

import java.util.Stack;
import syntaxtree.*;

public class Typecheck {
    public static void main(String[] args) {
        try {
            new MiniJavaParser(System.in);
            Goal program = MiniJavaParser.Goal();
            FirstVisitor visitor = new FirstVisitor();
            //program.accept(visitor, symbolTable);
            program.accept(visitor);
            String test = visitor.symbolTable.pop().getTest();
            System.out.println(test);
        } catch (ParseException e) {
            System.out.println("ParseException in Typecheck main");
        }
        return;
    }
}