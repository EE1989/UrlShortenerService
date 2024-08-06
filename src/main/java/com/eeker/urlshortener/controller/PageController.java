package com.eeker.urlshortener.controller;

import java.net.UnknownHostException;
import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eeker.urlshortener.dto.URLFormDTO;
import com.eeker.urlshortener.entity.URLForm;
import com.eeker.urlshortener.service.QuickLinkService;

@Controller
@RequestMapping("/")
public class PageController {

    private static final Logger LOGGER = Logger.getLogger(PageController.class.getName());

    private final QuickLinkService quickLinkService;

    public PageController(QuickLinkService shortenService) {
        this.quickLinkService = shortenService;
    }

    @RequestMapping
    public String indexPage(Model model) {
        model.addAttribute("urlForm", new URLFormDTO());
        return "index.htm";
    }

    @PostMapping("/linkReady")
    public String linkReadyPage(URLFormDTO urlForm, Model model) throws UnknownHostException {
        LOGGER.info("Original URL: " + urlForm.getUrl());

        if (quickLinkService.isValidUrl(urlForm.getUrl())) {
            urlForm.setShortenUrl(quickLinkService.shortenUrl(urlForm));
            model.addAttribute("originalUrl", urlForm.getUrl());
            model.addAttribute("shortenedUrl", urlForm.getShortenUrl());
           
        } else {
            LOGGER.warning("Invalid URL: " + urlForm.getUrl());
            model.addAttribute("error", "Invalid URL: " + urlForm.getUrl());
            model.addAttribute("urlForm", new URLForm());
           
        }
        return "linkready.htm";
    }
}
