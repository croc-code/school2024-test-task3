using LinksParserLib;

namespace Test;

public class LinksParserTest
{
    [Fact]
    public async void ParseLinksAsync_threeDomain_threeDomain()
    {
        var parser = new LinksParser();

        string expected = "{\"sites\":[\"example.com\",\"google.com\",\"wikipedia.org\"]}";
        string pathToHtml = ".\\three_domain.html";

        string result = await parser.ParseLinksAsync(pathToHtml);

        Assert.Equal(expected, result);
    }

    [Fact]
    public async void ParseLinksAsync_threeDomainWithStatic_threeDomain()
    {
        var parser = new LinksParser();

        string expected = "{\"sites\":[\"example.com\",\"google.com\",\"wikipedia.org\"]}";
        string pathToHtml = ".\\three_domain_with_one_static_link.html";

        string result = await parser.ParseLinksAsync(pathToHtml);

        Assert.Equal(expected, result);
    }

    [Fact]
    public async void ParseLinksAsync_domainAndWwwdomain_oneDomain()
    {
        var parser = new LinksParser();

        string expected = "{\"sites\":[\"example.com\"]}";
        string pathToHtml = ".\\domain_and_wwwdomain.html";

        string result = await parser.ParseLinksAsync(pathToHtml);

        Assert.Equal(expected, result);
    }

    [Fact]
    public async void ParseLinksAsync_domainAndSubdomain_twoDomain()
    {
        var parser = new LinksParser();

        string expected = "{\"sites\":[\"example.com\",\"subdomain.example.com\"]}";
        string pathToHtml = ".\\domain_and_subdomain.html";

        string result = await parser.ParseLinksAsync(pathToHtml);

        Assert.Equal(expected, result);
    }

    [Fact]
    public async void ParseLinksAsync_domainAndAnotherShemas_threeDomain()
    {
        var parser = new LinksParser();

        string expected = "{\"sites\":[\"example.com\"]}";
        string pathToHtml = ".\\domain_and_another_shemas.html";

        string result = await parser.ParseLinksAsync(pathToHtml);

        Assert.Equal(expected, result);
    }
}