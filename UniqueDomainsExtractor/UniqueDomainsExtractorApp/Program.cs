using UniqueDomainsExtractorApp;

if (args.Length < 1)
{
	Console.WriteLine("Wrong arguments amount. Please, set first argument: URI or file path to html-document");
	Environment.Exit(1);
}

try
{
	var doc = HtmlDocumentLoader.Load(args[0]);
	var uniqueDomains = UniqueDomainsExtractor.ExtractToArray(doc);
	var jsonUniqueDomains = DomainsJsonConverter.ConvertToJsonString(uniqueDomains);
	Console.WriteLine(jsonUniqueDomains);
}
catch (HtmlDocumentLoaderException e)
{
	Console.WriteLine(e.Message);
	Environment.Exit(1);
}
