import java.util.*;
import cs132.vapor.ast.*;

public class FlowGraph {

    // Private data members
    private Map<Node, List<Node>> adjNodes; // edges
    private List<Node> nodeList; // nodes

    // Constructor
    public FlowGraph() {
        this.adjNodes = new HashMap<>();
        this.nodeList = new ArrayList<>();
    }

    // create new node for graph
    public void addNode(VInstr instr, Set<String> def, Set<String> use) {
        // adjNodes.putIfAbsent(new Node(instr, def, use));
        this.nodeList.add(new Node(instr, def, use));
    }

    // Add edge
    public void addEdge(Node from, Node to) {
        if (from != null && to != null) {
            from.addSucc(to);
        }
    }

    // Get node
    public Node getNode() {
        // TODO: return
        return null;
    }

    // Calculate the liveness
    public void calcLiveness() { // calculate liveness

    }
}
