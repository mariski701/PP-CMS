package com.cms.pp.cms.pp.Alerts;

import com.cms.pp.cms.pp.Role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertCodeRepository extends JpaRepository<AlertCode, Integer> {
    AlertCode findByAlertName(String alertName);
}
