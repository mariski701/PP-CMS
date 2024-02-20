package com.cms.pp.cms.pp.repository;

import com.cms.pp.cms.pp.model.entity.AlertCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertCodeRepository extends JpaRepository<AlertCode, Integer> {

	AlertCode findByAlertCode(String alertCode);

}
