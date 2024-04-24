package ru.wwerlosh.service;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import ru.wwerlosh.converter.DomainJsonConverter;
import ru.wwerlosh.converter.JsonConverter;
import ru.wwerlosh.crawler.DomainExtractor;
import ru.wwerlosh.crawler.HtmlDomainExtractor;
import ru.wwerlosh.exception.InvalidDocumentTypeException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HtmlDomainProcessorTest {

    @Test
    void process() {
        JsonConverter<String> converter = new DomainJsonConverter<>();
        DomainExtractor extractor = new HtmlDomainExtractor();
        DomainProcessor processor = new HtmlDomainProcessor(extractor, converter);

        String expectedJson = "{\n" +
                "\"sites\": [\"blogs.oracle.com\", \"career.habr.com\", \"company.habr.com\", \"docs.oracle.com\", \"dzen.ru\", \"effect.habr.com\", \"facebook.com\", \"google.com\", \"habr.com\", \"jcp.org\", \"ru.wikipedia.org\", \"telegram.me\", \"translit.net\", \"tutorials.jenkov.com\", \"twitter.com\", \"vk.com\", \"youtube.com\"]\n" +
                "}";

        String result = processor.process(new File("page.html"));
        assertEquals(expectedJson, result);
    }
}