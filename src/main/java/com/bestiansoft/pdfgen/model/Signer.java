package com.bestiansoft.pdfgen.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Signer {
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