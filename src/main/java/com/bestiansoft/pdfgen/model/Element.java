package com.bestiansoft.pdfgen.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="ECS_ELE_MGT")
public class Element {
    @Id @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String eleId;

    @JsonBackReference
    @ManyToOne
    private Doc doc;

    @Column(name="SIGN_ID")
    private String signerNo;

    @Column(name="ELE_TYPE")
    private String inputType;

    private Integer page;

    @Column(name="ELE_POS_X")
    private Float x;

    @Column(name="ELE_POS_Y")
    private Float y;

    @Column(name="ELE_HIGHT")
    private Float h;

    @Column(name="ELE_WIDTH")
    private Float w;

    @Column(name="ELE_FONT_NAME")
    private String font;

    @Column(name="ELE_FONT_SIZE")
    private Integer charSize;

    private Integer eleOrd;

    @OneToMany(mappedBy = "elem", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SignerInput> signerInput; 

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
     * @return the inputType
     */
    public String getInputType() {
        return inputType;
    }

    /**
     * @param inputType the inputType to set
     */
    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    /**
     * @return the page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * @param page the page to set
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * @return the x
     */
    public Float getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(Float x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public Float getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(Float y) {
        this.y = y;
    }

    /**
     * @return the h
     */
    public Float getH() {
        return h;
    }

    /**
     * @param h the h to set
     */
    public void setH(Float h) {
        this.h = h;
    }

    /**
     * @return the w
     */
    public Float getW() {
        return w;
    }

    /**
     * @param w the w to set
     */
    public void setW(Float w) {
        this.w = w;
    }

    /**
     * @return the eleOrd
     */
    public Integer getEleOrd() {
        return eleOrd;
    }

    /**
     * @param eleOrd the eleOrd to set
     */
    public void setEleOrd(Integer eleOrd) {
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

    /**
     * @param font the font to set
     */
    public void setFont(String font) {
        this.font = font;
    }

    /**
     * @return the charSize
     */
    public Integer getCharSize() {
        return charSize;
    }

    /**
     * @param charSize the charSize to set
     */
    public void setCharSize(Integer charSize) {
        this.charSize = charSize;
    }

    /**
     * @return the font
     */
    public String getFont() {
        return font;
    }


}