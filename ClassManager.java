import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClassManager {
    // map of class names to fields
    private Map<String, ArrayList<String>> record = new LinkedHashMap<>();
    // map of class names to methods: inner map is for overridden methods
    private Map<String, Map<String, String>> vTable = new LinkedHashMap<>(); // e.g., <class, <overriden method, class>>

    // returns all class records
    public Map<String, ArrayList<String>> getAllRecords() {
        return this.record;
    }

    // returns all class v-tables
    public Map<String, Map<String, String>> getAllVTables() {
        return this.vTable;
    }

    // returns corresponding class record
    public ArrayList<String> getRecord(String className) {
        return this.record.get(className);
    }

    // returns corresponding class v-table
    public Map<String, String> getVTable(String className) {
        return this.vTable.get(className);
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
        // System.out.print(className + " methods: ");
        for (String key : methods.keySet()) {
            System.out.print("  :" + methods.get(key) + ".");
            System.out.println(key);
        }
        System.out.println("");
    }
}
