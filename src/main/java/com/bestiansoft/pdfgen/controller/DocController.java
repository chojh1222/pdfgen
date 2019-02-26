package com.bestiansoft.pdfgen.controller;

import com.bestiansoft.pdfgen.domain.PdfResponse;
import com.bestiansoft.pdfgen.model.Doc;
import com.bestiansoft.pdfgen.model.Element;
import com.bestiansoft.pdfgen.model.ElementSign;
import com.bestiansoft.pdfgen.service.DocService;
import com.bestiansoft.pdfgen.model.Signer;
import com.bestiansoft.pdfgen.repo.ElementRepository;
import com.bestiansoft.pdfgen.repo.ElementSignRepository;
import com.bestiansoft.pdfgen.vo.ElementsVo;
import com.bestiansoft.pdfgen.vo.SignVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

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

    

    @RequestMapping(value = "/v1/test", method = { RequestMethod.GET })
    public void test() {
        // Element e = new Element();
        // ElementSign es = new ElementSign();
        // es.setEleValue("12345");

        // es.setElement(e);
        // e.setElementSign(es);
        

        // elementRepository.save(e);
        Element e = elementRepository.findById("402880926928ec53016928ec75f50000").orElse(null);
        System.out.println( e.getElementSign() );
    }

    // 1. 생성자용 pdf 조회 - 임시
    @RequestMapping(value = "/v1/document/{docId}", method = { RequestMethod.GET })
    public Map<String, Object> getDoc(@PathVariable String docId) {
        // Doc doc = docService.getDoc(docId);

        Map<String, Object> ret = new HashMap<>();

        // ret.put("doc", doc.getFilePath());
        ret.put("doc", "http://localhost:8888/sample.pdf");
        ret.put("signers", Signer.signers);

        return ret;
    }

    // 2. 생성자 pdf 작성 완료
    @RequestMapping(value = "/v1/document/{docId}", method = { RequestMethod.POST })
    public void saveDoc(@PathVariable String docId, @RequestBody ElementsVo elementsVo) {
        Doc doc = new Doc();

        doc.setDocId(docId);
        doc.setFilePath("http://localhost:8888/sample.pdf");
        doc.setElements(elementsVo.getInputs());

        docService.saveDoc(doc);
    }

    // 3. 생성자 및 참여자 pdf 서명 화면
    @RequestMapping(value = "/v1/document/{docId}/signer/{signerNo}", method = { RequestMethod.GET })
    public Map<String, Object> getDocToSign(@PathVariable String docId, @PathVariable String signerNo) {
        Doc doc = docService.getDoc(docId);
        List<Element> elements = docService.getElements(doc, signerNo);

        Map<String, Object> ret = new HashMap<>();
        List<Element> allElem = doc.getElements();
        List<Element> signerElem = new ArrayList<>();

        for (Element e : allElem) {
            if (e.getSignerNo().equals(signerNo))
                signerElem.add(e);
        }

        ret.put("doc", doc.getFilePath());
        ret.put("inputs", elements);
        ret.put("signer", Signer.getSigner(signerNo));

        return ret;
    }

    @RequestMapping(value = "/v1/signer/{signerNo}/signs/sign", method = { RequestMethod.POST })
    public Map<String, Object> saveSign(HttpServletRequest request, @PathVariable String signerNo, @RequestBody SignVo signVo) throws IOException {
        String data = signVo.getSignImg();
        String base64Image = data.split(",")[1];
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);

        ElementSign es = new ElementSign();
        es.setEleSignValue(base64Image);

        elementSignRepository.save(es);


        BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));

        // write the image to a file
        String filename = signerNo + "_" + (++cnt) + ".png";
        String filepath = "src/main/resources/static/signs/" + filename;
        File outputfile = new File(filepath);
        ImageIO.write(img, "png", outputfile);

        String domain = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String retPath = domain + "/signs/" + filename;

        Map<String, Object> ret = new HashMap<>();
        ret.put("signImgs", retPath);
        return ret;
    }

    // 4. 생성자 및 참여자 서명 완료
    @RequestMapping(value = "/v1/document/{docId}/signer/{signerNo}", method = { RequestMethod.POST })
    public void saveInput(@PathVariable String docId, @PathVariable String signerNo, @RequestBody ElementsVo elementsVo) {
        
        List<Element> inputElements = elementsVo.getInputs();
        List<ElementSign> inputs = new ArrayList<>();
        for(Element inputElement : inputElements) {
            ElementSign es = new ElementSign();
            
            es.setEleValue(inputElement.getAddText());
            es.setEleSignValue(inputElement.getElementSign());

            inputElement.setElementSign(es);
            inputs.add(es);
        }

        docService.saveSignerInput(inputs);
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