package com.bestiansoft.pdfgen.config;

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
}