package com.college.democollege.specification;

import com.college.democollege.model.College;
import com.college.democollege.model.Courses;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class CollegeSpecification {

    public static Specification<College> getCollege(
            String name,
            Integer minratings, String city, Integer maxHostelFees,
            Double minplacements, String coursename, Long maxcoursefees, 
            Integer maxduration, Integer minduration) {
        
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")), "%" + name.trim().toLowerCase() + "%"
                ));
            }
            if (city != null && !city.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("location").get("city")), city.toLowerCase()
                ));
            }
            if ((coursename != null && !coursename.trim().isEmpty()) || maxcoursefees != null || minduration != null || maxduration != null) {
                
                Join<College, Courses> coursejoin = root.join("course");
                
                if (coursename != null && !coursename.trim().isEmpty()) {
                    predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(coursejoin.get("courseName")), "%" + coursename.toLowerCase() + "%"
                    ));
                }
                if (maxcoursefees != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(coursejoin.get("fees"), maxcoursefees));
                }
                if (maxduration != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(coursejoin.get("duration"), maxduration));
                }
                if (minduration != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(coursejoin.get("duration"), minduration));
                }
                
                query.distinct(true);
            }
            if (minratings != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("ratings"), minratings));
            }
            if (maxHostelFees != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("Hostelfees"), maxHostelFees));
            }
            if (minplacements != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("placements"), minplacements));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<College> getCollegeByRankAndExam(Integer rank, Integer diff,List<String> exams) {
        return (root,query,criteriaBuilder)->{
            List<Predicate> predicates = new ArrayList<>();
            if(rank!=null){
                int range = (diff!=null)?diff:2000;
                predicates.add(criteriaBuilder.between(root.get("cutoffRank"), rank-range, rank + range));
            }
            if (exams != null && !exams.isEmpty()) {
            List<Predicate> examPredicates = new ArrayList<>();
            
            for (String exam : exams) {
                if (exam != null) {
                    String ex = exam.trim().toLowerCase();
                    if (ex.equals("jee") || ex.equals("neet")) {
                        examPredicates.add(criteriaBuilder.equal(root.get(ex), true));
                    }
                }
            }
            if (!examPredicates.isEmpty()) {
                predicates.add(criteriaBuilder.or(examPredicates.toArray(new Predicate[0])));
            }
        }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
