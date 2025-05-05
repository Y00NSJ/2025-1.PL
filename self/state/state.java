package state;


import java.util.Stack;

class Pair {
    Identifier id;
    Value val;
    Pair(Identifier id, Value val) {
        this.id = id;
        this.val = val;
    }
}

class State extends Stack<Pair> {
    public State() {}

    public State(Identifier id, Value val) { push(id, val); }

    public State push(Identifier id, Value val) {
        super.push(new Pair(id, val));
        return this;
    }

    // 해당 변수의 위치를 반환
    public int lookup(Identifier id) {
        for (int i = size() - 1; i >= 0; i--) {
            if (id.equals(((Pair)get(i)).id))   return i;
        }
        return -1;
    }

    // 해당 변수의 값을 반환
    public Value get(Identifier id) {
        int location = lookup(id);
        if (location == -1) return null;
        Pair p = (Pair)(get(location));

        return (Value) p.val;
    }

    // 해당 변수의 값/변수명을 재설정
    public State set(Identifier id, Value val) {
        int location = lookup(id);
        if (location != -1)     super.set(location, new Pair(id, val));
        return this;
    }
}