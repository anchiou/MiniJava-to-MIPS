import cs132.vapor.ast.*;

public class InstructionTranslator {

    // Flowgraph
    private FlowGraph graph;

    // Constructor
    public InstructionTranslator() {
        graph = new FlowGraph();
    }

    // Translate array of instructions
    public FlowGraph translate(VInstr[] instructions) {

        for (VInstr instruction : instructions) {

            // Sets
            public Set<String> def = new HashSet<>();
            public Set<String> use = new HashSet<>();

            instruction.accept(new VInstr.Visitor<RuntimeException>() {

                // Get assignment information
                @Override
                public void visit(VAssign a) {

                }

                // Get branch information
                @Override
                public void visit(VBranch b) {

                }

                // Get BuiltIn information
                @Override
                public void visit(VBuiltIn c) {

                }

                // Get Call information
                @Override
                public void visit(VCall c) {

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

    return graph;

}
