import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class Scope {
    private String parentScope; // scope number of parent class if any
    private String className; // the class the scope corresponds to
    private Map<String, String> types; // <id, type>
    private Map<String, Map<String, String>> parameters; // handles multiple parameters: <class.method, parameterMap>

    public Scope (String className) {
        this.className = className;
        this.types = new HashMap<String, String>();
        this.parameters = new HashMap<String, Map<String, String>>();
    }

    public Scope (String parentScope, String className) {
        this.parentScope = parentScope;
        this.className = className;
        this.types = new HashMap<String, String>();
        this.parameters = new HashMap<String, Map<String, String>>();
    }

    public boolean contains(String id) {
        return this.types.containsKey(id);
    }

    public String getClassName() {
        return this.className;
    }

    public Map<String, String> getParameters(String method) {
        return this.parameters.get(method);
    }

    public String getType(String id) {
        return this.types.get(id);
    }

    public String getParentScope() {
        return this.parentScope;
    }

    public void putParameters(String method, Map<String, String> parameters) {
        this.parameters.put(method, parameters);
    }

    public void putType(String id, String type) {
        // System.out.println("In PutType id: " + id + " type: " + type);
        this.types.put(id, type);
    }

    public void printAll() {
        for (String key : this.types.keySet()) {
            System.out.print(key + " -> ");
            System.out.println(this.types.get(key));
            if (this.parameters.containsKey(this.className + "." + key)) {
                Map<String, String> paramList = this.parameters.get(this.className + "." + key);
                System.out.println(this.className + "." + key + " parameters:");
                for (String key2 : paramList.keySet()) {
                    System.out.print("\t" + key2 + " -> ");
                    System.out.println(paramList.get(key2));
                }
            }
        }
    }
}
