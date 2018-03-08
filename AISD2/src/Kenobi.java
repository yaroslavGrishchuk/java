import java.io.*;

public class Kenobi {
    public static void main(String[] args) throws IOException {
        FileInputStream in = new FileInputStream("kenobi.in ");
        Parser sc = new Parser(in);
        PrintWriter out = new PrintWriter(new File("kenobi.out"), "utf8");
        PrintWriter prw = new PrintWriter(out);
        int n = sc.nextInt();
        LinkedListStack<Integer> list = new LinkedListStack<>();
        for (int i = 0; i < n; i++) {
            String str = sc.nextString(4);
            switch (str) {
                case "add":
                    list.push(sc.nextInt());
                    break;
                case "take":
                    list.pop();
                    break;
                default:
                    list.exchange();
                    break;
            }
        }
        prw.println(list.size());
        prw.println(list);
        prw.close();
        in.close();
        out.close();
    }
    public static class LinkedListStack<T> {

        private final LinkedList<T> linkedList = new LinkedList<>();

        void push(T data) {
            linkedList.addFirst(data);
        }

        void pop() {
            linkedList.removeFirst();
        }

        int size() {
            return linkedList.size;
        }
        @Override
        public String toString() {
            return linkedList.toString();
        }
        void exchange() {
            linkedList.exchangeCentre();
        }
    }
    static class LinkedList<T> {
        int size = 0;
        private static class Node<T> {

            private T data;
            private Node<T> next;
            private Node<T> prev;

            Node(T data) {
                this.data = data;
            }

            @Override
            public String toString() {
                return data.toString();
            }
        }

        private Node<T> first = null;
        private Node<T> last = null;
        private Node<T> centre = null;

        void addFirst(T data) {
            Node<T> newFirst = new Node<>(data);
            newFirst.next = first;
            newFirst.prev = null;
            first = newFirst;
            if (size == 0) {
                last = newFirst;
                centre = newFirst;
            }
            if (first.next != null) {
                first.next.prev = first;
            }
            size++;
            if (size % 2 == 0) {
                centre = centre.prev;
            }
        }

        void removeFirst() {
            //System.out.println(last.prev + " " + last.next + " " + centre.prev + " " + centre.next + " " + first.prev + " " + first.next);
            first = first.next;
            if (size != 1) {
                first.prev = null;
            }
            size--;
            if (size <= 1) {
                centre = first;
            } else if (size % 2 != 0) {
                centre = centre.next;
            }

        }
        void exchangeCentre() {
            if (size > 1) {
                if (size % 2 != 0) {
                    Node<T> helper = last;
                    Node<T> helperSec = centre.next;
                    last.next = first;
                    last = centre;
                    centre = first;
                    first = helperSec;
                    first.prev = null;
                    last.next = null;
                    centre.prev = helper;
                } else {
                    Node<T> helper = last;
                    Node<T> helperSec = centre.next;
                    Node<T> helperTrd = first;
                    last.next = first;
                    last = centre;
                    centre = helper;
                    first = helperSec;
                    first.prev = null;
                    last.next = null;
                    centre.next = helperTrd;
                }
            }
        }

        @Override
        public String toString() {
            if (size > 0) {
                StringBuilder listBuilder = new StringBuilder();
                Node currentNode = first;
                while (currentNode != null) {
                    listBuilder.append(currentNode).append(" ");
                    currentNode = currentNode.next;
                }
                listBuilder.deleteCharAt(listBuilder.length() - 1);
                return listBuilder.reverse().toString();
            } else return "";
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
