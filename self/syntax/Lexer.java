package syntax;

import java.io.IOException;
import java.io.PushbackInputStream;

class Lexer {
    int token;
    int character;
    PushbackInputStream input;
    final int NUMBER = 256;

    Lexer(PushbackInputStream in) {
        input = in;
    }

    // 들어오는 토큰(수 or 문자) 읽어서 그대로 반환
    int getToken() {
        while (true) {
            try {
                character = input.read();      // 1. 들어오는 토큰을 인풋스트림 통해 읽기
                    if (character == ' ' || character == '\t' || character == '\r')     // 2-a. 공백문자    이면
                        ;
                    else if (Character.isDigit(character)) {                            // 2-b. 수        이면
                            input.unread(character); // 수 뒤에 있는 토큰까지 읽어버렸으므로 도로 돌려놓기
                            return NUMBER;
                    }
                    else                                                                // 2-c. 문자      이면
                        return character;
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    // 현재 토큰이 기대하는 토큰과 일치하는지 검사한 후, 통과하면 다음 토큰 읽기
    void match(int c) {
        if (token == c) {
            token = getToken();
        }
        else error();
    }

    void error( ) {
        System.out.printf("lexical analyzing error : %d\n", character);
        System.exit(1);
    }
}