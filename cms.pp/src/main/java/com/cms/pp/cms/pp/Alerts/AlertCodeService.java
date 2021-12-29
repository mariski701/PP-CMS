package com.cms.pp.cms.pp.Alerts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertCodeService {
    @Autowired
    private AlertCodeRepository alertCodeRepository;

    public List<AlertCode> getAlertCodes() {
        return alertCodeRepository.findAll();
    }



}
