import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClassManager {
    // map of class names to fields
    private Map<String, ArrayList<String>> record = new LinkedHashMap<>();
    // map of class names to methods
    private Map<String, Map<String, String>> vTable = new LinkedHashMap<>();

    // put class name and fields list
    public void putRecords(String class, ArrayList<String> fields) {
        this.record.put(class, fields);
    }

    // put class name and method maps (maps field names to offsets)
    public void putMethods(String class, Map<String, String> methods) {
        this.vTable.put(class, methods);
    }
}
