document.addEventListener("DOMContentLoaded", function() {
  let button = document.getElementById('ScanButton');
  button.addEventListener("click", function() {
    findLinks();
  });
});

function findLinks() {
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
  const hrefMatches = pageHTML.match(hrefPattern) || [];
  const links = [];
  hrefMatches.forEach(function(hrefMatch) {
    const hrefValue = hrefMatch.match(/href=["'](.*?)["']/i)[1];
    links.push(hrefValue);
  });
    const linkPattern = /^https?:\/\//i;
  return links.filter(function (link) {
    return link.match(linkPattern);
  });
}

function displayLinks(links) {
  let cleanedLinks = links.map(link => {
    return link.replace(/^(https?:\/\/)?(www\.)?([^\/]+).*$/i, '$3').toLowerCase();
  });

  let uniqueSortedLinks = [...new Set(cleanedLinks)].sort();

  let jsonResult = { "sites": uniqueSortedLinks };

  let jsonString = JSON.stringify(jsonResult);
  displayContent(jsonString);

  return jsonString;
}

function displayContent(content) {
  let newPre = document.createElement('pre');
  newPre.textContent = content;
  document.body.appendChild(newPre);
}
