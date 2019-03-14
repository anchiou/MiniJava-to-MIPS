import java.util.*;
import cs132.vapor.ast.*;

public class VM2MTranslator {
    // Output beginning of MIPS text section
    public void beginText() {
        System.out.println(".text\n");
        System.out.println("  jal Main");
        System.out.println("  li $v0 10");
        System.out.println("  syscall\n");
    }

    // Output end of MIPS text section
    // Includes print, error, heapAlloc functions and const variables
    public void endText() {
        System.out.println("_print:"); // _print
        System.out.println("  li $v0 1   # syscall: print integer");
        System.out.println("  syscall");
        System.out.println("  la $a0 _newline");
        System.out.println("  li $v0 4   # syscall: print string");
        System.out.println("  syscall");
        System.out.println("  jr $ra\n");

        System.out.println("_error:"); // _error
        System.out.println("  li $v0 4   # syscall: print string");
        System.out.println("  syscall");
        System.out.println("  li $v0 10  # syscall: exit");
        System.out.println("  syscall\n");

        System.out.println("_heapAlloc:"); // _heapAlloc
        System.out.println("  li $v0 9   # syscall: sbrk");
        System.out.println("  syscall");
        System.out.println("  jr $ra\n");

        // const data variables
        System.out.println(".data");
        System.out.println(".align 0");
        System.out.println("_newline: .asciiz \"\\n\"");
        System.out.println("_str0: .asciiz \"null pointer\\n\"");
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