package com.bestiansoft.pdfgen.repo;

import com.bestiansoft.pdfgen.model.Element;

import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD

public interface ElementRepository extends JpaRepository<Element, String> {
    
=======
import java.util.*;

import com.bestiansoft.pdfgen.model.Doc;

public interface ElementRepository extends JpaRepository<Element, String> {
    List<Element> findByDocAndSignerNo(Doc doc, String signerNo);
>>>>>>> 692fbd880c81d970993582150995ceef3f2d53b0
}