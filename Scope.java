import java.net.PortUnreachableException;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class Scope {
    String parentScope;
    Map<String, String> types;

    public Scope () {
        this.types = new HashMap<String, String>();
    }

    public Scope (String parentScope) {
        this.parentScope = parentScope;
        this.types = new HashMap<String, String>();
    }

    public boolean contains(String id) {
        return this.types.containsKey(id);
    }

    public void putType(String id, String type) {
        // System.out.println("In PutType id: " + id + " type: " + type);
        this.types.put(id, type);
    }

    public String getType(String id) {
        return this.types.get(id);
    }

    public String getParentScope() {
        return this.parentScope;
    }

    public void printAll() {
        for (String key : this.types.keySet()) {
            System.out.print(key + " -> ");
            System.out.println(this.types.get(key));
        }
    }
}
