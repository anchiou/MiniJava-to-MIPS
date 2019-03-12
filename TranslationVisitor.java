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

    public void printFunc(FlowGraph graph, VFunction function, Liveness liveness, AllocationMap map) {

        Map<Integer, Set<String>> labels = new HashMap<>();
        for (VCodeLabel label : function.labels) {
            labels.computeIfAbsent(label.instrIndex, k -> new LinkedHashSet<>()).add(label.ident);
        }

        int in = Math.max(function.params.length - 4, 0);
        int local = 0;
        int out = map.stackSize();

        for (Node node : graph.getNodeList()) {
            VInstr instr = node.getInstr();
            if (instr instanceof VCall) {
                VCall call = (VCall) instr;
                out = Math.max(call.args.length - 4, out);

                // out[n] - def[n]
                Set<String> liveOut = liveness.out.get(node);
                liveOut.removeAll(node.getDefSet()); // remove all variables in out[n] that are in def[n]

                // Save $t before function call, saved on high address of local stack
                int saves = (int) liveOut.stream().map(map::lookupRegister).filter(o -> o != null
                        && o.isCallerSaved()).distinct().count();
                local = Math.max(local, map.stackSize() + saves);
            }
        }

        // Output function sig
        System.out.print("func " + function.ident);
        System.out.println(" [in " + in + " out " + out + " local " + local + "]");

        // SAVE ALL CALLEE REGISTERS
        List<Register> callee = map.usedCalleeRegister();

        // Save all $s registers
        for (int i = 0; i < callee.size(); i++) {
            String output = "local[" + Integer.toString(i) + "]";
            System.out.println(output + " = " + callee.get(i).toString());
        }

        // load params into registers or (local) stack
        Register[] registers = {
            Register.a0, Register.a1, Register.a2, Register.a3
        };

        for (int i = 0; i < function.params.length; ++i) {
            Register dest = map.lookupRegister(function.params[i].ident);
            if (dest != null) {
                if (i < 4) { // parameters passed by registers
                    System.out.println(indent + dest.toString() + " = " + registers[i].toString());
                } else { // parameters passed by (in) stack
                    System.out.println(indent + dest.toString() + " = in[" + Integer.toString(i - 4) + "]");
                }
            } else {
                int offset = map.lookupStack(function.params[i].ident);
                if (offset != -1) { // Some parameters are never used
                    // Put remaining parameters into (local) stack
                    Register load = localPool.getRegister();
                    String output = "";
                    if (offset > 0) {
                        output = "[" + load.toString() + "+" + Integer.toString(offset) + "]";
                    } else {
                        output = "[" + load.toString() + "]";
                    }
                    System.out.println(indent + load.toString() + " = in[" + Integer.toString(i - 4) + "]");
                    System.out.println(indent + output + " = " + load.toString());
                    localPool.releaseRegister(load);
                }
            }
        }

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

                    if (localPool.contains(destination)){
                        localPool.releaseRegister(destination);
                    }

                }

                @Override
                public void visit(VBranch branch) {

                    String statement = "";

                    if (branch.value instanceof VVarRef) {
                        // do something with registers here (instead of branch.value its a register)
                        Register source = createRegister(map, branch.value.toString(), false);
                        statement = source.toString();
                    }

                    String out = "  ";
                    if (branch.positive) {
                        out += "if ";
                    } else {
                        out += "if0 ";
                    }

                    System.out.println(out + statement + " goto " + branch.target.toString());

                }

                @Override
                public void visit(VBuiltIn builtIn) {

                    String output = builtIn.op.name + "(";
                    List<Register> registers = new ArrayList<>();
                    for (VOperand operand : builtIn.args) {
                        if (operand instanceof VVarRef) {
                            Register source = createRegister(map, operand.toString(), false);
                            registers.add(source);
                            output += source.toString();
                            output += " ";
                        } else {
                            output += operand.toString();
                            output += " ";
                        }
                    }

                    output = output.substring(0, output.length() - 1);
                    output += ")";

                    for (Register reg : registers) {
                        if (localPool.contains(reg)) {
                            localPool.releaseRegister(reg);
                        }
                    }

                    if (builtIn.dest == null) {
                        System.out.println(indent + output);
                    } else if (builtIn.dest != null) {
                        Register destination = createRegister(map, builtIn.dest.toString(), true);
                        System.out.println(indent + destination.toString() + " = " + output);
                        if (localPool.contains(destination)) {
                            localPool.releaseRegister(destination);
                        }
                    }

                }

                @Override
                public void visit(VCall call) {
                    Register[] registers = {
                        Register.a0, Register.a1, Register.a2, Register.a3
                    };

                    if (call.addr instanceof VAddr.Label) {
                        System.out.println(indent + "call " + call.addr.toString());
                    } else {
                        Register reg = createRegister(map, call.addr.toString(), false);
                        System.out.println(indent + "call " + reg.toString());
                        if (localPool.contains(reg)) {
                            localPool.releaseRegister(reg);
                        }
                    }

                }

                @Override
                public void visit(VGoto go) {
                    System.out.println(indent + "goto " + go.target.toString());
                }

                @Override
                public void visit(VMemRead memRead) {

                    Register dst = loadVar(map, memRead.dest.toString(), true);

                    VMemRef.Global mem = (VMemRef.Global) memRead.source;
                    Register src = loadVar(map, mem.base.toString(), false);

                    int offset = mem.byteOffset;
                    String output = "";
                    if (offset > 0) {
                        output = "[" + src.toString() + "+" + Integer.toString(offset) + "]";
                    } else {
                        output = "[" + src.toString() + "]";
                    }

                    System.out.println(indent + dst.toString() + " = " + output);
                    releaseLocalReg(src);

                    writeVar(dst, map, memRead.dest.toString());
                    releaseLocalReg(dst);

                }

                @Override
                public void visit(VMemWrite memWrite) {
                    VMemRef.Global ref = (VMemRef.Global) memWrite.dest;
                    Register base = loadVar(map, ref.base.toString(), false);
                    String output = "";
                    int offset = ref.byteOffset;
                    if (offset > 0) {
                        output = "[" + base.toString() + "+" + Integer.toString(offset) + "]";
                    } else {
                        output = "[" + base.toString() + "]";
                    }

                    if (memWrite.source instanceof VVarRef) {
                        Register src = loadVar(map, memWrite.source.toString(), false);
                        System.out.println(indent + output + " = " + src.toString());
                        releaseLocalReg(src);
                    } else {
                        System.out.println(indent + output + " = " + memWrite.source.toString());
                    }

                    releaseLocalReg(base);
                }

                @Override
                public void visit(VReturn ret) {
                    if (ret.value != null) {
                        if (ret.value instanceof VVarRef) {
                            Register src = loadVar(map, ret.value.toString(), false);
                            if (src != Register.v0)
                                System.out.println(indent + Register.v0.toString() + " = " + src.toString());
                            releaseLocalReg(src);
                        } else {
                            System.out.println(indent + Register.v0.toString() + " = " + ret.value.toString());
                        }
                    }

                    // Restore $s registers
                    String output;
                    for (int i = 0; i < callee.size(); i++) {
                        output = "local[" + Integer.toString(i) + "]";
                        System.out.println(indent + callee.get(i).toString() + " = " + output);
                    }

                    System.out.println(indent + "ret");
                    // TODO: set $V registers to the return target
                }

            });

            indent = indent.substring(0, indent.length() - 2);

        }
    }

    private Register loadVar(AllocationMap map, String var, boolean dst) {
        Register register = map.lookupRegister(var);
        if (register != null) { // variable in register
            return register;
        } else { // variable on (local) stack
            int offset = map.lookupStack(var);
            String output = "local[" + Integer.toString(offset) + "]";
            Register load = localPool.getRegister();
            if (!dst) // for destinations, only a register
                System.out.println(load.toString() + " = " + output);
            return load;
        }
    }

    private void writeVar(Register register, AllocationMap map, String var) {
        int offset = map.lookupStack(var);
        String output = "local[" + Integer.toString(offset) + "]";
        if (offset != -1) {
            System.out.println(output + " = " + register.toString());
        }
    }

    private void releaseLocalReg(Register register) {
        if (localPool.contains(register)) {
            localPool.releaseRegister(register);
        }
    }
}
