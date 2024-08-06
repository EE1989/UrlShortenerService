package com.eeker.urlshortener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.MalformedURLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.eeker.urlshortener.config.PropertyConfig;
import com.eeker.urlshortener.dto.URLFormDTO;
import com.eeker.urlshortener.entity.URLForm;
import com.eeker.urlshortener.repository.URLFormRepository;
import com.eeker.urlshortener.service.QuickLinkService;

@SpringBootTest
class UrlShortenerApplicationTests {

	@Mock
    private URLFormRepository repository;

    @Mock
    private PropertyConfig propertyConfig;

    @InjectMocks
    private QuickLinkService quickLinkService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testIsValidUrlWithValidUrl() {
        assertTrue(quickLinkService.isValidUrl("http://www.example.com"));
    }

    @Test
    public void testIsValidUrlWithInvalidUrl() {
        assertFalse(quickLinkService.isValidUrl("invalid_url"));
    }

    @Test
    public void testShortenUrl() throws MalformedURLException {
      
        URLFormDTO urlFormDTO = new URLFormDTO();
        urlFormDTO.setUrl("http://www.example.com");
        URLForm urlForm = new URLForm();
        urlForm.setUrl("http://www.example.com");
        urlForm.setShortenUrl("http://localhost:8080/r/abcde");
        Mockito.when(repository.save(Mockito.any())).thenReturn(urlForm);
        Mockito.when(propertyConfig.getHost()).thenReturn("localhost");
        Mockito.when(propertyConfig.getPort()).thenReturn(8080);
        String result = quickLinkService.shortenUrl(urlFormDTO);
        assertNotNull(result);
        assertTrue(result.startsWith("http://localhost:8080/r/"));
    }

    @Test
    public void testShortenUrlWithMalformedURLException() {
        
        URLFormDTO urlFormDTO = new URLFormDTO();
        urlFormDTO.setUrl("invalid_url");
        String result = quickLinkService.shortenUrl(urlFormDTO);
        assertNull(result);
    }


    @Test
    public void testGetOriginalUrl() {
        String shortenedPath = "abcde";
        URLForm urlForm = new URLForm();
        urlForm.setUrl("http://www.example.com");
        Mockito.when(repository.findByShortenUrl(shortenedPath)).thenReturn(urlForm);
        String result = quickLinkService.getOriginalUrl(shortenedPath);
        assertNotNull(result);
        assertEquals(urlForm.getUrl(), result);
    }

    @Test
    public void testGetOriginalUrlWithInvalidShortenedPath() {
        String shortenedPath = "invalid_path";
        Mockito.when(repository.findByShortenUrl(shortenedPath)).thenReturn(null);
        String result = quickLinkService.getOriginalUrl(shortenedPath);
        assertNull(result);
    }
    
   
    @Test
    public void testShortenUrlWithNullUrl() {
        URLFormDTO urlFormDTO = new URLFormDTO();
        urlFormDTO.setUrl(null);
        String result = quickLinkService.shortenUrl(urlFormDTO);
        assertNull(result);
        assertEquals(null, result);
    }
    

    @Test
    public void testGetOriginalUrlWithNullShortenedPath() {
        String shortenedPath = null;
        String result = quickLinkService.getOriginalUrl(shortenedPath);
        assertNull(result);
    }

}
