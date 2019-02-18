import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class Scope {
    String parentScope; // scope number of parent class if any
    String className; // the class the scope corresponds to
    Map<String, String> types;

    public Scope (String className) {
        this.className = className;
        this.types = new HashMap<String, String>();
    }

    public Scope (String parentScope, String className) {
        this.parentScope = parentScope;
        this.className = className;
        this.types = new HashMap<String, String>();
    }

    public boolean contains(String id) {
        return this.types.containsKey(id);
    }

    public String getClassName() {
        return this.className;
    }

    public String getType(String id) {
        return this.types.get(id);
    }

    public String getParentScope() {
        return this.parentScope;
    }

    public void putType(String id, String type) {
        // System.out.println("In PutType id: " + id + " type: " + type);
        this.types.put(id, type);
    }

    public void printAll() {
        for (String key : this.types.keySet()) {
            System.out.print(key + " -> ");
            System.out.println(this.types.get(key));
        }
    }
}
