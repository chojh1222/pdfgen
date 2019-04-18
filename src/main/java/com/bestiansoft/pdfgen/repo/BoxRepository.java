package com.bestiansoft.pdfgen.repo;

import com.bestiansoft.pdfgen.model.Ebox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoxRepository extends JpaRepository<Ebox, String> {
    
    @Query(value = "select cntrct_no, reqer_id, a.atchfile_grp_seq, b.pdf_path "
    + "from ct0100m a, sm0710d b "
    + " where a.atchfile_grp_seq = b.atchfile_grp_seq "
    + " and cntrct_file_yn = 'Y' and cntrct_no = :cntrctNo"
    , nativeQuery = true)
    Ebox findByCntrctNo(@Param("cntrctNo") String cntrctNo);
}