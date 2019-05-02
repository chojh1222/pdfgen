package com.bestiansoft.pdfgen.model;

import java.util.Date;

// import javax.persistence.Column;
// import javax.persistence.Entity;
// import javax.persistence.GeneratedValue;
// import javax.persistence.Id;
// import javax.persistence.JoinColumn;
// import javax.persistence.ManyToOne;
// import javax.persistence.Table;
// import javax.persistence.Temporal;
// import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;

// import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

// @Entity
@Data
// @Table(name="ECS_DOC_HISTORY")
public class DocHistory {
    // @Id @GeneratedValue(generator="system-uuid")
    // @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    @JsonBackReference
    // @ManyToOne
    // @JoinColumn(name="DOC_ID")
    private Doc doc;

    private String signId;
    private String pdfName;
    private String pdfPath;

    // @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;
}