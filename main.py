from bs4 import BeautifulSoup
import json
import re


def find_unique_sites(html_file):
    # Загрузка HTML-файла
    with open(html_file, 'r', encoding='utf-8') as file:
        html_content = file.read()

    soup = BeautifulSoup(html_content, 'html.parser')
    links = soup.find_all('a')

    domains = set()
    for link in links:
        href = link.get('href')
        if href:
            # Использование регулярного выражения для извлечения домена
            match = re.search(r'https?://([^/]+)', href)
            if match:
                domain = match.group(1)
                domain = domain.replace('www.', '').lower()
                domains.add(domain)
    return json.dumps({"sites": sorted(list(domains))})


# Пример использования
#if __name__ == "__main__":
#    html_file = 'page.html'  # Замените на путь к вашему HTML-файлу
#    result = find_unique_sites(html_file)
#    print(result)
