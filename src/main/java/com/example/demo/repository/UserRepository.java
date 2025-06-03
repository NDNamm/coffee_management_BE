package com.example.demo.repository;

import com.example.demo.model.Product;
import com.example.demo.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
//    @Query("select u from Users u where lower(u.email) = lower(:email) ")
//    Users findUserByEmail(String email);

      Optional<Users> findUserByEmail(String email);

      Optional<Users> findUsersByPhone(String phone);

      @Query("select u from Users u where  lower(u.email) like lower(concat('%', :key,'%') )" +
              " or lower(u.fullName) like lower(concat('%', :key, '%') ) ")
      Page<Users> findUsersByFullNameOrEmail(@Param("key") String key, Pageable pageable);

}
