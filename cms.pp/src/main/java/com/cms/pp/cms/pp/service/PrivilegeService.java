package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.model.entity.Privilege;
import com.cms.pp.cms.pp.repository.PrivilegeRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@RequiredArgsConstructor
@Service("PrivilegeService")
public class PrivilegeService implements IPrivilegeService {

	private final PrivilegeRepository privilegeRepository;

	@Override
	public List<Privilege> findAllPrivileges() {
		return privilegeRepository.findAll();
	}

	@Override
	public Privilege findByName(String name) {
		return privilegeRepository.findByName(name);
	}

	@Override
	public Privilege findById(Long id) {
		return privilegeRepository.findById(id).orElse(null);
	}

}
