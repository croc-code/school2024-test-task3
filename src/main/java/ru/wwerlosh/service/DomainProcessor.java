package ru.wwerlosh.service;

import java.io.File;
import org.jsoup.nodes.Document;

public interface DomainProcessor {
    /**
     * Обрабатывает домены в файле.
     *
     * @param doc Файл для обработки доменов.
     * @return Результат обработки в виде JSON строки.
     */
    String process(File doc);
}
