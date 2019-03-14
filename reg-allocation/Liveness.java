import java.util.Map;
import java.util.Set;

public class Liveness {
    Map<Node, Set<String>>  in; // live-in
    Map<Node, Set<String>>  out; // live-out

    Liveness(Map<Node, Set<String>>  in, Map<Node, Set<String>>  out) {
        this.in = in;
        this.out = out;
    }
}