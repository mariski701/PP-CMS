package com.cms.pp.cms.pp.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserName(String userName);
    User findByUserMail(String userMail);
    List<User> findByUserNameIgnoreCaseContaining(String userName);
}
