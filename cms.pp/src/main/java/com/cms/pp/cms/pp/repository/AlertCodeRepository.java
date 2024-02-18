package com.cms.pp.cms.pp.repository;

import com.cms.pp.cms.pp.model.entity.AlertCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertCodeRepository extends JpaRepository<AlertCode, Integer> {

    @Query(value = "SELECT * FROM ALERT_CODE", nativeQuery = true)
    List<AlertCode> findAllEntries();
    AlertCode findByAlertName(String alertName);
    AlertCode findByAlertCode(String alertCode);
}
