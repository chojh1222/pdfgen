package com.bestiansoft.pdfgen.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
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

import org.apache.fontbox.encoding.StandardEncoding;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceCharacteristicsDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceEntry;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDRadioButton;
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
	public PdfResponse saveDoc(Doc doc) {
		Doc doc1 = docRepository.save(doc);

		System.out.println(" doc1 :: " + doc1.getDocId());
		return new PdfResponse(200, "success");
	}

	@Override
	public List<Element> getElements(Doc doc, String signerNo) {
		return elementRepository.findByDocAndSignerNo(doc, signerNo);
	}

	@Override
	public List<Element> getElementsType(Doc doc, String inputType) {
		return elementRepository.findByDocAndInputType(doc, inputType);
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
			return new PdfResponse(404, "객체가 없다.");
		}

		String saveRoot = pdfGenConfig.getDocHome();	// D:/ktpdf/pdfgen/src/main/resources/storage
		String oriFilePath = doc.getPdfPath()==null ? doc.getFilePath() : doc.getPdfPath();				// 최종 pdf 가 없다면 원본에서 조회해와야 한다.
		//String savePdfPath = saveRoot + File.separator + doc.getDocId()+".pdf";	// 저장 전체경로
		String savePdfPath = saveRoot + File.separator + doc.getDocId() + "_" + singerNo + ".pdf";		// 저장 전체경로
		String savePdfName = doc.getFileName();		// 별필요없을듯. 우선 파일명을 그대로 저장
		
		// 파일을 조회한다.
		try{
			PDDocument document = PDDocument.load(new File(oriFilePath));
			
			// checkbox
			PDAcroForm acroForm = new PDAcroForm(document);
			document.getDocumentCatalog().setAcroForm(acroForm);
			COSDictionary normalAppearances = new COSDictionary();
			PDAppearanceDictionary pdAppearanceDictionary = new PDAppearanceDictionary();
			pdAppearanceDictionary.setNormalAppearance(new PDAppearanceEntry(normalAppearances));
			pdAppearanceDictionary.setDownAppearance(new PDAppearanceEntry(normalAppearances));

			// 체크 표시 설정
			PDAppearanceStream pdAppearanceStream = new PDAppearanceStream(document);
			pdAppearanceStream.setResources(new PDResources());
			try (PDPageContentStream pdPageContentStream = new PDPageContentStream(document, pdAppearanceStream))
			{
				pdPageContentStream.setFont(PDType1Font.ZAPF_DINGBATS, 14.5f);
				pdPageContentStream.beginText();
				pdPageContentStream.newLineAtOffset(3, 4);
				pdPageContentStream.showText("\u2713");
				pdPageContentStream.endText();
			}catch (Exception e) {
				//TODO: handle exception
			}
			pdAppearanceStream.setBBox(new PDRectangle(18, 18));
			normalAppearances.setItem("Yes", pdAppearanceStream);

			// x 표시 설정
			pdAppearanceStream = new PDAppearanceStream(document);
			pdAppearanceStream.setResources(new PDResources());
			try (PDPageContentStream pdPageContentStream = new PDPageContentStream(document, pdAppearanceStream))
			{
				pdPageContentStream.setFont(PDType1Font.ZAPF_DINGBATS, 14.5f);
				pdPageContentStream.beginText();
				pdPageContentStream.newLineAtOffset(3, 4);
				// pdPageContentStream.showText("\u2718");		// x 표시... 바꿔야 함.
				pdPageContentStream.showText("");		// 공백 표시
				pdPageContentStream.endText();
			} catch (Exception e) {
				//TODO: handle exception
			}
			pdAppearanceStream.setBBox(new PDRectangle(18, 18));
			normalAppearances.setItem("Off", pdAppearanceStream);

			
			// checkbox
			
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

				// float x_adj = element.getX() * w;
				// float y_adj = element.getY() * h;
				// float w_adj = element.getW() * w;
				// float h_adj = element.getH() * h;
				float x_adj = element.getX();
				float y_adj = element.getY();				
				float w_adj = element.getW();
				float h_adj = element.getH();


				// log.info("adj="+x_adj+", " + y_adj + ", " + w_adj + ", " + h_adj);
				// System.out.println("adj=" + x_adj + ", " + y_adj + ", " + w_adj + ", " + h_adj);

				PDPageContentStream contentStream = new PDPageContentStream(document, page, AppendMode.APPEND, true, true);

				byte[] imageBytes = null;

				if (element.isSign()) {
					System.out.println("서명이다!!!!!");
					// DB에 있는 이미지를 읽어와서 넣어야 되는 경우
					/*
					String signUrl = pdfGenConfig.getImgPath();
					PDImageXObject pdImage = PDImageXObject.createFromFile(signUrl, document);
					contentStream.drawImage(pdImage, x_adj, y_adj, w_adj, h_adj);
					*/

					// 문서에 맞게 계산 , 문서높이 - 좌표 - 패드높이
					y_adj = h - y_adj - h_adj;

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
					System.out.println("텍스트 박스 이다!!");
					
					float fontSize = element.getCharSize();
					String inputText = element.getAddText() == null ? "" : element.getAddText();

					// 문서에 맞게 계산, 문서높이 - 좌표 - 폰트사이즈
					y_adj = h - y_adj - fontSize;

					contentStream.beginText();
					
					// 폰트를 읽어온다.
					if ("본고딕".equals(fontType)){
						pdFont = pdfGenConfig.getFont1();
					}else if ("본명조".equals(fontType)){
						pdFont = pdfGenConfig.getFont2();
					}else{
						// 기본폰트로 설정..
						pdFont = pdfGenConfig.getFont1();
					}

					InputStream fontStream = new FileInputStream(pdFont);
					PDType0Font pdfType0Font = PDType0Font.load(document, fontStream);

					float titleWidth = pdfType0Font.getStringWidth(inputText)/1000 * fontSize;
					System.out.println("titleWidth :: " + titleWidth);

					// 개행?
					// while(titleWidth > w_adj){
					// 	String tmp = inputText.substring(0, w_adj);
					// }

					// contentStream.setFont( getFont(input.getFont()), input.getCharSize());
					contentStream.setFont(pdfType0Font, fontSize);
					contentStream.newLineAtOffset(x_adj, y_adj);
					//contentStream.moveTextPositionByAmount(x_adj, y_adj);
					contentStream.showText(inputText); // 값 셋팅

					contentStream.endText();
				} else if (element.isCheckbox()) {
					System.out.println("체크박스이다!!");

					System.out.println(" 입력한 값이다 :: " + element.getAddText());					
					
					// 문서에 맞게 계산 , 문서높이 - 좌표 - 패드높이
					y_adj = h - y_adj - h_adj;
					PDCheckBox checkBox = new PDCheckBox(acroForm);
					acroForm.getFields().add(checkBox);
					checkBox.setPartialName(element.getEleId());	// 이름을 각각 틀리게 해야한다.
					checkBox.setFieldFlags(4);

					List<PDAnnotationWidget> widgets = checkBox.getWidgets();
					try {
						for (PDAnnotationWidget pdAnnotationWidget : widgets)
						{
							// pdAnnotationWidget.setRectangle(new PDRectangle(50, 750, 18, 18));
							System.out.println("adj=" + x_adj + ", " + y_adj + ", " + w_adj + ", " + h_adj);

							pdAnnotationWidget.setRectangle(new PDRectangle(x_adj, y_adj, w_adj, h_adj));
							pdAnnotationWidget.setPage(page);
							page.getAnnotations().add(pdAnnotationWidget);

							pdAnnotationWidget.setAppearance(pdAppearanceDictionary);
						}

						checkBox.setReadOnly(true);	// 선택불가
						
						if(element.getAddText().equals("Y")){						
							checkBox.check();	// 체크
						}else{
							checkBox.unCheck();		// X 표시
						}
						
						// document.save(new File("D://", "CheckBox.pdf"));
						// document.close();
					} catch (Exception e) {
						//TODO: handle exception
					}

				} else if (element.isRadio()) {
					System.out.println("라디오 이다!!");
					System.out.println(" 입력한 값이다 :: " + element.getAddText());	

					// 좌표 문서에 맞게, 
					y_adj = h - y_adj - h_adj;
					
					acroForm.setNeedAppearances(true);
					acroForm.setXFA(null);
					// document.getDocumentCatalog().setAcroForm(acroForm);
			
					PDFont font = PDType1Font.HELVETICA;
			
					PDResources res = new PDResources();
					COSName fontName = res.add(font);
					acroForm.setDefaultResources(res);
					acroForm.setDefaultAppearance('/' + fontName.getName() + " 10 Tf 0 g");
			
					// PDPageContentStream contents = new PDPageContentStream(document, page);
			
					List<String> options = Arrays.asList("1","2");	// 라디오 구분 명칭으로 표시됨.
					PDRadioButton radioButton = new PDRadioButton(acroForm);
					radioButton.setPartialName("RadioButton");
					
					// radioButton.setExportValues(options);
					// radioButton.getCOSObject().setName(COSName.DV, options.get(1));
					String selectedValue = element.getAddText();

					List<PDAnnotationWidget> widgets = new ArrayList<>();
					for (int i = 0; i < options.size(); i++) {
			
						PDAppearanceCharacteristicsDictionary fieldAppearance = new PDAppearanceCharacteristicsDictionary(new COSDictionary());
						// fieldAppearance.setBorderColour(new PDColor(new float[]{0, 0, 0}, PDDeviceRGB.INSTANCE));	// 라디오 테두리
			
						PDAnnotationWidget widget = new PDAnnotationWidget();					
						if(i>0){
							x_adj = x_adj + w_adj - h_adj;
						}
						System.out.println("x : " + x_adj + " Y : " + y_adj  + " W : " + w_adj + " H : " +  h_adj);
						widget.setRectangle(new PDRectangle(x_adj, y_adj, h_adj, h_adj));						
						widget.setAppearanceCharacteristics(fieldAppearance);
						widget.setPage(page);
						widget.getCOSObject().setItem(COSName.PARENT, radioButton);
						widget.setAnnotationFlags(4);
						
						widget.setAppearanceState(options.get(i).equals(selectedValue) ? selectedValue: "Off");
						widget.setHighlightingMode("I");
			
						// added by Tilman on 13.1.2017, without it Adobe does not set the values properly
						PDAppearanceDictionary appearance = new PDAppearanceDictionary();
						COSDictionary dict = new COSDictionary();

						COSStream off = new COSStream();
						off.setItem(COSName.BBOX, new PDRectangle(0, 0, 16, 16));
						off.setItem(COSName.RESOURCES, res);
						OutputStream osOff = off.createOutputStream();
						osOff.write(("q 0 g /" + fontName.getName() + " 16 Tf BT (O) Tj ET Q").getBytes());
						osOff.write(("").getBytes());
						osOff.close();
						dict.setItem("Off", off);

						COSStream on = new COSStream();
						on.setItem(COSName.BBOX, new PDRectangle(0, 0, 16, 16));
						on.setItem(COSName.RESOURCES, res);
						OutputStream osOn = on.createOutputStream();
						osOn.write(("q 0 g BT /" + fontName.getName() + " 16 Tf (X) Tj ET Q").getBytes());
						osOn.write(("").getBytes());
						osOn.close();
						dict.setItem(options.get(i), on);


						// dict.setItem(COSName.getPDFName("Off"), new COSDictionary());
						// dict.setItem(COSName.getPDFName(options.get(i)), new COSDictionary());
						PDAppearanceEntry appearanceEntry = new PDAppearanceEntry(dict);
						appearance.setNormalAppearance(appearanceEntry);
						widget.setAppearance(appearance);

						widgets.add(widget);
						page.getAnnotations().add(widget);
			
			
						// contentStream.beginText();
						// contentStream.setFont(font, 10);
						// contentStream.newLineAtOffset(56, 811 - i * (21) + 4);
						// // contents.showText(options.get(i));	// 라디오 버튼 뒤에 명칭으로 표시, 이건 필요없을듯.
						// // contentStream.showText(options.get(i));	// 라디오 버튼 뒤에 명칭으로 표시, 이건 필요없을듯.
						// contentStream.endText();
					}
					radioButton.setReadOnly(true);	// 선택불가?
					radioButton.setWidgets(widgets);					
					radioButton.setValue(selectedValue);
					acroForm.getFields().add(radioButton);
			
					// contents.close();
					// try(FileOutputStream output = new FileOutputStream("Test.pdf")) {
					// 	// document.save(output);
					// 	document.save(new File("D://", "Radio.pdf"));
					// }
					// document.close();
				}
				contentStream.close();

				// 메모
				if(element.getEleId() == null) {
					element.setDoc(doc);
					elementRepository.save(element);
				}

				// 저장처리 
				elem = elementRepository.findById(element.getEleId()).get();


				System.out.println("elem == null ======================================");
				System.out.println(elem == null);
				System.out.println(elem.getInputType());
				System.out.println( elem.getAddText() );
		
				// db에서 해당 element id로 저장된 record를 찾을 수 없을 경우 종료
				if(elem == null)
					return null;

				// 이전에 해당 element에 입력된 값이 없다면 새로입력
				if(elem.getElementSign() == null)
					elem.setElementSign(new ElementSign());


				elem.getElementSign().setEleValue(element.getAddText());


				elem.getElementSign().setEleSignValue(imageBytes);

				
				// elementSignRepository.save(elem.getElementSign());	// 값 저장처리
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
			return new PdfResponse(400, "저장 중 오류발생");
		} finally{

		}	

		return new PdfResponse(200, "저장 완료");
	}

	/**
	 * 최종 계약서 저장 처리
	 */
	@Transactional
	@Override
	public PdfResponse signComplete(String docId, String signerId){
		
		Doc doc = docRepository.findById(docId).orElse(null);

		if (doc == null) {
			System.out.println("객체가 없다.");
			return new PdfResponse(404, "객체가 없다.");
		}


		// pdf 파일 생성 (tsa 추가?)
		String saveRoot = pdfGenConfig.getDocHome();	// D:/ktpdf/pdfgen/src/main/resources/storage
		String oriFilePath = doc.getPdfPath();			// 파일의 절대경로...
		//String savePdfPath = saveRoot + File.separator + doc.getDocId()+".pdf";	// 저장 전체경로		
		String savePdfPath = saveRoot + File.separator + doc.getDocId() + "_final.pdf";		// 저장 전체경로
		String savePdfName = doc.getFileName();		// 별필요없을듯. 우선 파일명을 그대로 저장
		
		// 파일을 조회한다.
		try{
			PDDocument document = PDDocument.load(new File(oriFilePath));
			Element elem;
			
			// TSA 삽입 시작
			// TSA 삽입 끝

			document.save(savePdfPath);
			document.close();

			// pdf 저장 후 DB값 입력
			doc.setPdfName(savePdfName);
			doc.setPdfPath(savePdfPath);
			doc.setPdfRegDt(new Date());			
			docRepository.save(doc);


			// 히스토리값을 저장한다.
			String pdfName = new Date()+".pdf";			
			DocHistory docHistory = new DocHistory();			
			docHistory.setPdfName(pdfName);
			docHistory.setPdfPath(savePdfPath);
			docHistory.setRegDt(new Date()); 			
			docHistory.setSignId(signerId);
			List<DocHistory> docHistoryList = new ArrayList<DocHistory>();
			docHistoryList.add(docHistory);
			doc.setHistory(docHistoryList);
			
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

	@Transactional
	@Override
	public void checkbox(){
		PDDocument document = new PDDocument();

		PDPage page = new PDPage();
		document.addPage(page);

		PDAcroForm acroForm = new PDAcroForm(document);
		document.getDocumentCatalog().setAcroForm(acroForm);


		COSDictionary normalAppearances = new COSDictionary();
		PDAppearanceDictionary pdAppearanceDictionary = new PDAppearanceDictionary();
		pdAppearanceDictionary.setNormalAppearance(new PDAppearanceEntry(normalAppearances));
		pdAppearanceDictionary.setDownAppearance(new PDAppearanceEntry(normalAppearances));

		// 체크 표시 설정
		PDAppearanceStream pdAppearanceStream = new PDAppearanceStream(document);
		pdAppearanceStream.setResources(new PDResources());
		try (PDPageContentStream pdPageContentStream = new PDPageContentStream(document, pdAppearanceStream))
		{
			pdPageContentStream.setFont(PDType1Font.ZAPF_DINGBATS, 14.5f);
			pdPageContentStream.beginText();
			pdPageContentStream.newLineAtOffset(3, 4);
			pdPageContentStream.showText("\u2714");
			pdPageContentStream.endText();
		}catch (Exception e) {
			//TODO: handle exception
		}
		pdAppearanceStream.setBBox(new PDRectangle(18, 18));
		normalAppearances.setItem("Yes", pdAppearanceStream);

		// x 표시 설정
		pdAppearanceStream = new PDAppearanceStream(document);
		pdAppearanceStream.setResources(new PDResources());
		try (PDPageContentStream pdPageContentStream = new PDPageContentStream(document, pdAppearanceStream))
		{
			pdPageContentStream.setFont(PDType1Font.ZAPF_DINGBATS, 14.5f);
			pdPageContentStream.beginText();
			pdPageContentStream.newLineAtOffset(3, 4);
			pdPageContentStream.showText("\u2718");		// x 표시... 바꿔야 함.
			pdPageContentStream.endText();
		} catch (Exception e) {
			//TODO: handle exception
		}
		pdAppearanceStream.setBBox(new PDRectangle(18, 18));
		normalAppearances.setItem("Off", pdAppearanceStream);

		PDCheckBox checkBox = new PDCheckBox(acroForm);
		acroForm.getFields().add(checkBox);
		checkBox.setPartialName("CheckBoxField");
		checkBox.setFieldFlags(4);

		List<PDAnnotationWidget> widgets = checkBox.getWidgets();
		try {
			for (PDAnnotationWidget pdAnnotationWidget : widgets)
			{
				pdAnnotationWidget.setRectangle(new PDRectangle(50, 750, 18, 18));
				pdAnnotationWidget.setPage(page);
				page.getAnnotations().add(pdAnnotationWidget);

				pdAnnotationWidget.setAppearance(pdAppearanceDictionary);
			}

			checkBox.setReadOnly(true);	// 선택불가
			checkBox.check();	// 체크
			// checkBox.unCheck();		// X 표시


			document.save(new File("D://", "CheckBox.pdf"));
			document.close();
		} catch (Exception e) {
			//TODO: handle exception
		}
		
	}

	@Transactional
	@Override
	public void radio(){
		try {
			
			String fileName = "D://storage//Test.pdf";
			String selectedValue = "b";
			String name = "Radio";
	
			PDDocument document = new PDDocument();
			PDPage page = new PDPage(PDRectangle.A4);
			document.addPage(page);
	
			PDAcroForm acroForm = new PDAcroForm(document);
			acroForm.setNeedAppearances(true);
			document.getDocumentCatalog().setAcroForm(acroForm);
	
			// PDFont font = PDTrueTypeFont.load(document, new FileInputStream("C:/Windows/Fonts/arial.ttf"), StandardEncoding.INSTANCE);
			PDFont font = PDType1Font.HELVETICA;
	
			PDResources res = new PDResources();
			COSName fontName = res.add(font);
			acroForm.setDefaultResources(res);
			acroForm.setDefaultAppearance('/' + fontName.getName() + " 0 Tf 0 g");
	
			PDPageContentStream contents = new PDPageContentStream(document, page);
	
			List<String> options = Arrays.asList("a", "b", "c");
			PDRadioButton radioButton = new PDRadioButton(acroForm);
			radioButton.setPartialName("RadioButton");
			List<PDAnnotationWidget> widgets = new ArrayList<>();
			for (int i = 0; i < options.size(); i++)
			{
				PDRadioButton subRadioButton = new PDRadioButton(acroForm);
				subRadioButton.setPartialName(name);
	
				PDAnnotationWidget widget = subRadioButton.getWidgets().get(0);
	
				PDAppearanceCharacteristicsDictionary fieldAppearance = new PDAppearanceCharacteristicsDictionary(new COSDictionary());
				// fieldAppearance.setBorderColour(new PDColor(new float[]{0, 0, 0}, PDDeviceRGB.INSTANCE));
	
				widget.setRectangle(new PDRectangle(30, 800 - i * (21), 16, 16));
				widget.setAppearanceCharacteristics(fieldAppearance);
				widget.setPage(page);
				widget.getCOSObject().setItem(COSName.PARENT, radioButton);
				widget.setAnnotationFlags(4);
	
				widget.setAppearanceState(options.get(i).equals(selectedValue) ? selectedValue: "Off");
				widget.setHighlightingMode("N");
	
				PDAppearanceDictionary ap = new PDAppearanceDictionary();
	
				COSDictionary aeDict = new COSDictionary();
	
				COSStream off = new COSStream();
				// off.setItem(COSName.BBOX, new PDRectangle(0, 0, 16, 16));
				// off.setItem(COSName.RESOURCES, res);
				// OutputStream osOff = off.createOutputStream();
				// // osOff.write(("q 0 g /" + fontName.getName() + " 16 Tf BT (O) Tj ET Q").getBytes());
				// // osOff.write(("q 0 g /" + fontName.getName()).getBytes());
				// osOff.write(("").getBytes());
				// osOff.close();
				aeDict.setItem("Off", off);
	
				COSStream on = new COSStream();
				// on.setItem(COSName.BBOX, new PDRectangle(0, 0, 16, 16));
				// on.setItem(COSName.RESOURCES, res);
				// OutputStream osOn = on.createOutputStream();
				// // osOn.write(("q 0 g BT /" + fontName.getName() + " 16 Tf (X) Tj ET Q").getBytes());
				// osOn.write(("").getBytes());
				// osOn.close();
				aeDict.setItem(options.get(i), on);
	
				PDAppearanceEntry ae = new PDAppearanceEntry(aeDict);
				ap.setNormalAppearance(ae);
				widget.setAppearance(ap);
	
				widgets.add(widget);
				page.getAnnotations().add(widget);
			}
			radioButton.setReadOnly(true);	// 선택불가?
			radioButton.setWidgets(widgets);
			// System.out.println(radioButton.getOnValues());
			radioButton.setValue(selectedValue);
			acroForm.getFields().add(radioButton);
	
			// System.out.println(radioButton.getValue());
	
			//acroForm.refreshAppearances(); // not implemented
			contents.close();
			document.save(new FileOutputStream(fileName));
	
			document.close();
			
			// PDDocument document = new PDDocument();
			// PDPage page = new PDPage(PDRectangle.A4);
	
			// document.addPage(page);
	
			// PDAcroForm acroForm = new PDAcroForm(document);
			// acroForm.setNeedAppearances(true);
			// acroForm.setXFA(null);
			// document.getDocumentCatalog().setAcroForm(acroForm);
	
			// PDFont font = PDType1Font.HELVETICA;
	
			// PDResources res = new PDResources();
			// COSName fontName = res.add(font);
			// acroForm.setDefaultResources(res);
			// acroForm.setDefaultAppearance('/' + fontName.getName() + " 10 Tf 0 g");
	
			// PDPageContentStream contents = new PDPageContentStream(document, page);
	
			// List<String> options = Arrays.asList("a", "b");
			// PDRadioButton radioButton = new PDRadioButton(acroForm);
			// radioButton.setPartialName("RadioButtonParent");
			// radioButton.setExportValues(options);
			// radioButton.getCOSObject().setName(COSName.DV, options.get(1));
			
			// String selectedValue = "a";

			// List<PDAnnotationWidget> widgets = new ArrayList<>();
			// for (int i = 0; i < options.size(); i++) {
	
			// 	PDAppearanceCharacteristicsDictionary fieldAppearance = new PDAppearanceCharacteristicsDictionary(new COSDictionary());
			// 	fieldAppearance.setBorderColour(new PDColor(new float[]{0, 0, 0}, PDDeviceRGB.INSTANCE));
	
			// 	PDAnnotationWidget widget = new PDAnnotationWidget();
			// 	// widget.setRectangle(new PDRectangle(30, 811 - i * (21), 16, 16));
			// 	widget.setRectangle(new PDRectangle(0, 811 - i * (21), 16, 16));
			// 	// widget.setRectangle(new PDRectangle(0 , 358 , 226 ,20));
				
			// 	widget.setAppearanceState(options.get(i).equals(selectedValue) ? selectedValue: "Off");
			// 	widget.setAppearanceCharacteristics(fieldAppearance);
	
			// 	widgets.add(widget);
			// 	page.getAnnotations().add(widget);
	
			// 	// added by Tilman on 13.1.2017, without it Adobe does not set the values properly
			// 	PDAppearanceDictionary appearance = new PDAppearanceDictionary();
			// 	COSDictionary dict = new COSDictionary();
				
				
			// 	COSStream off = new COSStream();
	        //     off.setItem(COSName.BBOX, new PDRectangle(0, 0, 16, 16));
	        //     off.setItem(COSName.RESOURCES, res);
	        //     OutputStream osOff = off.createOutputStream();
	        //     osOff.write(("q 0 g /" + fontName.getName() + " 16 Tf BT (O) Tj ET Q").getBytes());
	        //     osOff.write(("").getBytes());
	        //     osOff.close();
	        //     dict.setItem("Off", off);
	 
	        //     COSStream on = new COSStream();
	        //     on.setItem(COSName.BBOX, new PDRectangle(0, 0, 16, 16));
	        //     on.setItem(COSName.RESOURCES, res);
	        //     OutputStream osOn = on.createOutputStream();
	        //     osOn.write(("q 0 g BT /" + fontName.getName() + " 16 Tf (X) Tj ET Q").getBytes());
	        //     osOn.write(("").getBytes());
	        //     osOn.close();
	        //     dict.setItem(options.get(i), on);

			// 	// dict.setItem(COSName.getPDFName("Off"), new COSDictionary());
			// 	// dict.setItem(COSName.getPDFName(options.get(i)), new COSDictionary());
			// 	PDAppearanceEntry appearanceEntry = new PDAppearanceEntry(dict);
			// 	appearance.setNormalAppearance(appearanceEntry);
			// 	widget.setAppearance(appearance);
	
	
			// 	contents.beginText();
			// 	contents.setFont(font, 10);
			// 	contents.newLineAtOffset(56, 811 - i * (21) + 4);
			// 	contents.showText(options.get(i));
			// 	contents.endText();				
			// }
			// radioButton.setReadOnly(true);	// 선택불가?			
			// // radioButton.setDefaultValue("a");			
			// radioButton.setWidgets(widgets);
			// radioButton.setValue("b");
			// acroForm.getFields().add(radioButton);
	
			// contents.close();
			// try(FileOutputStream output = new FileOutputStream("Test.pdf")) {
			// 	// document.save(output);
			// 	document.save(new File("D://storage//", "Radio.pdf"));
			// }
			// document.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}