package com.bestiansoft.pdfgen.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="ECS_ELE_MGT")
public class Element {
    @Id
    private String eleId;

    private String docId;
    private String signId;
    private String eleType;
    private String elePosX;
    private String elePosY;
    private String eleHigth;
    private String eleWidth;
    private String eleFontName;
    private String eleFontSize;
    private String eleOrd;

    // 주석
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt = new Date();

    /**
     * @return the eleId
     */
    public String getEleId() {
        return eleId;
    }

    /**
     * @param eleId the eleId to set
     */
    public void setEleId(String eleId) {
        this.eleId = eleId;
    }

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
     * @return the signId
     */
    public String getSignId() {
        return signId;
    }

    /**
     * @param signId the signId to set
     */
    public void setSignId(String signId) {
        this.signId = signId;
    }

    /**
     * @return the eleType
     */
    public String getEleType() {
        return eleType;
    }

    /**
     * @param eleType the eleType to set
     */
    public void setEleType(String eleType) {
        this.eleType = eleType;
    }

    /**
     * @return the elePosX
     */
    public String getElePosX() {
        return elePosX;
    }

    /**
     * @param elePosX the elePosX to set
     */
    public void setElePosX(String elePosX) {
        this.elePosX = elePosX;
    }

    /**
     * @return the elePosY
     */
    public String getElePosY() {
        return elePosY;
    }

    /**
     * @param elePosY the elePosY to set
     */
    public void setElePosY(String elePosY) {
        this.elePosY = elePosY;
    }

    /**
     * @return the eleHigth
     */
    public String getEleHigth() {
        return eleHigth;
    }

    /**
     * @param eleHigth the eleHigth to set
     */
    public void setEleHigth(String eleHigth) {
        this.eleHigth = eleHigth;
    }

    /**
     * @return the eleWidth
     */
    public String getEleWidth() {
        return eleWidth;
    }

    /**
     * @param eleWidth the eleWidth to set
     */
    public void setEleWidth(String eleWidth) {
        this.eleWidth = eleWidth;
    }

    /**
     * @return the eleFontName
     */
    public String getEleFontName() {
        return eleFontName;
    }

    /**
     * @param eleFontName the eleFontName to set
     */
    public void setEleFontName(String eleFontName) {
        this.eleFontName = eleFontName;
    }

    /**
     * @return the eleFontSize
     */
    public String getEleFontSize() {
        return eleFontSize;
    }

    /**
     * @param eleFontSize the eleFontSize to set
     */
    public void setEleFontSize(String eleFontSize) {
        this.eleFontSize = eleFontSize;
    }

    /**
     * @return the eleOrd
     */
    public String getEleOrd() {
        return eleOrd;
    }

    /**
     * @param eleOrd the eleOrd to set
     */
    public void setEleOrd(String eleOrd) {
        this.eleOrd = eleOrd;
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
}