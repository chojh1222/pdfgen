package com.bestiansoft.pdfgen.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="ecs_sign_mgt")
public class Sign {
    @Id @GeneratedValue
    private Long sign_id;

    private String user_id;
    private String sign_type;
    private String file_name;
    private byte[] file_store;

}