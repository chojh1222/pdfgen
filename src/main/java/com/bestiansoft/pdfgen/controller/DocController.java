package com.bestiansoft.pdfgen.controller;

import com.bestiansoft.pdfgen.domain.PdfResponse;
import com.bestiansoft.pdfgen.model.Doc;
import com.bestiansoft.pdfgen.service.DocService;
import com.bestiansoft.pdfgen.model.Signer;

import java.util.HashMap;

import com.bestiansoft.pdfgen.vo.ElementsVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@CrossOrigin(origins="*")
@RestController
public class DocController {

    @Autowired
    DocService docService;

    // 1. 생성자용 pdf 조회 - 임시
    @RequestMapping(value = "/v1/document/{docId}", method = { RequestMethod.GET })
    public Map<String, Object> getDoc(@PathVariable String docId) {
        // Doc doc = docService.getDoc(docId);
        
        System.out.println("생성자용 pdf 조회");

        Map<String, Object> ret = new HashMap<>();

        // ret.put("doc", doc.getFilePath());
        //ret.put("doc", "http://13.209.43.245:8080/sample.pdf");        
        ret.put("doc", "http://localhost:8888/sample.pdf");        
        // ret.put("doc", "/sample.pdf");
        ret.put("signers", Signer.signers);

        return ret;
    }

    // 2. 생성자 pdf 작성 완료
    @RequestMapping(value = "/v1/document/{docId}", method = { RequestMethod.POST })
    public void saveDoc(@PathVariable String docId, @RequestBody ElementsVo elementsVo) {
        Doc doc = new Doc();

        doc.setDocId(docId);
        doc.setFilePath("http://13.209.43.245:8080/sample.pdf");
        doc.setElements(elementsVo.getInputs());

        docService.saveDoc(doc);
    }
    
    // 3. 생성자 및 참여자 pdf 서명 화면
    @RequestMapping(value = "/v1/document/{docId}/signer/{signerNo}", method = { RequestMethod.GET })
    public Map<String, Object> getDocToSign(@PathVariable String docId, @PathVariable String signerNo) {
        Doc doc = docService.getDoc(docId);

        Map<String, Object> ret = new HashMap<>();
        ret.put("doc", doc.getFilePath());
        ret.put("inputs", doc.getElements());
        ret.put("signer", Signer.getSigner(signerNo));

        return ret;
    }

    // 4. 생성자 및 참여자 서명 완료 - 작업중
    @RequestMapping(value = "/v1/document/{docId}/signer/{signerNo}", method = { RequestMethod.POST })
    public Map<String, Object> getDocToSign(@PathVariable String docId, @PathVariable String signerNo, @RequestBody Doc inputDoc) {
        
        System.out.println("inputDoc : " + inputDoc.getDocId());
        // Doc doc = docService.getDoc(docId);

        Map<String, Object> ret = new HashMap<>();
        // ret.put("doc", doc.getFilePath());
        // ret.put("inputs", doc.getElements());
        // ret.put("signer", Signer.getSigner(signerNo));

        return ret;
    }
    
    /**
     * 5. 생성자가 최종 pdf 확인 화면
     *  - 문서아이디로 문서를 조회
     *  - 생성/참여자 엘리먼트들을 모두 보여줌(수정은 안되고..)
     */    
    @RequestMapping( value="/v1/document/{docId}/signComplete/{signerId}", method= {RequestMethod.GET} )
    public @ResponseBody PdfResponse contractView(@PathVariable String docId){        
        //return docService.getDoc(docId);
        return null;
    }


    // 6. 생성자 pdf 작성 완료
    //@RequestMapping( value="/v1/document/{docId}/signer/{signerId}", method= {RequestMethod.POST} )    
    @RequestMapping( value="/v1/document/{docId}/signComplete/{signerId}", method= {RequestMethod.POST} )
	//public @ResponseBody String req(@RequestBody Doc doc) {
    public @ResponseBody PdfResponse signComplete() {
        //log.info("req called");
        System.out.println("pdf 생성");
        
        // System.out.println(doc.getDocId());
        // List<Signer> signer = doc.getSigners();
        // System.out.println("signer :: " + signer.size());        
        String docId = "doc1";

        return docService.createPdf(docId);
        // return null;
	}
}