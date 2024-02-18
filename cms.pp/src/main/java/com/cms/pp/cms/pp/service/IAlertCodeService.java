package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.model.entity.AlertCode;

import java.util.List;

public interface IAlertCodeService {
    List<AlertCode> getAlertCodes();
    Object removeAlertCode(int id);
    Object addAlertCode(String alertCode, String alertName);
    Object editAlertCode(int id, String alertCode, String alertName);
    AlertCode findById(int id);
}
