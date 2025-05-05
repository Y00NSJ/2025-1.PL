package parser;

import java.io.*;

public class lexer {
    private char ch = ' ';
    private BufferedReader input;
    private final char lnChar = '\n';
    private final char eofChar = '\004';
    static boolean interactive = false;

    public lexer (String fileName) { // source filename
        try {
            input = new BufferedReader (new FileReader(fileName));
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
            System.exit(1);
        }
    }
    public lexer ( ) { // from standard input
        input = new BufferedReader (new InputStreamReader(System.in));
    }

    // 1. 문자 읽기
    private char getchar() { // Return next char
        int c = 0;
        try {
            c = input.read();
            if (c == -1)
                c = eofChar;
        } catch(IOException e) { System.err.println(e);}
        return (char) c;
    }

    // 2. 해당하는 토큰 반환
    public Token getToken() {
        do {
            // 2-a. 읽은 문자가 알파벳이면
            if (Character.isLetter(ch)) {
                String s = "";
                // 다음 문자가 알파벳/숫자인 한 계속 읽기 : letter(letter|digit)*
                do {
                    s += ch;
                    ch = getchar();
                } while (Character.isLetter(ch) || Character.isDigit(ch));

                // Id인지 KEYWORD인지 구별해 처리 후 리턴
                return Token.idORkeyword(s);
            }

            // 2-b. 읽은 문자가 숫자이면
            if (Character.isDigit(ch)) {
                String s = "";
                // 다음 문자가 숫자인 한 계속 읽기 : digit+
                do {
                    s += ch;
                    ch = getchar();
                } while (Character.isDigit(ch));

                return Token.NUMBER.setValue(s);
            }

            // 2-c. 읽은 문자가 연산자/구분자/문자 상수("string")
            switch (ch) {
                // 프로그램 진입점! ch는 처음에 ''이므로, 프로그램 시작 시 이 case문에 들어가 getChar() 실행
                case ' ': case '\t': case '\r':
                    ch = getchar();
                    break;

                case lnChar:
                    ch = getchar();
                    if (ch == '\r')			// for Windows
                        ch = getchar();		// for Windows
                    if (ch == lnChar && interactive)
                        return Token.EOF;
                    break;

                case '/':  // divide
                    ch = getchar();
                    if (ch != '/')  return Token.DIVIDE;
                    do {
                        ch = getchar();
                    } while (ch != lnChar);
                    ch = getchar();
                    break;

                case '\"':  // string literal
                    String s ="";
                    while ((ch = getchar()) != '\"')
                        s += ch;
                    ch = getchar();
                    return Token.STRLITERAL.setValue(s);
                case eofChar:
                    return Token.EOF;
                case '+': ch = getchar();
                    return Token.PLUS;
                case '-': ch = getchar();
                    return Token.MINUS;
                case '*': ch = getchar();
                    return Token.MULTIPLY;
                case '(': ch = getchar();
                    return Token.LPAREN;
                case ')': ch = getchar();
                    return Token.RPAREN;
                case '{': ch = getchar();
                    return Token.LBRACE;
                case '}': ch = getchar();
                    return Token.RBRACE;
                case '[': ch = getchar();
                    return Token.LBRACKET;
                case ']': ch = getchar();
                    return Token.RBRACKET;
                case ';': ch = getchar();
                    return Token.SEMICOLON;
                case ',': ch = getchar();
                    return Token.COMMA;
                case '&': ch = getchar();
                    return Token.AND;
                case '|': ch = getchar();
                    return Token.OR;
                case '=': ch = getchar();
                    if (ch != '=') return Token.ASSIGN;
                    else {ch = getchar(); return Token.EQUAL;}
                case '<': ch = getchar();
                    if (ch != '=') return Token.LT;
                    else {ch = getchar(); return Token.LTEQ;}
                case '>': ch = getchar();
                    if (ch != '=') return Token.GT;
                    else {ch = getchar(); return Token.GTEQ;}
                case '!': ch = getchar();
                    if (ch != '=') return Token.NOT;
                    else {ch = getchar(); return Token.NOTEQ;}
            }
        } while (true);
    }

    public static void main(String[] args) {
        lexer lexer;
        if (args.length == 0)
            lexer = new lexer( );
        else
            lexer = new lexer(args[0]);

        Token token = lexer.getToken();
        while (token != Token.EOF) {
            System.out.println(token.toString());
            token = lexer.getToken( );
        }
    }
}
