public class Register {
    // General use callee-saved
    public static final Register s0 = new Register("s0");
    public static final Register s1 = new Register("s1");
    public static final Register s2 = new Register("s2");
    public static final Register s3 = new Register("s3");
    public static final Register s4 = new Register("s4");
    public static final Register s5 = new Register("s5");
    public static final Register s6 = new Register("s6");
    public static final Register s7 = new Register("s7");

    // General use caller-saved
    public static final Register t0 = new Register("t0");
    public static final Register t1 = new Register("t1");
    public static final Register t2 = new Register("t2");
    public static final Register t3 = new Register("t3");
    public static final Register t4 = new Register("t4");
    public static final Register t5 = new Register("t5");
    public static final Register t6 = new Register("t6");
    public static final Register t7 = new Register("t7");
    public static final Register t8 = new Register("t8");

    // Reserved for argument passing
    public static final Register a0 = new Register("a0");
    public static final Register a1 = new Register("a1");
    public static final Register a2 = new Register("a2");
    public static final Register a3 = new Register("a3");

    // Return value / temporary loading
    public static final Register v0 = new Register("v0");
    public static final Register v1 = new Register("v1");

    private final String register;

    private Register(String register) {
        this.register = register;
    }

    public boolean isCallerSaved() {
        return this.register.startsWith("t");
    }

    public boolean isCalleeSaved() {
        return this.register.startsWith("s");
    }

<<<<<<< HEAD
    public boolean isArgumentPassing() {
        return this.register.startsWith("a");
    }

    public boolean isReturnOrLoading() {
=======
    public boolean isArgumentPass() {
        return this.register.startsWith("a");
    }

    // Is return or loading
    public boolean isRetOrLoad() {
>>>>>>> 43640ecf9812310305d8fa8f093dc0eaf621b952
        return this.register.startsWith("v");
    }

    @Override
    public String toString() {
        return "$" + this.register;
    }

    @Override
    public int hashCode() {
        return this.register.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Register)) {
            return false;
        }

        Register rhs = (Register) obj;
        return this.register.equals(rhs.register);
    }
}