package com.bestiansoft.pdfgen.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.bestiansoft.pdfgen.config.PdfGenConfig;
import com.bestiansoft.pdfgen.domain.PdfResponse;
import com.bestiansoft.pdfgen.model.Doc;
import com.bestiansoft.pdfgen.model.Element;
import com.bestiansoft.pdfgen.repo.DocRepository;
import com.bestiansoft.pdfgen.service.DocService;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DocServiceImpl implements DocService{

    @Autowired
    DocRepository docRepository;

    @Autowired 
    PdfGenConfig pdfGenConfig;

    // 문서생성 seq 번호
    private static int seq = 0;

    @Transactional
    @Override
    public Doc getDoc(String docId) {
        Doc doc = docRepository.findById(docId).orElse(null);
        return doc;
    }
	
	
	//public PdfResponse createPdf(InputReq inputReq) {
    
    /*
        문서 아이디로 DB를 조회하여 pdf파일에다가 입력한다.
    */
    @Override
    public PdfResponse createPdf(String docId) {
		if(docId==null) {
			// log.error("inputReq missing");
			System.out.println("null");
			return null;
		}
		
		docId = "doc1";
		Doc doc = docRepository.findById(docId).orElse(null);

		if(doc == null){
			System.out.println("객체가 없다.");
			return null;
		}

		System.out.println("doc info ");
		System.out.println("doc info " + doc.getDocId());
		System.out.println("doc info " + doc.getDoc());	// file_path
		System.out.println("doc info " + doc.getFileName());	// file_path

		// 파일경로를 조회한다.
		ClassPathResource resource = new ClassPathResource("test_file_name.pdf");
		System.out.println("resource.getPath() : " + resource.getPath());
		
		
		String url = this.getClass().getResource("").getPath();
		System.out.println("url : " + url);
		
		String pFilePath = doc.getDoc();
		String pFileName = doc.getFileName();
		
		List<Element> elements = doc.getElement();
		
		String docSave = pFileName + "_" + (seq++) + ".pdf";
		
		String fnDoc = pdfGenConfig.getDocHome() + docSave;
		System.out.println("fnDoc :: " + fnDoc);

		//File fDoc = new File(pFilePath);
		//File fDoc = new File(resource.getPath());
		String tmpPath = "D:\\ktpdf\\pdfgen\\src\\main\\resources\\";

		File fDoc = new File(tmpPath+"test_file_name.pdf");
		if( !fDoc.exists()) {
		// 	log.error("not exist {}", fnDoc);
			System.out.println("not exist {} : " + pFilePath);
		 	return null;
		} else {
		// 	log.info("exist {}", fnDoc);
		}
		
		if( !fDoc.canRead() ) {
		// 	log.error("can't read");
			System.out.println("can't read");
		}
		
		String fnSave = pdfGenConfig.getDocHome() + docSave;
		
		// List<InputBase> inputs = inputReq.getInputs();
		// if(inputs==null || inputs.size()<1) {
		// 	// no inputs
		// 	return new PdfResponse(doc);
		// }
		
		// 기존에 파일을 읽어야 한다.
		try (PDDocument document = PDDocument.load(new File(tmpPath+"test_file_name.pdf"))) {
			
			for(Element input : elements) {
				
				int pageNo = input.getPage();
				PDPage page = document.getPage(pageNo-1);	// 0부터 시작
				PDRectangle rectangle = page.getMediaBox();
				float h = rectangle.getHeight();
				float w = rectangle.getWidth();
				System.out.println("h: " + h + " w : " + w);

				float x_adj = input.getX()*w;
				float y_adj = input.getY()*h;
				float w_adj = input.getW()*w;
				float h_adj = input.getH()*h;
				
				//log.info("adj="+x_adj+", " + y_adj + ", " + w_adj + ", " + h_adj);
				System.out.println("adj="+x_adj+", " + y_adj + ", " + w_adj + ", " + h_adj);
				
				PDPageContentStream contentStream = new PDPageContentStream(document, page, AppendMode.APPEND, true, true);

				if(input.isSign()) {
					//String signUrl = pdfGenConfig.getDocHome() + input.getSignUrl();
					//PDImageXObject pdImage = PDImageXObject.createFromFile(signUrl, document);
					
					//contentStream.drawImage(pdImage, x_adj, y_adj, w_adj, h_adj);

				} else if(input.isText()) {

					System.out.println("여기까지 오나?" + input.getAddText());
					
					contentStream.beginText();
					
					// 폰트를 일거온다.
					InputStream fontStream = new FileInputStream("C:\\Windows\\Fonts\\NanumGothic_3.ttf");
					PDType0Font fontNanum = PDType0Font.load(document, fontStream);
					//contentStream.setFont( getFont(input.getFont()), input.getCharSize());
					contentStream.setFont( fontNanum, input.getCharSize());
					contentStream.newLineAtOffset(x_adj, y_adj);					
					contentStream.showText(input.getAddText());
					contentStream.endText();
				}
				contentStream.close();
			}
			
			System.out.println("여기까지 오나? fnSave : " + fnSave);

			document.save(fnSave);
			document.close();
			
			return new PdfResponse(fnSave);
		} catch (IOException e) {
			//log.error("PDDocument load fail for fnDoc={} : {}", fnDoc, e.toString());
			System.out.println("PDDocument load fail for fnDoc={} : {}" + e.toString());			

			e.printStackTrace();
		}
		
		return null;
	}

	private PDType1Font getFont(String font) {
		PDType1Font pdFont = PDType1Font.TIMES_ROMAN;
		
		if( "Times-Roman".equals(font) ) pdFont = PDType1Font.TIMES_ROMAN;
		else if( "Courier-Bold".equals(font) ) pdFont = PDType1Font.COURIER_BOLD;
		return pdFont;
	}

}