import java.util.HashMap;

public class TranslationHelper {
    HashMap<String, Scope> symbolTable;
    ClassManager classList;

    public TranslationHelper() {
        this.symbolTable = new HashMap<String, Scope>();
        this.classList = new ClassManager();
    }

    public TranslationHelper(HashMap<String, Scope> symbolTable, ClassManager classList) {
        this.symbolTable = symbolTable;
        this.classList = classList;
    }
}