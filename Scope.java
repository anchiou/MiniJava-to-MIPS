import java.net.PortUnreachableException;
import java.util.Map;
import java.util.HashMap;

public class Scope {
    String test;
    String parent;
    Map<String, String> types = new HashMap<String, String>();

    public Scope () {}

    public Scope (String value) {
        test = value;
    }

    public void putType(String name, String type) {
        System.out.println("In PutType name: " + name + " type: " + type);
        this.types.put(name, type);
    }

    public String getType(String name) {
        return this.types.get(name);
    }
}