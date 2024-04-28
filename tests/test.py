import pytest
import sys
import json
from src.url_parser import process_html


def test_link_number(capsys):
    process_html("tests/test.html")
    captured = capsys.readouterr()
    output = json.loads(captured.out)['sites']
    assert len(output) == 4


def test_members_sort(capsys):
    process_html("tests/test.html")
    captured = capsys.readouterr()
    output = json.loads(captured.out)['sites']
    assert output[0] == 'a.nika.ru'
    assert output[-1] == 'z.sub.nika.ru'


def test_unnecessary_prefixes(capsys):
    process_html("tests/test.html")
    captured = capsys.readouterr()
    output = json.loads(captured.out)['sites']
    assert output[-1] == 'z.sub.nika.ru'
    assert output[0] == 'a.nika.ru'
    assert output[1] == 'nika.ru'
    assert output[2] == 'veronika.ru'


def test_nonduplicated(capsys):
    process_html("tests/test.html")
    captured = capsys.readouterr()
    output = json.loads(captured.out)['sites']
    assert output.count('nika.ru') == 1
