package com.bestiansoft.pdfgen.config;

import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class PdfGenConfig {
	@Value("${pdfgen.doc.home}")
	private String docHome;

	@Value("${pdfgen.doc.font1}")
	private String font1;

	@Value("${pdfgen.doc.font2}")
	private String font2;

	@Value("${pdfgen.imgPath}")
	private String imgPath;

	@Value("${pdfgen.context.path}")
	private String contextPath;

	@Value("${pdfgen.stampType}")
	private String stampType;

	private String getPropertieName(String name){
		String value = "";
		
		Properties properties = new Properties();
		String propFileName = "application.properties";  // properties파일명
		
		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			properties.load(inputStream);

			for(String key : properties.stringPropertyNames()) {
				if(name.equals(key)){
					value = properties.getProperty(key);
					System.out.println(key + " => " + value);
				}
			}	
		} catch (Exception e) {
			//TODO: handle exception
		}	

		return value;
	}
}