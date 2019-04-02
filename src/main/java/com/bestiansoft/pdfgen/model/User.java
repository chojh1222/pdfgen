package com.bestiansoft.pdfgen.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

/**
 * 포탈쪽 전자계약 참여자
 */

@Data
@Embeddable
public class User implements Serializable{

    @Column(name = "BOX_ID", nullable = false)
    private String docId;     // 전자계약 번호
    
    @Column(name = "USER_SEQ", nullable = false)
    private Integer userSeq;     // 전자계약 시퀀스
}