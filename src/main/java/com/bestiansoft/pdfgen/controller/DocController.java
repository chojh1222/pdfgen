package com.bestiansoft.pdfgen.controller;

import java.util.HashMap;

import com.bestiansoft.pdfgen.model.Doc;
import com.bestiansoft.pdfgen.model.Signer;
import com.bestiansoft.pdfgen.service.DocService;
import com.bestiansoft.pdfgen.vo.ElementsVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@CrossOrigin(origins="*")
@RestController
public class DocController {

    @Autowired
    DocService docService;

    @RequestMapping(value = "/v1/document/{docId}", method = { RequestMethod.GET })
    public Map<String, Object> getDoc(@PathVariable String docId) {
        // Doc doc = docService.getDoc(docId);
        
        Map<String, Object> ret = new HashMap<>();

        // ret.put("doc", doc.getFilePath());
        ret.put("doc", "http://13.209.43.245:8080/sample.pdf");
        ret.put("signers", Signer.signers);

        return ret;
    }

    @RequestMapping(value = "/v1/document/{docId}", method = { RequestMethod.POST })
    public void saveDoc(@PathVariable String docId, @RequestBody ElementsVo elementsVo) {
        Doc doc = new Doc();

        doc.setDocId(docId);
        doc.setFilePath("http://13.209.43.245:8080/sample.pdf");
        doc.setElements(elementsVo.getInputs());

        docService.saveDoc(doc);
    }
    
    @RequestMapping(value = "/v1/document/{docId}/signer/{signerNo}", method = { RequestMethod.GET })
    public Map<String, Object> getDocToSign(@PathVariable String docId, @PathVariable String signerNo) {
        Doc doc = docService.getDoc(docId);

        Map<String, Object> ret = new HashMap<>();
        ret.put("doc", doc.getFilePath());
        ret.put("inputs", doc.getElements());
        ret.put("signer", Signer.getSigner(signerNo));

        return ret;
    }
}