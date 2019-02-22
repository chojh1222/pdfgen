package com.bestiansoft.pdfgen.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name="ECS_ELE_SIGN_MGT")
public class ElementSign {
    
    @JoinColumn(name="ELE_ID")
    private String eleId;

    @Column(name="DOC_ID")
    private String docId;

    private String eleValue;
    private Long eleSignValue;
    private String signYn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="REG_DT")
    private Date regDt = new Date();

    @JsonBackReference
    @ManyToOne
    private Doc doc;
}