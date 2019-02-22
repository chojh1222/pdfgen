package com.bestiansoft.pdfgen.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonInclude(Include.NON_NULL)
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=false)
@Data
public class PdfResponse extends Response {
	private String fnPdf;
	
	@JsonIgnore
	public PdfResponse(int code, String message) {
		super(code, message);
	}
	@JsonIgnore
	public PdfResponse(String fnPdf) {
		this(OK_CODE, "RESPONSE_OK");
		this.fnPdf = fnPdf;
	}
}
