package com.bestiansoft.pdfgen.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

// @Entity
public class Signer {

    public static List<Signer> signers = new ArrayList<>();
    static {
        Signer signer1 = new Signer();
        signer1.setEmail("kys@gmail.com");
        signer1.setSignerNm("kys");
        signer1.setSignerNo("signer1");
        signer1.setSignerType("owner");

        Signer signer2 = new Signer();
        signer2.setEmail("usang@gmail.com");
        signer2.setSignerNm("usang");
        signer2.setSignerNo("signer2");
        signer2.setSignerType("signer");

        signers.add(signer1);
        signers.add(signer2);
    }

    public static Signer getSigner(String signerNo) {
        for(Signer s : signers) {
            if(s.getSignerNo().equals(signerNo))
                return s;
        }
        return null;
    }

    @Id
    private String signerNo;
    private String signerNm;
    private String signerType;
    private String email;
    
    @JsonBackReference
    @ManyToOne
    private Doc doc;

    /**
     * @return the signerNo
     */
    public String getSignerNo() {
        return signerNo;
    }

    /**
     * @param signerNo the signerNo to set
     */
    public void setSignerNo(String signerNo) {
        this.signerNo = signerNo;
    }

    /**
     * @return the signerNm
     */
    public String getSignerNm() {
        return signerNm;
    }

    /**
     * @param signerNm the signerNm to set
     */
    public void setSignerNm(String signerNm) {
        this.signerNm = signerNm;
    }

    /**
     * @return the signerType
     */
    public String getSignerType() {
        return signerType;
    }

    /**
     * @param signerType the signerType to set
     */
    public void setSignerType(String signerType) {
        this.signerType = signerType;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the doc
     */
    public Doc getDoc() {
        return doc;
    }

    /**
     * @param doc the doc to set
     */
    public void setDoc(Doc doc) {
        this.doc = doc;
    }
    
    
}