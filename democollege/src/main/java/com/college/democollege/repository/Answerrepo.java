package com.college.democollege.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.college.democollege.model.Answer;

public interface Answerrepo extends JpaRepository<Answer,Long>{
    List<Answer> findByDiscussionIdOrderByCreatedTimeAsc(Long id);
}
