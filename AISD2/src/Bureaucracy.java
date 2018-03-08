import java.io.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class Bureaucracy {
    public static void main(String[] args) throws IOException {
        FileInputStream in = new FileInputStream("kenobi.in");
        Parser sc = new Parser(in);
        PrintWriter out = new PrintWriter(new File("kenobi.out"), "utf8");
        PrintWriter prw = new PrintWriter(out);
        Queue<Integer> list = new ArrayDeque<>();
        int n = sc.nextInt();
        int m = sc.nextInt();
        int min = Integer.MAX_VALUE;
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
            if (min > arr[i]) min = arr[i];
        }
        int[] mas = arr.clone();
        int iter = 0;
        int gg = 0;
        Arrays.sort(mas);
        int nHelper = n;
        while (m / nHelper > min && m > 0) {
            m -= nHelper * min;
            nHelper = 0;
            for (int i = 0; i < n; i++) {
                arr[i] -= min;
                if (arr[i] > 0) {
                    nHelper++;
                }
            }

            if (nHelper <= 0) break;
            gg += min;
            min = mas[iter] - gg;
            iter++;

        }

        if (nHelper <= 0) {
            prw.println(-1);
        } else {
            int b = m / nHelper;
            for (int i = 0; i < n; i++) {
                if (arr[i] -  b > 0) {
                    list.add(arr[i] - b);
                } else nHelper--;
            }
            m -= b * nHelper;
            if (nHelper > 0) {
                for (int i = 0; i < m; i++) {
                    if (list.peek() == 1) {
                        list.remove();
                    } else list.add(list.remove() - 1);
                }
            }

            int a = list.size();
            if (a == 0) {
                prw.println(-1);
            } else {
                prw.println(list.size());
                for (int i = 0; i < a; i++) {
                    prw.print(list.remove());
                    prw.print(" ");
                }
            }
        }
        prw.close();
        in.close();
        out.close();


    }
    static class Parser {
        final private int BUFFER_SIZE = 1 << 16;
        private DataInputStream din;
        private byte[] buffer;
        private int bufferPointer, bytesRead;
        boolean t = false;
        Parser(InputStream in) {
            din = new DataInputStream(in);
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead =  0;

        }
        String nextString(int maxSize) {
            byte[] ch = new byte[maxSize];
            int point =  0;
            if (t){
                return "";
            }
            try {
                byte c = read();
                while (c == ' ' || c == '\n' || c =='\r')
                    c = read();
                while (c != ' ' && c != '\n' && c!='\r' && c!= -1) {
                    ch[point] = c;
                    point++;
                    c = read();
                }
                if (c == -1) {
                    t = true;
                }
            } catch (Exception e) {t = true;}
            return new String(ch, 0,point);
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