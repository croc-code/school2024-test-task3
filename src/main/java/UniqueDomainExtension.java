import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

import DTO.Result;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

public class UniqueDomainExtension {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java UniqueDomainExtension <htmlFilePath>");
            return;
        }
        String htmlFilePath = args[0]; // "src/main/resources/croc.html"
        try {
            Set<String> uniqueDomains = findUniqueDomains(htmlFilePath);
            String jsonResult = toJson(uniqueDomains);
            System.out.println(jsonResult);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    // Метод для поиска уникальных доменов в HTML-файле
    public static Set<String> findUniqueDomains(String htmlFilePath) throws IOException, URISyntaxException {
        Set<String> uniqueDomains = new HashSet<>();
        File input = new File(htmlFilePath);
        Document doc = Jsoup.parse(input, "UTF-8");

        Elements links = doc.select("a[href]");

        for (Element link : links) {
            String href = link.attr("href");

            try {
                String domain = getDomain(href);
                if (domain != null) {
                    uniqueDomains.add(domain);
                }
            } catch (URISyntaxException e) {
                // Если не удается распарсить => не является внешней ссылкой => игнорируем
            }
        }

        return uniqueDomains;
    }

    // Метод для получения домена из URL
    private static String getDomain(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String scheme = uri.getScheme(); // mailto или http/https/mailto/ftp/ws/wss/ldap... :
        if (scheme==null) return null;

        if (scheme.equals("mailto")) {
            return uri.getSchemeSpecificPart().split("@")[1]; // Извлекаем домен почты
        } else {
            String host = uri.getHost(); // Извлекаем домен
            if (host == null) return null;
            // Удаление "www." если есть
            if (host.startsWith("www.")) {
                host = host.substring(4);
            }
            return host.toLowerCase();
        }
    }

    // Метод для преобразования в JSON
    public static String toJson(Set<String> domains) {
        ArrayList<String> sortedDomains = new ArrayList<>(domains);
        Collections.sort(sortedDomains);

        Gson gson = new Gson();
        return gson.toJson(new Result(sortedDomains));
    }


}
