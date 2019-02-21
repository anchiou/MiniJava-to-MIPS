import syntaxtree.*;
import visitor.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class TranslatorVisitor extends GJDepthFirst<String, TranslationHelper> {
    Vector<String> globalVector = new Vector<String>();
    ArrayList<String> expressionList;
    ArrayList<String> parameterList;
    String currScope = "scope0";
    int scopeCount = 0;

    String indent = "";
    int tempCount = 0;
    int labelCount = 1;
    int endCount = 1;
    int nullCount = 1;
    int whileCount = 1;
    int whileEndCount = 1;
    boolean isAssignment = false;
    boolean arithExpr = false;
    boolean nestedArtih = false;
    boolean isArrayAlloc = false;

    /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
    public String visit(Goal n, TranslationHelper helper) {
        String _ret = null;
        Map<String, Map<String, String>> vTables = helper.classList.getAllVTables();
        for (String key : vTables.keySet()) {
            System.out.println("const vmt_" + key);
            helper.classList.printMethods(key);
        }

        n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        n.f2.accept(this, helper);
        if (isArrayAlloc) {
            System.out.println("func ArrayAlloc(size)");
            indent += "  ";
            System.out.println(indent + "bytes = MulS(size 4)");
            System.out.println(indent + "bytes = Add(bytes 4)");
            System.out.println(indent + "v = HeapAllocZ(bytes)");
            System.out.println(indent + "[v] = size");
            System.out.println(indent + "ret v\n");
        }
        return _ret;
    }

    /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> "public"
    * f4 -> "static"
    * f5 -> "void"
    * f6 -> "main"
    * f7 -> "("
    * f8 -> "String"
    * f9 -> "["
    * f10 -> "]"
    * f11 -> Identifier()
    * f12 -> ")"
    * f13 -> "{"
    * f14 -> ( VarDeclaration() )*
    * f15 -> ( Statement() )*
    * f16 -> "}"
    * f17 -> "}"
    */
    public String visit(MainClass n, TranslationHelper helper) { // Always scope0
        String _ret=null;

        System.out.println("func Main()");
        indent += "  ";

        n.f0.accept(this, helper);
        String mainClass = n.f1.accept(this, helper);
        n.f2.accept(this, helper);
        n.f3.accept(this, helper);
        n.f4.accept(this, helper);
        n.f5.accept(this, helper);
        n.f6.accept(this, helper);
        n.f7.accept(this, helper);
        n.f8.accept(this, helper);
        n.f9.accept(this, helper);
        n.f10.accept(this, helper);
        n.f11.accept(this, helper);
        n.f12.accept(this, helper);
        n.f13.accept(this, helper);
        n.f14.accept(this, helper);
        n.f15.accept(this, helper);

        --scopeCount;
        this.currScope = "scope" + scopeCount; // update scope

        n.f16.accept(this, helper);
        n.f17.accept(this, helper);

        System.out.println(indent + "ret\n");
        indent = indent.substring(0, indent.length() - 2);

        return _ret;
    }

    /*
    * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
    public String visit(TypeDeclaration n, TranslationHelper helper) {
        String _ret=null;
        n.f0.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> ( VarDeclaration() )*
     * f4 -> ( MethodDeclaration() )*
     * f5 -> "}"
     */
    public String visit(ClassDeclaration n, TranslationHelper helper) {
        ++scopeCount;
        this.currScope = "scope" + scopeCount; // update scope
        // System.out.println("ClassDec: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret=null;
        n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        n.f2.accept(this, helper);
        n.f3.accept(this, helper);
        n.f4.accept(this, helper);
        n.f5.accept(this, helper);
        return _ret;
    }

    /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "extends"
    * f3 -> Identifier()
    * f4 -> "{"
    * f5 -> ( VarDeclaration() )*
    * f6 -> ( MethodDeclaration() )*
    * f7 -> "}"
    */
    public String visit(ClassExtendsDeclaration n, TranslationHelper helper) {
        ++scopeCount;
        this.currScope = "scope" + scopeCount; // update scope
        // System.out.println("ClassExtendsDec: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret=null;
        n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        n.f2.accept(this, helper);
        n.f3.accept(this, helper);
        n.f4.accept(this, helper);
        n.f5.accept(this, helper);
        n.f6.accept(this, helper);
        n.f7.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     * f2 -> ";"
     */
    public String visit(VarDeclaration n, TranslationHelper helper) {
        // System.out.println("VarDec: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

       String _ret=null;
       String type = n.f0.accept(this, helper);
       String id = n.f1.accept(this, helper);
       n.f2.accept(this, helper);
       return _ret;
    }

    /**
    * f0 -> "public"
    * f1 -> Type()
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( FormalParameterList() )?
    * f5 -> ")"
    * f6 -> "{"
    * f7 -> ( VarDeclaration() )*
    * f8 -> ( Statement() )*
    * f9 -> "return"
    * f10 -> Expression()
    * f11 -> ";"
    * f12 -> "}"
    */
    public String visit(MethodDeclaration n, TranslationHelper helper) {
        // System.out.println("MethDec: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());
        ++scopeCount;
        this.currScope = "scope" + scopeCount; // update scope

        tempCount = 0;
        String _ret= "MethodDeclaration";
        n.f0.accept(this, helper);
        n.f1.accept(this, helper);

        String className = helper.symbolTable.get(this.currScope).getClassName();
        String id = n.f2.accept(this, helper);
        String method = className + "." + id;
        n.f3.accept(this, helper);

        this.parameterList = new ArrayList<String>();
        n.f4.accept(this, helper);
        System.out.print("func " + method + "(this");
        for (int i = 0; i < this.parameterList.size(); ++i) {
            System.out.print(" " + this.parameterList.get(i));
        }
        System.out.println(")");

        indent += "  ";

        n.f5.accept(this, helper);
        n.f6.accept(this, helper);
        n.f7.accept(this, helper);
        n.f8.accept(this, helper);
        n.f9.accept(this, helper);
        String returnId = n.f10.accept(this, helper);
        n.f11.accept(this, helper);
        n.f12.accept(this, helper);
	    System.out.println(indent + "ret " + returnId + "\n");
        indent = "";
        return _ret;
    }

    /**
     * f0 -> FormalParameter()
     * f1 -> ( FormalParameterRest() )*
     */
    public String visit(FormalParameterList n, TranslationHelper helper) {
        // System.out.println("FormalParamList: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());
        String _ret = null;
        String parameter = n.f0.accept(this, helper);
        this.parameterList.add(parameter);
        n.f1.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     */
    public String visit(FormalParameter n, TranslationHelper helper) { // Within method scope
        // System.out.println("FormalParam: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        n.f0.accept(this, helper);
        String _ret = n.f1.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> ","
     * f1 -> FormalParameter()
     */
    public String visit(FormalParameterRest n, TranslationHelper helper) {
        String _ret = null;
        n.f0.accept(this, helper);
        String parameter = n.f1.accept(this, helper);
        this.parameterList.add(parameter);
        return _ret;
    }

    /**
     * f0 -> ArrayType()
     *       | BooleanType()
     *       | IntegerType()
     *       | Identifier()
     */
    public String visit(Type n, TranslationHelper helper) {
        // System.out.println("Type: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret = n.f0.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> "int"
     * f1 -> "["
     * f2 -> "]"
     */
    public String visit(ArrayType n, TranslationHelper helper) {
        // System.out.println("ArrayType: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret=null;
        n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        n.f2.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> "boolean"
     */
    public String visit(BooleanType n, TranslationHelper helper) {
        // System.out.println("BooleanType: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret=null;
        n.f0.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> "int"
     */
    public String visit(IntegerType n, TranslationHelper helper) {
        // System.out.println("IntType: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret=null;
        n.f0.accept(this, helper);
        return _ret;
    }

    /**
    * f0 -> Block()
    *       | AssignmentStatement()
    *       | ArrayAssignmentStatement()
    *       | IfStatement()
    *       | WhileStatement()
    *       | PrintStatement()
    */
    public String visit(Statement n, TranslationHelper helper) {
        // System.out.println("Statement: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret=null;
        n.f0.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> "{"
     * f1 -> ( Statement() )*
     * f2 -> "}"
     */
    public String visit(Block n, TranslationHelper helper) {
        // System.out.println("Block: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret=null;
        n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        n.f2.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "="
     * f2 -> Expression()
     * f3 -> ";"
     */
    public String visit(AssignmentStatement n, TranslationHelper helper) {
        // System.out.println("----AssnStmt: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        this.isAssignment = true;
        String _ret=null;
        String id = n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        String exprResult = n.f2.accept(this, helper);
        System.out.println(indent + id + " = " + exprResult);
        if (exprResult.matches("t.(.*)")) {
            System.out.println(indent + "if t." + tempCount + " goto :null" + nullCount);
            System.out.println(indent + "  Error" + "(" + "\"null pointer\"" + ")");
            System.out.println(indent + "null" + nullCount + ":");
            ++nullCount;
            ++tempCount;
        }
        n.f3.accept(this, helper);
        this.isAssignment = false;
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "["
     * f2 -> Expression()
     * f3 -> "]"
     * f4 -> "="
     * f5 -> Expression()
     * f6 -> ";"
     */
public String visit(ArrayAssignmentStatement n, TranslationHelper helper) {
        // System.out.println("ArrayAssnStmt: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret=null;
        String id = n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        String first = n.f2.accept(this, helper);
        n.f3.accept(this, helper);
        n.f4.accept(this, helper);
        String second = n.f5.accept(this, helper);
        n.f6.accept(this, helper);
        return _ret;
    }

    /**
    * f0 -> "if"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    * f5 -> "else"
    * f6 -> Statement()
    */
    public String visit(IfStatement n, TranslationHelper helper) {
        // System.out.println("IfStmt: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret= "IfStatement";
        n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        String Exp = n.f2.accept(this, helper);
        System.out.println(indent + "if0 t." + tempCount + " goto :if" + labelCount + "_else");
        indent += "  ";
	    tempCount++;
        n.f3.accept(this, helper);
        n.f4.accept(this, helper);
        // First block
        System.out.println(indent + "goto :if" + endCount + "_end");
        n.f5.accept(this, helper);
        indent = indent.substring(0, indent.length() - 2);
        System.out.println(indent + "if" + labelCount + "_else:");
        indent += "  ";
        n.f6.accept(this, helper);
        // second block
        indent = indent.substring(0, indent.length() - 2);
        System.out.println(indent + "if" + endCount + "_end:");
        labelCount++;
        return _ret;
    }

    /**
     * f0 -> "while"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> Statement()
     */
    public String visit(WhileStatement n, TranslationHelper helper) {
        // System.out.println("WhileStmt: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

	    String _ret= "WhileStatement";
        n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        System.out.println(indent + "while" + whileCount + "_top:");
        String Exp = n.f2.accept(this, helper);
        System.out.println(indent + "if0 t." + tempCount + " goto :while" + whileEndCount + "_end");
        ++tempCount;
        // System.out.println("-------------------HELLO " + Exp);
        indent += "  ";
        n.f3.accept(this, helper);
        n.f4.accept(this, helper);
        System.out.println(indent + "goto :while" + whileCount + "_top");
        ++whileCount;
        indent = indent.substring(0, indent.length()-2);
        System.out.println(indent + "while" + whileEndCount + "_end:");
        ++whileEndCount;
        return _ret;
    }

    /**
     * f0 -> "System.out.println"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> ";"
     */
    public String visit(PrintStatement n, TranslationHelper helper) {
        // System.out.println("PrintStmt: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret=null;
        n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        String e = n.f2.accept(this, helper);
        n.f3.accept(this, helper);
        n.f4.accept(this, helper);
        System.out.println(indent + "PrintIntS(" + e + ")");
        return _ret;
    }

    /**
     * f0 -> AndExpression()
     *       | CompareExpression()
     *       | PlusExpression()
     *       | MinusExpression()
     *       | TimesExpression()
     *       | ArrayLookup()
     *       | ArrayLength()
     *       | MessageSend()
     *       | PrimaryExpression()
     */
    public String visit(Expression n, TranslationHelper helper) {
        // System.out.println("----Expression: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret = n.f0.accept(this, helper);
        // System.out.println("--------------Expr: " +_ret);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "&&"
     * f2 -> PrimaryExpression()
     */
    public String visit(AndExpression n, TranslationHelper helper) {
        // System.out.println("AndExpression: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());
        String _ret="boolean";
        String first = n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        String second = n.f2.accept(this, helper);
        System.out.println(indent + "t." + tempCount + " = And(" + first + " " + second + ")");
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "<"
     * f2 -> PrimaryExpression()
     */
    public String visit(CompareExpression n, TranslationHelper helper) {
        // System.out.println("CompareExpr: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());
        String _ret="boolean";
        String first = n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        String second = n.f2.accept(this, helper);
        String className = helper.symbolTable.get(currScope).getClassName();
        int fieldOffset = helper.classList.getFieldOffset(className, second);
        System.out.println(indent + "t." + tempCount + " = LtS(" + first + " " + second + ")");
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "+"
     * f2 -> PrimaryExpression()
     */
    public String visit(PlusExpression n, TranslationHelper helper) {
        // System.out.println("PlusExpr: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        if (!this.arithExpr) {
            this.arithExpr = true;
        } else {
            this.nestedArtih = true;
        }
        String _ret = null;
        String first = n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        String second = n.f2.accept(this, helper);
        if (!this.isAssignment || this.nestedArtih) {
            _ret = "t." + tempCount;
            System.out.println(indent + "t." + tempCount + " = Add(" + first + " " + second + ")");
            if (this.nestedArtih) {
                this.nestedArtih = false;
                ++tempCount;
            } else {
                this.arithExpr = false;
            }
        } else {
            _ret = "Add(" + first + " " + second + ")";
        }
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "-"
     * f2 -> PrimaryExpression()
     */
    public String visit(MinusExpression n, TranslationHelper helper) {
        // System.out.println("MinusExpression: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        if (!this.arithExpr) {
            this.arithExpr = true;
        } else {
            this.nestedArtih = true;
        }
        String _ret = null;
        String first = n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        String second = n.f2.accept(this, helper);
	    if (!this.isAssignment || this.nestedArtih) {
            _ret = "t." + tempCount;
            System.out.println(indent + "t." + tempCount + " = Sub(" + first + " " + second + ")");
            if (this.nestedArtih) {
                this.nestedArtih = false;
                ++tempCount;
            } else {
                this.arithExpr = false;
            }
        } else {
            _ret = "Sub(" + first + " " + second + ")";
        }
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "*"
     * f2 -> PrimaryExpression()
     */
    public String visit(TimesExpression n, TranslationHelper helper) {
        // System.out.println("TimesExpression: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        if (!this.arithExpr) {
            this.arithExpr = true;
        } else {
            this.nestedArtih = true;
        }

        String _ret = null;
        String first = n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        String second = n.f2.accept(this, helper);

        if (!this.isAssignment || this.nestedArtih) {
            _ret = "t." + tempCount;
            System.out.println(indent + "t." + tempCount + " = MulS(" + first + " " + second + ")");
            if (this.nestedArtih) {
                this.nestedArtih = false;
                ++tempCount;
            } else {
                this.arithExpr = false;
            }
        } else {
            _ret = "MulS(" + first + " " + second + ")";
        }
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "["
     * f2 -> PrimaryExpression()
     * f3 -> "]"
     */
    public String visit(ArrayLookup n, TranslationHelper helper) {
        // System.out.println("ArrayLookup: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());
        String _ret="int";
        String arr = n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        String value = n.f2.accept(this, helper);
        n.f3.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> "length"
     */
    public String visit(ArrayLength n, TranslationHelper helper) {
        // System.out.println("ArrayLength: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret="int";
        String first = n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        n.f2.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( ExpressionList() )?
     * f5 -> ")"
     */
    public String visit(MessageSend n, TranslationHelper helper) {
        ++scopeCount;
        this.currScope = "scope" + scopeCount; // update scope
        // System.out.println("----MessageSend: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String name = n.f0.accept(this, helper);

        n.f1.accept(this, helper);

        // System.out.println("---------------" + helper.symbolTable.get(currScope).getClassName());
        String className = helper.symbolTable.get(currScope).getClassName();
        String methodName = n.f2.accept(this, helper);
        int methodOffset = helper.classList.getMethodOffset(className, methodName);

        String callTemp = "t." + tempCount; // temporary variable that holds method access
        System.out.println(indent + "t." + tempCount + " = [" + name + "]");
        System.out.println(indent + "t." + tempCount + " = [t." + tempCount + "+" + methodOffset + "]");
        tempCount++;

        n.f3.accept(this, helper);

        this.expressionList = new ArrayList<String>();
        n.f4.accept(this, helper);

        String _ret = "t." + tempCount;
        String callString = "call " + callTemp + "(" + name;
        for (int i = 0; i < this.expressionList.size(); ++i) {
            callString += " " + this.expressionList.get(i);
        }
        callString += ")";

        if (this.isAssignment && !this.arithExpr) {
            _ret = callString;
        } else {
            System.out.println(indent + "t." + tempCount + " = " + callString);
            tempCount++;
        }

        n.f5.accept(this, helper);
        --scopeCount;
        this.currScope = "scope" + scopeCount; // update scope
        return _ret;
    }

    /**
     * f0 -> Expression()
     * f1 -> ( ExpressionRest() )*
     */
    public String visit(ExpressionList n, TranslationHelper helper) {
        // System.out.println("ExprList: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());
        String _ret = null;
        String expr = n.f0.accept(this, helper);
        this.expressionList.add(expr);
        n.f1.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> ","
     * f1 -> Expression()
     */
    public String visit(ExpressionRest n, TranslationHelper helper) {
        // System.out.println("ExprRest: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret = null;
        n.f0.accept(this, helper);
        String expr = n.f1.accept(this, helper);
        this.expressionList.add(expr);
        return _ret;
    }

    /**
     * f0 -> IntegerLiteral()
     *       | TrueLiteral()
     *       | FalseLiteral()
     *       | Identifier()
     *       | ThisExpression()
     *       | ArrayAllocationExpression()
     *       | AllocationExpression()
     *       | NotExpression()
     *       | BracketExpression()
     */
    public String visit(PrimaryExpression n, TranslationHelper helper) {
        // System.out.println("-----PrimaryExpr: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String f0 = n.f0.accept(this, helper);
        String _ret = f0;

        if (!f0.matches("([0-9])+|this|Not|t.(.*)") && f0 != null) {
            if (this.parameterList != null) {
                if (this.parameterList.contains(f0)) {
                    _ret = f0;
                } else {
                    String className = helper.symbolTable.get(currScope).getClassName();
                    int fieldOffset = helper.classList.getFieldOffset(className, f0);
                    if (fieldOffset > 0) {
                        System.out.println(indent + "t." + tempCount + " = [this+" + fieldOffset + "]");
                        _ret = "t." + tempCount;
                        ++tempCount;
                    }
                }
            }
        } else {
            _ret = f0;
        }

        return _ret;
    }

    /**
     * f0 -> <INTEGER_LITERAL>
     */
    public String visit(IntegerLiteral n, TranslationHelper helper) {
        // System.out.println("IntLiteral: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());
        String _ret = n.f0.toString();
        n.f0.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> "true"
     */
    public String visit(TrueLiteral n, TranslationHelper helper) {
        // System.out.println("TrueLit: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret="1";
        n.f0.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> "false"
     */
    public String visit(FalseLiteral n, TranslationHelper helper) {
        // System.out.println("FalseLit: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret="0";
        n.f0.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> <IDENTIFIER>
     */
    public String visit(Identifier n, TranslationHelper helper) {
        // System.out.println("----Identifier: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret = n.f0.toString();
        n.f0.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> "this"
     */
    public String visit(ThisExpression n, TranslationHelper helper) {
        // System.out.println("----ThisExpr: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret = "this";
        n.f0.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> "new"
     * f1 -> "int"
     * f2 -> "["
     * f3 -> Expression()
     * f4 -> "]"
     */
    public String visit(ArrayAllocationExpression n, TranslationHelper helper) {
        // System.out.println("ArrayAllocExpr: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        n.f2.accept(this, helper);
        String expr = n.f3.accept(this, helper);
        System.out.println(indent + "t." + tempCount + " = call :AllocArray(" + expr + ")");
        n.f4.accept(this, helper);
        String _ret="t." + tempCount;
        ++tempCount;
        isArrayAlloc = true;
        return _ret;
    }

    /**
     * f0 -> "new"
     * f1 -> Identifier()
     * f2 -> "("
     * f3 -> ")"
     */
    public String visit(AllocationExpression n, TranslationHelper helper) {
        // System.out.println("AllocExpr: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret=null;
        n.f0.accept(this, helper);

        String id = n.f1.accept(this, helper);
        int classSize = helper.classList.getClassSize(id); // get size of class

        System.out.println(indent + "t." + tempCount + " = HeapAllocZ(" + classSize + ")");
        System.out.println(indent + "[t." + tempCount + "] = :vmt_" + id);
        _ret = "t." + tempCount;

        if (!isAssignment) {
            System.out.println(indent + "if t." + tempCount + " goto :null" + nullCount);
            System.out.println(indent + "  Error" + "(" + "\"null pointer\"" + ")");
            System.out.println(indent + "null" + nullCount + ":");
            ++nullCount;
            ++tempCount;
        }

        // System.out.println(indent + "t." + tempCount + " = [t." + (tempCount-1) + "]");
        // System.out.println(indent + "t." + tempCount + " = [t." + tempCount + "+0]");

        n.f2.accept(this, helper);
        n.f3.accept(this, helper);
        return _ret;
    }

    /**
     * f0 -> "!"
     * f1 -> Expression()
     */
    public String visit(NotExpression n, TranslationHelper helper) {
        // System.out.println("NotExpr: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());
        String _ret="Not";
        n.f0.accept(this, helper);
        n.f1.accept(this, helper);
        //System.out.println(indent + "t." + tempCount + " = Not(" + first + " " + second + ")");
        return _ret;
    }

    /**
     * f0 -> "("
     * f1 -> Expression()
     * f2 -> ")"
     */
    public String visit(BracketExpression n, TranslationHelper helper) {
        // System.out.println("BracketExpr: " + this.currScope + " -> " + helper.symbolTable.get(this.currScope).getClassName());

        String _ret=null;
        n.f0.accept(this, helper);
        _ret = n.f1.accept(this, helper);
        n.f2.accept(this, helper);
        return _ret;
    }
}
