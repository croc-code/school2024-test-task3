package me.jeugenedev.croc;

import me.jeugenedev.croc.representation.DomainManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public class MainTest {
    @Test
    public void test1Html_ShouldContainsCorrectResults() {
        DomainManager correctDomains = new DomainManager();
        correctDomains.addDomain("data.google.com");
        correctDomains.addDomain("static.cdn.croc.ru");
        correctDomains.addDomain("styles.dev");
        correctDomains.addDomain("ya.ru");

        Assertions.assertEquals(
                correctDomains.toString(),
                Main.findDomains(MainTest.class.getClassLoader().getResourceAsStream("test-1.html"),
                        StandardCharsets.UTF_8)
        );
    }

    @Test
    public void test2Html_ShouldContainsCorrectResults() {
        DomainManager correctDomains = new DomainManager();
        Stream.of("vk.com", "mc.yandex.ru").sorted().forEach(correctDomains::addDomain);

        Assertions.assertEquals(
                correctDomains.toString(),
                Main.findDomains(MainTest.class.getClassLoader().getResourceAsStream("test-2.html"),
                        StandardCharsets.UTF_8)
        );
    }

    @Test
    public void performanceTest() {
        // estimated performance
        long time = System.currentTimeMillis();
        String domains = Main.findDomains(MainTest.class.getClassLoader().getResourceAsStream("performance.html"),
                StandardCharsets.UTF_8);
        time = System.currentTimeMillis() - time;
        Assertions.assertTrue(time < 2000);
        System.out.println(domains);
    }
}
