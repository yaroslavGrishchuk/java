
import java.io.*;

public class SystemOfNoTouchedPlurality {
    private static Element[] arr;
    public static void main(String[] args) throws IOException {
        FileInputStream in = new FileInputStream("kenobi.in");
        Parser sc = new Parser(in);
        PrintWriter out = new PrintWriter(new File("kenobi.out"), "utf8");
        PrintWriter prw = new PrintWriter(out);
        arr = new Element[sc.nextInt() + 1];
        for (int i = 1; i < arr.length; i++) {
            arr[i] = new Element(i, i, i);
        }
        boolean t = true;
        while (t) {
            String str = sc.nextString(5);
            switch (str) {
                case "":
                    t = false;
                    break;
                case "union":
                    union(Integer.parseInt(sc.nextString(10)), Integer.parseInt(sc.nextString(10)));
                    break;
                case "get":
                    int a = get(Integer.parseInt(sc.nextString(10)));
                    prw.print(arr[a].min);
                    prw.print(" ");
                    prw.print(arr[a].max);
                    prw.print(" ");
                    prw.print(arr[a].amount);
                    prw.println();
                    break;
                default:
                    System.out.println("gg.wp");
                    break;
            }
        }

        in.close();
        prw.close();
        out.close();
    }

    private static void union(int x, int y) {
        int a = get(x);
        int b = get(y);
        if (a == b) {
            return;
        }
        if (arr[a].amount > arr[b].amount) {
            arr[b].parent = a;
            arr[a].min = Math.min(arr[b].min, arr[a].min);
            arr[a].max = Math.max(arr[b].max, arr[a].max);
            arr[a].amount = arr[b].amount + arr[a].amount;
        } else{
            arr[a].parent = b;
            arr[b].min = Math.min(arr[b].min, arr[a].min);
            arr[b].max = Math.max(arr[b].max, arr[a].max);
            arr[b].amount = arr[b].amount + arr[a].amount;
        }
    }
    private static int get(int x) {
        if (arr[x].parent != x) {
            arr[x].parent = get(arr[x].parent);
        }
        return arr[x].parent;
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
        public String nextString(int maxSize) {
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
        public int nextInt() {
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
            } catch (Exception e) {}
            return ret;
        }

        private void fillBuffer() {
            try {
                bytesRead = din.read(buffer, bufferPointer =  0, BUFFER_SIZE);
            } catch (Exception e) {}
            if (bytesRead == -1) buffer[ 0] = -1;
        }

        private byte read() {
            if (bufferPointer == bytesRead) fillBuffer();
            return buffer[bufferPointer++];
        }
    }
    static class Element{
        int min;
        int max;
        int parent;
        int amount = 1;
        Element(int min, int max, int parent) {
            this.min = min;
            this.max = max;
            this.parent = parent;
        }
    }
}
