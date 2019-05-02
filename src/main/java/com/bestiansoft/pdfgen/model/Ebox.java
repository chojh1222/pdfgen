package com.bestiansoft.pdfgen.model;

import java.util.List;

// import javax.persistence.CascadeType;
// import javax.persistence.Column;
// import javax.persistence.Entity;
// import javax.persistence.FetchType;
// import javax.persistence.Id;
// import javax.persistence.JoinColumn;
// import javax.persistence.OneToMany;
// import javax.persistence.OneToOne;
// import javax.persistence.Table;

import lombok.Data;

/**
 * 포탈쪽 전자계약 보관함
 */
// @Entity
@Data
// @Table(name="CT0100M")
public class Ebox {    
    // @Id
    // @Column(name = "CNTRCT_NO", nullable=false, length=20)
    private String docId;       // 계약번호

    // @Column(name = "REQER_ID", nullable = false)
    private String reqerId;     // 요청자

    // @Column(name = "REGISTER", nullable = false)
    // private String register;     // 등록자

    // @Column(name = "ATCHFILE_GRP_SEQ", nullable = false)
    private Integer attGrpSeq;     // 첨부파일 그룹 순번

    // @Column(name = "PDF_PATH")
    private String pdfFilePath; //pdf파일경로

    // @OneToOne(mappedBy = "box", cascade = CascadeType.ALL)
    // @JoinColumn(name = "ATCHFILE_GRP_SEQ")
    // private EboxDoc eBoxDoc;

    // @OneToMany(mappedBy = "box", fetch=FetchType.LAZY , cascade = CascadeType.ALL)
    private List<EboxUser> users;

}