package com.bestiansoft.pdfgen.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class FileUtil {

 //파일을 복사하는 메소드
 public static boolean fileCopy(String inFileName, String outFileName) throws Exception{
  boolean result = false;
  
  try {
      System.out.println("fileCopy start : " + new Date());
      System.out.println("inFileName : " + inFileName);
      System.out.println("outFileName : " + outFileName);

      FileInputStream fis = new FileInputStream(inFileName);
      FileOutputStream fos = new FileOutputStream(outFileName);
     
   //   int data = 0;
   //   while((data=fis.read())!=-1) {
   //    fos.write(data);
   //   }

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
  
 //파일을 삭제하는 메소드
 public static void fileDelete(String deleteFileName) {    

   System.out.println("fileDelete 1 : " + new Date());

    File I = new File(deleteFileName);
    I.delete();    
    System.out.println(deleteFileName + " : fileDelete complate : " + new Date());
   }
}