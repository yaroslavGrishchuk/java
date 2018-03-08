package crawler;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class SimpleWebCrawler implements WebCrawler {
    Deque<URLnDeep> stack = new ArrayDeque<>();
    Downloader downl;
    HashMap<String, Page> map = new HashMap<>();
    HashMap<String, Image> photoMap = new HashMap<>();
    SimpleWebCrawler(Downloader zpd) {
        this.downl = zpd;
    }
    @Override
    public Page crawl(String address, int depth) {
        int numberOfPicture = 0;
        String url = address;
        stack.addFirst(new URLnDeep(url, depth, null));
        while (!stack.isEmpty()) {
            url = stack.getFirst().url;
            if (stack.getFirst().deep != 0 && !map.containsKey(url)) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(downl.download(url), "UTF8"))) {
                    try {
                        int prev = in.read();
                        int now = in.read();

                        StringBuilder title = new StringBuilder();
                        StringBuilder linkImg = new StringBuilder();
                        StringBuilder href = new StringBuilder();
                        List<String> linksImages = new ArrayList<>();
                        List<String> hrefs = new ArrayList<>();
                        while (now != -1) {
                            if (prev == '<') {
                                if (now == 'a') {
                                    prev = now;
                                    now = in.read();
                                    if (Character.isWhitespace(now)) {
                                        while (now != '>') {
                                            if (parse(in, "href")) {
                                                while (now != '"') {
                                                    prev = now;
                                                    now = in.read();
                                                }
                                                prev = now;
                                                now = in.read();
                                                while (now != '"') {
                                                    if ((char) now == '#') {
                                                        href = new StringBuilder(merge(url, href.toString()));
                                                        //href.append('/');
                                                        break;
                                                    }
                                                    href.append((char) now);
                                                    prev = now;
                                                    now = in.read();
                                                }
                                                hrefs.add(merge(url, href.toString().replaceAll("&lt;", "<")
                                                        .replaceAll("&gt;", ">").replaceAll("&amp;", "&")
                                                        .replaceAll("&mdash;", "\u2014").replaceAll("&nbsp;", "\u00A0")));
                                                href = new StringBuilder();
                                            } else {
                                                now = in.read();
                                            }
                                        }
                                        continue;
                                    }
                                } else if (now == 'i') {
                                    if (parse(in, "mg")) {
                                        prev = now;
                                        now = in.read();
                                        if (Character.isWhitespace(now)) {
                                            while (now != '>') {
                                                if (parse(in, "src")) {

                                                    while (now != '"') {
                                                        prev = now;
                                                        now = in.read();
                                                    }
                                                    prev = now;
                                                    now = in.read();
                                                    while (now != '"') {
                                                        linkImg.append((char) now);
                                                        prev = now;
                                                        now = in.read();
                                                    }
                                                    linksImages.add(merge(url, linkImg.toString().replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                                                            .replaceAll("&amp;", "&").replaceAll("&mdash;", "\u2014")
                                                            .replaceAll("&nbsp;", "\u00A0")));
                                                    linkImg = new StringBuilder();
                                                    break;
                                                } else {
                                                    now = in.read();
                                                }
                                            }
                                            continue;
                                        }
                                    }
                                } else if (now == 't') {
                                    if (parse(in, "itle")) {
                                        prev = 'e';
                                        now = in.read();
                                        if (!Character.isLetter(now)) {
                                            while (now != '>') {
                                                prev = now;
                                                now = in.read();
                                            }
                                            prev = now;
                                            now = in.read();
                                            while (now != '<') {
                                                title.append((char) now);
                                                prev = now;
                                                now = in.read();
                                            }
                                            title = new StringBuilder(title.toString().replaceAll("&lt;", "<")
                                                    .replaceAll("&gt;", ">").replaceAll("&amp;", "&")
                                                    .replaceAll("&mdash;", "\u2014").replaceAll("&nbsp;", "\u00A0"));
                                            continue;
                                        }
                                    }
                                } else if (now == '!') {
                                    if (in.read() == '-' && in.read() == '-') {
                                        prev = now;
                                        now = in.read();
                                        while (now != '>') {
                                            while (prev != '-' && now != '-') {
                                                prev = now;
                                                now = in.read();
                                            }
                                            prev = now;
                                            now = in.read();
                                        }
                                    }

                                }
                            }
                            prev = now;
                            now = in.read();
                        }

                        map.put(url, new Page(url, title.toString()));
                        Page page = map.get(url);
                        if (map.get(stack.getFirst().parent) != null) {
                            map.get(stack.getFirst().parent).addLink(page);
                        }
                        int beg = 0;
                        int end = 0;
                        for (int i = 0; i < linksImages.size(); i++) {
                            if (!photoMap.containsKey(linksImages.get(i))) {
                                try (InputStream bfr = (downl.download(linksImages.get(i)))) {
                                    beg = linksImages.get(i).lastIndexOf('.');
                                    end = beg + 1;
                                    for (; end < linksImages.get(i).length() && Character.isLetter(linksImages.get(i).charAt(end)); end++) {
                                        //do nothing
                                    }
                                    if (end == -1) {
                                        end = linksImages.get(i).length();
                                    }
                                    try (PrintStream prw = new PrintStream("image" + numberOfPicture + linksImages.get(i).substring(beg, end))) {
                                        byte[] buffer = new byte[1 << 16];
                                        int read;
                                        while ((read = bfr.read(buffer)) != -1) {
                                            for (int j = 0; j < read; j++) {
                                                prw.write(buffer[j]);
                                            }
                                        }
                                    } catch (IOException e) {
                                        System.out.println(e.getMessage());
                                    }
                                } catch (MalformedURLException e) {
                                    System.out.println("MalformedURLException" + e.getMessage());
                                } catch (IOException e) {
                                    System.out.println("IOException" + e.getMessage());
                                }
                                photoMap.put(linksImages.get(i), new Image(linksImages.get(i), "image" + numberOfPicture +
                                        linksImages.get(i).substring(beg, end)));
                                numberOfPicture++;
                            }
                            page.addImage(photoMap.get(linksImages.get(i)));

                        }
                        for (int i = 0; i < hrefs.size(); i++) {
                            stack.addLast(new URLnDeep(hrefs.get(i), stack.getFirst().deep - 1, url));
                        }


                    } catch (NullPointerException e) {
                        System.out.println("NullPointerException " + e.getMessage());
                        map.get(stack.getFirst().parent).addLink(new Page(url, ""));
                    }
                } catch (UnsupportedEncodingException e) {
                    System.out.println("unsEE" + e.getMessage());
                } catch (MalformedURLException e) {
                    System.out.println("MAl" + e.getMessage());
                } catch (IOException e) {
                    System.out.println("IOException " + e.getMessage());
                    if (!map.containsKey(url)) {
                        map.put(url, new Page(url, ""));
                    }
                    map.get(stack.getFirst().parent).addLink(map.get(stack.getFirst().url));
                }
            } else if (map.containsKey(url)) {
                if (!map.containsKey(url)) {
                    map.put(url, new Page(url, ""));
                }
                map.get(stack.getFirst().parent).addLink(map.get(stack.getFirst().url));
            } else {
                map.put(stack.getFirst().url, new Page(stack.getFirst().url, ""));
                map.get(stack.getFirst().parent).addLink(map.get(stack.getFirst().url));
            }
            stack.removeFirst();
        }
        return (map.get(address));
    }


    String merge(String urlLeft, String urlRight) throws MalformedURLException {
        return (new URL(new URL(urlLeft), urlRight)).toString();
    }

    class URLnDeep {
        String url;
        int deep;
        String parent;
        URLnDeep (String url, int deep, String parent) {
            this.deep = deep;
            this.url = url;
            this.parent = parent;
        }
    }

    boolean parse(BufferedReader in, String str) throws IOException {
        int c = in.read();
        while (Character.isWhitespace(c)) {
            c = in.read();
        }
        for (int i = 0; i < str.length(); i++) {

            if (str.charAt(i) != c) {
                while (!Character.isWhitespace(c) && c != '<' && c != '>') {
                    in.mark(1);
                    c = in.read();
                }
                in.reset();
                return false;
            }
            if (i != str.length() - 1) {
                c = in.read();
            }
            in.mark(1);
        }
        return true;
    }

}
