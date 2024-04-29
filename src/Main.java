import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.TreeSet;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите путь до HTML-файла:");
        String path = sc.nextLine();    //вводим путь до HTML-файла на диске компьютера

        TreeSet<String> sites = new TreeSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\"|\\'>|</a>|</A>");    //создаем массив строк в необработанном виде
                for (String word : words) {
                    if (!word.isBlank() && (word.startsWith("http") || !word.isBlank() && word.startsWith("https"))) {
                        try {
                            URL url = new URL(word);
                            String host = url.getHost().replace("www.", "");
                            if (!host.isBlank()) {
                                sites.add(host);    //добавляем в сет найденный сайт в обработанном виде
                            }
                        } catch (Exception e) {
                            // игнорируем
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder jsonOutput = new StringBuilder();    //создаем SB для вывода в требуемом виде
        jsonOutput.append("{\n");
        jsonOutput.append("  \"sites\": [\n");
        for (String site : sites) {
            jsonOutput.append("    \"").append(site).append("\",\n");    //добавляем найденные ссылки
        }
        if (!sites.isEmpty()) {
            jsonOutput.deleteCharAt(jsonOutput.lastIndexOf(","));
        }
        jsonOutput.append("  ]\n");
        jsonOutput.append("}");

        System.out.println(jsonOutput.toString()); // выводим результат
    }
}
