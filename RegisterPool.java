import java.util.*;

public class RegisterPool {

    private Set<Register> all = new LinkedHashSet<>();
    private Set<Register> use = new HashSet<>();

    // Add all registers to "all"
    public RegisterPool(Register[] registers) {
        Collections.addAll(all, registers);
    }

    public static RegisterPool CreateGlobalPool() {
        Register[] registers = {

            // Caller saved registers
            Register.t0, Register.t1, Register.t2, Register.t3,
            Register.t4, Register.t5, Register.t6, Register.t7,
            Register.t8,

            // Callee saved registers
            Register.s0, Register.s1, Register.s2, Register.s3,
            Register.s4, Register.s5, Register.s6, Register.s7

        };
        return new RegisterPool(registers);
    }

    public static RegisterPool CreateLocalPool() {
        Register[] registers = {

            Register.v0, Register.v1,
            Register.a0, Register.a1, Register.a2, Register.a3

        };
        return new RegisterPool(registers);
    }

    public boolean addRegister(Register[] registers) {
        return Collections.addAll(all, registers);
    }

    public boolean contains(Register register) {
        return all.contains(register);
    }

    public boolean isInUse(Register register) {
        return use.contains(register);
    }

    public boolean hasFreeRegisters() {
        if (all.size() > use.size())
            return true;
        return false;
    }

    public void releaseRegister(Register register) {
        use.remove(register);
    }

    public int numAvailable() {
        return all.size() - use.size();
    }

    public Register getRegister() {
        Set<Register> open = new LinkedHashSet<>(all);
        open.removeAll(use);

        Register register = null;
        if (!open.isEmpty()) {
            register = open.iterator().next();
            use.add(register);
        }
        return register;
    }

}
