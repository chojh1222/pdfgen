package com.bestiansoft.pdfgen.controller;

import com.bestiansoft.pdfgen.model.Doc;
import com.bestiansoft.pdfgen.service.DocService;
import com.bestiansoft.pdfgen.vo.ElementsVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins="*")
@RestController
public class DocController {

    @Autowired
    DocService docService;

    @RequestMapping(value = "/v1/document/{docId}", method = { RequestMethod.GET })
    public Doc getDoc(@PathVariable String docId) {
        Doc doc = docService.getDoc(docId);
        System.out.println("==================================");
        System.out.println("==================================");
        System.out.println( doc.getSigners().get(0).getSignerNo() );
        return doc;
    }

    @RequestMapping(value = "/v1/document/{docId}", method = { RequestMethod.POST })
    public void saveDoc(@PathVariable String docId, @RequestBody ElementsVo elementsVo) {
        Doc doc = new Doc();

        doc.setDocId(docId);
        // doc.doc = filepath (변수명 변경필요)
        doc.setDoc("http://13.209.43.245:8080/sample.pdf");
        doc.setElements(elementsVo.getInputs());

        docService.saveDoc(doc);
    }
}