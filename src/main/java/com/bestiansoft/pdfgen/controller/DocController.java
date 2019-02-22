package com.bestiansoft.pdfgen.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.bestiansoft.pdfgen.domain.PdfResponse;
import com.bestiansoft.pdfgen.model.Doc;
import com.bestiansoft.pdfgen.model.Element;
import com.bestiansoft.pdfgen.service.DocService;
import com.bestiansoft.pdfgen.model.Signer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins="*")
@RestController
public class DocController {

    @Autowired
    DocService docService;

    // 생성자용 pdf 조회 - 임시
    @RequestMapping(value = "/v1/document/{docId}", method = { RequestMethod.GET })
    public Doc req(@PathVariable String docId) {
        Doc doc = docService.getDoc(docId);        
        // System.out.println( doc.getSigners().get(0).getSignerNo() );
        return doc;
    }

    /**
     * 생성자가 pdf를 최초 작성하는 경우 POST
     *    PDF 파일저장(포탈에 있는 경로?)
     *    속성들의 좌표데이터 저장
     
     * 사용자(생성자, 참여자)의 pdf 조회 GET
     *    모든 속성값들을 조회
    */
     // 생성자용 pdf 조회 - 임시
    // @RequestMapping(value = "/v1/document/{docId}/signer/{signerId}", method = { RequestMethod.GET })
    // public Doc reqSigner(@PathVariable String docId) {
    //     Doc doc = docService.getDoc(docId);        
    //     System.out.println( doc.getSigners().get(0).getSignerNo() );
    //     return doc;
    // }

    /*
     * 사용자(생성자, 참여자) 가 서명하는경우
     *    사용자별 속성의 모든값을 저장
     */


     /*
     * 생성자가 계약을 완료하는 경우
     *    PDF 생성
     */
    //@RequestMapping( value="/v1/document/{docId}/signer/{signerId}", method= {RequestMethod.POST} )    
    @RequestMapping( value="/v1/document/{docId}/signer/{signerId}", method= {RequestMethod.GET} )
	//public @ResponseBody String req(@RequestBody Doc doc) {
    public @ResponseBody PdfResponse req() {
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