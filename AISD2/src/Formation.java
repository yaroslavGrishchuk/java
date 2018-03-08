import java.io.*;

public class Formation {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        FileInputStream in = new FileInputStream("kenobi.in");
        Parser sc = new Parser(in);
        PrintWriter out = new PrintWriter(new File("kenobi.out"), "utf8");
        PrintWriter prw = new PrintWriter(out);
        int n = sc.nextInt();
        int m = sc.nextInt();
        Soldat[] arr = new Soldat[n + 1];
        for (int i = 0; i < n + 1; i++) {
            arr[i] = new Soldat(0,0);
        }
        for (int i = 0; i < m; i++) {
            String str = sc.nextString(5);
            switch (str) {
                case "left": {
                    int a = sc.nextInt();
                    int b = sc.nextInt();
                    arr[a].left = arr[b].left;
                    arr[a].rigth = b;
                    arr[b].left = a;
                    arr[arr[a].left].rigth = a;
                    break;
                }
                case "right": {
                    int a = sc.nextInt();
                    int b = sc.nextInt();
                    arr[a].rigth = arr[b].rigth;
                    arr[a].left = b;
                    arr[b].rigth = a;
                    arr[arr[a].rigth].left = a;
                    break;
                }
                case "leave": {
                    int a = sc.nextInt();
                    arr[arr[a].left].rigth = arr[a].rigth;
                    arr[arr[a].rigth].left = arr[a].left;
                    arr[a].left = 0;
                    arr[a].rigth = 0;
                    break;
                }
                default: {
                    int a = sc.nextInt();
                    prw.print(arr[a].left);
                    prw.print(" ");
                    prw.print(arr[a].rigth);
                    prw.println();
                    break;
                }
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

    static class Soldat{
        int left;
        int rigth;
        Soldat(int rigth, int left) {
            this.left = left;
            this.rigth = rigth;
        }
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
