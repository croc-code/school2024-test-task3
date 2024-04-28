using HtmlAgilityPack;
using System.Text.Json;

namespace LinksParserLib
{
    public class LinksParser
    {
        public async Task<string> ParseLinksAsync(string pathToDocument)
        {
            if (!File.Exists(pathToDocument))
            {
                throw new FileNotFoundException("File not found", pathToDocument);
            }

            string htmlContent = await File.ReadAllTextAsync(pathToDocument);

            var htmlDocument = new HtmlDocument();
            htmlDocument.LoadHtml(htmlContent);

            var listNodes = htmlDocument.DocumentNode.SelectNodes("//a[@href]");
            var setDomains = new SelectedSites();

            if (listNodes != null)
            {
                foreach (var linkNode in listNodes)
                {
                    string url = linkNode.GetAttributeValue("href", "");
                    Uri uri;
                    if (!Uri.TryCreate(url, UriKind.Absolute, out uri))
                    {
                        continue;
                    }

                    string domain = uri.Host;
                    if (string.IsNullOrEmpty(domain))
                    {
                        continue;
                    }

                    domain = TryRemoveWww(domain);

                    setDomains.Sites.Add(domain);
                }
            }  
            return JsonSerializer.Serialize(setDomains);
        } 
        private string TryRemoveWww(string domain) 
        {
            if(domain.Contains("www.")) 
            {
                return domain.Replace("www.", "");
            }
            return domain;
        }
    }
}

