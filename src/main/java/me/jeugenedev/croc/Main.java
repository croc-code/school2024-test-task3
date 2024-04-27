package me.jeugenedev.croc;

import me.jeugenedev.croc.analyzer.TextAnalyzer;
import me.jeugenedev.croc.analyzer.impl.HtmlLinksTextAnalyzer;
import me.jeugenedev.croc.representation.DomainManager;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
    }

    public static String findDomainsFromString(String string, Charset cs) {
        ByteArrayInputStream byteArray = new ByteArrayInputStream(string.getBytes(cs));
        return findDomains(byteArray, cs);
    }

    public static String findDomainsFromFile(File file, Charset cs) throws IOException {
        return findDomains(Files.newInputStream(file.toPath()), cs);
    }

    public static String findDomains(InputStream stream, Charset cs) {
        Set<String> domains = new HashSet<>();
        DomainManager domainManager = new DomainManager();
        try (BufferedReader buf = new BufferedReader(new InputStreamReader(stream, cs))) {
            StringBuilder bucket = new StringBuilder();
            int countInBucket = 0, maxSize = 200;
            String line;

            while ((line = buf.readLine()) != null) {
                bucket.append(line).append('\n');

                if(countInBucket++ >= maxSize) {
                    processBucket(bucket, domains);
                    countInBucket = 0;
                    bucket.setLength(0);
                }
            }
            processBucket(bucket, domains);

            domains.stream()
                    .map(domain -> domain.startsWith("www.") ? domain.substring("www.".length()) : domain)
                    .map(String::toLowerCase)
                    .sorted()
                    .forEach(domainManager::addDomain);
            return domainManager.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void processBucket(StringBuilder bucket, Set<String> domains) {
        if (bucket.isEmpty()) return;

        TextAnalyzer textAnalyzer = new HtmlLinksTextAnalyzer(bucket.deleteCharAt(bucket.length()-1).toString());
        textAnalyzer.findAll().stream().map(String::valueOf).forEach(domains::add);
    }
}