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

import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

@Service
public class TestService {
    public String getTest() throws SQLException {
        try(
            Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/test", "root", "1234");
            PreparedStatement pstmt = conn.prepareStatement("SELECT 'a' as CNT FROM DUAL");
            ResultSet rs = pstmt.executeQuery();
        ) {
            if(rs.next()) {
                return rs.getString("CNT");
            } else {
                return null;
            }
        } 
    }

    public int uploadImage(byte[] img) throws SQLException {
        try(
            Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/test", "root", "1234");
            PreparedStatement pstmt = conn.prepareStatement("insert into ecs_sign_mgt (FILE_STORE) values (?)");
            ) {
            pstmt.setBytes(1, img);
            int cnt = pstmt.executeUpdate();
            return cnt;
        }
    }

    public int uploadImage(String img) throws SQLException {
        String partSeparator = ",";
        byte[] decodedByte = null;
        if (img.contains(partSeparator)) {
            String encodedImg = img.split(partSeparator)[1];
            decodedByte = Base64.getDecoder().decode(encodedImg);
        }
        return uploadImage(decodedByte);
    }

    public int deleteImage(String imgId) throws SQLException {
        try(
            Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/test", "root", "1234");
            PreparedStatement pstmt = conn.prepareStatement("delete from ecs_sign_mgt where sign_id = ?");
            ) {
            pstmt.setString(1, imgId);
            int cnt = pstmt.executeUpdate();
            return cnt;
        }
    }

    public List<Image> getImages() throws SQLException {
        try(
            Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/test", "root", "1234");
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

}