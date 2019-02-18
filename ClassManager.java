import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClassManager {
    // map of class names to fields
    private Map<String, ArrayList<String>> record = new LinkedHashMap<>();
    // map of class names to methods: inner map is for overridden methods
    private Map<String, Map<String, String>> vTable = new LinkedHashMap<>(); // e.g., <class, <overriden method, class>>

    // return all class records
    public Map<String, ArrayList<String>> getAllRecords() {
        return this.record;
    }

    // return all class v-tables
    public Map<String, Map<String, String>> getAllVTables() {
        return this.vTable;
    }

    // return size of class record
    public int getClassSize(String className) {
        if (this.record.containsKey(className)) {
            return this.record.get(className).size() * 4 + 4;
        }
        return 4;
    }

    // return corresponding class record
    public ArrayList<String> getRecord(String className) {
        return this.record.get(className);
    }

    // return corresponding class v-table
    public Map<String, String> getVTable(String className) {
        return this.vTable.get(className);
    }

    public int getMethodOffset(String className, String method) {
        int index = 0;
        Map<String, String> vTable = this.vTable.get(className);
        for (String key : vTable.keySet()) {
            if (key == method && vTable.get(key) == className) {
                break;
            }
            ++index;
        }

        return index * 4;
    }

    public int getFieldOffset(String className, String field) {
        return this.record.get(className).lastIndexOf(field) * 4 + 4;
    }

    // put class name and fields list
    public void putFields(String className, ArrayList<String> fields) {
        this.record.put(className, fields);
    }

    // put class name and method maps (maps field names to offsets)
    public void putMethods(String className, Map<String, String> methods) {
        this.vTable.put(className, methods);
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
        for (String key : methods.keySet()) {
            System.out.print("  :" + methods.get(key) + ".");
            System.out.println(key);
        }
        System.out.println("");
    }
}
