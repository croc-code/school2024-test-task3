import re
import sys
import json
from os.path import isfile
from argparse import ArgumentParser
from bs4 import BeautifulSoup


def is_valid_url(url: str) -> bool:
    '''
        whether url is correct or not
    '''
    if not url or len(url) < 3:
        return False
    regex = r'[\S\.]*\S+\.\w+'
    return re.search(regex, url)


def format_url(url: str) -> str | None:
    '''
        getting rid of prefixes and link parameters
    '''
    regex = r'(\S+\:\/\/){0,1}(www\.){0,1}'
    if re.match(regex, url):
        url = re.split(regex, url, maxsplit=1)[-1]
    if '/' in url:
        url = url.split('/', maxsplit=1)[0]
    return url.lower() if url else None


def prepare_output(sites: dict) -> str:
    '''
        json-like output formatting
    '''
    return json.dumps({"sites": sorted(list(sites))})


def process_html(html: str):
    '''
        opens html and collect external links
    '''
    sites = set()  # not to duplicate links

    with open(html, encoding="utf=8", mode="r") as infile:
        html_content = infile.read()
    b_soup = BeautifulSoup(html_content, "html.parser")
    for tag_a in b_soup.findAll("a"):
        href = tag_a.attrs.get("href")
        if is_valid_url(href):
            url = format_url(href)
            if url and len(url) > 1:
                sites.add(url)

    print(prepare_output(sites))


def main():
    '''
        main func to run script
    '''
    parser = ArgumentParser(
        usage='''
    Path to the html-file should be specified:
        python3 url_parser.py <html-file path>
    Example:
        python3 url_parser.py i.want.to.go.to.this.summer.school.html
    Run python3 url_parser.py -h for more information.
    '''
    )

    parser.add_argument('html_file', help='path to the html-file to check')
    args = parser.parse_args()
    if not args.html_file.endswith('.html'):
        print('Incorrect file extension, try again.')
        sys.exit(1)
    if not isfile(args.html_file):
        print('Not a regular file, try another one.')
        sys.exit(1)

    process_html(args.html_file)


if __name__ == "__main__":
    main()
