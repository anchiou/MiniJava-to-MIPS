import syntaxtree.*;

public class J2V {
    public static void main(String[] args) throws ParseException {
        new MiniJavaParser(System.in);
        Goal program = MiniJavaParser.Goal();
        FirstVisitor visitor1 = new FirstVisitor();
        TranslationHelper helper = new TranslationHelper();
        program.accept(visitor1, helper);

        for (String key : helper.symbolTable.keySet()) {
            System.out.println(key + ": ");
            helper.symbolTable.get(key).printAll();
            System.out.println("\n");
        }
        // for (int i = 0; i < helper.symbolTable.size(); ++i) {
        //     helper.symboleTable.at(i).printAll();
        // }

        TranslatorVisitor visitor2 = new TranslatorVisitor();
        program.accept(visitor2, helper);
    }
}