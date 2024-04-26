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
Денис Романенко 

## Описание реализации
Я реализовал решение задачи на базе Java-библиотеке Jsoup. В моей реализации присутствует два варианта передачи страницы.
Метод extractDomainsFromHtmlFile() обрабатывает html-файл, переданный ему (файл должен располагаться в директории resources).
Метод extractDomainsFromUrl() аналогичен предыдущему, но работает со ссылкой на web-страницу.

## Инструкция по сборке и запуску решения
Проект необходимо клонировать по ссылке в рабочую директорию.  Перед (во время) нужно включить обработку аннотаций для работы Lombok.
Для запуска решения необходимо запустить метод main() класса HttpParser. Собирается проект, с помощью системы сборки Maven.

