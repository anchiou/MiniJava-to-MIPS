import java.util.*;
import cs132.vapor.ast.*;

public class V2VMTranslator {

    // Private data members
    private InstructionVisitor instrVisitor;
    private TranslationVisitor transVisitor;

    private FlowGraph graph;

    private RegisterPool pool; // pool of free registers
    private Map<String, Register> registerMap; // map of argument to register it occupies
    private List<Interval> active; // live intervals that overlap current point and have been placed in registers
    private Set<String> spillStack;
    private Set<String> unused;

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

        this.graph = instrVisitor.createFlowGraph(function.body);
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
        this.pool = RegisterPool.CreateGlobalPool();
        this.registerMap = new LinkedHashMap<>();
        this.spillStack = new LinkedHashSet<>();
        this.unused = new HashSet<>();

        // active <-{}
        this.active = new ArrayList<Interval>();

        // sort intervals in order of increasing start point
        intervals.sort(Comparator.comparingInt(Interval::getStart));

        // TODO: deal with argument passing

        for (Interval i : intervals) {
            expireOldIntervals(i);
            if (active.size() == pool.numAvailable()) {
                spillAtInterval(i);
            } else {
                registerMap.put(i.getVar(), pool.getRegister()); // register[i] <- a register removed from pool of free registers
                active.add(i); // add i to active, sorted by increasing end point
            }
        }
    }

    public void expireOldIntervals(Interval i) {
        // sort intervals in active in order of increasing end point
        active.sort(Comparator.comparingInt(Interval::getEnd));

        for (int j = 0; j < active.size(); ++j) {
            Interval old = active.get(j);
            if (old.getEnd() >= i.getStart()) { // if endpoint[j] ≥ startpoint[i]
                return;
            }
            active.remove(old); // remove old interval from active
            pool.releaseRegister(registerMap.get(old.getVar())); // add register[j] to pool of free registers

            // TODO: deal with argument passing
        }
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
