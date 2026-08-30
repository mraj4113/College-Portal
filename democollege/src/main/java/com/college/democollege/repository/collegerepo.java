package com.college.democollege.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.college.democollege.model.College;
public interface collegerepo extends JpaRepository<College,Long>,JpaSpecificationExecutor<College>{

}
