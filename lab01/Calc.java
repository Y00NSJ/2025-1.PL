import java.io.*;

class Calc {
    int token; int value; int ch;
    private PushbackInputStream input;
    final int NUMBER=256;

    Calc(PushbackInputStream is) {
        input = is;
    }

    int getToken( )  { /* tokens are characters */
        while(true) {
            try  {
	            ch = input.read();
                if (ch == ' ' || ch == '\t' || ch == '\r') ;
                else 
                    if (Character.isDigit(ch)) {
                        value = number( );
	               input.unread(ch);
		     return NUMBER;
	          }
	          else return ch;
	  } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    private int number( )  {
    /* number -> digit { digit } */
        int result = ch - '0';
        try  {
            ch = input.read();
            while (Character.isDigit(ch)) {
                result = 10 * result + ch -'0';
                ch = input.read(); 
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        return result;
    }

    void error( ) {
        System.out.printf("parse error : %d\n", ch);
        System.exit(1);
    }

    void match(int c) { 
        if (token == c) 
	    token = getToken();
        else error();
    }

    void command( ) {
    /* command -> expr '\n' */
        Object result = expr();
        if (token == '\n') /* end the parse and print the result */
	    System.out.println(result);
        else error();
    }

    Object expr() {
		Object result;
        if (token == '!') {
            match('!');
            result = !(boolean) expr();
        // TODO Q. 'truelabc' 같은 게 들어오면 어떻게 처리해야 할까?
        } else if (token == 't') {
            match('t');
            if (token == 'r') match('r');
            if (token == 'u') match('u');
            if (token == 'e') match('e');
            result = true;
        } else if (token == 'f') {
            match('f');
            if (token == 'a') match('a');
            if (token == 'l') match('l');
            if (token == 's') match('s');
            if (token == 'e') match('e');
            result = false;
        } else {
            result = bexp();
            // TODO &&과 & 중에 뭘 의도하신 걸까?
            while (token == '&' || token == '|') {
                boolean left = (boolean) result;
                if (token == '&') {
                    match('&');
                    boolean right = (boolean) bexp();
                    result = left & right;
                } else {
                    match('|');
                    boolean right = (boolean) bexp();
                    result = left | right;
                }
            }
        }
        return result;
	}

    Object bexp( ) {
        Object result = aexp();
        if (token == '=' | token == '!' | token == '<' | token == '>') {
            String operator = relop();
            int right = aexp();

            switch (operator) {
                case "==":
                    result = (int)result == right;
                    break;
                case "!=":
                    result = (int)result != right;
                    break;
                case "<":
                    result = (int)result < right;
                    break;
                case ">":
                    result = (int)result > right;
                    break;
                case "<=":
                    result = (int)result <= right;
                    break;
                case ">=":
                    result = (int)result >= right;
                    break;
            }
        }

        return result;
	}

    String relop() {
        String result = "";
        switch (token) {
            case '=':
                match('=');
                if (token == '=') match('=');
                result = "==";
                break;
            case '!':
                match('!');
                if (token == '=') match('=');
                result = "!=";
                break;
            case '<':
                match('<');
                if (token == '=') {
                    match('=');
                    result = "<=";
                } else result = "<";
                break;
            case '>':
                match('>');
                if (token == '=') {
                    match('=');
                    result = ">=";
                } else result = ">";
                break;
        }
        return result;
    }

    int aexp( ) {
    /* expr -> term { '+' term } */
        int result = term();
        while (token == '+' || token == '-') {
            if (token == '+') {
                match('+');
                result += term();
            }
            else {
                match('-');
                result -= term();
            }
        }
        return result;
    }

    int term( ) {
    /* term -> factor { '*' factor } */
       int result = factor();
       while (token == '*' || token == '/') {
           if (token == '*') {
               match('*');
               result *= factor();
           }
           else {
               match('/');
               result /= factor();
           }
       }
       return result;
    }

    int factor() {
    /* factor -> '(' expr ')' | number */
        int result = 0;
        if (token == '(') {
            match('(');
            result = aexp();
            match(')');
        }
        else if (token == NUMBER) {
            result = value;
	        match(NUMBER); //token = getToken();
        }
        return result;
    }

    void parse( ) {
        token = getToken(); // get the first token
        command();          // call the parsing command
    }

    public static void main(String args[]) { 
        Calc calc = new Calc(new PushbackInputStream(System.in));
        while(true) {
            System.out.print(">> ");
            calc.parse();
        }
    }
}