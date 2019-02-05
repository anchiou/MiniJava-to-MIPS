import syntaxtree.*;
import visitor.*;
import java.util.HashMap;
import java.util.Vector;

public class SecondVisitor extends GJDepthFirst<String, HashMap<String, Scope>> {

    Vector<String> globalVector = new Vector<String>();
    String currScope = "scope0";
    int scopeCounter = 0;

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
    public String visit(MainClass n, HashMap<String, Scope> argu) { // Always scope0
        System.out.println("MainClass");
        System.out.println(" currScope: " + this.currScope);

        String _ret=null;
        // argu.get(this.currScope).printAll();
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        n.f7.accept(this, argu);
        n.f8.accept(this, argu);
        n.f9.accept(this, argu);
        n.f10.accept(this, argu);
        n.f11.accept(this, argu);
        n.f12.accept(this, argu);
        n.f13.accept(this, argu);
        n.f14.accept(this, argu);
        n.f15.accept(this, argu);
        n.f16.accept(this, argu);
        n.f17.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
    public String visit(TypeDeclaration n, HashMap<String, Scope> argu) {
        System.out.println("TypeDecl");

        String _ret=null;
        n.f0.accept(this, argu);
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
    public String visit(ClassDeclaration n, HashMap<String, Scope> argu) {
        System.out.println("ClassDecl");

        // ++this.scopeCounter;
        // this.currScope = "scope" + this.scopeCounter;
        // System.out.println(" currScope: " + this.currScope);
        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
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
    public String visit(ClassExtendsDeclaration n, HashMap<String, Scope> argu) {
        System.out.println("ClassExtends");

        // ++this.scopeCounter;
        // this.currScope = "scope" + this.scopeCounter;
        // System.out.println(" currScope: " + this.currScope);
        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        n.f7.accept(this, argu);
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
    public String visit(MethodDeclaration n, HashMap<String, Scope> argu) {
        System.out.println("MethodDecl");

        // ++this.scopeCounter;
        // this.currScope = "scope" + this.scopeCounter;
        // System.out.println(" currScope: " + this.currScope);
        String _ret= "MethodDeclaration";
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        String classType = n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        n.f7.accept(this, argu);
        n.f8.accept(this, argu);
        n.f9.accept(this, argu);
        String returnType = n.f10.accept(this, argu);

        if ((argu.get(this.currScope).getType(classType)) != (argu.get(this.currScope).getType(returnType)) &&
            (argu.get(this.currScope).getType(classType)) != returnType) {
            System.out.println("Type error: 1");
            System.exit(0);
        }
        n.f11.accept(this, argu);
        n.f12.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
    public String visit(ArrayType n, HashMap<String, Scope> argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "boolean"
    */
    public String visit(BooleanType n, HashMap<String, Scope> argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "int"
    */
    public String visit(IntegerType n, HashMap<String, Scope> argu) {
        String _ret=null;
        n.f0.accept(this, argu);
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
    public String visit(Statement n, HashMap<String, Scope> argu) {
        System.out.println("Statement");

        String _ret=null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
    public String visit(Block n, HashMap<String, Scope> argu) {
        System.out.println("Block");

        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
    public String visit(AssignmentStatement n, HashMap<String, Scope> argu) {
        System.out.println("AssignmentStmt");

        String _ret=null;
        String id = n.f0.accept(this, argu);
        if (!argu.get(this.currScope).contains(id)) {
            System.out.println("Type error: 2");
            System.exit(0);
        }
        n.f1.accept(this, argu);
        String s = n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        String idType = argu.get(this.currScope).getType(id);
        if (s != idType) {
            System.out.println("Type error");
            System.exit(0);
        }
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
    public String visit(ArrayAssignmentStatement n, HashMap<String, Scope> argu) {
        System.out.println("ArrayAssignmentStmt");

        String _ret=null;
        String id = n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        String first = n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        String second = n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        if (first == "int" && second == "int" && id == "array") {
            return _ret;
        }
        System.out.println("Type error");
        System.exit(0);
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
    public String visit(IfStatement n, HashMap<String, Scope> argu) {
        System.out.println("IfStmt");

        String _ret= "IfStatement";
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        String Exp = n.f2.accept(this, argu);
        if (!(Exp == "boolean")) {
            System.out.println("Type error");
            System.exit(0);
        }
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
    public String visit(WhileStatement n, HashMap<String, Scope> argu) {
        System.out.println("WhileStmt");

        String _ret= "WhileStatement";
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        String Exp = n.f2.accept(this, argu);
        if (!(Exp == "boolean")) {
            System.out.println("Type error");
            System.exit(0);
        }
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
    public String visit(PrintStatement n, HashMap<String, Scope> argu) {
        System.out.println("PrintStmt");

        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        String e = n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        if (e == "int") {
            return _ret;
        }
        System.out.println("Print: Type error");
        System.exit(0);
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
    public String visit(Expression n, HashMap<String, Scope> argu) {
        System.out.println("Expression");

        String _ret = n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
    * f1 -> "&&"
    * f2 -> PrimaryExpression()
    */
    public String visit(AndExpression n, HashMap<String, Scope> argu) {
        System.out.println("AndExpression");

        String _ret="boolean";
        String first = n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        String second = n.f2.accept(this, argu);
        if (first == "boolean" && second == "boolean") {
            return _ret;
        }
        System.out.println("Type error: Expected boolean");
        System.exit(0);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
    public String visit(CompareExpression n, HashMap<String, Scope> argu) {
        String _ret="boolean";
        String first = n.f0.accept(this, argu);
        if (first != "int") {
            first = argu.get(this.currScope).getType(first);
        }
        System.out.println("CompareExpression: " + first);

        n.f1.accept(this, argu);

        String second = n.f2.accept(this, argu);
        if (second != "int") {
            second = argu.get(this.currScope).getType(second);
        }

        if (first == "int" && second == "int") {
            return _ret;
        }
        System.out.println("Type error: Expected int");
        System.exit(0);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
    public String visit(PlusExpression n, HashMap<String, Scope> argu) {
        System.out.println("PlusExpression");

        String _ret="int";
        String first = n.f0.accept(this, argu);
        if (first != "int") {
            first = argu.get(this.currScope).getType(first);
        }

        n.f1.accept(this, argu);

        String second = n.f2.accept(this, argu);
        if (second != "int") {
            second = argu.get(this.currScope).getType(second);
        }

        if (first == "int" && second == "int") {
            return _ret;
        }
        System.out.println("Type error: Expected int");
        System.exit(0);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
    public String visit(MinusExpression n, HashMap<String, Scope> argu) {
        System.out.println("MinusExpression");

        String _ret = "int";
        String first = n.f0.accept(this, argu);
        if (first != "int") {
            first = argu.get(this.currScope).getType(first);
        }
        System.out.println("Minus.first: " + first);

        n.f1.accept(this, argu);

        String second = n.f2.accept(this, argu);
        System.out.println("Minus.second: " + second);

        if (first == "int" && second == "int") {
            return _ret;
        }

        System.out.println("Type error: Expected int");
        System.exit(0);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
    public String visit(TimesExpression n, HashMap<String, Scope> argu) {
        System.out.println("TimesExpression");

        String _ret="int";
        String first = n.f0.accept(this, argu);
        if (first != "int") {
            first = argu.get(this.currScope).getType(first);
        }
        System.out.println("Times.first: " + first);

        n.f1.accept(this, argu);

        String second = n.f2.accept(this, argu);
        if (second != "int") {
            second = argu.get(this.currScope).getType(second);
        }
        System.out.println("Times.second: " + second);

        if (first == "int" && second == "int") {
            return _ret;
        }
        System.out.println("Type error: Expected int");
        System.exit(0);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
    public String visit(ArrayLookup n, HashMap<String, Scope> argu) {
        System.out.println("ArrayLookup");

        String _ret="int";
        String arr = n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        String value = n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        if (arr == "array" && value == "int") {
            return _ret;
        }
        System.out.println("Type error: 5");
        System.exit(0);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
    public String visit(ArrayLength n, HashMap<String, Scope> argu) {
        System.out.println("ArrayLength");

        String _ret="int";
        String first = n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        if (first == "array") {
            return _ret;
        }
        System.out.println("Type error: Expected integer array.");
        System.exit(0);
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
    public String visit(MessageSend n, HashMap<String, Scope> argu) {
        System.out.println("MessageSend");
        System.out.println(" currScope: " + this.currScope);

        String _ret = null;
        String className = n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        String methodName = n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        String exprType = n.f4.accept(this, argu);
        n.f5.accept(this, argu);

        boolean classExists = false;
        for (String key : argu.keySet()) {
            if (argu.get(key).contains(className)) {
                classExists = true;
                break;
            }
        }

        if (!classExists && className != "this") {
            System.out.println("<-- MessageSend Scope -->");
            argu.get(this.currScope).printAll();
            System.out.println("<-- MessageSend End Scope -->");
            System.out.println("MessageSend no class: " + className);
            System.out.println("Type error: No such class");
            System.exit(0);
        }

        boolean methodExists = false;
        String parent = "";
        for (String key : argu.keySet()) {
            if (argu.get(key).contains(methodName)) {
                methodExists = true;
                parent = argu.get(key).getParentScope();
                this.currScope = key;
                break;
            }
        }

        // type checks if methodName is valid
        if (!methodExists) {
            System.out.println("<-- MessageSend Scope -->");
            argu.get(this.currScope).printAll();
            System.out.println("<-- MessageSend End Scope -->");
            System.out.println("Type error: 6"); // method doesn't exist
            System.exit(0);
        }

        // if (exprType != argu.get(this.currScope))

        _ret = argu.get(this.currScope).getType(methodName); // set _ret to method return type

        return _ret;
    }

    /**
     * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
    public String visit(ExpressionList n, HashMap<String, Scope> argu) {
        System.out.println("ExprList");

        String _ret = null;
        String expr = n.f0.accept(this, argu);
        System.out.println("ExprList.expr: " + expr);
        globalVector.add(expr);
        String exprRest = n.f1.accept(this, argu);
        System.out.println("ExprList.exprRest: " + exprRest);
        if (exprRest != null) {
            globalVector.add(exprRest);
        }
        _ret = expr;
        return _ret;
    }

    /**
     * f0 -> ","
    * f1 -> Expression()
    */
    public String visit(ExpressionRest n, HashMap<String, Scope> argu) {
        System.out.println("ExprRest");

        String _ret=null;
        n.f0.accept(this, argu);
        _ret = n.f1.accept(this, argu);
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
    public String visit(PrimaryExpression n, HashMap<String, Scope> argu) {
        System.out.println("PrimaryExpr");

        String _ret = n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> <INTEGER_LITERAL>
    */
    public String visit(IntegerLiteral n, HashMap<String, Scope> argu) {
        System.out.println("IntegerLiteral");

        String _ret = "int";
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "true"
    */
    public String visit(TrueLiteral n, HashMap<String, Scope> argu) {
        System.out.println("TrueLiteral");

        String _ret="boolean";
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "false"
    */
    public String visit(FalseLiteral n, HashMap<String, Scope> argu) {
        System.out.println("FalseLiteral");

        String _ret="boolean";
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> <IDENTIFIER>
    */
    public String visit(Identifier n, HashMap<String, Scope> argu) {
        System.out.println("Identifier: " + n.f0.toString());

        String _ret = n.f0.toString();
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "this"
    */
    public String visit(ThisExpression n, HashMap<String, Scope> argu) {
        System.out.println("ThisExpr");

        String _ret = "this";
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
    public String visit(ArrayAllocationExpression n, HashMap<String, Scope> argu) {
        System.out.println("ArrayAllocExpr");

        String _ret="array";
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        String expr = n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        if (expr == "int") {
            return _ret;
        }
        System.out.println("Type error: expected int.");
        System.exit(0);
        return _ret;
    }

    /**
     * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
    public String visit(AllocationExpression n, HashMap<String, Scope> argu) {
        System.out.println("AllocationExpr");

        String _ret=null;
        n.f0.accept(this, argu);
        String id = n.f1.accept(this, argu);
        // System.out.println("In Alloc:" + id);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);

        boolean classExists = false;
        for (String key : argu.keySet()) {
            if (argu.get(key).contains(id)) {
                classExists = true;
                break;
            }
        }
        if (classExists) {
            return id;
        }
        System.out.println("Type error: no such class exists. (680)");
        System.exit(0);
        return _ret;
    }

    /**
     * f0 -> "!"
    * f1 -> Expression()
    */
    public String visit(NotExpression n, HashMap<String, Scope> argu) {
        System.out.println("NotExpr");

        String _ret="boolean";
        n.f0.accept(this, argu);
        String s = n.f1.accept(this, argu);
        if (s == "boolean") {
            return _ret;
        }
        System.out.println("Type error: expected boolean.");
        System.exit(0);
        return _ret;
    }

    /**
     * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
    public String visit(BracketExpression n, HashMap<String, Scope> argu) {
        System.out.println("BracketExpr");

        String _ret=null;
        n.f0.accept(this, argu);
        _ret = n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }
}
