package ru.wwerlosh.service;

import java.io.File;
import java.util.Set;
import ru.wwerlosh.converter.JsonConverter;
import ru.wwerlosh.crawler.DomainExtractor;

public class HtmlDomainProcessor implements DomainProcessor {
    private final DomainExtractor domainExtractor;
    private final JsonConverter<String> jsonConverter;

    public HtmlDomainProcessor(DomainExtractor domainExtractor, JsonConverter<String> jsonConverter) {
        this.domainExtractor = domainExtractor;
        this.jsonConverter = jsonConverter;
    }

    @Override
    public String process(File doc) {
        Set<String> domains = domainExtractor.extractDomains(doc);
        return jsonConverter.convert(domains);
    }
}