package com.cms.pp.cms.pp.user;

import com.cms.pp.cms.pp.Role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserName(String userName);
    User findByUserMail(String userMail);
    List<User> findByUserNameIgnoreCaseContaining(String userName);
    List<User> findUserByRoles(Role role);
}
