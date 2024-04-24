package ru.wwerlosh.crawler;

import java.io.File;
import java.util.Set;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import ru.wwerlosh.crawler.HtmlDomainExtractor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HtmlDomainExtractorTest {
    @Test
    void testExtractDomains() {
        File doc = new File("page.html");

        HtmlDomainExtractor domainExtractor = new HtmlDomainExtractor();

        Set<String> domains = domainExtractor.extractDomains(doc);
        assertTrue(domains.contains("ru.wikipedia.org"));
        assertTrue(domains.contains("tutorials.jenkov.com"));
    }
}
