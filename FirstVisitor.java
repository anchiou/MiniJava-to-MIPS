import syntaxtree.*;
import visitor.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FirstVisitor extends GJDepthFirst<String, TranslationHelper> {
   String classScope = "";
   String currClass = "";

   String currScope = "scope0";
   int scopeCount = 0;

   ArrayList<String> fields; // class record fields list
   Map<String, String> methods; // class v-table methods list
   Map<String, String> parameterList;

   /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
   public String visit(Goal n, TranslationHelper helper) {
      String _ret = null;
      n.f0.accept(this, helper);
      n.f1.accept(this, helper);
      n.f2.accept(this, helper);
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
   public String visit(MainClass n, TranslationHelper helper) {
      String _ret=null;

      n.f0.accept(this, helper);

      String id = n.f1.accept(this, helper);
      this.currClass = id;
      helper.symbolTable.put("scope" + this.scopeCount, new Scope(id));
      ++this.scopeCount;
      helper.symbolTable.get(this.currScope).putType(id, "class");

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
      n.f16.accept(this, helper);
      n.f17.accept(this, helper);
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
         String _ret = null;
         this.fields = new ArrayList<String>();
         this.methods = new LinkedHashMap<String, String>();

         n.f0.accept(this, helper);

         String id = n.f1.accept(this, helper);
         if (helper.symbolTable.get(this.currScope).contains(id)) {
            System.out.println("Type error");
            return null;
         }
         this.currClass = id;

         String nextScope = "scope" + this.scopeCount;
         helper.symbolTable.put(nextScope, new Scope(id));
         this.currScope = nextScope;
         ++this.scopeCount;
         helper.symbolTable.get(this.currScope).putType(id, "class");

         n.f2.accept(this, helper);

         this.classScope = this.currScope; // keep track of class' scope
         n.f3.accept(this, helper);

         n.f4.accept(this, helper);
         n.f5.accept(this, helper);

         helper.classList.putFields(id, this.fields); // add class record to manager
         // helper.classList.printFields(id);

         helper.classList.putMethods(id, this.methods); // add class v-table to manager
         // helper.classList.printMethods(id);

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
      String _ret=null;

      n.f0.accept(this, helper);

      String id = n.f1.accept(this, helper);
      if (helper.symbolTable.get(this.currScope).contains(id)) {
         System.out.println("Type error");
         System.exit(0);
      }
      this.currClass = id;

      n.f2.accept(this, helper);

      String parent = n.f3.accept(this, helper); // parent class
      String parentScope = "";
      for (String key : helper.symbolTable.keySet()) {
         if (helper.symbolTable.get(key).contains(parent)) {
            parentScope = key;
         }
      }

      String nextScope = "scope" + this.scopeCount;
      helper.symbolTable.put(nextScope, new Scope(parentScope, id));
      this.currScope = nextScope;
      ++this.scopeCount;
      helper.symbolTable.get(this.currScope).putType(id, "class");

      this.fields = new ArrayList<String>(helper.classList.getRecord(parent)); // copy parent class record
      if (this.fields == null) {
         System.out.println("Type error");
         System.exit(0);
      }
      this.methods = new LinkedHashMap<String, String>(helper.classList.getVTable(parent)); // copy parent class v-table

      n.f4.accept(this, helper);

      this.classScope = this.currScope; // keep track of class' scope
      n.f5.accept(this, helper);

      n.f6.accept(this, helper);
      n.f7.accept(this, helper);

      helper.classList.putFields(id, this.fields); // add class record to manager
      // helper.classList.printFields(id);

      helper.classList.putMethods(id, this.methods); // add class v-table to manager
      // helper.classList.printMethods(id);

      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public String visit(VarDeclaration n, TranslationHelper helper) {
      String _ret=null;
      String type = n.f0.accept(this, helper);
      String id = n.f1.accept(this, helper);
      if (helper.symbolTable.get(this.currScope).contains(id)) {
         System.out.println("Type error");
         return null;
      }
      helper.symbolTable.get(this.currScope).putType(id, type);

      if (this.classScope == this.currScope) { // check if it is a class variable (not method variable)
         this.fields.add(id); // add field to field list
      }

      n.f2.accept(this, helper);
      return id;
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
      String _ret=null;

      n.f0.accept(this, helper);
      String returnType = n.f1.accept(this, helper);
      String id = n.f2.accept(this, helper);
      // System.out.println("Method id: " + id + "currClass: " + this.currClass);

      if (helper.symbolTable.get(this.currScope).contains(id)) {
         System.out.println("Type error");
         return null;
      }

      String parentScope = this.currScope;
      // System.out.println("Method " + id + " parentScope: " + parentScope);
      String nextScope = "scope" + this.scopeCount;
      helper.symbolTable.put(nextScope, new Scope(parentScope, this.currClass)); // create scope with (parent class/parent scope)
      this.currScope = nextScope;
      ++this.scopeCount;
      helper.symbolTable.get(this.currScope).putType(id, returnType);

      this.methods.put(id, this.currClass); // FIXME? don't want to replace superclass method in v-table when overriding

      n.f3.accept(this, helper);

      this.parameterList = new HashMap<String, String>();
      n.f4.accept(this, helper);
      helper.symbolTable.get(this.currScope).putParameters(this.currClass + "." + id, parameterList);

      n.f5.accept(this, helper);
      n.f6.accept(this, helper);
      n.f7.accept(this, helper);
      n.f8.accept(this, helper);
      n.f9.accept(this, helper);
      n.f10.accept(this, helper);
      n.f11.accept(this, helper);
      n.f12.accept(this, helper);
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
   public String visit(FormalParameter n, TranslationHelper helper) { // Within method scope
      String _ret = null;
      String type = n.f0.accept(this, helper);
      String id = n.f1.accept(this, helper);
      // System.out.println("Parameter id: " + id);
      // System.out.println("Parameter type: " + type);
      this.parameterList.put(id, type);
      return _ret;
   }

   /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
   public String visit(Type n, TranslationHelper helper) {
      String _ret = n.f0.accept(this, helper);
      return _ret;
   }

   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public String visit(ArrayType n, TranslationHelper helper) {
      String _ret = "array";
      n.f0.accept(this, helper);
      n.f1.accept(this, helper);
      n.f2.accept(this, helper);
      return _ret;
   }

   /**
    * f0 -> "boolean"
    */
   public String visit(BooleanType n, TranslationHelper helper) {
      String _ret = n.f0.toString();
      n.f0.accept(this, helper);
      return _ret;
   }

   /**
    * f0 -> "int"
    */
   public String visit(IntegerType n, TranslationHelper helper) {
      String _ret = n.f0.toString();
      n.f0.accept(this, helper);
      return _ret;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public String visit(Identifier n, TranslationHelper helper) {
      String _ret=null;
      n.f0.accept(this, helper);
      _ret = n.f0.toString();
      return _ret;
   }
}