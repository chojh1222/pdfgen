package com.bestiansoft.pdfgen.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.bestiansoft.pdfgen.domain.PdfResponse;
import com.bestiansoft.pdfgen.model.Doc;
import com.bestiansoft.pdfgen.model.Element;
import com.bestiansoft.pdfgen.model.ElementSign;

public interface DocService {

    public Doc getDoc(String docId);

    public void saveDoc(Doc doc);
    
    //PdfResponse createPdf(String docId);
    // public PdfResponse createPdf(String docId);

    public List<Element> getElements(Doc doc, String signerNo);

    public List<Element> getElements(Doc doc);

    public PdfResponse saveSignerInput(String docId, String singerNo, List<Element> inputElements);

    public void saveTsa();

    public void readPdf(HttpServletResponse response, String fileName);
}