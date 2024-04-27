package me.jeugenedev.croc.representation;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс {@link DomainManager} для агрегации доменами.
 */
public class DomainManager {
    /**
     * Список, содержащий домены.
     */
    private final List<String> domains = new ArrayList<>();

    /**
     * Добавляет новый домен.
     * @param domain Домен в строковом представлении
     */
    public void addDomain(String domain) {
        this.domains.add(domain);
    }

    /**
     * Переопределяет строковое представление объекта класса {@link DomainManager}.
     * @return Строка в формате JSON
     */
    @Override
    public String toString() {
        return "{\"sites\": " + domains.stream().map(e -> '"' + e + '"').toList() + '}';
    }
}
