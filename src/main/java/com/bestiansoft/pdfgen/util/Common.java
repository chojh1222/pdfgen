package com.bestiansoft.pdfgen.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import com.bestiansoft.pdfgen.config.PdfGenConfig;

import org.apache.fontbox.ttf.OTFParser;
import org.apache.fontbox.ttf.TTFParser;
import org.apache.fontbox.ttf.TrueTypeCollection;
import org.apache.fontbox.ttf.TrueTypeFont;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;



@Component
public class Common {

    @Autowired
    PdfGenConfig pdfGenConfig;
    
    @Value("${server.host}")    
      private String serverHost;

    @Value("${server.port}")    
    private String serverPort;

    @Autowired 
    private ResourceLoader resourceLoader;

  /**
   * doc id를 받아 url 형식으로 리턴
   * http://localhost:8888/ecs/docId
   */
  public String getFileUrl(String docId){
      String fileUrl = ""; 	

      if(!docId.equals("")){
          fileUrl = serverHost + ":" + serverPort + pdfGenConfig.getContextPath() + "/" + docId;
      }
      
      return fileUrl;    
  }
    
  /**
   * 파일을 복사
   * inFileName : 원본
   * outFileName : 복사본
   */
  public static boolean fileCopy(String inFileName, String outFileName) throws Exception{
    boolean result = false;
  
    try {
        System.out.println("fileCopy start : " + new Date());
        System.out.println("inFileName : " + inFileName);
        System.out.println("outFileName : " + outFileName);

        FileInputStream fis = new FileInputStream(inFileName);
        FileOutputStream fos = new FileOutputStream(outFileName);
   
        FileChannel fcin =  fis.getChannel();
        FileChannel fcout = fos.getChannel();
        
        long size = fcin.size();
        fcin.transferTo(0, size, fcout);

        System.out.println("fileCopy complate : " + new Date());

        fis.close();
        fos.close();

        result = true;
     
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      // throw e;
      throw e;
    }

    return result;
  }
  
  /**
   * 파일을 삭제
   */
  public static void fileDelete(String deleteFileName) {

    System.out.println("fileDelete 1 : " + new Date());

    File I = new File(deleteFileName);
    I.delete();    
    
    System.out.println(deleteFileName + " : fileDelete complate : " + new Date());
  }


  public String testMD5(String str){

    String MD5 = "";     
    try{    
        MessageDigest md = MessageDigest.getInstance("MD5");     
        md.update(str.getBytes());     
        byte byteData[] = md.digest();    
        StringBuffer sb = new StringBuffer();     
        for(int i = 0 ; i < byteData.length ; i++){    
            sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));    
        }    
        MD5 = sb.toString();    
    }catch(NoSuchAlgorithmException e){    
        e.printStackTrace();     
        MD5 = null;     
    }    
    return MD5;    
  }

  public String testSHA256(String str){
      String SHA = ""; 	
      try{    
          MessageDigest sh = MessageDigest.getInstance("SHA-256");     
          sh.update(str.getBytes());     
          byte byteData[] = sh.digest();    
          StringBuffer sb = new StringBuffer();     
          for(int i = 0 ; i < byteData.length ; i++){    
              sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));    
          }    
          SHA = sb.toString();
      }catch(NoSuchAlgorithmException e){    
          e.printStackTrace();     
          SHA = null;     
      }    
      return SHA;    
  }

  /**
   * 라디오 버튼 선택 이미지
   */
  public String getOnNString(){
    String onNString = "0 G\n"
					+ "q\n"
					+ "  1 0 0 1 8 8 cm\n"
					+ "  3.5 0 m\n"
					+ "  3.5 1.9331 1.9331 3.5 0 3.5 c\n"
					+ "  -1.9331 3.5 -3.5 1.9331 -3.5 0 c\n"
					+ "  -3.5 -1.9331 -1.9331 -3.5 0 -3.5 c\n"
					+ "  1.9331 -3.5 3.5 -1.9331 3.5 0 c\n"
					+ "  f\n"
          + "Q";
    return onNString;
  }

  /**
   * 현재 ttf 타입에 대해서 정상처리됨
   *  otf 폰트는 폰트의 사이즈만큼 pdf파일 사이즈가 증가되는 문제
   *  ttc 폰트는 안됨.
   */
  public PDType0Font getPDType0Font(PDDocument document, String fontType) throws Exception{
    
    String pdFont = "";

    if ("Nanum Gothic".equals(fontType)){
      pdFont = resourceLoader.getResource("classpath:"+pdfGenConfig.getFont1()).getURI().getPath();
    }else if ("Nanum Myeongjo".equals(fontType)){
      pdFont = resourceLoader.getResource("classpath:"+pdfGenConfig.getFont2()).getURI().getPath();
    }else{
      pdFont = resourceLoader.getResource("classpath:"+pdfGenConfig.getFont1()).getURI().getPath();
    }

    // pdFont = Common.class.getResource("").getPath()+pdfGenConfig.getFont1();
    // pdFont = pdfGenConfig.getFont1();

    System.out.println("pdFont :: " + pdFont);
    
    TTFParser ttfParser = new TTFParser(false, false);					
    TrueTypeFont ttf = null;
    PDType0Font pDType0Font = null;

    PDFont otf = null;
    try
    {
       // 확장자 조회
      String ext = getExt(pdFont);
      System.out.println("ext :: " + ext);

      if(ext.equals("otf")){
        
        System.out.println("otf 이다");

        // ttf = ttfParser.parse(pdFont);
        OTFParser oTFParser= new OTFParser();
        ttf = oTFParser.parse(pdFont);
        
        pDType0Font = PDType0Font.load(document, ttf, false);

      }else if(ext.equals("ttc")){

        // TTC 작업중...
        System.out.println("ttc 이다");
      
        File fontFile = new File("D:/ktpdf/pdfgen/target/classes/font/GULIM.TTC");
        // File fontFile = new File("C:/Windows/Fonts/msgothic.ttc");

        pDType0Font = PDType0Font.load(document, new TrueTypeCollection(fontFile).getFontByName("굴림"), true);
       
        
      }else if(ext.equals("ttf")){
        
        System.out.println("ttf 이다");

        // 되는것 
        InputStream fontStream = new FileInputStream(pdFont);
        pDType0Font = PDType0Font.load(document, fontStream, false);	
        // 되는것 

        // InputStream fontStream = new FileInputStream(pdFont);
        // PDFont font = PDTrueTypeFont.loadTTF(document, fontStream);

      }
      
    }
    catch (NullPointerException e) // TTF parser is buggy
    {
      System.out.println("Could not load font file: " + pdFont + e);
      throw e;
    }
    catch (IOException e)
    {
      System.out.println("Could not load font file: " + pdFont + e);
      throw e;
    }

    return pDType0Font;
  }


  /**
   * 확장자 구하기
   */
  public String getExt(String strFileName){
    System.out.println("strFileName : " + strFileName);
    String ext = "";
    if(strFileName == null || strFileName.equals("")){
      
    }else{
      int pos = strFileName.lastIndexOf(".");
      if(pos != -1){
        ext = strFileName.substring( pos + 1 ).toLowerCase();
      }
    }

    return ext;
  }

}