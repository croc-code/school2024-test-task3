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
Ивкин Иван Андреевич (ivkin2003@list.ru)
## Описание реализации
Для реализации я использовал библиотеку BeautifulSoup в Python. Позльзователю предлагается ввести путь к html файлу, если такой файл найден не был, то программа сообщит об этом и попросит ввести путь еще раз.
Программа содержит 2 функции: 
1. Функция extract_domain принимает на вход url и при помощи urlparse возвращает только ту часть, в которой указан домен.
2. Функция get_domains принимает на вход путь к html файлу и создает объект BeautifulSoup. В этом объекте я ищу все ссылки по тегу 'a' и записываю результат в links. Затем я передаю каждую ссылку в функцию extract_domain и записываю результат в set, чтобы исключить повторения. Далее из полученного set формируется list и сортируется по алфавиту. Этот список выводится в консоль, а на его основе формируется строка json и возвращается функцией через return.
## Инструкция по сборке и запуску решения
Для запуска на устройстве должна стоять версия python 3.6 или выше
Перед запуском необходимо установать библиотеку bs4, сделать это можно командой
- pip install bs4
после установки нужно перейти в директорию проекта и в терминале написать
- .\main.py
