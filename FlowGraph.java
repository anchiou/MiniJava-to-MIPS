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

    // Create new node for graph
    public void addNode(VInstr instr, Set<String> def, Set<String> use) {
        this.nodeList.add(new Node(instr, def, use));
    }

    // Add edge
    public void addEdge(Node from, Node to) {
        if (from != null && to != null && from != to
                && nodeList.contains(from) && nodeList.contains(to)) {
            adjNodes.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            to.addPred(from);
            from.addSucc(to);
        }
    }

    // Return nodes
    public List<Node> getNodeList() {
        return this.nodeList;
    }

    // Calculate liveness
    public Liveness calcLiveness() {
        Map<Node, Set<String>> in = new HashMap<>(); // maps nodes to live-in
        Map<Node, Set<String>> out = new HashMap<>(); // maps nodes to live-out

        for (Node node : this.nodeList) {
            in.put(node, new HashSet<>());
            out.put(node, new HashSet<>());
        }

        Set<String> prevIn, prevOut, newIn, newOut; // in'[n], out'[n], in[n], out[n]
        Set<String> tempDef; // temporary def[n]
        boolean isUpdated;

        do {
            isUpdated = false;
            for (Node node : this.nodeList) {
                prevIn = new HashSet<>(in.get(node));     // copy old in values
                prevOut = new HashSet<>(out.get(node));   // copy old out values

                // in[n] <-- use[n] ∪ (out[n] − def [n])
                newIn = node.getUseSet(); // all variables in use[n]
                tempDef = new HashSet<>(prevOut);
                tempDef.removeAll(node.getDefSet()); // remove all variables in out[n] that are in def[n]
                newIn.addAll(tempDef); // use[n] ∪ (out[n] − def [n])

                // out[n] <-- U_s∈succ[n] in[s]
                newOut = new HashSet<>();
                for (Node succ : node.getSucc()) {
                    newOut.addAll(in.get(succ)); // union of all in[n] for each n in succ[n]
                }

                in.put(node, newIn);
                out.put(node, newOut);

                if (!prevIn.equals(newIn) || !prevOut.equals(newOut)) {
                    isUpdated = true;
                }
            }
        } while (isUpdated);

        // for (Node key : in.keySet()) {
        //     Set<String> temp = in.get(key);
        //     for (String value : temp) {
        //         System.out.println("in: " + value);
        //     }
        // }
        // System.out.println("-----------------------------------");
        // for (Node key : out.keySet()) {
        //     Set<String> temp = out.get(key);
        //     for (String value : temp) {
        //         System.out.println("out: " + value);
        //     }
        // }

        return new Liveness(in, out);
    }
}