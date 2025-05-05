package state;

public class sint {
    // 전역 상태
    static State state = new State();

    // 전역 변수 선언
    State Eval(State s, Command c) {
        // Command -> Decl | Stmt
        if (c instanceof Decl) {
            Decls decls = new Decls();
            decls.add((Decl)c);

            // 스택에 상태 추가
            return allocate(s, decls);
        }
        if (c instanceof Stmt) {
            return Eval(s, (Stmt) c);
        }
        throw new IllegalArgumentException("no command");
    }

    State allocate (State state, Decls decls) {
        // add entries for declared variables on the state
        //
        if (decls != null) {
            for (Decl d : decls) {
                Value v;
                if (d.expr != null) {
                    v = V(state, d.expr);  // 초기화 Expr (O)
                } else {
                    v = new Value(d.type); // 초기화 Expr (X) => 언어 설정 기본값
                }
                state.push(d.id, v);
            }
        }
        return state;
    }

    // 대입문
    State Eval(State s, Assignment a) {
        // 상태에 대입문으로 인한 변경사항 추가
        return state.set(a.id, V(s, a.expr));
    }


    /* 수식 값 계산 */
    Value V(State s, Expr expr) {
        if (expr instanceof Value)
            return (Value)expr;

        if (expr instanceof Identifier) {
            Identifier id = (Identifier)expr;
            return (Value) (state.get(id));
        }

        if (expr instanceof Binary) {
            Binary b = (Binary)expr;
            Value v1 = V(state, b.expr1);
            Value v2 = V(state, b.expr2);

            return binaryOperation(b.op, v1, v2);
        }

        if (expr instanceof Unary) {
            Unary u = (Unary)expr;
            Value v = V(state, u.expr);

            return unaryOperation(u.op, v);
        }

        throw new IllegalArgumentException("no operation");
    }

    Value binaryOperation(Operator op, Value v1, Value v2) {
        check(!v1.undef && !v2.undef,"reference to undef value");
        switch (op.val) {
            case "+":
                return new Value(v1.intValue() + v2.intValue());
            case "-":
                return new Value(v1.intValue() - v2.intValue());
            case "*":
                return new Value(v1.intValue() * v2.intValue());
            case "/":
                return new Value(v1.intValue() / v2.intValue());
            //
            // relational operations
            //
            case "==":
                if (v1.type == Type.STRING && v2.type == Type.STRING) {
                    return new Value(v1.stringValue().equals(v2.stringValue()));
                }
                return new Value(v1.intValue() == v2.intValue());
            case "!=":
                if (v1.type == Type.STRING && v2.type == Type.STRING) {
                    return new Value(!v1.stringValue().equals(v2.stringValue()));
                }
                return new Value(v1.intValue() != v2.intValue());
            case "<":
                if (v1.type == Type.STRING && v2.type == Type.STRING) {
                    return new Value(v1.stringValue().compareTo(v2.stringValue()) < 0);
                }
                return new Value(v1.intValue() < v2.intValue());
            case ">":
                if (v1.type == Type.STRING && v2.type == Type.STRING) {
                    return new Value(v1.stringValue().compareTo(v2.stringValue()) > 0);
                }
                return new Value(v1.intValue() > v2.intValue());
            case "<=":
                if (v1.type == Type.STRING && v2.type == Type.STRING) {
                    return new Value(v1.stringValue().compareTo(v2.stringValue()) <= 0);
                }
                return new Value(v1.intValue() <= v2.intValue());
            case ">=":
                if (v1.type == Type.STRING && v2.type == Type.STRING) {
                    return new Value(v1.stringValue().compareTo(v2.stringValue()) >= 0);
                }
                return new Value(v1.intValue() >= v2.intValue());


            //
            // logical operations
            //
            case "&":
                return new Value(v1.boolValue() & v2.boolValue());
            case "|":
                return new Value(v1.boolValue() | v2.boolValue());

            default:
                throw new IllegalArgumentException("no operation");
        }
    }

    Value unaryOperation(Operator op, Value v) {
        check( !v.undef, "reference to undef value");
        return switch (op.val) {
            case "!" -> new Value(!v.boolValue());
            case "-" -> new Value(-v.intValue());
            default -> throw new IllegalArgumentException("no operation: " + op.val);
        };
    }

    // 변수 생성만 하고 초기화는 안 된 경우를 캐치
    static void check(boolean test, String msg) {
        if (test) return;
        System.err.println(msg);
    }
}
