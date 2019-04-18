package com.bestiansoft.pdfgen.vo;

import java.util.List;

import com.bestiansoft.pdfgen.model.Element;

import lombok.Data;

@Data
public class ElementsVo {
    
    private String userHash;

    private List<Element> inputs;        
}