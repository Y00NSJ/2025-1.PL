package syntax;

public class Main {
    public static void main(String[] args) {
        Parser p = new Parser();
        while(true) {
            System.out.print(">> ");
            p.parse();
        }
    }
}
