package com.cms.pp.cms.pp.Priviliges;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrivilegeService {
    @Autowired
    private PrivilegeRepository privilegeRepository;

    public List<Privilege> findAllPrivileges() {
        return privilegeRepository.findAll();
    }

    public Privilege findByName(String name) {
        return privilegeRepository.findByName(name);
    }

    public Privilege findById(Long id) {
        return privilegeRepository.findById(id).orElse(null);
    }
}
