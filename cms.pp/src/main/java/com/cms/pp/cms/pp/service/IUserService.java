package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.model.CustomTopCommentersClass;
import com.cms.pp.cms.pp.model.dto.CMSUserDTO;
import com.cms.pp.cms.pp.model.entity.User;

import java.util.List;

public interface IUserService {
    User findById(int id);
    Object addUser(User user);
    Object addCMSUser(CMSUserDTO cmsUserDTO);
    Object deleteUser(int id);
    List<User> getUsers();
    Object logout();
    Object loginToService(String userMail, String password);
    Object changePassword(String oldPassword, String newPassword);
    List<User> findByUserNameIgnoreCaseContaining(String userName);
    List<User> findSomeUsersByLazyLoadingAndUserName(int page, int size, String username);
    Object editUserMail(String newUserMail);
    Object editUserName(String newUsername);
    Object editUserRole(String roleName, int id);
    List<User> findCmsUsers();
    Object changeUserMail(int id, String newMail);
    Object changeUserName(int id, String newUserName);
    List<CustomTopCommentersClass> findTheBestCommenter(int size);
}
