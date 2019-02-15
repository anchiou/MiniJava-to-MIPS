import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClassManager {
    // map of class names to fields
    private Map<String, ArrayList<String>> record = new LinkedHashMap<>();
    // map of class names to methods
    private Map<String, Map<String, String>> vTable = new LinkedHashMap<>();

    // put class name and fields list
    public void putRecords(String className, ArrayList<String> fields) {
        this.record.put(className, fields);
    }

    // put class name and method maps (maps field names to offsets)
    public void putMethods(String className, Map<String, String> methods) {
        this.vTable.put(className, methods);
<<<<<<< HEAD
    }

    public void printFields(String className) {
        ArrayList<String> fields = this.record.get(className);
        System.out.print(className + " fields: ");
        for (int i = 0; i < fields.size(); ++i) {
            System.out.print(fields.get(i) + "  ");
        }
        System.out.println("");
    }

    public void printMethods(String className) {
        Map<String, String> methods = this.vTable.get(className);
        System.out.print(className + " methods: ");
        for (String key : methods.keySet()) {
            System.out.println(key + "  ");
            // System.out.println(this.types.get(key));
        }
        System.out.println("");
    }
=======
    }
>>>>>>> 742921a63d8b78d23a4764a28cdf54bb0153f249
}
