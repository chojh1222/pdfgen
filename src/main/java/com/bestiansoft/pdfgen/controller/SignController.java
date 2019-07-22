package com.bestiansoft.pdfgen.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.bestiansoft.pdfgen.model.Image;
import com.bestiansoft.pdfgen.service.SignService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins="*")
@RestController
public class SignController {

    @Autowired
    // TestService testService;
    SignService signService;

    @RequestMapping(value = "/test", method = { RequestMethod.POST })
    public String req(@RequestBody Image image) throws SQLException {
        System.out.println("=====================================");
        System.out.println("=====================================");
        System.out.println("=====================================");
        System.out.println(image.getArr().get(0));
        // System.out.println(signId);
        return "999";
    }

    @RequestMapping(value = "/upload", method = { RequestMethod.POST })
    public String upload(MultipartFile file) throws SQLException, IOException {
        int res = signService.uploadImage(file.getBytes());
        return String.valueOf(res);
    }
    
    @RequestMapping(value = "/images", method = { RequestMethod.GET })
    public List<Image> getImages() throws SQLException, IOException {
        System.out.println("images start ");
        List<Image> res = signService.getImages();

        System.out.println("res : " + res.size());
        return res;
    }

    @RequestMapping(value = "/uploadsign", method = { RequestMethod.POST })
    public String uploadsign(String data) throws SQLException, IOException {
        System.out.println("upload sign!");        
        signService.uploadImage(data);
        return "done";
    }

    @RequestMapping(value = "/delsign", method = { RequestMethod.POST })
    public String delsign(String imgId) throws SQLException, IOException {
        System.out.println("===========================================");
        System.out.println("===========================================");
        System.out.println("del sign!!!!");
        System.out.println("imgId = " + imgId);
        signService.deleteImage(imgId);
        return "done";
    }
}
