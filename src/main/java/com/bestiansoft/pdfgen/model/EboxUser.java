package com.bestiansoft.pdfgen.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

/**
 * 포탈쪽 전자계약 참여자
 */
@Entity
@Data
@Table(name="CT0110D")
public class EboxUser {

    // @Column(name = "CNTRCT_NO", nullable = false)
    // private String docId;     // 계약 번호

    @Id
    @Column(name = "PARTCPNT_NO", nullable = false)
    private String userNo;     // 참여자번호, (key)

    @Column(name = "PARTCPNT_SEQ", nullable = false)
    private String userSeq;     // 참여자순번

    @Column(name = "PARTCPNT_NM", nullable = false)
    private String userNm;      // 성명

    // @EmbeddedId
    // private User user;

    @ManyToOne
    @JoinColumn(name="CNTRCT_NO", insertable = false, updatable = false)
    private Ebox box;
}