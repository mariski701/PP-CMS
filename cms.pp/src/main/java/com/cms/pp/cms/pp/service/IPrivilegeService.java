package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.model.entity.Privilege;

import java.util.List;

public interface IPrivilegeService {
    List<Privilege> findAllPrivileges();
    Privilege findByName(String name);
    Privilege findById(Long id);
}
