package ru.wwerlosh.converter;

import java.util.Collection;

public interface JsonConverter<T> {
    /**
     * Преобразует коллекцию объектов в строковое представление в формате JSON.
     *
     * @param objects Коллекция объектов для конвертации.
     * @return Строковое представление объектов в формате JSON.
     */
    String convert(Collection<T> objects);
}
