package parser;

enum Token {
    ID(""), // letter(letter|digit)*

    NUMBER(""), STRLITERAL(""),     // digit+, ".*"

    BOOL("bool"),CHAR("char"), ELSE("else"), FALSE("false"), FLOAT("float"),
    STRING("string"), IF("if"), INT("int"),  TRUE("true"), WHILE("while"),
    RETURN("return"), VOID("void"), FUN("fun"),  THEN("then"), LET("let"),
    IN("in"), END("end"), READ("read"), PRINT("print"), DO("do"),  FOR("for"),

    ASSIGN("="), EQUAL("=="),  LT("<"), LTEQ("<="), GT(">"),
    GTEQ(">="),  NOT("!"),    NOTEQ("!="), PLUS("+"), MINUS("-"),
    MULTIPLY("*"), DIVIDE("/"), AND("&"), OR("|"),

    EOF("<<EOF>>"),
    LBRACE("{"), RBRACE("}"), LBRACKET("["), RBRACKET("]"),
    LPAREN("("), RPAREN(")"), SEMICOLON(";"), COMMA(",")
    ;


    private String value;   // NUMBER 이나 String Literal이 들어가는 변수

    private Token (String v) {
        value = v;
    }

    public String value( ) { return value; }    // value 꺼내오기

    public Token setValue(String v) {
        this.value = v;
        return this;
    }

    public static Token idORkeyword (String name) {
        for (Token token : Token.values()) {
            if (token.value().equals(name))
                return token;
            if (token == Token.EOF)
                break;
        }
        return ID.setValue(name);
    } // keyword or ID
}
