package com.eeker.urlshortener.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.eeker.urlshortener.config.PropertyConfig;
import com.eeker.urlshortener.dto.URLFormDTO;
import com.eeker.urlshortener.entity.URLForm;
import com.eeker.urlshortener.repository.URLFormRepository;

@Service
public class QuickLinkService {

	private static final Logger LOGGER = LoggerFactory.getLogger(QuickLinkService.class);

	private final PropertyConfig propertyConfig;
	private final URLFormRepository urlFormRepository;

	public QuickLinkService(URLFormRepository urlFormRepository, PropertyConfig propertyConfig) {
		this.urlFormRepository = urlFormRepository;
		this.propertyConfig = propertyConfig;
	}

	public boolean isValidUrl(String url) {
		try {
			new URL(url);
			return true;
		} catch (MalformedURLException e) {
			return false;
		}
	}

	public String shortenUrl(URLFormDTO originalUrl) {
		try {
			URL url = new URL(originalUrl.getUrl());
			String path = shortenPath(url.getPath());
			String shortenedUrl = generateBaseUrl(url) + "/" + path;
			originalUrl.setShortenUrl(shortenedUrl);
			URLForm urlForm = URLFormMapper.mapDtoToEntity(originalUrl);
			saveUrlForm(urlForm);
			LOGGER.info("URL shortened: Original={}, Shortened={}", originalUrl.getUrl(), shortenedUrl);
			return shortenedUrl;
		} catch (MalformedURLException e) {
			LOGGER.error("Error shortening URL: Original={}", originalUrl.getUrl(), e);
			return originalUrl.getShortenUrl();
		}
	}

	private void saveUrlForm(URLForm urlForm) {
		urlFormRepository.save(urlForm);
	}

	private String shortenPath(String path) {
		String randomString = RandomStringUtils.randomAlphanumeric(5);
		return randomString;
	}

	private String generateBaseUrl(URL url) {
		String protocol = "http";
		String host = propertyConfig.getHost();
		int port = propertyConfig.getPort();
		String portString = (port != -1) ? (":" + port) : "";
		return protocol + "://" + host + portString + "/r";
	}

	@CacheEvict(value = "urlCache", allEntries = true)
	public void clearCache() {
		LOGGER.info("URL cache cleared.");
	}

	@Cacheable(value = "urlCache")
	public String getOriginalUrl(String shortenedPath) {
		URLForm urlForm = urlFormRepository.findByShortenUrl(shortenedPath);
		String originalUrl = (urlForm != null) ? urlForm.getUrl() : null;
		LOGGER.info("Retrieved original URL for shortened path {}: {}", shortenedPath, originalUrl);
		return originalUrl;
	}
}
