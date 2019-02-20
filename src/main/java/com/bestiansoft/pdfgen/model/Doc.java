package com.bestiansoft.pdfgen.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="ECS_DOC_MGT")
public class Doc {
    @Id
    private String docId;
    
    private String userId;
    private String docName;
    private String fileName;

    @Column(name="FILE_PATH")
    private String doc;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt = new Date();

    @JsonManagedReference
    @OneToMany(mappedBy = "doc", fetch = FetchType.EAGER)
    private List<Signer> signers;

    /**
     * @return the docId
     */
    public String getDocId() {
        return docId;
    }

    /**
     * @param docId the docId to set
     */
    public void setDocId(String docId) {
        this.docId = docId;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the docName
     */
    public String getDocName() {
        return docName;
    }

    /**
     * @param docName the docName to set
     */
    public void setDocName(String docName) {
        this.docName = docName;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the doc
     */
    public String getDoc() {
        return doc;
    }

    /**
     * @param doc the doc to set
     */
    public void setDoc(String doc) {
        this.doc = doc;
    }

    /**
     * @return the regDt
     */
    public Date getRegDt() {
        return regDt;
    }

    /**
     * @param regDt the regDt to set
     */
    public void setRegDt(Date regDt) {
        this.regDt = regDt;
    }

    /**
     * @return the signers
     */
    public List<Signer> getSigners() {
        return signers;
    }

    /**
     * @param signers the signers to set
     */
    public void setSigners(List<Signer> signers) {
        this.signers = signers;
    }

    
}