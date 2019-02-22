package com.bestiansoft.pdfgen.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class PdfGenConfig {
	@Value("${pdfgen.doc.home}")
	private String docHome;
}