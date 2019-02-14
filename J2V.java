import syntaxtree.*;

public class J2V {
    public static void main(String[] args) throws ParseException {
        new MiniJavaParser(System.in);
        Goal program = MiniJavaParser.Goal();
        FirstVisitor visitor1 = new FirstVisitor();
        program.accept(visitor1);
        TranslatorVisitor visitor2 = new TranslatorVisitor();
        program.accept(visitor2, visitor1.symbolTable);
    }
}
