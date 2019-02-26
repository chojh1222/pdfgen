package com.bestiansoft.pdfgen.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Data
@Table(name="ECS_DOC_HISTORY")
public class DocHistory {
    @Id
    private String id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="DOC_ID")
    private Doc doc;

    private String signId;
    private String pdfName;
    private String pdfPath;
    private String regDt;
}