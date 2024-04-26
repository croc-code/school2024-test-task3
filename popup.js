document.addEventListener("DOMContentLoaded", function() {
  let button = document.getElementById('ScanButton');
  button.addEventListener("click", function() {
    findLinks();
  });
});

function findLinks() {
  //здесь получаем html разметку страницы и вызываем функцию поиска ссылок
  chrome.tabs.query({ active: true, currentWindow: true }, tabs => {
    const activeTab = tabs[0];
    chrome.scripting.executeScript({
      target: { tabId: activeTab.id },
      function: () => document.documentElement.outerHTML
    }, result => {
      if (!result || result.length === 0 || !result[0].result) {
        displayContent("Script execution failed or returned no result");
        return;
      }

      const pageHTML = result[0].result;
      const links = findLinksUsingRegex(pageHTML);
      displayLinks(links);
    });
  });
}

function findLinksUsingRegex(pageHTML) {
  const hrefPattern = /href=["'](.*?)["']/gi;
  //получаем список всех ссылок со страницы в формате href=ссылка
  const hrefMatches = pageHTML.match(hrefPattern) || [];
  const links = [];
  //проходимся по каждой позиции и вытягиваем ссылки, потом добавляем их в links
  hrefMatches.forEach(function(hrefMatch) {
    const hrefValue = hrefMatch.match(/href=["'](.*?)["']/i)[1];
    links.push(hrefValue);
  });
  /*
  в links происходит поиск по наличию протокола, ибо иначе, даже если ловить и исключать #,/,mailto:,
  могут подтягиваться имена файлов, ибо те похожи на названия сторонних сайтов
   */
    const linkPattern = /^https?:\/\//i;
  return links.filter(function (link) {
    return link.match(linkPattern);
  });
}

//Приведение ссылок к требуемому виду
function displayLinks(links) {
  let cleanedLinks = links.map(link => {
    //Удаление протокола, www и приведение текста к нижнему регистру
    return link.replace(/^(https?:\/\/)?(www\.)?([^\/]+).*$/i, '$3').toLowerCase();
  });
  //удаление копий и сортировка по алфавиту
  let uniqueSortedLinks = [...new Set(cleanedLinks)].sort();
  //Приведение к виду json строки
  let jsonResult = { "sites": uniqueSortedLinks };

  let jsonString = JSON.stringify(jsonResult);
  displayContent(jsonString);

  return jsonString;
}

//Отвечает за создание блока для текста и вывод его на экран
function displayContent(content) {
  let newPre = document.createElement('pre');
  newPre.textContent = content;
  document.body.appendChild(newPre);

  //Создание кнопки "Скопировать" и ее логика
  let copyButton = document.createElement('button');
  copyButton.textContent = 'Copy';
  copyButton.addEventListener('click', function() {
    navigator.clipboard.writeText(content)
      .then(() => {
        copyButton.textContent = 'Copied';
      })
      .catch(err => {
        displayContent('Unable to copy to clipboard: ' + err);
      });
  });

  document.body.appendChild(copyButton);
}
