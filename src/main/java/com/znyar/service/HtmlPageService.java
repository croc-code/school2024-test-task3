package com.znyar.service;

import com.znyar.connection.Connection;
import com.znyar.exception.NotFoundException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

@Service
public class HtmlPageService {

    private Set<String> getDomains(String url) {

        Set<String> domains = new HashSet<>();

        Document document = Connection.getURLSource(url);

        try {

            Elements links = document.select("a[href]");

            for (Element link : links) {

                String innerUrl = link.attr("href");

                if (innerUrl.startsWith("https://")) {

                    String domain = new URI(innerUrl).getHost();

                    if (domain.contains("www.")) {
                        domain = domain.replace("www.", "");
                    }

                    domains.add(domain);

                }

            }

            if (domains.isEmpty()) {
                throw new NotFoundException("Sites not found");
            }

            return domains;

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    public String getDomainsJson(String url) {

        JSONObject jsonData = new JSONObject();
        jsonData.put("sites", getDomains(url).stream().sorted().toList());

        return jsonData.toString();
    }

}
