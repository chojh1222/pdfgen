package com.bestiansoft.pdfgen.repo;

import com.bestiansoft.pdfgen.model.Element;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

import com.bestiansoft.pdfgen.model.Doc;

public interface ElementRepository extends JpaRepository<Element, String> {
    List<Element> findByDocAndSignerNo(Doc doc, String signerNo);
}