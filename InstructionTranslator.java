import java.util.*;
import cs132.vapor.ast.*;

public class InstructionTranslator {

    // Flowgraph
    public FlowGraph graph;

    // Constructor
    public InstructionTranslator() {
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
                	if (g.target instanceof VVaddr.var) {
                		use.add(g.target.toString());
                	}
                }

                // Get memread information
                @Override
                public void visit(VMemRead r) {
                	if (r.dest instanceof VVarRef) {
                		def.add(r.dest.toString());
                	}
                	if (r.source instanceof VMemRef) {
                		use.add(r.source.toString());
                	}
                }

                // Get memwrite information
                @Override
                public void visit(VMemWrite w) {
                	if (r.dest instanceof VMemRef) {
                		def.add(r.dest.toString());
                	}
                	if (r.source instanceof VOperand) {
                		use.add(r.source.toString());
                	}
                }

                // Get return information
                @Override
                public void visit(VReturn r) {
                    if (r.value instanceof VVarRef) {
                        use.add(r.value.toString());
                    }
                }

            });

            graph.addNode(instruction, def, use);

        }

        return this.graph;

    }

}
