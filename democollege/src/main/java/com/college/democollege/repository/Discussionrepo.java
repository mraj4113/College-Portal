package com.college.democollege.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.college.democollege.model.Discussion;

public interface Discussionrepo extends JpaRepository<Discussion,Long>,JpaSpecificationExecutor<Discussion>{

    List<Discussion> findByCollegeIdOrderByCreatedTimeDesc(Long id);
    List<Discussion> findAllByOrderByCreatedTimeDesc();
    List<Discussion> findByTitleContainingIgnoreCase(String title);
    boolean existsBySlug(String finalSlug);

}
