package com.bestiansoft.pdfgen.controller;

import com.bestiansoft.pdfgen.model.Doc;
import com.bestiansoft.pdfgen.service.DocService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins="*")
@RestController
public class DocController {

    @Autowired
    DocService docService;

    @RequestMapping(value = "/v1/document/{docId}", method = { RequestMethod.GET })
    public Doc req(@PathVariable String docId) {
        Doc doc = docService.getDoc(docId);
        System.out.println("==================================");
        System.out.println("==================================");
        System.out.println( doc.getSigners().get(0).getSignerNo() );
        return doc;
    }
}