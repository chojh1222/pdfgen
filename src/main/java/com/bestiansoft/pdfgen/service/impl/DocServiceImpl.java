package com.bestiansoft.pdfgen.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.bestiansoft.pdfgen.config.PdfGenConfig;
import com.bestiansoft.pdfgen.domain.PdfResponse;
import com.bestiansoft.pdfgen.model.Doc;
import com.bestiansoft.pdfgen.model.DocHistory;
import com.bestiansoft.pdfgen.model.Element;
import com.bestiansoft.pdfgen.model.ElementSign;
import com.bestiansoft.pdfgen.repo.DocRepository;
import com.bestiansoft.pdfgen.repo.ElementRepository;
import com.bestiansoft.pdfgen.repo.ElementSignRepository;
import com.bestiansoft.pdfgen.service.DocService;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DocServiceImpl implements DocService {

	@Autowired
	DocRepository docRepository;

	@Autowired
	ElementRepository elementRepository;

	@Autowired
	ElementSignRepository elementSignRepository;
	
	@Autowired
	PdfGenConfig pdfGenConfig;

	// 문서생성 seq 번호
	private static int seq = 0;

	@Override
	public Doc getDoc(String docId) {
		Doc doc = docRepository.findById(docId).orElse(new Doc());
		return doc;
	}

	@Override
	public void saveDoc(Doc doc) {
		docRepository.save(doc);
	}

	@Override
	public List<Element> getElements(Doc doc, String signerNo) {
		return elementRepository.findByDocAndSignerNo(doc, signerNo);
	}

	@Override
	public List<Element> getElements(Doc doc) {
		return elementRepository.findByDoc(doc);
	}

	/**
	 * 파일경로를 url 로 받아서 브라우저에서 보여준다.
	 * 추후 세션이나 권한 체크를 할 수 있게 하려고 이렇게 해봄
	 */
	@Override
	public void readPdf(HttpServletResponse response, String fileName){
		
		FileInputStream fis = null;
        BufferedOutputStream bos = null;
		
        try {

			String pdfFileName = pdfGenConfig.getDocHome() + File.separator + fileName;

            File pdfFile = new File(pdfFileName);

            //클라이언트 브라우져에서 바로 보는 방법(헤더 변경)
            response.setContentType("application/pdf");

            //★ 이 구문이 있으면 [다운로드], 이 구문이 없다면 바로 target 지정된 곳에 view 해줍니다.
            // response.addHeader("Content-Disposition", "attachment; filename="+pdfFile.getName()+".pdf");

            //파일 읽고 쓰는 건 일반적인 Write방식이랑 동일합니다. 다만 reponse 출력 스트림 객체에 write.
            fis = new FileInputStream(pdfFile);
            int size = fis.available(); //지정 파일에서 읽을 수 있는 바이트 수를 반환
            byte[] buf = new byte[size]; //버퍼설정
            int readCount = fis.read(buf);
            response.flushBuffer();
            bos = new BufferedOutputStream(response.getOutputStream());
            bos.write(buf, 0, readCount);
            bos.flush();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close(); //close는 꼭! 반드시!
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}

	/**
	 * PDF파일에 값 또는 서명정보를 입력
	 */
	@Transactional
	@Override
	public PdfResponse saveSignerInput(String docId, String singerNo, List<Element> inputElements) {

		// PDF 파일 저장 처리
		// docId 로 기초가 되는 파일 조회
		System.out.println("pdf 생성 및 객체 저장 시작 ");
		System.out.println("docId : " + docId);
		Doc doc = docRepository.findById(docId).orElse(null);

		if (doc == null) {
			System.out.println("객체가 없다.");
			return new PdfResponse(999, "객체가 없다.");
		}

		String saveRoot = pdfGenConfig.getDocHome();	// D:/ktpdf/pdfgen/src/main/resources/storage
		String oriFilePath = doc.getPdfPath()==null ? doc.getFilePath() : doc.getPdfPath();				// 최종 pdf 가 없다면 원본에서 조회해와야 한다.
		//String savePdfPath = saveRoot + File.separator + doc.getDocId()+".pdf";	// 저장 전체경로
		String savePdfPath = saveRoot + File.separator + doc.getDocId() + "_" + singerNo + ".pdf";		// 저장 전체경로
		String savePdfName = doc.getFileName();		// 별필요없을듯. 우선 파일명을 그대로 저장
		
		// 파일을 조회한다.
		try{
			PDDocument document = PDDocument.load(new File(oriFilePath));
			Element elem;

			for (Element element : inputElements) {

				int pageNo = element.getPage();
				System.out.println("pageNo : " + pageNo);
				PDPage page = document.getPage(pageNo - 1); // 0부터 시작
				PDRectangle rectangle = page.getMediaBox();
				float h = rectangle.getHeight();
				float w = rectangle.getWidth();
				String fontType = element.getFont();
				String pdFont = pdfGenConfig.getFont1();
				System.out.println("h: " + h + " w : " + w);

				float x_adj = element.getX() * w;
				float y_adj = element.getY() * h;
				float w_adj = element.getW() * w;
				float h_adj = element.getH() * h;
				// float x_adj = element.getX();
				// float y_adj = h - element.getY() - 12;
				// float w_adj = element.getW();
				// float h_adj = element.getH();


				// log.info("adj="+x_adj+", " + y_adj + ", " + w_adj + ", " + h_adj);
				System.out.println("adj=" + x_adj + ", " + y_adj + ", " + w_adj + ", " + h_adj);

				PDPageContentStream contentStream = new PDPageContentStream(document, page, AppendMode.APPEND, true, true);

				byte[] imageBytes = null;

				if (element.isSign()) {
					// DB에 있는 이미지를 읽어와서 넣어야 되는 경우
					/*
					String signUrl = pdfGenConfig.getImgPath();
					PDImageXObject pdImage = PDImageXObject.createFromFile(signUrl, document);
					contentStream.drawImage(pdImage, x_adj, y_adj, w_adj, h_adj);
					*/

					// 서명이미지 저장
					String signUrl = element.getSignUrl();
					String inputType = element.getInputType();					
					if("sign".equals(inputType) && signUrl != null && signUrl.contains("base64")) {
						String base64Image = signUrl.split(",")[1];
						imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);

						PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, imageBytes, "sign.gif");
						contentStream.drawImage(pdImage, x_adj, y_adj, w_adj, h_adj);
					}

				} else if (element.isText()) {

					// System.out.println("여기까지 오나?" + input.getAddText());

					contentStream.beginText();
					
					// 폰트를 읽어온다.
					if ("Times-Roman".equals(fontType)){
						pdFont = pdfGenConfig.getFont1();
					}else if ("Courier-Bold".equals(fontType)){
						pdFont = pdfGenConfig.getFont2();
					}

					InputStream fontStream = new FileInputStream(pdFont);
					PDType0Font pdfType0Font = PDType0Font.load(document, fontStream);
					// contentStream.setFont( getFont(input.getFont()), input.getCharSize());
					contentStream.setFont(pdfType0Font, element.getCharSize());
					contentStream.newLineAtOffset(x_adj, y_adj);
					//contentStream.moveTextPositionByAmount(x_adj, y_adj);
					contentStream.showText(element.getAddText()); // 값 셋팅
					contentStream.endText();
				}
				contentStream.close();

				// 저장처리 
				elem = elementRepository.findById(element.getEleId()).get();
		
				// db에서 해당 element id로 저장된 record를 찾을 수 없을 경우 종료
				if(elem == null)
					return null;

				// 이전에 해당 element에 입력된 값이 없다면 새로입력
				if(elem.getElementSign() == null)
					elem.setElementSign(new ElementSign());

				elem.getElementSign().setEleValue(element.getAddText());
				elem.getElementSign().setEleSignValue(imageBytes);
				
				elementSignRepository.save(elem.getElementSign());	// 값 저장처리
			}

			System.out.println("여기까지 오나? fnSave : " + savePdfPath);

			document.save(savePdfPath);
			document.close();

			String pdfName = new Date()+".pdf";
			
			DocHistory docHistory = new DocHistory();
			// 히스토리값을 저장한다.			
			docHistory.setPdfName(pdfName);
			docHistory.setPdfPath(savePdfPath);
			docHistory.setRegDt(new Date()); 			
			docHistory.setSignId(singerNo);
			List<DocHistory> docHistoryList = new ArrayList<DocHistory>();
			docHistoryList.add(docHistory);
			doc.setHistory(docHistoryList);
			
			// pdf 저장 후 DB값 입력
			doc.setPdfName(pdfName);
			doc.setPdfPath(savePdfPath);
			doc.setPdfRegDt(new Date());
			
			docRepository.save(doc);

		} catch (IOException e) {
			// log.error("PDDocument load fail for fnDoc={} : {}", fnDoc, e.toString());
			System.out.println("PDDocument load fail for fnDoc={} : {}" + e.toString());

			e.printStackTrace();
		} finally{

		}	

		return new PdfResponse(200, "저장 완료");
	}


	private PDType1Font getFont(String font) {
		PDType1Font pdFont = PDType1Font.TIMES_ROMAN;

		if ("Times-Roman".equals(font))
			pdFont = PDType1Font.TIMES_ROMAN;
		else if ("Courier-Bold".equals(font))
			pdFont = PDType1Font.COURIER_BOLD;
		return pdFont;
	}

	/**
	 * TSA 값을 넣어야 함...
	 */
	@Transactional
	@Override
	public void saveTsa(){
		System.out.println(" TSA start ");
		
		File pdfFile = new File("D://ktpdf//pdfgen//src//main//resources//static//sample.pdf");
		File signedPdfFile = new File("D://ktpdf//pdfgen//src//main//resources//static//sample_1.pdf");
		String NAME = "test";
		String LOCATION = "seoul";
		String REASON = "timestamp";

		try (
			FileInputStream fis1 = new FileInputStream(pdfFile);
			FileOutputStream fos = new FileOutputStream(signedPdfFile);
			FileInputStream fis = new FileInputStream(signedPdfFile);
			PDDocument doc = PDDocument.load(pdfFile)) {
			int readCount;
			final byte[] buffer = new byte[8 * 1024];
			while ((readCount = fis1.read(buffer)) != -1) {
				fos.write(buffer, 0, readCount);
			}
			
			
			final PDSignature signature = new PDSignature();
			signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
			signature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);
			signature.setName(NAME);
			signature.setLocation(LOCATION);
			signature.setReason(REASON);
			
			System.out.println("Calendar.getInstance() : " + Calendar.getInstance());
			signature.setSignDate(Calendar.getInstance());

			doc.addSignature(signature);
			doc.saveIncremental(fos);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	
}