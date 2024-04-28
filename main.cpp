#include <wctype.h>

#include <iostream>
#include <regex>
#include <string>
#include <fstream>
#include <stdexcept> /* runtime_error */
#include <set> 
#include <algorithm> /* transform */


// STANDART C++11
class Solution
{
public:
	static std::wstring LinkGraph(const std::string& filename)
	{
		std::ifstream page(filename, std::ios_base::in); // open html file
		if (!page.is_open())
			throw std::runtime_error("File open error!");

		// regex describe href
		std::regex domen_regex("(http|ftp|https):\\/\\/(www\\.)?([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:\\/~+#-]*[\\w@?^=%&\\/~+#-])?"); 
		const int domen_capture_group = 3; // number capture group of domen
		std::smatch sm;

		// '«' unicode symbol need wstring 
		std::wstring ret_json = L"{«sites»: ["; 
		
		std::set<std::string> found_sites;
		std::string word;
		while(page >> word) // read file by word
		{
			std::regex_search(word, sm, domen_regex);
			if (sm.size() > 0)
				found_sites.insert(sm[domen_capture_group]);
		}
		
		// convert string to wstring
		auto to_wstring = [](const std::string& str) -> std::wstring
		{	
		  size_t len = std::mbstowcs(nullptr, &str[0], 0);
		  if (len == -1)
		  	throw std::runtime_error("Invalid source string!");

		  std::wstring wstr(len, 0);
		  std::mbstowcs(&wstr[0], &str[0], wstr.size());
		  return wstr;
		};

		bool first = true;
		for (const auto& iter : found_sites)
		{
			if (!first)
			{
				ret_json.append(L", ");
			} else 
				first = false;
			ret_json.append(L"«").append(to_wstring(iter)).push_back(L'»');
		}

		ret_json.append(L"]}");

		return ret_json;
	}


};

int main()
{
	/*
	std::string page_html;

	std::cout << "Input file name: " << std::endl;
	std::cin >> page_html;
	*/

	std::wstring json = Solution::LinkGraph("page.html");

	// convert wstring to its lower case
	auto wstr_tolower = [](std::wstring& s) 
	{
		std::transform(s.begin(), s.end(), s.begin(), [](
			std::wstring::value_type c){ 
				return std::towlower(c); 
			});
	};

	// parse json to obtain sites one by one
	std::wstring site;
	size_t begin, end; begin = json.find(L'«', json.find(L'«') + 1);
	while (begin != std::wstring::npos)
	{
		end = json.find(L'»', begin + 1);
		site = json.substr(begin + 1, end - (begin + 1));
		wstr_tolower(site); std::wcout << site << '\n';

		begin = json.find(L'«', end + 1);
	}

	return 0;
}