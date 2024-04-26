package com.zakharenko;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.Set;

public class Crawler {
    public static void main(String[] args) {
        // Try to extract unique sites from HTML file and convert them to JSON
        try {
            Set<String> uniqueSites = takeUniqueSitesFromHTML("src/main/resources/page.html");
            SetWrapper wrapper = new SetWrapper(uniqueSites);
            String jsonFormat = convertToJson(wrapper);
            System.out.println(jsonFormat);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String convertToJson(SetWrapper setWrapper) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(setWrapper);
    }

    // Method to extract unique sites from HTML file
    private static Set<String> takeUniqueSitesFromHTML(String path) throws IOException {
        Document document = Jsoup.parse((new File(path)), "UTF-8");
        Elements elements = document.select("a[href]");

        Set<String> sites = new TreeSet<>();

        for (Element element : elements) {
            String link = element.attr("abs:href");
            String domain = extractDomain(link);
            if (domain != null) {
                sites.add(domain);
            }
        }
        return sites;
    }

    // Method to extract domain from URL
    private static String extractDomain(String link) {
        try {
            URI uri = new URI(link);
            String host = uri.getHost(); // Get host from URI
            if (host != null) {
                // Remove "www." if present
                return host.startsWith("www.") ? host.substring(4) : host;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}