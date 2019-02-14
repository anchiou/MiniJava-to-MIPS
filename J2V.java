import syntaxtree.*;

public class J2V {
    public static void main(String[] args) throws ParseException {
        new MiniJavaParser(System.in);
        Goal program = MiniJavaParser.Goal();
        FirstVisitor visitor1 = new FirstVisitor();
        TranslationHelper helper = new TranslationHelper();
        program.accept(visitor1, helper);
        TranslatorVisitor visitor2 = new TranslatorVisitor();
        program.accept(visitor2, helper);
    }
}