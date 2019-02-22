package com.bestiansoft.pdfgen.repo;

import com.bestiansoft.pdfgen.model.Element;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ElementRepository extends JpaRepository<Element, String> {
    
}