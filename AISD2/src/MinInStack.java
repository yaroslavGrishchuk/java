import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class MinInStack {
    private static List<Dict> list = new ArrayList<>();
    private static int len = 1;

    public static void main(String[] args) throws IOException {
        PrintWriter in = new PrintWriter(new File("kenobi.out"), "utf8");
        Parser sc = new Parser(new FileInputStream("kenobi.in"));
        PrintWriter prw = new PrintWriter(in);

        int n = sc.nextInt();
        list.add(new Dict(Integer.MAX_VALUE,Integer.MAX_VALUE));
        for (int i = 0; i < n; i++) {
            int helper = sc.nextInt();
            if (helper == 1) {
                add(sc.nextInt());
            } else if (helper == 2) {
                pop();
            } else prw.println(minimum());
        }
        prw.close();
        in.close();

    }
    static class Dict {
        int value;
        int min;
        Dict(int a, int b){
            value = a;
            min = b;
        }
    }

    private static void add(int element) {
        list.add(new Dict(element, Math.min(element, list.get(len - 1).min)));
        len++;
    }
    private static void pop() {
        len--;
        list.remove(len);
    }
    private static int minimum() {
        return (list.get(len - 1).min);
    }




    static class Parser {
        final private int BUFFER_SIZE = 1 << 16;
        private DataInputStream din;
        private byte[] buffer;
        private int bufferPointer, bytesRead;

        Parser(InputStream in) {
            din = new DataInputStream(in);
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead =  0;
        }

        int nextInt() {
            int ret =  0;
            boolean neg;
            try {
                byte c = read();
                while (c <= ' ')
                    c = read();
                neg = c == '-';
                if (neg)
                    c = read();
                do {
                    ret = ret * 10 + c - '0';
                    c = read();
                } while (c > ' ');

                if (neg) return -ret;
            } catch (Exception ignored) {}
            return ret;
        }

        private void fillBuffer() {
            try {
                bytesRead = din.read(buffer, bufferPointer =  0, BUFFER_SIZE);
            } catch (Exception ignored) {}
            if (bytesRead == -1) buffer[ 0] = -1;
        }

        private byte read() {
            if (bufferPointer == bytesRead) fillBuffer();
            return buffer[bufferPointer++];
        }
    }
}
