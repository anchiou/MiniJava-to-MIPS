import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClassManager {
    // map of class names to fields
    private Map<String, ArrayList<String>> record = new LinkedHashMap<>();
    // map of class names to methods
    private Map<String, Map<String, String>> vtable = new LinkedHashMap<>();

    // put class name and field maps (maps field names to offsets)
    public void putRecords(String str, Map<String, String> map) {

    }

    // put class name and method maps (maps field names to offsets)
    public void putFunction(String str, Map<String, String> map) {

    }

}
