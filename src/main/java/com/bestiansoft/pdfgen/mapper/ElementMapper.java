package com.bestiansoft.pdfgen.mapper;

import java.util.List;

import com.bestiansoft.pdfgen.model.Doc;
import com.bestiansoft.pdfgen.model.Element;
import com.bestiansoft.pdfgen.model.ElementSign;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ElementMapper {
    List<Element> findByDocAndSignerNo(Doc doc, String signerNo);
    List<Element> findByDocAndInputType(Doc doc, String inputType);
    List<Element> findByDoc(Doc doc);
    void insertElement(Element element);
    void updateElement(Element element);
    void insertElementSign(ElementSign elementSign);
}