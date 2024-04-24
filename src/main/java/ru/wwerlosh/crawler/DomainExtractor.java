package ru.wwerlosh.crawler;

import java.io.File;
import java.util.Set;

public interface DomainExtractor {
    /**
     * Извлекает домены из файла.
     *
     * @param doc Файл для извлечения доменов.
     * @return Множество строк, представляющих извлеченные домены.
     */
    Set<String> extractDomains(File doc);
}
