package ru.wwerlosh.converter;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class DomainJsonConverter<T extends Comparable<T>> implements JsonConverter<T> {

    @Override
    public String convert(Collection<T> objects) {
        Set<T> sortedDomains = new TreeSet<>(objects);
        StringBuilder jsonOutput = new StringBuilder();
        jsonOutput.append("{\n\"sites\": [");
        for (T domain : sortedDomains) {
            jsonOutput.append("\"").append(domain).append("\", ");
        }
        if (!sortedDomains.isEmpty()) {
            jsonOutput.delete(jsonOutput.length() - 2, jsonOutput.length());
        }
        jsonOutput.append("]\n}");
        return jsonOutput.toString();
    }
}
