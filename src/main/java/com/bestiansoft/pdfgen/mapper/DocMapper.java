package com.bestiansoft.pdfgen.mapper;

import java.util.List;

import com.bestiansoft.pdfgen.model.Doc;
import com.bestiansoft.pdfgen.model.DocHistory;
import com.bestiansoft.pdfgen.model.Element;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DocMapper {
    Doc getDoc(String docId);
    void insertDoc(Doc doc);
    void updateDoc(Doc doc);
}