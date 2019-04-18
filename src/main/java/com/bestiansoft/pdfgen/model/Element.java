package com.bestiansoft.pdfgen.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Data
@Table(name="ECS_ELE_MGT")
public class Element {
    @Id @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String eleId;

    @Column(name="SIGN_ID")
    private String signerNo;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="DOC_ID")
    private Doc doc;

    @Column(name="ELE_TYPE")
    private String inputType;

    @Column(name="ELE_POS_X")
    private Float x;

    @Column(name="ELE_POS_Y")
    private Float y;

    @Column(name="ELE_HEIGHT")
    private Float h;

    @Column(name="ELE_WIDTH")
    private Float w;

    @Column(name="ELE_FONT_NAME")
    private String font;

    @Column(name="ELE_FONT_SIZE")
    private Integer charSize;

    @Column(name="ELE_ORD")
    private Integer eleOrd;

    @Column(name="PAGE")
    private Integer page;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="REG_DT")
    private Date regDt = new Date();

    @Column(name="GBN_CD")
    private String gbnCd;

    // 사용자 입력값
    @Transient
    private String addText;

    // 사용자 입력사인
    @Transient
    private String signUrl;
            
    @JsonIgnore
	public boolean isSign() {
		return "sign".equals(inputType);
	}
	@JsonIgnore
	public boolean isText() {
		return "text".equals(inputType);
    }
    @JsonIgnore
	public boolean isCheckbox() {
		return "checkbox".equals(inputType);
    }
    @JsonIgnore
	public boolean isRadio() {
		return "radio".equals(inputType);
    }
    
    @JsonManagedReference
    @OneToOne(mappedBy="element", cascade=CascadeType.ALL)
    private ElementSign elementSign;


    /**
     * @param elements the elements to set
     */
    public void setElementSign(ElementSign elementSign) {
        this.elementSign = elementSign;
        if(elementSign != null)
            elementSign.setElement(this);
    }


}


    


