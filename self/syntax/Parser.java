package syntax;

import java.io.PushbackInputStream;

public class Parser {
    Lexer lexer = new Lexer(new PushbackInputStream(System.in));

    void parse() {
        lexer.token = lexer.getToken();
        command();
    }

    void command() {
        expr();
        if (lexer.token == '\n')  System.out.println("Parsing completed successfully.");
        else error();
    }

    void expr() {
        term();
        while (lexer.token == '+' || lexer.token == '-') {
            if (lexer.token == '+') lexer.match('+');
            else                    lexer.match('-');
            term();
        }
    }

    void term() {
        factor();
        while (lexer.token == '*' || lexer.token == '/') {
            if (lexer.token == '*') lexer.match('*');
            else                    lexer.match('/');
            factor();
        }
    }

    void factor() {
        if (lexer.token == lexer.NUMBER) {
            lexer.match(lexer.NUMBER);
            number();
        }
        else if (lexer.token == '(') {
            lexer.match('(');
            expr();
            lexer.match(')');
        }
    }

    void number() {
        while (true) {
            try {
                lexer.character = lexer.input.read();
                if (!Character.isDigit(lexer.character)) break;
            } catch (Exception e) {
                System.err.println(e);
                break;
            }
        }

        if (lexer.character != -1) {
            try {
                lexer.input.unread(lexer.character);
            } catch (Exception e) {
                System.err.println(e);
            }
        }

        lexer.token = lexer.getToken();
//        try {
//            lexer.character = lexer.input.read();
//            while (Character.isDigit(lexer.character))
//                lexer.character = lexer.input.read();
//            lexer.input.unread(lexer.character);
//        } catch (Exception e) {
//            System.err.println(e);
//        }
    }

    void error( ) {
        System.out.printf("parse error : %d\n", lexer.character);
        System.exit(1);
    }
}
