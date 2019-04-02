package com.bestiansoft.pdfgen.repo;

import com.bestiansoft.pdfgen.model.Ebox;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoxRepository extends JpaRepository<Ebox, String> {
    
}