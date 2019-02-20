package com.bestiansoft.pdfgen;

import com.bestiansoft.pdfgen.repo.DocRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class JpaRunner implements ApplicationRunner {

    @Autowired
    DocRepository docRepository;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {

    }

}