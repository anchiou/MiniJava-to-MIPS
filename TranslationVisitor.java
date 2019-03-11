import cs132.vapor.ast.*;
import java.util.*;

public class TranslationVisitor {

    String indent = "";

    public void printFunc(VFunction function) {

        // Output function sig
        System.out.print("func " + function.ident);
        System.out.println(" [in " + function.stack.in + " out "
            + function.stack.out + " local " + function.stack.local + "]");

        for (int i = 0; i < function.body.length; ++i) {

            // Output function labels
            // for (VCodeLabel label : function.labels) {
            //     System.out.println(label.ident + ":");
            // }

            indent += "  ";

            // Visitor for instruction printing
            function.body[i].accept(new VInstr.Visitor<RuntimeException>() {

                @Override
                public void visit(VAssign assignment) {

                    if (assignment.source instanceof VVarRef) {
                        // register stuff here
                    } else {
                        System.out.println(indent + assignment.dest.toString() + " = " + assignment.source.toString());
                    }

                }

                @Override
                public void visit(VBranch branch) {

                    String statement = "";

                    // Determine branch
                    if (branch.positive) {
                        statement += "if ";
                    } else {
                        statement += "if0 ";
                    }

                    if (branch.value instanceof VVarRef) {
                        // do something with registers here (instead of branch.value its a register)
                        System.out.println(indent + statement + "REGISTER HERE" + " goto " + branch.target.toString());
                    } else {
                        System.out.println(indent + statement + branch.value.toString() + " goto " + branch.target.toString());
                    }

                }

                @Override
                public void visit(VBuiltIn builtIn) {

                    String output = builtIn.op.name + "(";

                    for (VOperand operand : builtIn.args) {
                        if (operand instanceof VVarRef) {
                            // registers
                        } else {
                            output += operand.toString();
                            output += " ";
                        }
                    }

                    output = output.replace(" ", "");
                    output += ")";

                    if (builtIn.dest != null) {
                        System.out.println(indent + builtIn.dest + " = " + output);
                    } else if (builtIn.dest == null) {
                        System.out.println(indent + output);
                    }

                }

                @Override
                public void visit(VCall call) {

                    if (call.addr instanceof VAddr.Label) {
                        System.out.println(indent + "call " + call.addr.toString());
                    }

                }

                @Override
                public void visit(VGoto go) {
                    System.out.println(indent + "goto " + go.target.toString());
                }

                @Override
                public void visit(VMemRead memRead) {

                    VMemRef.Global mem = (VMemRef.Global) memRead.source;
                    System.out.println(indent + "REGISTER HERE " + "[" + mem.base.toString() + "+" + mem.byteOffset + "]");

                }

                @Override
                public void visit(VMemWrite memWrite) {



                }

                @Override
                public void visit(VReturn ret) {

                    // Restore $S registers here and set $V registers to the return target

                }

            });

            indent = indent.substring(0, indent.length()-2);

        }

    }

}
