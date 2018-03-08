import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Hospital {
    static List<Integer> queue;

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        FileInputStream in = new FileInputStream("hospital.in");
        Parser sc = new Parser(in);
        PrintWriter out = new PrintWriter(new File("hospital.out"), "utf8");
        PrintWriter prw = new PrintWriter(out);
        int n = sc.nextInt();
        queue = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String str = sc.nextString(1);
            if (str.equals("+")) {
                queue.add(sc.nextInt());
            } else if (str.equals("*")) {
                queue.add((queue.size()+1)/2,sc.nextInt());
            } else {
                prw.println(queue.remove(0));
            }
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.close();
        prw.close();
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
