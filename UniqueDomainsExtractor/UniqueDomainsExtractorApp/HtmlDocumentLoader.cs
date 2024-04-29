using HtmlAgilityPack;

namespace UniqueDomainsExtractorApp
{
	public static class HtmlDocumentLoader
	{
		public static HtmlDocument Load(string html)
		{
			HtmlDocument doc = new HtmlDocument();

			try
			{
				doc.Load(html);
			}
			catch (Exception e)
			{
				var message = string.Concat($"Error when loading a document: {e.Message}\n",
					"Most likely you have specified an incorrect URI or file path.");
				throw new HtmlDocumentLoaderException(message);
			}

			return doc;
		}
	}

	public class HtmlDocumentLoaderException : Exception
	{
		public HtmlDocumentLoaderException(string message) : base(message) { }
	}
}
