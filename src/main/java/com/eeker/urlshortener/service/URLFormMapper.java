package com.eeker.urlshortener.service;

import com.eeker.urlshortener.dto.URLFormDTO;
import com.eeker.urlshortener.entity.URLForm;

public class URLFormMapper {

    public static URLForm mapDtoToEntity(URLFormDTO urlFormDTO) {
        URLForm urlForm = new URLForm();
        urlForm.setUrl(urlFormDTO.getUrl());
        urlForm.setShortenUrl(urlFormDTO.getShortenUrl());
        return urlForm;
    }
}