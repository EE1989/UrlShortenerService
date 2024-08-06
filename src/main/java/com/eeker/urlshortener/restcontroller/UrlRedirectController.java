package com.eeker.urlshortener.restcontroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eeker.urlshortener.service.QuickLinkService;

@RestController
@RequestMapping("/r")
public class UrlRedirectController {

    private static final String REDIRECT_BASE_URL = "http://localhost:8080/r/";

    private final QuickLinkService quickLinkService;

    public UrlRedirectController(QuickLinkService shortenService) {
        this.quickLinkService = shortenService;
    }

    @GetMapping("/{shortURLLink}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortURLLink) {
        String originalUrl = quickLinkService.getOriginalUrl(REDIRECT_BASE_URL + shortURLLink);
        if (originalUrl != null) {
            return ResponseEntity.status(HttpStatus.FOUND).location(java.net.URI.create(originalUrl)).build();
        } else {
            throw new ResourceNotFoundException("Original URL not found for the given shortened path");
        }
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ex.getMessage();
    }

    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }
}

