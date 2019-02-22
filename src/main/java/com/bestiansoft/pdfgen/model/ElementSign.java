package com.bestiansoft.pdfgen.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Data
@Table(name="ECS_ELE_SIGN_MGT")
public class ElementSign {
    
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    
    @JsonBackReference
    @OneToOne
    @JoinColumn(name="ELE_ID")
    private Element element;

    private String eleValue;
    private Byte[] eleSignValue;
    private String signYn;

    @Temporal(TemporalType.TIMESTAMP)    
    private Date regDt = new Date();    
}