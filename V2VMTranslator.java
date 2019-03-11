import java.util.*;
import cs132.vapor.ast.*;

public class V2VMTranslator {

    // Private data members
    private InstructionVisitor instrVisitor;
    private TranslationVisitor transVisitor;
    private FlowGraph graph;

    // Constructor
    public V2VMTranslator() {
        transVisitor = new TranslationVisitor();
        instrVisitor = new InstructionVisitor();
        graph = new FlowGraph();
    }

    // Translator
    public void translate(VaporProgram p) {

        // Print data segments
        for (VDataSegment segment : p.dataSegments) {
            translateDataSegments(segment);
        }

        // Print functions
        for (VFunction function : p.functions) {
            translateFunction(function);
        }

    }

    // Translate data segments
    public void translateDataSegments(VDataSegment segment) {

        System.out.println("const " + segment.ident.toString());
        for (VOperand val : segment.values) {
            System.out.println("  " + val.toString());
        }
        System.out.print("\n");

    }

    // Function translator
    public void translateFunction(VFunction function) {

        // for (VVarRef.Local param : function.params) {
        //     System.out.println(param.toString());
        // }
        //
        // for (String var : function.vars) {
        //     System.out.println(var.toString());
        // }

        graph = instrVisitor.createFlowGraph(function.body);
        Liveness liveness = graph.calcLiveness();

        Map<String, Interval> intervals = new HashMap<>(); // maps variables to its live interval
        List<Set<String>> active = new ArrayList<>();

        // live intervals (used by the linear scan algorithm) calculated using the active sets
        // by top-down scan of the instructions
        for (Node node : graph.getNodeList()) {
            // active[n] = def[n] U in[n]
            Set<String> act = node.getDefSet(); // def[n]
            act.addAll(liveness.in.get(node));  // in[n]
            active.add(act);
        }

        for (int i = 0; i < active.size(); ++i) {
            for (String variable : active.get(i)) {
                if (intervals.containsKey(variable)) {
                    intervals.get(variable).setEnd(i); // update end of interval
                } else {
                    intervals.put(variable, new Interval(variable, i, i)); // create new interval
                }
            }
        }

        // TODO: pass live intervals into register allocation

        transVisitor.printFunc(function);

        System.out.println("");

    }
}
