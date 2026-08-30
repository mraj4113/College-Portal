package com.college.democollege.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.college.democollege.model.User;

public interface Userrepo  extends JpaRepository<User,Long>{

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

}
