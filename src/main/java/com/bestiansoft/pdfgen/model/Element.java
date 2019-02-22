package com.bestiansoft.pdfgen.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="ECS_ELE_MGT")
public class Element {
    @Id
    private String eleId;

    @Column(name="DOC_ID")
    private String docId;

    @Column(name="SIGN_ID")
    private String SignerNo;

    @Column(name="ELE_TYPE")
    private String inputType;

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

    @Column(name="ELE_ORD")
    private String eleOrd;

    @Column(name="PAGE")
    private Integer page;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="REG_DT")
    private Date regDt = new Date();

    @JsonBackReference
    @ManyToOne
    private Doc doc;
            
    @JsonIgnore
	public boolean isSign() {
		return "sign".equals(inputType);
	}
	@JsonIgnore
	public boolean isText() {
		return "text".equals(inputType);
	}    

}