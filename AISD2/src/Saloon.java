import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Saloon {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        int[] coming;
        int[] leaving;

        FileInputStream in = new FileInputStream("kenobi.in");
        Parser sc = new Parser(in);
        PrintWriter out = new PrintWriter(new File("kenobi.out"), "utf8");
        PrintWriter prw = new PrintWriter(out);
        int n = sc.nextInt();
        int amount = 0;
        int lastFreedom = 0;
        int now = 0;
        int patient = 0;
        coming = new int[n];
        leaving = new int[n];
        boolean t = true;
        for (int i = 0; i < n; i++) {
            if (t) {
                coming[i] = sc.nextInt() * 60 + sc.nextInt();
                patient = sc.nextInt();
            }
            t = true;
            if (amount == 0){
                amount++;
                leaving[i] = coming[i] + 20;
                lastFreedom = leaving[i];
            } else if (leaving[now] > coming[i]){
                if (amount > patient){
                    leaving[i] = coming[i];
                } else {
                    leaving[i] = lastFreedom + 20;
                    amount++;
                    lastFreedom = leaving[i];
                }
            } else{
                amount--;
                now++;
                while (now < i && leaving[now] == coming[now]) now++;
                if (now < n){
                    i--;
                    t = false;
                }
            }
        }
        for (int i = 0; i < n; i++) {
            prw.print(leaving[i] / 60);
            prw.print(" ");
            prw.print(leaving[i] % 60);
            prw.println();
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
