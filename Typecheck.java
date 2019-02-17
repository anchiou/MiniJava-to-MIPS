import java.util.Stack;
import syntaxtree.*;

public class Typecheck {
    public static void main(String[] args) {
        try {
            new MiniJavaParser(System.in);
            Goal program = MiniJavaParser.Goal();

            FirstVisitor visitor1 = new FirstVisitor();
            TranslationHelper helper = new TranslationHelper();
            program.accept(visitor1, helper);

            SecondVisitor visitor2 = new SecondVisitor();
            program.accept(visitor2, helper.symbolTable);
            System.out.println("Program type checked successfully");
        } catch (ParseException e) {
            System.out.println("Type error");
            return;
        }
        return;
    }
}
