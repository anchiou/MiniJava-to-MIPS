import cs132.vapor.ast.*;

public class InstructionTranslator {

    public void translate(VInstr[] instructions) {

        for (VInstr instruction : instructions) {

            instruction.accept(new VInstr.Visitor<RuntimeException>() {

                @Override
                public void visit(VAssign a) {

                }

                @Override
                public void visit(VBranch b) {

                }

                @Override
                public void visit(VBuiltIn c) {

                }

                @Override
                public void visit(VCall c) {

                }

                @Override
                public void visit(VGoto g) {

                }

                @Override
                public void visit(VMemRead r) {

                }

                @Override
                public void visit(VMemWrite w) {

                }

                @Override
                public void visit(VReturn r) {

                }

            });

        }

    }

}
