import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.Stack;

public class Posfix {
    public static void main(String[] args) {
        Scanner sc = null;
        PrintWriter prw = null;
        try {
            prw=writerToFile("postfix.out");
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            sc = openFile("postfix.in");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int sum = 0;
        Stack<Integer> stack = new Stack<>();
        while (sc.hasNext()) {
            String str = sc.next();
            if (str.equals("*")) {
                sum = stack.pop() * stack.pop();
                stack.push(sum);
            } else if (str.equals("+")) {
                sum = stack.pop() + stack.pop();
                stack.push(sum);
            } else if (str.equals("-")) {
                sum = -(stack.pop() - stack.pop());
                stack.push(sum);
            } else {
                stack.push(Integer.valueOf(str));
            }
        }
        prw.print(stack.pop());

        sc.close();
        prw.close();
    }
    private static Scanner openFile(String name) throws FileNotFoundException {
        return new Scanner(new File(name), "utf8");
    }
    private static PrintWriter writerToFile(String name) throws UnsupportedEncodingException, FileNotFoundException {
        return new PrintWriter(new File(name), "utf8");
    }
}
