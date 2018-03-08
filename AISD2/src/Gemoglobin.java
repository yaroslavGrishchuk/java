import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Gemoglobin {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        Stack<Long> stack = new Stack<>();
        List<Long> list = new LinkedList<>();
        FileInputStream in = new FileInputStream("hemoglobin.in");
        Parser sc = new Parser(in);
        PrintWriter out = new PrintWriter(new File("hemoglobin.out"), "utf8");
        PrintWriter prw = new PrintWriter(out);
        int n = sc.nextInt();

        for (int i = 0; i < n; i++) {
            String str = sc.nextString(15);
            if(str.charAt(0) == '+') {
                long a = Long.parseLong(str.substring(1,str.length()));
                stack.push(a);
                if (list.isEmpty()) {
                    list.add(0, a);
                } else list.add(0,list.get(0) + a);
            } else if(str.charAt(0) == '?') {
                int a = Integer.parseInt(str.substring(1,str.length()));
                if (a == list.size()) {
                    prw.println(list.get(0));
                } else prw.println(list.get(0) - list.get(a));
            } else {
                prw.println(stack.pop());
                list.remove(0);
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
        public long nextLong() {
            long ret =  0;
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
            } catch (Exception ignored) {}
            if (bytesRead == -1) buffer[ 0] = -1;
        }

        private byte read() {
            if (bufferPointer == bytesRead) fillBuffer();
            return buffer[bufferPointer++];
        }

    }
}
