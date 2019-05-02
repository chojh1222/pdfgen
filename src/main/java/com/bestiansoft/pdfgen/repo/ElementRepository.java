package com.bestiansoft.pdfgen.repo;

import com.bestiansoft.pdfgen.model.Element;

// import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import com.bestiansoft.pdfgen.model.Doc;

// public interface ElementRepository extends JpaRepository<Element, String> {
public interface ElementRepository {
    @Transactional(readOnly = true)
    List<Element> findByDocAndSignerNo(Doc doc, String signerNo);

    @Transactional(readOnly = true)
    List<Element> findByDoc(Doc doc);

    @Transactional(readOnly = true)
    List<Element> findByDocAndInputType(Doc doc, String inputType);
}