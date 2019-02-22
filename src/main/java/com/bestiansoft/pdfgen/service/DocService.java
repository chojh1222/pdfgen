package com.bestiansoft.pdfgen.service;

import com.bestiansoft.pdfgen.domain.PdfResponse;
import com.bestiansoft.pdfgen.model.Doc;

public interface DocService {

    public Doc getDoc(String docId);

    public void saveDoc(Doc doc);
    
    //PdfResponse createPdf(String docId);
    public PdfResponse createPdf(String docId);
}