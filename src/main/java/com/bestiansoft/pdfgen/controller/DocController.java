package com.bestiansoft.pdfgen.controller;

import com.bestiansoft.pdfgen.domain.PdfResponse;
import com.bestiansoft.pdfgen.model.Doc;
import com.bestiansoft.pdfgen.model.Element;
import com.bestiansoft.pdfgen.model.ElementSign;
import com.bestiansoft.pdfgen.service.DocService;
import com.bestiansoft.pdfgen.model.Signer;
import com.bestiansoft.pdfgen.repo.ElementRepository;
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

@CrossOrigin(origins = "*")
@RestController
public class DocController {

    @Autowired
    DocService docService;

    @Autowired
    ElementRepository elementRepository;

    // 1. 생성자용 pdf 조회 - 임시
    @RequestMapping(value = "/v1/document/{docId}", method = { RequestMethod.GET })
    public Map<String, Object> getDoc(@PathVariable String docId) {
        // Doc doc = docService.getDoc(docId);

        Map<String, Object> ret = new HashMap<>();

        // ret.put("doc", doc.getFilePath());
        ret.put("doc", "http://localhost:8080/sample.pdf");
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
        List<Element> allElem = doc.getElements();
        List<Element> signerElem = new ArrayList<>();

        for (Element e : allElem) {
            if (e.getSignerNo().equals(signerNo))
                signerElem.add(e);
        }

        ret.put("doc", doc.getFilePath());
        ret.put("inputs", signerElem);
        ret.put("signer", Signer.getSigner(signerNo));

        return ret;
    }

    @RequestMapping(value = "/v1/signer/{signerNo}/signs/sign", method = { RequestMethod.POST })
    public void saveSign(@PathVariable String signerNo, @RequestBody SignVo signVo) throws IOException {
        // String encSignImg = signVo.getSignImg();
        // String partSeparator = ",";
        // byte[] decodedByte = null;
        // if (encSignImg.contains(partSeparator)) {
        //     encSignImg = encSignImg.split(partSeparator)[1];
        //     decodedByte = Base64.getDecoder().decode(encSignImg);
        // }

        String data = signVo.getSignImg();
        String base64Image = data.split(",")[1];
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));

        
        // write the image to a file
        File outputfile = new File("src/main/resources/static/test_image2.png");
        ImageIO.write(img, "png", outputfile);
    }

    // 4. 생성자 및 참여자 서명 완료
    @RequestMapping(value = "/v1/document/{docId}/signer/{signerNo}", method = { RequestMethod.POST })
    public void saveInput(@PathVariable String docId, @PathVariable String signerNo) {
        Element elem = new Element();
        elem.setEleId("402880926913c3d70169142e16d10004");
        ElementSign value = new ElementSign();
        value.setEleValue("test input!");
        elem.setElementSign(value);

        elementRepository.save(elem);
    }

    
    // 5. 생성자 pdf 확인 화면    
    // ex) @RequestMapping(value = "/v1/document/{docId}/signerAll/", method = { RequestMethod.GET })


    // 6. 생성자 pdf 작성 완료
    //@RequestMapping( value="/v1/document/{docId}/signer/{signerId}", method= {RequestMethod.POST} )    
    @RequestMapping( value="/v1/document/{docId}/signComplete/{signerId}", method= {RequestMethod.GET} )
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