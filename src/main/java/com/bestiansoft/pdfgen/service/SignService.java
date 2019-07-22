package com.bestiansoft.pdfgen.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.bestiansoft.pdfgen.model.Image;
import com.bestiansoft.pdfgen.model.Sign;
import com.bestiansoft.pdfgen.model.Test;
import com.bestiansoft.pdfgen.repo.SignRepository;
import com.bestiansoft.pdfgen.repo.TestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

@Service
public class SignService {

    @Autowired
    SignRepository repository;

    /**
     * 목록 조회
     */
    public List<Image> getImages() throws SQLException {

        System.out.println("TestService start !!");
        
        try(
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "1234");
            PreparedStatement pstmt = conn.prepareStatement("select SIGN_ID, FILE_STORE from ecs_sign_mgt");
            ResultSet rs = pstmt.executeQuery();
        ) {
            List<Image> ret = new ArrayList<>();
            while(rs.next()) {
                int imageId = rs.getInt(1);
                String enc = Base64Utils.encodeToString(rs.getBytes(2));
                
                ret.add(new Image(imageId, "", "", "", enc));
            }
            return ret;
        } 
    }


    /**
     * 싸인 및 도장업로드 이미지를 저장처리
     */
    public int uploadImage(String img) throws SQLException {
        String partSeparator = ",";
        byte[] decodedByte = null;
        if (img.contains(partSeparator)) {
            String encodedImg = img.split(partSeparator)[1];
            decodedByte = Base64.getDecoder().decode(encodedImg);
        }

        int cnt =0;

        Sign sign = new Sign();
        sign.setUser_id("testUser");
        sign.setFile_store(decodedByte);
        repository.save(sign);

        return 1;
    }

    /**
     * 삭제 처리
     * @param imgId
     * @return
     * @throws SQLException
     */
    public int deleteImage(String imgId) throws SQLException {
        int cnt = 0;
        try {
            Long delId = Long.parseLong(imgId);
            repository.deleteById(delId);
        } catch (Exception e) {
            //TODO: handle exception
        }

        return cnt;
    }
}