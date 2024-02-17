package com.cms.pp.cms.pp.repository;

import com.cms.pp.cms.pp.model.entity.ConfigurationFlags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationFlagsRepository extends JpaRepository<ConfigurationFlags, Integer> {

}
