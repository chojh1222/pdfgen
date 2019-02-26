package com.bestiansoft.pdfgen.service;

import java.util.List;

import com.bestiansoft.pdfgen.domain.PdfResponse;
import com.bestiansoft.pdfgen.model.Doc;
import com.bestiansoft.pdfgen.model.Element;
import com.bestiansoft.pdfgen.model.ElementSign;

public interface DocService {

    public Doc getDoc(String docId);

    public void saveDoc(Doc doc);
    
    //PdfResponse createPdf(String docId);
    public PdfResponse createPdf(String docId);

    public List<Element> getElements(Doc doc, String signerNo);

    public void saveSignerInput(List<ElementSign> input);
}