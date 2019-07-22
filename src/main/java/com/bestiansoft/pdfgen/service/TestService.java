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
import com.bestiansoft.pdfgen.model.Test;
import com.bestiansoft.pdfgen.repo.TestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

@Service
public class TestService {

    @Autowired
    TestRepository repository;

    public String getTest() throws SQLException {
        try(
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "1234");
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
        int cnt =0;


        try(
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "1234");
            PreparedStatement pstmt = conn.prepareStatement("insert into ecs_sign_mgt (FILE_STORE) values (?)");
            ) {

            
            pstmt.setBytes(1, img);
            cnt = pstmt.executeUpdate();
            
            System.out.println("cnt :: " + cnt);
            
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println("error : " + e.toString());
        }

        return cnt;
    }

    public int uploadImage(String img) throws SQLException {
        String partSeparator = ",";
        byte[] decodedByte = null;
        if (img.contains(partSeparator)) {
            String encodedImg = img.split(partSeparator)[1];
            decodedByte = Base64.getDecoder().decode(encodedImg);
        }

        // System.out.println(img);
        // System.out.println("=============================================");
        // System.out.println("=============================================");
        // System.out.println("=============================================");
        // System.out.println("=============================================");
        // System.out.println("=============================================");

        int cnt =0;

        String sql = "insert into ecs_sign_mgt (sign_id, user_id, sign_type, file_name, FILE_STORE) values ("
            +" (select COALESCE(max(sign_id)+1, 1) AS sign_id from ecs_sign_mgt),? ,? ,? ,? )";

        try(
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "1234");
            //PreparedStatement pstmt = conn.prepareStatement("insert into ecs_sign_mgt (FILE_STORE) values (?)");
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ) {

            
            pstmt.setString(1, "testUser"); //user_id
            pstmt.setString(2, "sign"); // sign_type
            pstmt.setString(3, "testFile"); // file_name
            pstmt.setBytes(4, decodedByte); // FILE_STORE
            cnt = pstmt.executeUpdate();
            
            System.out.println("cnt :: " + cnt);
            
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println("error : " + e.toString());
        }

        return cnt;

        // Test test = new Test();
        // test.setImg(decodedByte);
        // repository.save(test);

        //System.out.println( Base64Utils.encodeToString(decodedByte) );

        // return 1;

        // return uploadImage(decodedByte);
    }

    public int deleteImage(String imgId) throws SQLException {
        try(
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "1234");
            PreparedStatement pstmt = conn.prepareStatement("delete from ecs_sign_mgt where sign_id = ?");
            ) {
            // pstmt.setString(1, imgId);
            pstmt.setInt(1, Integer.parseInt(imgId));
            int cnt = pstmt.executeUpdate();
            return cnt;
        }
    }

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

}