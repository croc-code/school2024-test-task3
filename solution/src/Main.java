import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // Создаю экземпляр класса LinkExtractor, чтобы получить доступ к методу getLinksFromHtml()
        var linksParser = new LinkExtractor();
        // Вывожу результат в консоль, чтобы проверить его
        System.out.println(linksParser.getLinksFromHtml());
    }
}