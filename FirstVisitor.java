import syntaxtree.*;
import visitor.*;
import java.util.Map;
import java.util.HashMap;

public class FirstVisitor extends GJNoArguDepthFirst<String> {
   HashMap<String, Scope> symbolTable = new HashMap<String, Scope>();
   int scopeCount = 0;
   String currScope = "scope0";

   /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
   public String visit(Goal n) {
      Scope mainScope = new Scope();
      this.symbolTable.put("scope" + this.scopeCount, mainScope);
      ++this.scopeCount;
      String _ret = null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
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
   public String visit(MainClass n) {
      String _ret=null;
      n.f0.accept(this);
      String id = n.f1.accept(this);
      this.symbolTable.get(this.currScope).putType(id, "class");
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      n.f8.accept(this);
      n.f9.accept(this);
      n.f10.accept(this);
      n.f11.accept(this);
      n.f12.accept(this);
      n.f13.accept(this);
      n.f14.accept(this);
      n.f15.accept(this);
      n.f16.accept(this);
      n.f17.accept(this);
      return _ret;
   }

   /**
    * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
   public String visit(TypeDeclaration n) {
      String _ret=null;
      n.f0.accept(this);
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
   public String visit(ClassDeclaration n) {
         String _ret = null;
         n.f0.accept(this);
         String id = n.f1.accept(this);
         if (this.symbolTable.get(this.currScope).contains(id)) {
            System.out.println("Type error");
            return null;
         }
         String nextScope = "scope" + this.scopeCount;
         this.symbolTable.put(nextScope, new Scope());
         this.currScope = nextScope;
         ++this.scopeCount;
         this.symbolTable.get(this.currScope).putType(id, "class");
         n.f2.accept(this);
         n.f3.accept(this);
         n.f4.accept(this);
         n.f5.accept(this);
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
   public String visit(ClassExtendsDeclaration n) {
      String _ret=null;
      n.f0.accept(this);
      String id = n.f1.accept(this);
      // System.out.println("Class Extends id: " + id);
      if (this.symbolTable.get(this.currScope).contains(id)) {
         System.out.println("Type error");
         return null;
      }
      String nextScope = "scope" + this.scopeCount;
      this.symbolTable.put(nextScope, new Scope());
      this.currScope = nextScope;
      this.symbolTable.get(this.currScope).putType(id, "class");
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public String visit(VarDeclaration n) {
      String _ret=null;
      String type = n.f0.accept(this);
      String id = n.f1.accept(this);
      if (this.symbolTable.get(this.currScope).contains(id)) {
         System.out.println("Type error");
         return null;
      }
      this.symbolTable.get(this.currScope).putType(id, type);
      // System.out.println("Returned from ScopePut");
      n.f2.accept(this);
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
   public String visit(MethodDeclaration n) {
      String _ret=null;
      n.f0.accept(this);
      String returnType = n.f1.accept(this);
      String id = n.f2.accept(this);
      // System.out.println("Method id: " + id + "returnType: " + returnType);

      if (this.symbolTable.get(this.currScope).contains(id)) {
         System.out.println("Type error");
         return null;
      }

      String parentScope = this.currScope;
      // System.out.println("Method " + id + " parentScope: " + parentScope);
      String nextScope = "scope" + this.scopeCount;
      this.symbolTable.put(nextScope, new Scope(parentScope)); // create scope with (parent class, parent scope)
      this.currScope = nextScope;
      ++this.scopeCount;
      this.symbolTable.get(this.currScope).putType(id, returnType);

      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      n.f8.accept(this);
      n.f9.accept(this);
      n.f10.accept(this);
      n.f11.accept(this);
      n.f12.accept(this);
      return _ret;
   }

   /**
    * f0 -> FormalParameter()
    * f1 -> ( FormalParameterRest() )*
    */
   public String visit(FormalParameterList n) {
      String _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
   public String visit(FormalParameter n) { // Within method scope
      String _ret=null;
      String type = n.f0.accept(this);
      // System.out.println("Parameter type: " + type);
      String id = n.f1.accept(this);
      // System.out.println("Parameter id: " + id);
      this.symbolTable.get(this.currScope).putType(id, type);
      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
   public String visit(FormalParameterRest n) {
      String _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
   public String visit(Type n) {
      String _ret = n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public String visit(ArrayType n) {
      String _ret = "array";
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }

   /**
    * f0 -> "boolean"
    */
   public String visit(BooleanType n) {
      String _ret = n.f0.toString();
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "int"
    */
   public String visit(IntegerType n) {
      String _ret = n.f0.toString();
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public String visit(Identifier n) {
      String _ret=null;
      n.f0.accept(this);
      _ret = n.f0.toString();
      return _ret;
   }
}