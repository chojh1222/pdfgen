package com.bestiansoft.pdfgen.service;

import com.bestiansoft.pdfgen.model.Doc;
import com.bestiansoft.pdfgen.repo.DocRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DocService {

    @Autowired
    DocRepository docRepository;

    @Transactional
    public Doc getDoc(String docId) {
        Doc doc = docRepository.findById(docId).orElse(null);
        return doc;
    }
}