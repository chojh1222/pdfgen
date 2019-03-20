package com.bestiansoft.pdfgen.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// import javax.ws.rs.Produces;
import javax.servlet.http.HttpSession;

import com.bestiansoft.pdfgen.config.PdfGenConfig;
import com.bestiansoft.pdfgen.domain.PdfResponse;
import com.bestiansoft.pdfgen.model.Doc;
import com.bestiansoft.pdfgen.model.Element;
import com.bestiansoft.pdfgen.model.ElementSign;
import com.bestiansoft.pdfgen.model.Signer;
import com.bestiansoft.pdfgen.repo.ElementRepository;
import com.bestiansoft.pdfgen.repo.ElementSignRepository;
import com.bestiansoft.pdfgen.service.DocService;
import com.bestiansoft.pdfgen.vo.ElementsVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*")
@RestController
public class DocController {

    static long cnt = 0;

    @Autowired
    DocService docService;

    @Autowired
    ElementRepository elementRepository;

    @Autowired
    ElementSignRepository elementSignRepository;

    @Autowired
	PdfGenConfig pdfGenConfig;

    /**
     * pdf 파일을 읽어 브라우저에 보여줌
     * - 경로를 어떻게 받아야 하는가.../pdf/test/test.pdf 로 온다면??
     * - 파일아이디를 받아서 조회하는로직이 필요할듯.
     */
    @RequestMapping(value = "/escDoc/pdf/{fileId}", method = { RequestMethod.GET })
    public void readPdf(HttpServletResponse response, @PathVariable String fileId) throws Exception {
        System.out.println("filename : " + fileId);
        docService.readPdf(response, fileId);
    }

    /**
     * 템플릿 문서 아이디를 받아 생성자의 엘리먼트들을 조회
     */
    @RequestMapping(value = "/elements/{tmpDocId}/{userId}", method = { RequestMethod.GET })
    public Map<String, Object> getElements(@PathVariable String tmpDocId, @PathVariable String userId) throws Exception {
        System.out.println("=================== getElements ");
        System.out.println("tmpDocId : " + tmpDocId);
        System.out.println("userId : " + userId);
        
        Doc doc = new Doc();
        doc.setDocId(tmpDocId);        
        List<Element> elements = docService.getElements(doc, userId);

        System.out.println("조회를 했다..?");

        Map<String, Object> ret = new HashMap<>();
        ret.put("inputs", elements);
        
        return ret;
    }

    // /**
    //  * 1. 생성자용 pdf 조회
    //  *  - 세션체크 및 권한 체크 등 필요할까? , 파일경로는?
    //  */
    // @RequestMapping(value = "/v1/document/{docId}", method = { RequestMethod.GET })
    // public Map<String, Object> getDoc(HttpSession session, @PathVariable String docId) {
    //     System.out.println("1.생성자용 pdf 조회 - 임시");
        
    //     // 세션체크 ?
    //     String userId = (String)session.getAttribute("user");
    //     System.out.println("userId :: " + userId);
    //     if(userId == null){

    //     }
    //     // Doc doc = docService.getDoc(docId);
        
    //     Map<String, Object> doc = new HashMap<>();
    //     doc.put("docId", docId);
    //     doc.put("docName", "테스트문서");
    //     doc.put("fileName", "계약서1.pdf");
    //     // doc.put("filePath", "http://localhost:8888" + pdfGenConfig.getContextPath() + "/sample.pdf");
    //     doc.put("filePath", "null");
    //     doc.put("userId", "signer1");
    //     doc.put("signers", Signer.signers); // 생성자, 참여자목록
    //     doc.put("tmpDocId", "1");

    //     return doc;
    // }

    /**
     * 1. 생성자용 pdf 조회
     *  - 세션체크 및 권한 체크 등 필요할까? , 파일경로는?
     */
    @RequestMapping(value = "/v1/document/{docId}/{tmpDocId}/{userId}", method = { RequestMethod.GET })
    public Map<String, Object> getDoc(HttpSession session, @PathVariable String docId, @PathVariable String tmpDocId, @PathVariable String userId) {
        System.out.println("1.생성자용 pdf 조회");
        
        // 세션체크 ?
        // String userId = (String)session.getAttribute("user");
        // System.out.println("userId :: " + userId);
        // if(userId == null){

        // }

        Map<String, Object> doc = new HashMap<>();
        
        // 템플릿 아이디가 있는 경우 조회한다.
        if(!tmpDocId.equals("NULL")){
            System.out.println("템플릿 아이디가 있는 경우 조회한다. tmpDocId : " + tmpDocId);
            Doc docObj = new Doc();
            docObj.setDocId(tmpDocId);
            List<Element> elements = docService.getElements(docObj, userId);
            doc.put("inputs", elements);
        }        

        // 파일아이디? 로 넘어왔다면 조회...파일경로로 넘어온다면 뭐...        
        // doc.put("filePath", "http://localhost:8888" + pdfGenConfig.getContextPath() + "/sample.pdf");
        // doc.put("filePath", "null");
        // doc.put("fileName", "계약서1.pdf");

        // doc.put("docName", "테스트문서");                
        // doc.put("userId", "signer1");
        // doc.put("signers", Signer.signers); // 생성자, 참여자목록
        // doc.put("tmpDocId", "1");

        return doc;
    }

