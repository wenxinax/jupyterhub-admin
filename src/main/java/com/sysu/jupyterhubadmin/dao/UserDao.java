package com.sysu.jupyterhubadmin.dao;

import com.sysu.jupyterhubadmin.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
    List<User> getAllUser();
    int addUser(User user);
    int editUserName(@Param("oldName") String oldName, @Param("newName") String newName);
    int deleteUser(String username);
    User getUserByName(String username);
    User signin(@Param("username") String username, @Param("password") String password);
}
