# Тестовое задание для отбора на Летнюю ИТ-школу КРОК по разработке

## Условие задания
Однажды теплым летним вечером вас посетила идея разработать свое расширение для браузера для построения ссылочного графа. Что это означает на практике — ваше расширение активируется на какой-либо web-странице сайта, определяет список уникальных внешних ссылок, после чего повторяет алгоритм для каждой ссылки. Максимальная глубина поиска, визуализация собранных данных и прочие вопросы вы сочли вторичными, а начать решено было с малого — с обходчика страниц, который бы находил уникальные ссылки.

В процессе проектирования вы решили немного упростить ваш mvp и в итоге поставили себе задачу следующим образом: реализовать поиск всех уникальных ресурсов (доменов) в рамках страницы, на которые есть ссылки. При этом, формулируя задачу, вы сделали следующие допущения:
- Доменом считается запись вида example.com;
- Поддомен, например, sub.example.com,  считается отдельным ресурсом;
- Протокол (при наличии) не имеет значения.

Требования к реализации:
1. Реализация должна содержать, как минимум, одну процедуру (функцию/метод), отвечающую за поиск уникальных ресурсов, и должна быть описана в readme.md в соответствии с чек-листом;
2. В качестве входных данных программа использует реальный html-файл (page.html)	, считав который, начинает выполнять поиск;
3. Процедура (функция/метод) поиска должна возвращать строку в формате json следующего формата:
   - {«sites»: [«mail.ru», «rbc.ru», «ria.ru»]}
4. Найденные в соответствии с условием задачи домены должны выводиться в нижнем регистре без указания протокола и «www» в алфавитном порядке.

## Автор решения

