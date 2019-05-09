package com.bestiansoft.pdfgen.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Data
@Table(name="ECS_DOC_MGT")
public class Doc {
    @Id
    private String docId;
    
    private String userId;
    
    // private String docName;    
    // private String fileName;

    private String filePath;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt = new Date();

    // private String pdfName;
    // private String pdfPath;
    // private String pdfSignId;
    
    // @Temporal(TemporalType.TIMESTAMP)
    // private Date pdfRegDt;

    // @JsonManagedReference
    // @OneToMany(mappedBy = "doc", fetch = FetchType.EAGER)
    // private List<Signer> signers;
    @JsonManagedReference
    @OneToMany(mappedBy = "doc", cascade = CascadeType.ALL)
    private List<Element> elements;

    public void addElement(Element element) {
        if(this.elements == null)
            this.elements = new ArrayList<>();
        this.getElements().add(element); 
        element.setDoc(this);
    }

    /**
     * @param elements the elements to set
     */
    public void setElements(List<Element> elements) {
        this.elements = new ArrayList<>();
        for(Element elem : elements) {
            this.addElement(elem);
        }
    }

    @JsonManagedReference
    @OneToMany(mappedBy = "doc", cascade = CascadeType.ALL)
    private List<DocHistory> history;
}