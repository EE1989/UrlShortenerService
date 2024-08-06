package com.eeker.urlshortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eeker.urlshortener.entity.URLForm;

@Repository
public interface URLFormRepository extends JpaRepository<URLForm, Long> {

	URLForm  findByUrl(String name);
	URLForm  findByShortenUrl(String name);
}
