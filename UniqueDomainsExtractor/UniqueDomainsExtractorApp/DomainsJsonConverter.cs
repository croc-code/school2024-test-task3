using Newtonsoft.Json;

namespace UniqueDomainsExtractorApp
{
	public static class DomainsJsonConverter
	{
		public static string ConvertToJsonString(string[] domains)
		{
			var jsonObject = new
			{
				sites = domains
			};
			return JsonConvert.SerializeObject(jsonObject, Formatting.Indented);
		}
	}
}
