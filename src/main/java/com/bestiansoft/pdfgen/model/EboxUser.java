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
@Table(name="ECS_BOX_USER")
public class EboxUser {

    // @Id
    // @Column(name = "USER_SEQ", nullable = false)
    // private Integer userSeq;     // 전자계약 시퀀스

    @EmbeddedId
    private User user;

    @Column(name = "USER_NM")
    private String userNm;      // 성명    

    @ManyToOne    
    @JoinColumn(name="BOX_ID", insertable = false, updatable = false)
    private Ebox box;
}