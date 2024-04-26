using HtmlAgilityPack;
using System.Threading.Tasks;

namespace LinksParserLib
{
    public class LinksParser
    {
        public async Task<string> ParseLinks(string htmlContent)
        {
            var htmlDocument = new HtmlDocument();
            htmlDocument.LoadHtml(htmlContent);

            var listNodes = htmlDocument.DocumentNode.SelectNodes("//a[@href]");
            var listLinks = new List<string>();
            var setDomains = new HashSet<string>();

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

                    if (!setDomains.Contains(domain))
                    {
                        setDomains.Add(domain);
                    }
                }
            }  
            return await Task.Run(() => setDomains.Aggregate((x, y) => x + "\n" + y));
        } 
    }
}