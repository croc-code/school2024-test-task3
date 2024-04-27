package com.aikido;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите абсолютный путь к HTML файлу:");
        String path = in.nextLine();
        System.out.println(getResult(path));
    }

    /*
    * This method receives an absolute path to the .html file.
    * If a such file exists returns Elements object where each Element is a html tag contains "href" attribute.
    * As a result method returns all possible lines with external inks.
    * */
    private static Elements getTags(String path) throws IOException {
        try{
            File file = new File(path);
            Document document = Jsoup.parse(file,"UTF-8");
            return document.select("[href]");

        }catch (FileNotFoundException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    /*
    * This method receives Elements object with possible links.
    * Regular expressions choose only links by removing the "http://" and "https://" prefix.
    * Returns a set of unique links.
    * */
    private static Set<String> getUniqueLinks(Elements tags){
        if(tags == null || tags.size() < 1){
            return null;
        }

        Set<String> links = new HashSet();
        Pattern pattern = Pattern.compile("(?:https?://)(.+?)(?=\")");

        for (Element tag: tags) {
            Matcher matcher = pattern.matcher(tag.toString());
            if(matcher.find()){
                links.add(matcher.group(1));
            }
        }

        return links;
    }

    /*
    * This method receives a set of links.
    * Returns a set of unique domain names by parsing links.
    * */
    private static Set<String> getUniqueDomains(Set<String> links){
        if(links == null){
            return null;
        }

        Set<String> domains = new HashSet<>();

        for (String link: links) {
            String[] dom = link.split("/");

            if (dom[0].contains("www.")){
               dom[0] = dom[0].replace("www.","");
            }

            dom[0] = dom[0].toLowerCase();
            domains.add(dom[0]);
        }

        return domains;
    }

    /*
    * This method receives a set of domain names.
    * Returns a JSON-formatted string containing all domain names.
    * */
    private static String jsonShaper(Set<String> domains){
        if(domains == null){
            return "{«sites»: []}";
        }

        String[] doms = domains.toArray(new String[0]);
        Arrays.sort(doms);

        StringBuilder json = new StringBuilder();
        json.append("{«sites»: [");

        for (int i =0; i<doms.length;i++) {
            json.append("«").append(doms[i]).append("»");
            if(i<doms.length-1){
                json.append(", ");
            }
        }

        json.append("]}");

        return json.toString();
    }

    /*
    * This is a resulting method.
    * Receives an absolute path to an HTML file and calls other private methods to retrieve the result.
    * */
    public static String getResult(String path) throws IOException {
        Elements tags = getTags(path);
        Set<String> uniqueLinks = getUniqueLinks(tags);
        Set<String> uniqueDomains = getUniqueDomains(uniqueLinks);

        return jsonShaper(uniqueDomains);
    }
}