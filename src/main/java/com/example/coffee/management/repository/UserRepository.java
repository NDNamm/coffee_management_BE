package com.example.coffee.management.repository;

import com.example.coffee.management.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

      Optional<Users> findUserByEmail(String email);

      Optional<Users> findUsersByPhoneNumber(String phoneNumber);

      @Query("select u from Users u where  lower(u.email) like lower(concat('%', :key,'%') )" +
              " or lower(u.fullName) like lower(concat('%', :key, '%') ) ")
      Page<Users> findUsersByFullNameOrEmail(@Param("key") String key, Pageable pageable);

}
