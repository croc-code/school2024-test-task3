package org.cianid.nadir;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class HttpParser {
    public static void main(String[] args) {
        log.info("Example of methods work:");

        String url = "https://blog.hubspot.com/website/10-best-technology-website-designs-in-2023";
        log.info(extractDomainsFromUrl(url));

        String filePath = "testPage.html";
        log.info(extractDomainsFromHtmlFile(filePath));
    }

    /**
     * @param filePath путь к html файлу страницы (Файл должен находиться в директории resources)
     * @return строка в формате json, содержащая список доменов или пустой список, при ошибке чтения или доступа
     */
    public static String extractDomainsFromHtmlFile(String filePath) {
        try {
            File input = new File("src/main/resources/" + filePath);
            Document doc = Jsoup.parse(input, "UTF-8");

            return extractDomains(doc);
        } catch (IOException e) {
            log.error("Can't read from file " + filePath + ". Returning empty list of domains.");

            return "{«sites»: []}";
        }
    }

    /**
     * @param url ссылка на ресурс
     * @return строка в формате json, содержащая список доменов или пустой список, при ошибке чтения или доступа
     */
    public static String extractDomainsFromUrl(String url) {
        try {
            return extractDomains(Jsoup.connect(url).get());
        } catch (IOException e) {
            log.error("Can't connect to " + url + ". Returning empty list of domains.");

            return "{«sites»: []}";
        }
    }


    private static String extractDomains(Document doc) {
        Elements links = doc.getElementsByTag("a");

        List<String> domains = links.stream()
                .map(link -> link.attr("href"))
                .map(HttpParser::extractDomain)
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        return buildJson(domains);
    }

    private static String buildJson(List<String> domains) {
        StringBuilder jsonBuilder = new StringBuilder("{«sites»: [");

        domains.forEach(domain -> jsonBuilder.append("«").append(domain).append("», "));

        int length = jsonBuilder.length();
        jsonBuilder.replace(length - 2, length, "]}");

        return jsonBuilder.toString();
    }

    private static String extractDomain(String url) {
        try {
            URI uri = new URI(url);
            String domain = uri.getHost();
            if (domain != null) {
                return domain.startsWith("www.") ? domain.substring(4) : domain;
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
