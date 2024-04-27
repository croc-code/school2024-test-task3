from bs4 import BeautifulSoup
from urllib.parse import urlparse
import os
import json
# Функция для извлечения домена из ссылки

def extract_domain(url):
    #ля получения домена используем urlparse
    parsed_url = urlparse(url)
    domain = parsed_url.netloc
    domain = domain.lower()
    # если домен начинается с www, то удаляем их
    if domain[:3] == 'www':
        return domain[4:]
    else:
        return domain


def get_domains(filepath):

    with open(filepath, 'r') as file:
        html_content = file.read()
    #создаем объект BeautifulSoup, в котором будем искать ссылки
    soup = BeautifulSoup(html_content, 'html.parser')
    links = soup.find_all('a')
    unique_domains = set()
    #ссылки кладем в сет чтобы избежать дубликатов
    for link in links:
        href = link.get('href')
        if href:
            domain = extract_domain(href)
            if domain: # отсеиваем ссылки, в которых нет доменов
                unique_domains.add(domain)
    #формируем из сета лист, попутно сортируя его
    domain_sort = sorted(list(unique_domains))
    #из полученного листа делаем json, а после выводим в консоль
    data = {'sites': domain_sort}
    json_data = json.dumps(data)
    for domain in domain_sort:
        print(domain)
    #возвращаем json объект
    return json_data

print("Enter file path")
path = input()
while not(os.path.exists(path)):
    #если файла не существует, то просим повторить ввод
    print("file does not exist. Enter correct file path")
    path = input()
get_domains(path)