    // 2. 생성자 pdf 작성 완료
    @RequestMapping(value = "/v1/document/{docId}", method = { RequestMethod.POST })
    public @ResponseBody PdfResponse saveDoc(@PathVariable String docId, @RequestBody Doc doc) {
        //Doc doc = new Doc();
        String urlFilePath = doc.getFilePath();
        // filePath 가 url로 넘어온다. http://localhost:8888/docPdf/sample.pdf
        System.out.println("doc.filePath : " + urlFilePath);
        String dbFilePath = urlFilePath.substring(urlFilePath.indexOf(pdfGenConfig.getContextPath()) + pdfGenConfig.getContextPath().length(), urlFilePath.length() );
        System.out.println(" dbFilePath : " + dbFilePath);

        doc.setDocId(docId);
        doc.setFilePath(pdfGenConfig.getDocHome()+ dbFilePath);
        // doc.setElements(doc.getElements());

        PdfResponse pdfRes = docService.saveDoc(doc);
        return pdfRes;
    }

    // signer 의 해당 input box 만 return
    // // 3. 생성자 및 참여자 pdf 서명 화면
    // @RequestMapping(value = "/v1/document/{docId}/signer/{signerNo}", method = { RequestMethod.GET })
    // public Map<String, Object> getDocToSign(@PathVariable String docId, @PathVariable String signerNo) {
    //     Doc doc = docService.getDoc(docId);
    //     List<Element> elements = docService.getElements(doc, signerNo);

    //     Map<String, Object> ret = new HashMap<>();
    //     ret.put("doc", doc.getFilePath());
    //     ret.put("inputs", elements);
    //     ret.put("signer", Signer.getSigner(signerNo));

    //     return ret;
    // }

    /**
     * 3. 생성자 및 참여자 pdf 서명 화면
     *  - 세션체크 등 추가...
     */    
    @RequestMapping(value = "/v1/document/{docId}/signer/{signerNo}", method = { RequestMethod.GET })
    public Map<String, Object> getDocToSign(@PathVariable String docId, @PathVariable String signerNo) {
        
        System.out.println("3. 생성자 및 참여자 pdf 서명 화면" );

        Doc doc = docService.getDoc(docId);
        // List<Element> elements = docService.getElements(doc);
        if(doc != null){
            System.out.println("a");
        }else{
            System.out.println("b");
        }

        System.out.println("signerNo : " + signerNo);
        List<Element> elements = docService.getElements(doc, signerNo);        

        for(Element e : elements) {
            ElementSign es = e.getElementSign();
            if(es == null)
                continue;
            e.setAddText( es.getEleValue() );
            if(es.getEleSignValue() == null)
                continue;
            String base64Img = Base64Utils.encodeToString(es.getEleSignValue());
            base64Img = "data:image/png;base64," + base64Img;
            e.setSignUrl( base64Img );
        }

        Map<String, Object> ret = new HashMap<>();
        String docFilePath = doc.getPdfPath()==null ? doc.getFilePath() : doc.getPdfPath(); // 서명한 pdf를 불러오고 없으면 최초 파일을 불러와        
        File filePath = new File(docFilePath);
        if(!filePath.exists()){
            ret.put("filePath", "file not exists");
        }else{
            
            // 브라우저에서 열수 있게 url 형식으로 만들어 본다...이게 맞는건지 모르겠네..
            String urlFilePath = "http://localhost:8888" + pdfGenConfig.getContextPath() + docFilePath.replace(pdfGenConfig.getDocHome(), "");
            System.out.println("urlFilePath :: " + urlFilePath);

            ret.put("filePath", urlFilePath);
            ret.put("inputs", elements);
            ret.put("signer", Signer.getSigner(signerNo));  // 사용자 정보조회 구현 필요    
        }

        return ret;
    }

