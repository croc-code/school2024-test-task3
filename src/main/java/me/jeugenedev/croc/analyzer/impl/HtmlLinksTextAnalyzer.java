package me.jeugenedev.croc.analyzer.impl;

import me.jeugenedev.croc.analyzer.NextNotFoundException;
import me.jeugenedev.croc.analyzer.TextAnalyzer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlLinksTextAnalyzer extends TextAnalyzer {
    /**
     * Паттерн для поиска соответствий вида {@code ключ="ссылка"}.
     */
    private static final Pattern ATTR_WITH_LINK_PATTERN = Pattern
            .compile("[\\w.\\-]+=([\"'])(\\w+://([\\w.\\-]+)/?\\S*)\\1");

    /**
     * Порядковый номер группы, в которой находится доменное имя. Соответствует паттерну
     * {@code ATTR_WITH_LINK_PATTERN}.
     */
    private static final int GROUP_DOMAIN = 3;

    /**
     * Глобальный {@link Matcher} для реализации итеративного поведения объекта
     * класса {@link HtmlLinksTextAnalyzer}.
     */
    private Matcher globalMatcher;

    /**
     * Создает текстовый анализатор ссылок на внешние ресурсы в html-документах
     * для конкретной последовательности символов.
     *
     * @param text Последовательность символов
     */
    public HtmlLinksTextAnalyzer(CharSequence text) {
        super(text);
    }

    /**
     * Поиск всех значений по тексту {@code getText()} и паттерну {@code ATTR_WITH_LINK_PATTERN}.
     * @return Немодифицируемый {@link Collection} с найденными значениями
     */
    @Override
    public Collection<? extends CharSequence> findAll() {
        List<CharSequence> matches = new ArrayList<>();
        Matcher linkMatcher = ATTR_WITH_LINK_PATTERN.matcher(getText());

        while (linkMatcher.find()) {
            matches.add(linkMatcher.group(GROUP_DOMAIN));
        }

        return Collections.unmodifiableList(matches);
    }

    /**
     * Выполняет поиск следующего совпадения по паттерну {@code ATTR_WITH_LINK_PATTERN}
     * из текста {@code getText()}.
     * @return {@link CharSequence}
     * @throws NextNotFoundException Если следующее значение не может быть найдено
     */
    @Override
    public CharSequence next() {
        if (this.globalMatcher == null) {
            this.globalMatcher = ATTR_WITH_LINK_PATTERN.matcher(getText());
        }

        if (this.globalMatcher.find()) {
            return this.globalMatcher.group(GROUP_DOMAIN);
        }

        throw new NextNotFoundException(this);
    }
}
