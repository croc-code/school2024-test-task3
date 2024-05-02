import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class LinkExtractor {
    public String getLinksFromHtml() throws IOException {
        // Указываю путь к файлу с данными которые буду читать
        var fileName = "solution/data/dailydev.html";
        // Создаю экземпляр класса ObjectMapper для того, чтобы в конце создать строку с результатом
        var objectMapper = new ObjectMapper();
        // Создаю сет для хранения ссылок
        Set<String> links = new HashSet<>();
        // Загружаю html файл в объект "doc" с помощью Jsoup, который позволит парсить html.
        var doc = Jsoup.parse(new File(fileName), "UTF-8");
        // Извлекаю все элементы link, у которых есть атрибут "href" в переменную elements
        var elements = doc.select("link[href]");
        // Создаю цикл с помощью которого получаю значение атрибута href для текущего link
        for (Element element : elements) {
            String href = element.attr("href");
            //Проверяю что ссылка не заканчивается на .json, .png и .css, т.к. такие ссылки мне не подходят
            if (!href.endsWith(".json")&& !href.endsWith(".png") && !href.endsWith(".css")) {
                // Добавляю ссылку в сет
                links.add(element.attr("href"));

            }
        }
        // Создаю список в который перезапишу сет с некоторой корректировкой
        List<String> finalLinks = new ArrayList<>();
        // Прохожусь по сету
        for (String link : links) {
            // Убираю часть с протоколом из ссылки
            var processedLink = link.replace("https://", "");
            // Если в конце ссылки есть "/", то её тоже убираю
            if (processedLink.endsWith("/")) {
                processedLink = processedLink.substring(0, processedLink.length() - 1);
            }
            // Записываю получившиеся ссылки в список
            finalLinks.add(processedLink);
        }
        // Сортирую их в алфавитном порядке
        Collections.sort(finalLinks);
        // Записываю результат в переменную согласно требованиям
        var result = "{\"sites\": " + objectMapper.writeValueAsString(finalLinks) + "}";
        // Возвращаю результат
        return result;
    }
}
