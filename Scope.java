import java.net.PortUnreachableException;
import java.util.Map;

public class Scope {
    String test;
    Map<String, String> types;

    public Scope (String value) {
        test = value;
    }

    /**
     * @return the test
     */
    public String getTest() {
        return test;
    }

    /**
     * @param test the test to set
     */
    public void setTest(String test) {
        this.test = test;
    }
}