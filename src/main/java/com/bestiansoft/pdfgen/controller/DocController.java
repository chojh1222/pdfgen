package com.bestiansoft.pdfgen.controller;

import java.io.File;
import java.util.ArrayList;
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
import com.bestiansoft.pdfgen.model.Ebox;
import com.bestiansoft.pdfgen.model.EboxUser;
import com.bestiansoft.pdfgen.model.Element;
import com.bestiansoft.pdfgen.model.ElementSign;
import com.bestiansoft.pdfgen.model.Signer;
import com.bestiansoft.pdfgen.repo.ElementRepository;
import com.bestiansoft.pdfgen.repo.ElementSignRepository;
import com.bestiansoft.pdfgen.service.DocService;
import com.bestiansoft.pdfgen.util.Common;
import com.bestiansoft.pdfgen.vo.ElementsVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
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

    @Autowired
    Common common;

    /**
     * pdf 파일을 읽어 브라우저에 보여줌
     * - 경로를 어떻게 받아야 하는가.../pdf/test/test.pdf 로 온다면??
     * - 파일아이디를 받아서 조회하는로직이 필요할듯.
     */
    @RequestMapping(value = "/escDoc/pdf/{docId}", method = { RequestMethod.GET })
    public void readPdf(HttpServletRequest request, HttpServletResponse response, @PathVariable String docId) throws Exception {
        System.out.println("docId : " + docId);
        docService.readPdf(request, response, docId);
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

    /**
     * 1. 생성자용 pdf 조회
     *  - 세션체크 및 권한 체크 등 필요할까? , 파일경로는 DB조회
     */
    @RequestMapping(value = "/v1/document/{docId}/{tmpDocId}/{userId}", method = { RequestMethod.GET })
    public Map<String, Object> getDoc(HttpSession session, @PathVariable String docId, @PathVariable String tmpDocId, @PathVariable String userId) {
        System.out.println("1.생성자용 pdf 조회");
        
        // 세션체크 ?
        // String userId = (String)session.getAttribute("user");
        // System.out.println("userId :: " + userId);
        // if(userId == null){

        // }

        System.out.println("docId :: " + docId);
        System.out.println("tmpDocId :: " + tmpDocId);
                
        Map<String, Object> doc = new HashMap<>();
        // 포탈DB에서 기본정보를 조회
        Ebox ebox = docService.getBoxInfo(docId);

        System.out.println("filePath : " + ebox.getPdfFilePath());

        if(ebox == null){
            System.out.println("getEbox is null");
        }else{
            System.out.println("getEbox is not null");
            System.out.println("ebox.getUsers() " + ebox.getUsers().size());

            List<Signer> signers = new ArrayList<>();
            for(EboxUser eboxUser : ebox.getUsers()){            
                System.out.println(eboxUser.getUserSeq() + " : " + eboxUser.getUserNm());
                

                Signer signer = new Signer();                
                signer.setSignerNm(eboxUser.getUserNm());
                signer.setSignerId(eboxUser.getUserNo());   // 참여자키
                signer.setSignerNo(eboxUser.getUserSeq());  // 참여자순번
                // user.getUserNm();                
                signers.add(signer);
            }

            String urlFilePath = common.getFileUrl(docId);

            System.out.println("urlFilePath :: " + urlFilePath);

            doc.put("docId", ebox.getDocId());
            doc.put("filePath", urlFilePath);
            doc.put("signers", signers);
            // doc.put("signers", Signer.signers); // 생성자, 참여자목록
        }
        
        // 템플릿 아이디가 있는 경우 조회한다.
        if(!tmpDocId.equals("null")){
            System.out.println("템플릿 아이디가 있는 경우 조회한다. tmpDocId : " + tmpDocId);
            Doc docObj = new Doc();
            docObj.setDocId(tmpDocId);
            List<Element> elements = docService.getElements(docObj, userId);
            System.out.println("elements " + elements.size());
            
            if(elements.size() > 0) doc.put("inputs", elements);
        }

        System.out.println("1.생성자용 pdf 조회 끝");

        return doc;
    }

    // 2. 생성자 pdf 작성 완료
    @RequestMapping(value = "/v1/document/{docId}", method = { RequestMethod.POST })
    public @ResponseBody PdfResponse saveDoc(@PathVariable String docId, @RequestBody Doc doc) {        

        // 포탈의 첨부파일경로를 디비에 저장
        Ebox ebox = docService.getBoxInfo(docId);
        String filePath = ebox.getPdfFilePath();
        String userNo = ebox.getReqerId();

        System.out.println("userNo ::: " + userNo);
        
        // String filePath = ebox.getEBoxDoc().getPdfPath();
        // String userNo = ebox.getUserNo();        

        doc.setDocId(docId);
        doc.setFilePath(filePath);
        doc.setUserId(userNo);
        // doc.setElements(doc.getElements());

        PdfResponse pdfRes = docService.saveDoc(doc);
        return pdfRes;
    }

    /**
     * 3. 생성자 및 참여자 pdf 서명 화면
     *  - 세션체크 등 추가...
     */    
    @RequestMapping(value = "/v1/document/{docId}/signer/{signerNo}", method = { RequestMethod.GET })
    public Map<String, Object> getDocToSign(@PathVariable String docId, @PathVariable String signerNo) {
        
        System.out.println("3. 생성자 및 참여자 pdf 서명 화면" );
        System.out.println("docId : " + docId);
        System.out.println("signerNo : " + signerNo);

        Map<String, Object> ret = new HashMap<>();
        Doc doc = docService.getDoc(docId);
        // List<Element> elements = docService.getElements(doc);
        if(doc != null){
            System.out.println("a");

            List<Element> allElements = docService.getElements(doc);
            List<Element> elements = new ArrayList<>();
            
            for(Element element : allElements) {
                if("memo".equals(element.getInputType() ) || element.getSignerNo().equals(signerNo)) {            
                    elements.add(element);
                }
            }

            for(Element e : elements) {            
                ElementSign es = e.getElementSign();
                if(es == null){
                    continue;                
                }else{
                    e.setAddText( es.getEleValue() );
                }
                    
                if(es.getEleSignValue() == null){
                    continue;
                }else{
                    String base64Img = Base64Utils.encodeToString(es.getEleSignValue());
                    base64Img = "data:image/png;base64," + base64Img;
                    e.setSignUrl( base64Img );
                }
            }
            
            // String docFilePath = doc.getPdfPath()==null ? doc.getFilePath() : doc.getPdfPath(); // 서명한 pdf를 불러오고 없으면 최초 파일을 불러와        
            String docFilePath = doc.getFilePath(); // 원본에 덮어쓰기에 원본pdf 경로를 읽어온다.
            System.out.println("docFilePath :: " + docFilePath);
            File filePath = new File(docFilePath);
            if(!filePath.exists()){
                ret.put("filePath", "file not exists");
            }else{
                String urlFilePath = common.getFileUrl(docId);

                Signer signer = new Signer();
                signer.setSignerNo(signerNo);

                ret.put("filePath", urlFilePath);
                ret.put("inputs", elements);
                // ret.put("signer", Signer.getSigner(signerNo));
                ret.put("signer", signer);  // 사용자 정보조회 구현 필요할 수 있음.
            }
            
        }else{
            System.out.println("객체가 존재하지 않습니다.");

            Signer signer = new Signer();
            ret.put("signer", signer);  // 사용자 정보조회 구현 필요할 수 있음.
        }
        // List<Element> elements = docService.getElements(doc, signerNo);     
        
        return ret;
    }

    // 4. 생성자 및 참여자 서명 완료
    @RequestMapping(value = "/v1/document/{docId}/signer/{signerNo}", method = { RequestMethod.POST })
    public @ResponseBody PdfResponse saveInput(@PathVariable String docId, @PathVariable String signerNo, @RequestBody ElementsVo elementsVo) {

        System.out.println("4. 생성자 및 참여자 서명 완료" );
        System.out.println("docId : " + docId);
        System.out.println("signerNo : " + signerNo);

        List<Element> inputElements = elementsVo.getInputs();
        String userHash = elementsVo.getUserHash();
        System.out.println("elementsVo.getUserHash() : " + elementsVo.getUserHash());

        PdfResponse pdfRes = docService.saveSignerInput(docId, signerNo, userHash, inputElements);

        return pdfRes;
    }
        
    /**
     * 5. 생성자가 최종 pdf 확인 화면
     *  - 문서아이디로 문서를 조회
     *  - 최종 PDF를 보여줌...메모는??
     */    
    @RequestMapping( value="/v1/document/{docId}/signComplete/{signerNo}", method= {RequestMethod.GET} )
    public Map<String, Object> contractView(@PathVariable String docId, @PathVariable String signerNo){      
        
        System.out.println("5. 생성자가 최종 pdf 확인 화면" );
        System.out.println("docId : " + docId);
        System.out.println("signerNo : " + signerNo);

        Doc doc = docService.getDoc(docId);
        // List<Element> elements = docService.getElements(doc);
        if(doc != null){
            System.out.println("a");
        }else{
            System.out.println("b");
        }

        String inputType = "memo";
        List<Element> elements = docService.getElementsType(doc, inputType);    // 메모 전체를 가져온다.

        for(Element e : elements) {
            System.out.println("2222");
            ElementSign es = e.getElementSign();
            if(es == null){
                continue;                
            }else{
                e.setAddText( es.getEleValue() );
            }
        }
        
        Map<String, Object> ret = new HashMap<>();
        // String docFilePath = doc.getPdfPath()==null ? doc.getFilePath() : doc.getPdfPath(); // 서명한 pdf를 불러오고 없으면 최초 파일을 불러와 
        String docFilePath = doc.getFilePath(); // 원본에 덮어쓰기에 원본pdf 경로를 읽어온다.       
        File filePath = new File(docFilePath);
        if(!filePath.exists()){
            ret.put("filePath", "file not exists");
        }else{

            String urlFilePath = common.getFileUrl(docId);

            Signer signer = new Signer();
            signer.setSignerNo(signerNo);

            ret.put("filePath", urlFilePath);
            ret.put("inputs", elements);
            // ret.put("signer", Signer.getSigner(signerNo));  // 사용자 정보조회 구현 필요    
            ret.put("signer", signer);  // 사용자 정보조회 구현 필요   
        }

        return ret;
    }


    /**
     * 6. 생성자 pdf 작성 완료
     *  - 최종문서에 tsa값을 삽입
     *   => 포탈에서 진행하기로 함.
     *  - 처리해야 할게 있나?
     */
    @RequestMapping( value="/v1/document/{docId}/signComplete/{signerId}", method= {RequestMethod.POST} )    
    public @ResponseBody PdfResponse signComplete(@PathVariable String docId, @PathVariable String signerId) {
        //log.info("req called");
        
        System.out.println("최종 PDF 저장 처리 시작 ");
        // List<Signer> signer = doc.getSigners();
        // System.out.println("signer :: " + signer.size());        
        //String docId = "doc1";
        // PdfResponse pdfRes = docService.signComplete(docId, signerId);
        PdfResponse pdfRes = null;
        return pdfRes;
    }
    
    /**
     * 7. 사용자별 서명결과 조회
     *  필요없다 - 포탈에서 정보를 가지고 있음.
     */
    // 
    @RequestMapping( value="/escDoc/{docId}/docSign/{signerId}", method= {RequestMethod.GET} )
    public Map<String, Object> docSign(@PathVariable String docId, @PathVariable String signerId) {
        
        // 문서 및 사용자 유저로 조회
        System.out.println("사용자별 서명결과 조회 시작");
        
        Doc doc = new Doc();
        doc.setDocId(docId);        
        List<Element> elements = docService.getElements(doc, signerId);

        Map<String, Object> ret = new HashMap<>();
        ret.put("docId", docId);
        ret.put("signerId", signerId);
        ret.put("signYn", "");
        

        for(Element e : elements) {
            if("sign".equals(e.getInputType())){
                ElementSign es = e.getElementSign();
                
                if(es == null){
                    ret.put("signYn", "N");
                    ret.put("signDt", "");
                }else{
                    ret.put("signYn", "Y");
                    ret.put("signDt", e.getRegDt());
                }
            }                        
        }

        return ret;
    }

    /**
     * test - TSA 입력
     */
    @RequestMapping( value="/tsa", method= {RequestMethod.GET} )
    public Map<String, Object> docTsa() {
        
        // 
        docService.saveTsa();
        
        Map<String, Object> ret = new HashMap<>();
        ret.put("result", "");
        return ret;
    }

    /**
     * test - 체크박스 입력
     */
    @RequestMapping( value="/checkbox", method= {RequestMethod.GET} )
    public Map<String, Object> checkbox() {        
        // 
        docService.checkbox();
        
        Map<String, Object> ret = new HashMap<>();
        ret.put("result", "");
        return ret;
    }

    /**
     * test - 라디오 입력
     */
    @RequestMapping( value="/radio", method= {RequestMethod.GET} )
    public Map<String, Object> radio() {        
        // 
        docService.radio();
        
        Map<String, Object> ret = new HashMap<>();
        ret.put("result", "");
        return ret;
    }

    /**
     * test - 라디오 입력
     */

    @Autowired 
    private ResourceLoader resourceLoader;

    @RequestMapping( value="/test", method= {RequestMethod.GET} )
    public void test() {
        // 
        try{
            System.out.println(" ================= ");
            // System.out.println(resourceLoader.getResource("classpath:/font/NotoSansCJKkr-Regular.otf").getURI().getPath());            

            docService.test();

            System.out.println(" ================= ");
        }catch(Exception e){

        }
    }


}