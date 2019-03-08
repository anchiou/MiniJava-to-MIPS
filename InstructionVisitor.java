import java.util.*;
import cs132.vapor.ast.*;

public class InstructionVisitor {

    // Flowgraph
    public FlowGraph graph;

    // Constructor
    public InstructionVisitor() {
        graph = new FlowGraph();
    }

    // Translate array of instructions
    public FlowGraph translate(VInstr[] instructions) {

        for (VInstr instruction : instructions) {

            // Sets
            Set<String> def = new HashSet<>();
            Set<String> use = new HashSet<>();

            instruction.accept(new VInstr.Visitor<RuntimeException>() {

                // Get assignment information
                @Override
                public void visit(VAssign a) {
                    def.add(a.dest.toString());
                    if (a.source instanceof VVarRef) {
                        use.add(a.source.toString());
                    }
                }

                // Get branch information
                @Override
                public void visit(VBranch b) {
                    if (b.value instanceof VVarRef) {
                        use.add(b.value.toString());
                    }
                }

                // Get BuiltIn information
                @Override
                public void visit(VBuiltIn c) {
                    if (c.dest != null) {
                        def.add(c.dest.toString());
                    }
                    for (VOperand operand : c.args) {
                        if (operand instanceof VVarRef) {
                            use.add(operand.toString());
                        }
                    }
                }

                // Get Call information
                @Override
                public void visit(VCall c) {
                    def.add(c.dest.toString());
                    if (c.addr instanceof VAddr.Var) {
                        use.add(c.addr.toString());
                    }
                    for (VOperand operand : c.args) {
                        if (operand instanceof VVarRef) {
                            use.add(operand.toString());
                        }
                    }
                }

                // Get goto information
               @Override
                public void visit(VGoto g) {
                	if (g.target instanceof VAddr.Var) {
                		use.add(g.target.toString());
                	}
                }

                // Get memread information
                @Override
                public void visit(VMemRead r) {
                	def.add(r.dest.toString());
                    VMemRef.Global globalReference = (VMemRef.Global)r.source;
                    use.add(globalReference.base.toString());
                }

                // Get memwrite information
                @Override
                public void visit(VMemWrite w) {
                    VMemRef.Global globalReference = (VMemRef.Global)w.dest;
                    use.add(globalReference.base.toString());
                    if (w.source instanceof VVarRef) {
                        use.add(w.source.toString());
                    }
                }

                // Get return information
                @Override
                public void visit(VReturn r) {
                    if (r.value != null && r.value instanceof VVarRef) {
                        use.add(r.value.toString());
                    }
                }

            });

            // Add new node with current instruction, def, and node to flowgraph
            graph.addNode(instruction, def, use);

        }

        // Return the created flowgraph
        return this.graph;

    }

}