    // 4. 생성자 및 참여자 서명 완료
    @RequestMapping(value = "/v1/document/{docId}/signer/{signerNo}", method = { RequestMethod.POST })
    public @ResponseBody PdfResponse saveInput(@PathVariable String docId, @PathVariable String signerNo, @RequestBody ElementsVo elementsVo) {
        List<Element> inputElements = elementsVo.getInputs();
        PdfResponse pdfRes = docService.saveSignerInput(docId, signerNo, inputElements);

        return pdfRes;
    }
        
    /**
     * 5. 생성자가 최종 pdf 확인 화면
     *  - 문서아이디로 문서를 조회
     *  - 최종 PDF를 보여줌...메모는??
     */    
    @RequestMapping( value="/v1/document/{docId}/signComplete/{signerNo}", method= {RequestMethod.GET} )
    public Map<String, Object> contractView(@PathVariable String docId, @PathVariable String signerNo){        
        Doc doc = docService.getDoc(docId);
        // List<Element> elements = docService.getElements(doc);
        if(doc != null){
            System.out.println("a");
        }else{
            System.out.println("b");
        }

        System.out.println("signerNo : " + signerNo);
        String inputType = "memo";
        List<Element> elements = docService.getElementsType(doc, inputType);    // 메모 전체를 가져온다.
        
        Map<String, Object> ret = new HashMap<>();
        String docFilePath = doc.getPdfPath()==null ? doc.getFilePath() : doc.getPdfPath(); // 서명한 pdf를 불러오고 없으면 최초 파일을 불러와        
        File filePath = new File(docFilePath);
        if(!filePath.exists()){
            ret.put("filePath", "file not exists");
        }else{
            
            // 브라우저에서 열수 있게 url 형식으로 만들어 본다...이게 맞는건지 모르겠네..
            String urlFilePath = "http://localhost:8888" + pdfGenConfig.getContextPath() + docFilePath.replace(pdfGenConfig.getDocHome(), "");
            System.out.println("urlFilePath :: " + urlFilePath);

            ret.put("filePath", urlFilePath);
            ret.put("inputs", elements);
            // ret.put("signer", Signer.getSigner(signerNo));  // 사용자 정보조회 구현 필요    
            ret.put("signer", "signer1");  // 사용자 정보조회 구현 필요   
        }

        return ret;
    }


    // 6. 생성자 pdf 작성 완료
    /**
     * 최종 pdf 에 TSA 값을 셋팅, TSA 값을 받아오기 위해서 문서의 hash 값이 필요하다고 함.
     */    
    @RequestMapping( value="/v1/document/{docId}/signComplete/{signerId}", method= {RequestMethod.POST} )    
    public @ResponseBody PdfResponse signComplete(@PathVariable String docId, @PathVariable String signerId) {
        //log.info("req called");
        
        System.out.println("최종 PDF 저장 처리 시작 ");
        // List<Signer> signer = doc.getSigners();
        // System.out.println("signer :: " + signer.size());        
        //String docId = "doc1";
        PdfResponse pdfRes = docService.signComplete(docId, signerId);

        return pdfRes;
    }
    
    // 7. 사용자별 서명결과 조회
    @RequestMapping( value="/v1/document/{docId}/docSign/{signerId}", method= {RequestMethod.GET} )
    public Map<String, Object> docSign(@PathVariable String docId, @PathVariable String signerId) {
        
        // 문서 및 사용자 유저로 조회
        Map<String, Object> ret = new HashMap<>();
        ret.put("docId", "");
        ret.put("signerId", "");
        ret.put("signYn", "");
        ret.put("signDt", "");

        return ret;
    }

    // test TSA 입력
    @RequestMapping( value="/tsa", method= {RequestMethod.GET} )
    public Map<String, Object> docTsa() {
        
        // 
        docService.saveTsa();
        
        Map<String, Object> ret = new HashMap<>();
        ret.put("result", "");
        return ret;
    }

    // test 체크박스 입력
    @RequestMapping( value="/checkbox", method= {RequestMethod.GET} )
    public Map<String, Object> checkbox() {        
        // 
        docService.checkbox();
        
        Map<String, Object> ret = new HashMap<>();
        ret.put("result", "");
        return ret;
    }


    // test 체크박스 입력
    @RequestMapping( value="/radio", method= {RequestMethod.GET} )
    public Map<String, Object> radio() {        
        // 
        docService.radio();
        
        Map<String, Object> ret = new HashMap<>();
        ret.put("result", "");
        return ret;
    }


}