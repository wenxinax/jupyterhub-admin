package com.sysu.jupyterhubadmin.service;

import com.sysu.jupyterhubadmin.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUser();
    int addUser(User user);
    int editUserName(String oldName, String newName);
    int deleteUser(String username);
    User getUserByName(String username);
    boolean signin(String username, String password);
}
