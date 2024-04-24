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
        if (args.length == 0) {
            System.out.println("Usage: java Main <html_file>");
            return;
        }

        String htmlFile = args[0];
        try {
            String result = findUniqueDomains(htmlFile);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String findUniqueDomains(String htmlFile) throws IOException {
        String htmlContent = Files.readString(Paths.get(htmlFile));

        Pattern linkPattern = Pattern.compile("(?:[\\p{L}\\d-]+\\.)+[\\p{L}\\d-]+");
        Matcher matcher = linkPattern.matcher(htmlContent);

        Set<String> uniqueDomains = new HashSet<>();
        while (matcher.find()) {
            String domain = matcher.group();
            if (domain.startsWith("www.")) {
                domain = domain.substring(4);
            }
            uniqueDomains.add(domain.toLowerCase());
        }

        List<String> domainsList = new ArrayList<>(uniqueDomains);
        domainsList.sort(String::compareTo);

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

