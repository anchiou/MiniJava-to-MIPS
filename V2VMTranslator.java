import java.util.*;
import cs132.vapor.ast.*;

public class V2VMTranslator {

    // Private data members
    private InstructionVisitor instrVisitor;
    private TranslationVisitor transVisitor;
    private FlowGraph graph;
    private List<Interval> active;
    private Set<String> spillStack;
    private Set<String> unused;
    private Map<String, Register> registerMap;

    // Constructor
    public V2VMTranslator() {
        transVisitor = new TranslationVisitor();
        instrVisitor = new InstructionVisitor();
        graph = new FlowGraph();
        spillStack = new LinkedHashSet<>();
        unused = new HashSet<>();
        registerMap = new LinkedHashMap<>();
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

        // Pass live intervals and function parameters into register allocation
        registerAlloc(new ArrayList<>(intervals.values()), function.params);

        transVisitor.printFunc(function);

        System.out.println("");

    }

    // Linear scan register allocation
    public void registerAlloc(ArrayList<Interval> intervals, VVarRef.Local[] params) {
        // active ←{}
        active = new ArrayList<Interval>();

        // sort intervals in order of increasing start point
        intervals.sort(Comparator.comparingInt(Interval::getStart));

        for (Interval i : intervals) {
            expireOldIntervals(i);
            if (active.size() == R) {
                spillAtInterval(i);
            } else {
                // register[i] ← a register removed from pool of free registers
                // add i to active, sorted by increasing end point
            }
        }
    }

    public void expireOldIntervals(Interval i) {
        // foreach interval j in active, in order of increasing end point
            // if endpoint[j] ≥ startpoint[i] then
                // return
            // remove j from active
            // add register[j] to pool of free registers
    }

    public void spillAtInterval(Interval i) {

        // Sort intervals in active by start time
        active.sort(Comparator.comparingInt(Interval::getStart));

        Interval spilled = null;

        // determine spill
        if (!active.isEmpty()) {
            int index = active.size() - 1;
            do {
                spilled = active.get(index -= 1);
            } while (index >= 0 && unused.contains(spilled.getVar()));
            if (index < 0) {
                spilled = null;
            } else {
                spilled = spilled;
            }
        }

        // Add spill to function stack if interval is larger else add interval to spill stack
        if (spilled != null && spilled.getEnd() > i.getEnd()) {
            registerMap.put(i.getVar(), registerMap.get(spilled.getVar()));
            registerMap.remove(spilled.getVar());
            spillStack.add(spilled.getVar());
            active.remove(spilled);
            active.add(i);
        } else {
            spillStack.add(i.getVar());
        }

    }
}
