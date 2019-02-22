package com.bestiansoft.pdfgen.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class Response {
	protected int 		code;
	protected String		message;
	
	public Response() {}
	public Response(int code, String message) {
		this.code=code;
		this.message=message;
	}
	
	public final static int OK_CODE=200;
	public final static int CODE_5001=5001;
	
	@JsonIgnore
	public static Response ok() {
		return new Response(OK_CODE, "RESPONSE_OK");
	}
	@JsonIgnore
	public static Response err5001() {
		return new Response(CODE_5001, "필수 파라미터 오류");
	}	
	
	@JsonIgnore
	public static boolean isOk(int code) {
		return code==OK_CODE;
	}
	@JsonIgnore
	protected boolean isOk() {
		return isOk(code);
	}
	
}
