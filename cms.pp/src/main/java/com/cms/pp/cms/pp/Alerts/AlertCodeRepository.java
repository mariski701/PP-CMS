package com.cms.pp.cms.pp.Alerts;

import com.cms.pp.cms.pp.Article.Language;
import com.cms.pp.cms.pp.Role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertCodeRepository extends JpaRepository<AlertCode, Integer> {
    AlertCode findByAlertName(String alertName);
    AlertCode findByAlertCode(String alertCode);
}
