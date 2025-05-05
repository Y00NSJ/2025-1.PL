package state;
// root: leaf ... leaf


import java.util.ArrayList;

class Indent {
    public static void display(int level, String s) {
        String tab = "";
        System.out.println();
        for (int i = 0; i < level; i++) tab = tab + "\t";
        System.out.print(tab + s);
    }
}

class Type {
    // Type = int | bool | string | fun | array | except | void
    final static Type INT = new Type("int");
    final static Type BOOL = new Type("bool");
    final static Type STRING = new Type("string");
    final static Type VOID = new Type("void");
    final static Type FUN = new Type("fun");
    final static Type ARRAY = new Type("array");
    final static Type EXC = new Type("exc");
    final static Type RAISEDEXC = new Type("raisedexc");
    final static Type UNDEF = new Type("undef");
    final static Type ERROR = new Type("error");

    protected String id;
    protected Type(String s) { id = s; }
    public String toString ( ) { return id; }
    public void display(int level) {
        Indent.display(level, "Type: " + id);
    }
}

abstract class Command {
    // Command: Decl | Function | Stmt
    Type type =Type.UNDEF;
    public void display(int l) {}
}

class Decl extends Command {
    // Decl: Type id [Expr]
    Identifier id;
    Expr expr = null;
    int arraysize = 0;

    Decl (String s, Type t) {
        id = new Identifier(s); type = t;
    }

    Decl (String s, Type t, Expr e) {
        id = new Identifier(s); type = t; expr = e;
    }
}

/* ----------------------------------------- */
abstract class Stmt extends Command {
    // Stmt: Assignment | Read | Print | Stmts | If | While | Let | Empty
}

class Assignment extends Stmt {
    // Assign: id Expr
    Identifier id;
    Expr expr;

    Assignment (Identifier i, Expr e) { id = i; expr = e; }
}

class Read extends Stmt {
    // Read: id
    Identifier id;

    Read (Identifier i) { id = i; }
}

class Print extends Stmt {
    // Print: Expr
    Expr expr;
    Print (Expr e) { expr = e; }
}

class Stmts extends Stmt {
    // Stmts: stmt*
    public ArrayList<Stmt> stmts = new ArrayList<Stmt>();

    Stmts() { super(); }

    Stmts(Stmt s) { stmts.add(s); }
}

class If extends Stmt {
    // If: Expr Stmt1 [Stmt2]
    Expr expr;
    Stmt stmt1;
    Stmt stmt2;

    If (Expr e, Stmt s) { expr = e; stmt1 = s; stmt2 = new Empty( );}
    If (Expr e, Stmt s1, Stmt s2) { expr = e; stmt1 = s1; stmt2 = s2; }
}

class While extends Stmt {
    // While: Expr Stmt
    Expr expr;
    Stmt stmt;
    While (Expr e, Stmt s) { expr = e; stmt = s; }
}

class Let extends Stmt {
    // Let: Decls Stmts
    Decls decls;
    Stmts stmts;

    Let(Decls ds, Stmts ss) {
        decls = ds;
        stmts = ss;
    }
}

class Decls extends ArrayList<Decl> {
    // Decls = Decl*

    Decls() { super(); };
    Decls(Decl d) {
        this.add(d);
    }
}

class Empty extends Stmt {
}


/* ----------------------------------------- */
abstract class Expr extends Stmt {
    // Expr = Identifier | Value | Binary | Unary | Call
}

class Binary extends Expr {
    // Binary = op expr1 expr2
    Operator op;
    Expr expr1, expr2;

    Binary(Operator o, Expr e1, Expr e2) { op = o; expr1 = e1; expr2 = e2; }
}

class Unary extends Expr {
    // Unary = op expr
    Operator op;
    Expr expr;

    Unary(Operator o, Expr e) { op = o; expr = e; }
}

class Identifier extends Expr {
    // Identifier = String id
    private String id;

    Identifier(String s) { id = s; }

    public String toString( ) { return id; }

    public boolean equals (Object obj) {
        String s = ((Identifier) obj).id;
        return id.equals(s);
    }
}

class Value extends Expr {
    // Value = int | bool | string | array | function
    protected boolean undef = true;
    Object value = null; // Type type;

    Value(Type t) {
        type = t;
        if (type == Type.INT) value = new Integer(0);
        if (type == Type.BOOL) value = new Boolean(false);
        if (type == Type.STRING) value = "";
        undef = false;
    }

    Value(Object v) {
        if (v instanceof Integer) type = Type.INT;
        if (v instanceof Boolean) type = Type.BOOL;
        if (v instanceof String) type = Type.STRING;
        value = v; undef = false;
    }

    Object value() { return value; }

    int intValue( ) {
        if (value instanceof Integer)
            return ((Integer) value).intValue();
        else return 0;
    }

    boolean boolValue( ) {
        if (value instanceof Boolean)
            return ((Boolean) value).booleanValue();
        else return false;
    }

    String stringValue ( ) {
        if (value instanceof String)
            return (String) value;
        else return "";
    }


    Type type ( ) { return type; }

    public String toString( ) {
        //if (undef) return "undef";
        if (type == Type.INT) return "" + intValue();
        if (type == Type.BOOL) return "" + boolValue();
        if (type == Type.STRING) return "" + stringValue();
        return "undef";
    }
}

class Operator {
    String val;

    Operator (String s) {
        val = s;
    }

    public String toString( ) {
        return val;
    }

    public boolean equals(Object obj) {
        return val.equals(obj);
    }

    public void display(int level) {
        Indent.display(level, "Operator: " + val);
    }
}