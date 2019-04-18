package com.bestiansoft.pdfgen.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bestiansoft.pdfgen.domain.PdfResponse;
import com.bestiansoft.pdfgen.model.Doc;
import com.bestiansoft.pdfgen.model.Ebox;
import com.bestiansoft.pdfgen.model.Element;
import com.bestiansoft.pdfgen.model.ElementSign;

public interface DocService {

    public Ebox getBoxInfo(String boxId);
    
    public Doc getDoc(String docId);

    public PdfResponse saveDoc(Doc doc);
    
    //PdfResponse createPdf(String docId);
    // public PdfResponse createPdf(String docId);

    public List<Element> getElements(Doc doc, String signerNo);

    public List<Element> getElementsType(Doc doc, String inputType);

    public List<Element> getElements(Doc doc);

    public PdfResponse saveSignerInput(String docId, String singerNo, String userHash, List<Element> inputElements);

    public PdfResponse signComplete(String docId, String signerId);

    public void saveTsa();    

    public void readPdf(HttpServletRequest request, HttpServletResponse response, String docId);

    public void checkbox();

    public void radio();

    public void test();
}