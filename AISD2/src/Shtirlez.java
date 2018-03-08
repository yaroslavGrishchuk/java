import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.Stack;

public class Shtirlez {
    public static void main(String[] args) {
        Scanner sc = null;
        PrintWriter prw = null;
        try {
            prw=writerToFile("decode.out");
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            sc = openFile("decode.in");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Stack<Character> stack = new Stack<>();
        String str = sc.nextLine();
        for (int i = 0; i < str.length(); i++) {
            if (!stack.empty() && stack.peek() == str.charAt(i)) {
                stack.pop();
            } else {
                stack.push(str.charAt(i));
            }
        }
        for (char ch: stack) {
            assert prw != null;
            prw.print(ch);
        }
        sc.close();
        if (prw != null) {
            prw.close();
        }
    }
    private static Scanner openFile(String name) throws FileNotFoundException {
        return new Scanner(new File(name), "utf8");
    }
    private static PrintWriter writerToFile(String name) throws UnsupportedEncodingException, FileNotFoundException {
        return new PrintWriter(new File(name), "utf8");
    }
}
