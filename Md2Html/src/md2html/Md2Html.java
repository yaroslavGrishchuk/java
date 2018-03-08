package md2html;


import java.io.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class Md2Html {
    public static void main(String[] args) {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), "UTF-8"))) {
            try (PrintWriter out = new PrintWriter(args[1], "UTF-8")) {
                Deque<Vec2i> stack = new ArrayDeque<>();
                final char[] markSet = {'`', '-', '_', '*', '+', '~' };
                final String[][] htmlSet = {{"code"}, {"s"}, {"em","strong"}, {"em", "strong"}, {"u"},{"mark"}};
                final int[][] quanity = {{1},{2},{1,2},{1,2},{2},{1}};
                int prev = '\n';
                int now = '\n';
                boolean first = true;

                while (now != -1) {
                    if ((char) now == '\r') {
                        now = in.read();
                    }
                    if ((char) now == '\n' && (char) prev == '\n') {
                        if (!first) {
                            if (stack.peek().x != -1) {
                                throw new RuntimeException("tag of type \'"+ htmlSet[stack.peek().x][stack.peek().y] + "\' not closed" );
                            } else {
                                out.print("</");
                                int h = stack.pop().y;
                                if (h == 0) {
                                    out.print("p");
                                } else {
                                    out.print("h");
                                    out.print(h);
                                }
                                out.print(">");
                                out.println();
                            }
                        } else {
                            first = false;
                        }
                        while (now != -1 && (now == '\n' || now == '\r')) {
                            now = in.read();
                        }
                        if (now == -1) {
                            break;
                        } else if ((char) now == '#') {
                            int i = 0;
                            while (i <= 6 && (char) now == '#') {
                                now = in.read();
                                i++;
                            }
                            if (Character.isWhitespace(now)) {
                                stack.push(new Vec2i(-1,i));
                                out.print("<h");
                                out.print(i);
                                out.print(">");
                                now = in.read();
                                continue;
                            } else {
                                out.print("<p>");
                                stack.push(new Vec2i(-1,0));
                                for (int j = 0; j < i; j++) {
                                    out.print("#");
                                }
                            }

                        } else {
                            stack.push(new Vec2i(-1, 0));
                            out.print("<p>");
                        }
                    }
                    //indexOf применен не корректно! требуется свой метод contained или альтернатива, также в паре строчек ниже
                    int index =  java.util.Arrays.asList(markSet).indexOf((char) now);
                    if (index != -1) {
                        int iter = 0;
                        while ((char) now == markSet[index]) {
                            iter++;
                            now = in.read();
                        }
                        if (!((Character.isWhitespace(now) || (char) now == '\n' || (char) now == '\r') && Character.isWhitespace(prev) && iter == 1 && (markSet[index] == '*' || markSet[index] == '_'))) {
                            int indOfNum = Arrays.asList(quanity[index]).indexOf(iter);
                            if (indOfNum != -1) {
                                if (!stack.isEmpty() && stack.peek().x == index && stack.peek().y == indOfNum) {
                                    out.print("</");
                                    out.print(htmlSet[index][indOfNum]);
                                    out.print(">");
                                    stack.pop();
                                } else {
                                    out.print("<");
                                    out.print(htmlSet[index][indOfNum]);
                                    out.print(">");
                                    stack.push(new Vec2i(index, indOfNum));
                                }
                            } else {
                                for (int i = 0; i < iter; i++) {
                                    out.print(markSet[index]);
                                    prev = markSet[index];
                                }
                            }
                        } else {
                            out.print(markSet[index]);
                        }


                        continue;
                    } else if ((char) now == '\\') {
                        now = in.read();
                    } else if ((char) now == '>') {
                        out.print("&gt");
                        now = ';';
                    } else if ((char) now == '<') {
                        out.print("&lt");
                        now = ';';
                    } else if ((char) now == '&') {
                        out.print("&amp");
                        now = ';';
                    }
                    prev = now;
                    now = in.read();
                    if (now == '\r') {
                        now = in.read();
                    }
                    if (!((char) now == '\n' && prev == '\n') && !(now == -1 && prev == '\n')) {
                        out.print((char) prev);
                    }
                }
                if (!stack.isEmpty()) {
                    if (stack.peek().x == -1) {
                        if (stack.peek().y == 0) {
                            out.print("</p>");
                        } else {
                            out.print("</h");
                            out.print(stack.peek().y);
                            out.print(">");
                        }
                    } else {
                        throw new RuntimeException("tag of type \'"+ htmlSet[stack.peek().x][stack.peek().y] + "\' not closed" );
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Output file can not be created or changed " + e.getMessage());
            } catch (SecurityException e) {
                System.out.println("Output file is forbidden to be created orr changed by security manager " + e.getMessage());
            } catch (UnsupportedEncodingException e) {
                System.out.println("Encoding error " + e.getMessage());
            } catch (IOException e) {
                System.out.println("IOException " + e.getMessage());
            }
        } catch (FileNotFoundException e) {
            System.out.println("file can not be found " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.out.println("Encoding error " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOExecption " +e.getMessage());
        }
    }

    private static class Vec2i {
        int x;
        int y;
        Vec2i(int x,int y) {
            this.x = x;
            this.y = y;
        }
    }

}
