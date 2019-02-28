package com.bestiansoft.pdfgen.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Data
@Table(name="ECS_ELE_SIGN_MGT")
public class ElementSign {
    
    @Id
    private String id;
    
    @JsonBackReference
    @OneToOne
    @MapsId
    @JoinColumn(name="ELE_ID")
    private Element element;

    private String eleValue;

    @Lob
    private byte[] eleSignValue;
    
    @Temporal(TemporalType.TIMESTAMP)    
    private Date regDt = new Date();  


    
}