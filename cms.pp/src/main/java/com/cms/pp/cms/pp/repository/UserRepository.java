package com.cms.pp.cms.pp.repository;

import com.cms.pp.cms.pp.model.entity.Role;
import com.cms.pp.cms.pp.model.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	User findByUserName(String userName);

	User findByUserMail(String userMail);

	List<User> findByUserNameIgnoreCaseContaining(String userName, Sort sort);

	List<User> findByUserNameIgnoreCaseContaining(String userName, Pageable pageable);

	List<User> findUserByRoles(Role role);

}
