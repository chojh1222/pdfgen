package com.bestiansoft.pdfgen.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

/**
 * 포탈쪽 전자계약 보관함
 */
@Entity
@Data
@Table(name="ECS_BOX")
public class Ebox {    
    @Id
    @Column(name = "BOX_ID", nullable=false, length=17)
    private String docId;       // 전자계약번호

    @Column(name = "PDF_FILE_PATH")
    private String pdfFilePath; //pdf파일경로

    @OneToMany(mappedBy = "box", cascade = CascadeType.ALL)    
    private List<EboxUser> users;
}