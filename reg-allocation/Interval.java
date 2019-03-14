public class Interval {
    private final String variable;
    private int start;
    private int end;

    public Interval(String var, int start, int end) {
        this.variable = var;
        this.start = start;
        this.end = end;
    }

    public int getEnd() {
        return this.end;
    }

    public int getStart() {
        return this.start;
    }

    public String getVar() {
        return this.variable;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}