#### Евгений Матеюк
[@writenowme](https://t.me/writenowme)

## Описание реализации
Реализация предполагает использование контракта для поиска вхождений, который описывается в классе
[TextAnalyzer](./src/main/java/me/jeugenedev/croc/analyzer/TextAnalyzer.java). В контракт входят два метода:
```findAll()``` и ```next()```, подробнее о которых можно прочитать в ранее упомянутом классе.

Для поиска доменных имен в HTML-файлах используется конкретная реализация 
[HtmlLinksTextAnalyzer](./src/main/java/me/jeugenedev/croc/analyzer/impl/HtmlLinksTextAnalyzer.java). Данный
класс выполняет поиск вхождений строки на основе Regex шаблона. Ниже показаны некоторые примеры строк, которые 
совпадают с шаблоном:

> key="http://value" \
> k-e-y="wss://va-lue.ru" \
> ke_y="ftp://vk.com/"

Вы можете создать свой [TextAnalyzer](./src/main/java/me/jeugenedev/croc/analyzer/TextAnalyzer.java) для поиска доменов,
просто используйте следующий код:

```java
public class Main {
    public static void main(String[] args) {
        String myHtmlLine = "<iframe src='https://ya.ru'></iframe>";
        me.jeugenedev.croc.analyzer.TextAnalyzer ta = 
                new me.jeugenedev.croc.analyzer.impl.HtmlLinksTextAnalyzer(myHtmlLine);
        System.out.println(ta.findAll()); // out: [ya.ru]
    }
}
```

### Представление и агрегация доменными именами

Для взаимодействия с множеством доменных имен, была написана реализация класса
[DomainManager](./src/main/java/me/jeugenedev/croc/representation/DomainManager.java). Данный класс занимается
агрегацией доменных имен и в текущей версии используется только для вывода списка доменов
в указанном формате через метод ```toString()```. Вы можете попробовать дополнить код выше использованием
данного класса, например:

```java
public class Main {
    public static void main(String[] args) {
        String myHtmlLine = "<iframe src='https://ya.ru'></iframe>";
        me.jeugenedev.croc.analyzer.TextAnalyzer ta =
                new me.jeugenedev.croc.analyzer.impl.HtmlLinksTextAnalyzer(myHtmlLine);
        me.jeugenedev.croc.representation.DomainManager dm = new DomainManager();
        ta.findAll().forEach(e -> dm.addDomain((String) e)); // Создаем поток строк и каждый его элемент добавляем в DomainManager
        System.out.println(dm); // out: {"sites": ["ya.ru"]}
    }
}
```

### Методы поиска и сортировки доменов

Для решения данной задачи предлагается использовать три метода, которые различаются типами передаваемых
параметром, такие как: ```findDomainsFromString(String string, Charset cs)```, 
```findDomainsFromFile(File file, Charset cs) throws IOException``` и ```findDomains(InputStream stream, Charset cs)```.

Методы ```findDomainsFromString(String string, Charset cs)``` и
```findDomainsFromFile(File file, Charset cs) throws IOException``` предлагают удобное использование при входных
данных типа String и File соотвественно.

Основная реализация содержится в методе ```findDomains(InputStream stream, Charset cs)```. Данный метод выполняет
поиск доменных имен в потоке данных, которые в совокупности удовлетворяют синтаксису HTML. 

Для анализа документа и поиска доменов входящий поток данных разбивается на фрагменты, которые передаются на
анализ последовательно. В одном фрагменте может быть до 200 строк. По данной причине использовать метод 
```findDomainsFromString(String string, Charset cs)``` не рекомендуется, так как это приводит к дублированию
одинаковых данных в памяти.

После обработки всех фрагментов входящего потока данных, а также в результате анализа всех сегментов, в
```Set<String> domains``` содержится множество уникальных доменных имен. После чего каждый элемент такого
множества проверяется на наличие "www." в домене N-уровня, где N - это последний уровень доменного имени,
сортируется и помещается в ```DomainManager domainManager```.

> Результатом метода является строка, удовлетворяющая условию задания. Или результат работы менеджера:
> ```DomainManager#toString()```.

**Обратите внимание!** Метод не предназначен для анализа блокирующих входящий потоков. Например 
```Socket#getInputStream()```.

## Инструкция по сборке и запуску решения

### Окружение
> Проект был реализован на языке программирования Java версии 17.

### Сборка проекта
> Чтобы собрать проект с помощью Maven, используйте для этого
> следующую команду: ```mvn -DskipTests clean package```. Готовый исполняемый JVM файл
> находится по пути ```./target/croc.jar```.

### Запуск проекта
> Для запуска используйте команду ```java -jar ./target/croc.jar```.

## Модификация решения
> Решение было предоставлено в качестве библиотеки, подключаемой извне,
> реализация (и использование) которой описана выше. Однако проект можно
> модифицировать для самостоятельного запуска. Для этого ниже будут описаны
> некоторые из примеров.

### Интерактивный вариант использования
> Предлагает вариант использования, когда пользователь вручную выбирает html файл,
> с помощью File Chooser.
```java
import java.nio.charset.StandardCharsets;
import javax.swing.*;

public class Main {
    public static void main(String[] args) throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(null);
        System.out.println(findDomainsFromFile(fileChooser.getSelectedFile(), StandardCharsets.UTF_8));
        System.out.println("Press key for exit.");
        System.in.read();
    }

// public static String findDomainsFromString(String string, Charset cs) {
// }

// public static String findDomainsFromFile(File file, Charset cs) throws IOException {
// }

// public static String findDomains(InputStream stream, Charset cs) {
// }
}
```

### Вставка строки
> Вариант использования с использованием java.lang.String для поиска
> доменов в html.
```java
public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println(findDomainsFromString("<iframe src='wss://google.com'></iframe>", StandardCharsets.UTF_8));
    }

//  public static String findDomainsFromString(String string, Charset cs) {
//  }

//  public static String findDomainsFromFile(File file, Charset cs) throws IOException {
//  }

//  public static String findDomains(InputStream stream, Charset cs) {
//  }
}
```