using HtmlAgilityPack;

namespace UniqueDomainsExtractorApp
{
	public static class UniqueDomainsExtractor
	{
		public static string[] ExtractToArray(HtmlDocument doc)
		{
			var linkNodes = doc.DocumentNode.SelectNodes("//a[@href]");

			var uniqueDomains = new HashSet<string>();
			foreach (var node in linkNodes)
			{
				try
				{
					var uri = new Uri(node.GetAttributeValue("href", string.Empty));
					if (uri.Host != string.Empty) { uniqueDomains.Add(uri.Host); }
				}
				catch (UriFormatException) { }
			}

			return uniqueDomains.ToArray();
		}
	}
}
