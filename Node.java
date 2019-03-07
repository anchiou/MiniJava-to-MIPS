import cs132.vapor.ast.*;

import java.util.HashSet;
import java.util.Set;

public class Node {
    private VInstr instr; // instruction
    private Set<Node> pred = new HashSet<>(); // set of predecessor nodes (in-edges)
    private Set<Node> succ = new HashSet<>(); // set of successor nodes (out-edges)

    private Set<String> def; // set of variables the node defines
    private Set<String> use; // set of variables used by node

    public Node(VInstr instr, Set<String> def, Set<String> use) {
        this.instr = instr;
        this.def = def;
        this.use = use;
    }

    public VInstr getInstr() {
        return this.instr;
    }

    public Set<Node> getPred() {
        return new HashSet<>(this.pred);
    }

    public Set<Node> getSucc() {
        return new HashSet<>(this.succ);
    }

    public Set<String> getDefSet() {
        return new HashSet<>(this.def);
    }

    public Set<String> getUseSet() {
        return new HashSet<>(this.use);
    }

    public void addPred(Node p) {
        if (p != null) this.pred.add(p);
    }

    public void addSucc(Node s) {
        if (s != null) this.succ.add(s);
    }
}