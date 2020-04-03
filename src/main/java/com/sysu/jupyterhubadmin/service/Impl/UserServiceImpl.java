package com.sysu.jupyterhubadmin.service.Impl;

import com.sysu.jupyterhubadmin.dao.UserDao;
import com.sysu.jupyterhubadmin.entity.User;
import com.sysu.jupyterhubadmin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public List<User> getAllUser() {
        return userDao.getAllUser();
    }

    @Override
    public int addUser(User user) {
        return userDao.addUser(user);
    }

    @Override
    public int editUserName(String oldName, String newName) {
        return userDao.editUserName(oldName, newName);
    }

    @Override
    public int deleteUser(String username) {
        return userDao.deleteUser(username);
    }

    @Override
    public User getUserByName(String username) {
        return userDao.getUserByName(username);
    }

    @Override
    public boolean signin(String username, String password) {
        return userDao.signin(username, password) != null;
    }

}
