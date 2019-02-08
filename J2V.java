import syntaxtree.*;

public class J2V {
    public static void main(String[] args) throws ParseException {
        new MiniJavaParser(System.in);
        Goal program = MiniJavaParser.Goal();
        program.accept(new FirstVisitor()); // Pass in visitor
    }
}
