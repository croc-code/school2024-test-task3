package ru.wwerlosh.crawler;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlDomainExtractor implements DomainExtractor {
    @Override
    public Set<String> extractDomains(File doc) {
        Document document = null;
        try {
            document = Jsoup.parse(doc, "UTF-8", "");
        } catch (IOException e) {
            System.err.println("Ошибка: Файл не найден или не может быть прочитан.");
            System.exit(1);
        }


        Set<String> domains = new HashSet<>();
        Elements links = document.select("a[href]");
        for (Element link : links) {
            String url = link.absUrl("href");
            String domain = extractDomain(url);
            domains.add(domain.toLowerCase());
        }
        return domains;
    }

    private String extractDomain(String url) {
        String[] parts = url.split("/");
        String domainPart = parts[2];
        if (domainPart.startsWith("www.")) {
            domainPart = domainPart.substring(4);
        }
        return domainPart;
    }
}
