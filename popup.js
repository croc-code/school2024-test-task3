document.addEventListener("DOMContentLoaded", function() {
  let button = document.getElementById('ScanButton');
  button.addEventListener("click", function() {
    chrome.tabs.query({ active: true, currentWindow: true }, function(tabs) {
      let activeTab = tabs[0];
      let tabURL = activeTab.url;
      findLinks(tabURL);
    });
  });
});

function findLinks(pageURL) {
  fetch(pageURL)
    .then(response => response.text())
    .then(html => {
      const links = findLinksUsingRegex(html);
      displayLinks(links);
    })
    .catch(error => displayContent('Error while fetching HTML: ' + error));
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

// function displayContent(content){
//   let existingPre = document.querySelector('pre');
//   if (existingPre) {
//     existingPre.textContent = content;
//   } else {
//     let newPre = document.createElement('pre');
//     newPre.textContent = content;
//     document.body.appendChild(newPre);
//   }
// }
function displayContent(content) {
  let newPre = document.createElement('pre');
  newPre.textContent = content;
  document.body.appendChild(newPre);
}
