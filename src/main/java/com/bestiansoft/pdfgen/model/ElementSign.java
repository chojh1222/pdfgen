package com.bestiansoft.pdfgen.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
<<<<<<< HEAD
=======
import javax.persistence.Lob;
import javax.persistence.MapsId;
>>>>>>> 692fbd880c81d970993582150995ceef3f2d53b0
import javax.persistence.OneToOne;
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
    private String eleSignValue;
    
    private String signYn;

    @Temporal(TemporalType.TIMESTAMP)    
    private Date regDt = new Date();  
    
    
}