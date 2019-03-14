import java.util.*;
import cs132.vapor.ast.*;

public class VM2MTranslator {
    // Output beginning of text segment in MIPS
    public void beginText() {
        System.out.println(".text\n");
        System.out.println("  jal Main");
        System.out.println("  li $v0 10");
        System.out.println("  syscall\n");
    }

    // Translate data segments
    public void translateDataSegments(VDataSegment[] segments) {
        System.out.println(".data\n");
        for (VDataSegment segment : segments) {
            System.out.println(segment.ident + ":");
            for (VOperand value : segment.values) {
                System.out.println("  " + value.toString());
            }
            System.out.print("\n");
        }
    }

    // Translate function
    public void translateFunction(VFunction function) {
        System.out.println(function.ident + ":");

        System.out.print("\n");
    }
}