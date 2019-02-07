import syntaxtree.*;

public class J2V {
    public static void main(String[] args) {
        try {
            new MiniJavaParser(System.in);
            Goal program = MiniJavaParser.Goal();

        } catch (ParseException e) {
            return;
        }
    }
}
