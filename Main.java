package croc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        // Если не передан путь к файлу в командной строке, завершаем работу программы
        if (args.length == 0) {
            System.out.println("Usage: java Main <html_file>");
            return;
        }

        // Получаем из аргументов путь к файлу, запускаем нашу функцию, печатаем результат в консоль
        String htmlFile = args[0];
        try {
            String result = findUniqueDomains(htmlFile);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String findUniqueDomains(String htmlFile) throws IOException {
        // Получение текстового представления html-файла
        String htmlContent = Files.readString(Paths.get(htmlFile));

        // Компиляция паттерна и матчинг по тексту
        Pattern linkPattern = Pattern.compile("(?:[\\p{L}\\d-]+\\.)+[\\p{L}\\d-]+");
        Matcher matcher = linkPattern.matcher(htmlContent);

        Set<String> uniqueDomains = new HashSet<>();
        // Пока находятся совпадения с паттерном, добавляем их в множество
        // Если домен начинается с 'www.', удаляем данную часть из домена
        while (matcher.find()) {
            String domain = matcher.group();
            if (domain.startsWith("www.")) {
                domain = domain.substring(4);
            }
            uniqueDomains.add(domain.toLowerCase());
        }

        // Приводим с списку найденные домены и сортируем по условию задачи
        List<String> domainsList = new ArrayList<>(uniqueDomains);
        domainsList.sort(String::compareTo);

        // Создаём JSON-строку формата, заданного в условии
        StringBuilder jsonBuilder = new StringBuilder("{\"sites\": [");
        boolean first = true;
        for (String domain : domainsList) {
            if (!first) {
                jsonBuilder.append(",");
            } else {
                first = false;
            }
            jsonBuilder.append("\"").append(domain).append("\"");
        }
        jsonBuilder.append("]}");

        return jsonBuilder.toString();
    }
}

