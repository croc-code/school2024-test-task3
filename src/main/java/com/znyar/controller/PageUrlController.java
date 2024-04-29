package com.znyar.controller;

import com.znyar.service.HtmlPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PageUrlController {

    private final HtmlPageService htmlPageService;

    @GetMapping("/get_domains")
    public String getDomains(@RequestParam(value = "url") String url) {
        return htmlPageService.getDomainsJson(url);
    }

}
