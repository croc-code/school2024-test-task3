package ru.wwerlosh;

import java.io.File;
import ru.wwerlosh.converter.DomainJsonConverter;
import ru.wwerlosh.converter.JsonConverter;
import ru.wwerlosh.crawler.DomainExtractor;
import ru.wwerlosh.crawler.HtmlDomainExtractor;
import ru.wwerlosh.service.DomainProcessor;
import ru.wwerlosh.service.HtmlDomainProcessor;

public class App {
    public static void main( String[] args ) {
        if (args.length != 1) {
            System.out.println("Usage: java App <path_to_page_html>");
            return;
        }

        String filePath = args[0];

        JsonConverter<String> converter = new DomainJsonConverter<>();
        DomainExtractor extractor = new HtmlDomainExtractor();
        DomainProcessor processor = new HtmlDomainProcessor(extractor, converter);

        System.out.println(processor.process(new File(filePath)));
    }
}
