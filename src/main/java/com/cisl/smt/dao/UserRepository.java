package com.cisl.smt.dao;

import com.cisl.smt.po.User;
//import org.apache.ibatis.annotations.Param;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select * from t_user where username=:username", nativeQuery = true)
    User findUserByUsername(@Param("username") String username);
}
