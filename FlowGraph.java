import java.util.Map;

import cs132.vapor.ast.*;

public class FlowGraph {
    private Map<Node, List<Node>> adjNodes; // edges

    public FlowGraph() {
        // TODO: graph constructor
    }

    // create new node for graph
    public void addNode(VInstr instr, Set<String> def, Set<String> use) {
        adjNodes.putIfAbsent(new Node(instr, def, use));x
    }

    public void addEdge(Node from, Node to) {
        if (from != null && to != null) {
            from.addSucc(to);
        }
    }

    public void calcLiveness() { // calculate liveness

    }
}