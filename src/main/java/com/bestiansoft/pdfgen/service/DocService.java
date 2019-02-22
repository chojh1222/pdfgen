package com.bestiansoft.pdfgen.service;

import com.bestiansoft.pdfgen.domain.PdfResponse;
import com.bestiansoft.pdfgen.model.Doc;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface DocService {

    @Transactional
    public Doc getDoc(String docId);

    
    //PdfResponse createPdf(String docId);
    public PdfResponse createPdf(String docId);
}