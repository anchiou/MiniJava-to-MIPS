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

                }

                // Get memread information
                @Override
                public void visit(VMemRead r) {

                }

                // Get memwrite information
                @Override
                public void visit(VMemWrite w) {

                }

                // Get return information
                @Override
                public void visit(VReturn r) {

                }

            });

            graph.addNode(instruction, def, use);

        }

    }

    return this.graph;

}
