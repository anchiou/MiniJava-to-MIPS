import java.util.Stack;
import syntaxtree.*;

public class Typecheck {
    public static void main(String[] args) {
        try {
            new MiniJavaParser(System.in);
            Goal program = MiniJavaParser.Goal();
            FirstVisitor visitor1 = new FirstVisitor();
            program.accept(visitor1);
            // String test = visitor.symbolTable.pop().getType("num_aux");
            SecondVisitor visitor2 = new SecondVisitor();
            program.accept(visitor2, visitor1.symbolTable);
        } catch (ParseException e) {
            System.out.println("ParseException in Typecheck main");
        }
        return;
    }
}