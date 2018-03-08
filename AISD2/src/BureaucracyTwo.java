import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BureaucracyTwo {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        FileInputStream in = new FileInputStream("kenobi.in");
        Parser sc = new Parser(in);
        PrintWriter out = new PrintWriter(new File("kenobi.out"), "utf8");
        PrintWriter prw = new PrintWriter(out);
        List<Integer> list = new LinkedList<>();
        int n = sc.nextInt();
        int m = sc.nextInt();
        for (int i = 0; i < n; i++) {
            list.add(sc.nextInt());
        }

        while (n > 0){
            int min = Integer.MAX_VALUE;
            int raz = m / n;
            for (int i = 0; i < n; i++) {
                if (min > list.get(i)) {
                    min = list.get(i);
                }
            }
            if (min >= raz) {
                for (int i = 0; i < n; i++) {
                    list.set(i, list.get(i) - raz);
                }
                int k = m % n;
                for (int i = 0; i < k; i++) {
                    if (list.get(0) == 0) {
                        list.remove(0);
                        i--;
                        n--;
                    } else if (list.get(0) == 1) {
                        n--;
                        list.remove(0);
                    } else list.add(list.remove(0) - 1);
                    m--;
                }
                break;

            } else {
                m -= n * min;
                for (int i = n - 1; i >= 0; i--) {
                    list.set(i, list.get(i) - min);
                    if (list.get(i) == 0) {
                        n--;
                        list.remove(i);
                    }
                }
            }
        }

        for (int i = n -1; i >= 0; i--) {
            if (list.get(i) == 0) {
                n--;
                list.remove(i);
            }
        }

        if (n <= 0) {
            prw.println(-1);
        } else {
            prw.println(n);
            for (int i = 0; i < n; i++) {
                prw.print(list.get(i));
                prw.print(" ");
            }
        }
        prw.close();
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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