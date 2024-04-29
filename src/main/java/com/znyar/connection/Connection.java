package com.znyar.connection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Connection
{
    public static Document getURLSource(String url) {
        try {
            return Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                    .get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
