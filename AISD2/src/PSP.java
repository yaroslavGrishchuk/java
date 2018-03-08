import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.Stack;

public class PSP {
    public static void main(String[] args) {
        Scanner sc = null;
        PrintWriter prw = null;
        try {
            prw=writerToFile("kenobi.out");
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            sc = openFile("kenobi.in");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Stack<Character> stack = new Stack<>();
        String str = sc.nextLine();
        boolean t = true;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '(' || str.charAt(i) == '[' || str.charAt(i) == '{') {
                stack.push(str.charAt(i));
            } else {
                if (stack.empty()) {
                    prw.print("NO");
                    t = false;
                    break;
                } else if (str.charAt(i) == ')') {
                    if (stack.peek() == '(') {
                        stack.pop();
                    } else {
                        t = false;
                        prw.print("NO");
                        break;
                    }
                } else if (str.charAt(i) == ']') {
                    if (stack.peek() == '[') {
                        stack.pop();
                    } else {
                        t = false;
                        prw.print("NO");
                        break;
                    }
                } else if (str.charAt(i) == '}'){
                    if (stack.peek() == '{') {
                        stack.pop();
                    } else {
                        t = false;
                        prw.print("NO");
                        break;
                    }
                }
            }
        }
        if (t && !stack.isEmpty()){
            prw.print("NO");
        } else
        if (t) {
            prw.print("YES");
        }

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
