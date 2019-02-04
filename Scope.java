import java.net.PortUnreachableException;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class Scope {
    String parent;
    Map<String, String> types;

    public Scope () {
        types = new HashMap<String, String>();
    }

    public Scope (String value) {
        parent = value;
        types = new HashMap<String, String>();
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

    public String getParent() {
        return this.parent;
    }

    public void printAll() {
        for (String key : this.types.keySet()) {
            System.out.println(key);
            System.out.println(this.types.get(key));
        }
    }
}
