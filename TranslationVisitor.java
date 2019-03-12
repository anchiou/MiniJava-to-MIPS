import java.util.*;
import cs132.vapor.ast.*;

public class TranslationVisitor {

    private RegisterPool localPool = RegisterPool.CreateLocalPool();

    String indent = "";

    public Register createRegister(AllocationMap map, String s, boolean isDestination) {
        Register register = map.lookupRegister(s);
        if (register != null) {
            return register;
        }
        else {
            int offset = map.lookupStack(s);
            Register var = localPool.getRegister();
            if (!isDestination) {
                System.out.println(indent + var.toString());
            }
            return var;
        }
    }

    public void printFunc(VFunction function, Liveness liveness, AllocationMap map) {

        Map<Integer, Set<String>> labels = new HashMap<>();
        for (VCodeLabel label : function.labels) {
            labels.computeIfAbsent(label.instrIndex, k -> new LinkedHashSet<>()).add(label.ident);
        }

        int in = Math.max(function.params.length - 4, 0);
        int local = 0;
        int out = 0;

        // Output function sig
        System.out.print("func " + function.ident);
        System.out.println(" [in " + in + " out " + out + " local " + local + "]");

        // SAVE ALL CALLEE REGISTERS

        // load params into registers or local
        // Register[] registers = {
        //     Register.a0, Register.a1, Register.a2, Register.a3
        // };
        // for (int i = 0; i < function.params.length; ++i) {
        //
        // }

        // print body stuff
        for (int i = 0; i < function.body.length; ++i) {

            indent += "  ";

            // Output labels
            if (labels.containsKey(i)) {
                indent = indent.substring(0, indent.length() - 2);
                Set<String> set = labels.get(i);
                for (Iterator<String> it = set.iterator(); it.hasNext(); ) {
                    String s = it.next();
                    System.out.println(s + ":");
                }
                indent += "  ";
            }

            // Visitor for instruction printing
            function.body[i].accept(new VInstr.Visitor<RuntimeException>() {

                @Override
                public void visit(VAssign assignment) {

                    Register destination = createRegister(map, assignment.dest.toString(), true);

                    if (assignment.source instanceof VVarRef) {
                        // register stuff here
                        Register source = createRegister(map, assignment.source.toString(), false);
                        System.out.println(indent + destination.toString() + " = " + source.toString());
                        if (localPool.contains(source)){
                            localPool.releaseRegister(source);
                        }
                    } else {
                        System.out.println(indent + destination.toString() + " = " + assignment.source.toString());
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

            indent = indent.substring(0, indent.length() - 2);

        }
    }
}
