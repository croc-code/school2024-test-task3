using System.Text.Json.Serialization;

namespace LinksParserLib
{
    public class SelectedSites
    {
        [JsonPropertyName("sites")]
        public SortedSet<string> Sites { get; set; }
        public SelectedSites()
        {
            Sites = new SortedSet<string>();
        }
    }
}