package typecheck;

import syntaxtree.*;
import typecheck.*;

public class Typecheck {
    public static void main(String[] args) {
        try {
            new MiniJavaParser(System.in);
            Goal program = MiniJavaParser.Goal();
            VisitorPhaseOne visitor = new VisitorPhaseOne();
            program.accept(visitor);
        } catch (ParseException e) {
            System.out.println("ParseException in Typecheck main");
        }
    }
}