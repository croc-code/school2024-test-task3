package ru.barkhatnat;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String fileName;
        do {
            System.out.println("Enter file name with it's extension: ");
            fileName = scanner.next();
            String path = "src/main/resources/" + fileName;
            File file = new File(path);
            try {
                if (fileName.endsWith(".html")) {
                    if (file.exists() && file.isFile()) {
                        String htmlPageToString = Files.readString(Path.of(file.toURI()));
                        Document html = Jsoup.parse(htmlPageToString);
                        Set<String> visitedDomains = new HashSet<>();
                        System.out.println(search(html, visitedDomains));
                        break;
                    } else {
                        System.out.println("File not found.");
                    }
                } else {
                    System.out.println("Incorrect file extension. Please enter a file with the extension .html");
                }
            } catch (Exception e) {
                System.out.println("An error occurred.");
            }
        } while (true);
        scanner.close();
    }

    //Основной метод поиска
    public static String search(Document htmlDocument, Set<String> visitedDomains) {
        Set<String> domains = new HashSet<>();
        Elements links = htmlDocument.select("a");
        for (Element link : links) {
            processLink(link, domains, visitedDomains);
        }
        return toJson(domains).toString();
    }

    //Метод обработки домена под установленные требования
    public static String urlToDomain(URL url) {
        String domain = url.getHost();
        if (domain.startsWith("www.")) {
            domain = domain.substring(4);
        }
        return domain.toLowerCase();
    }

    //Метод выявления валидных url
    public static URL stringToUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException | IllegalArgumentException e) {
            return null;
        }
    }

    //Метод получения домена из URL
    public static void processLink(Element link, Set<String> domains, Set<String> visitedDomains) {
        String linkHref = link.attr("href");
        URL url = stringToUrl(linkHref);
        if (url != null) {
            String domain = urlToDomain(url);
            String protocol = url.getProtocol();
            //Случай протокола mailto
            if (protocol.equals("mailto")) {
                String email = linkHref.replace("mailto:", "");
                domain = email.substring(email.indexOf('@') + 1);
            }
            if (!visitedDomains.contains(domain)) {
                domains.add(domain);
                visitedDomains.add(domain);
            }
        }
    }

    //Метод преобразования множества в JSON-формат
    public static JSONObject toJson(Set<String> hashset) {
        JSONObject json = new JSONObject();
        TreeSet<String> sortedDomains = new TreeSet<>(hashset);
        json.put("sites", sortedDomains);
        return json;
    }

}
