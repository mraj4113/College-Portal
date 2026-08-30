package com.college.democollege.service;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.college.democollege.model.College;
import com.college.democollege.repository.collegerepo;
import com.college.democollege.specification.CollegeSpecification;

import jakarta.transaction.Transactional;

@Service
public class collegeserv {
    private final collegerepo Collegerepo;
    private final ModelMapper modelmapper;
    
    public collegeserv(collegerepo c, ModelMapper m){
        this.Collegerepo = c;
        this.modelmapper = m;
    }
    public Page<College> getFilteredColleges(
        String name,
        Integer minratings, String city, Integer maxHostelFees,
            Double minplacements, String coursename, Long maxcoursefees, 
            Integer maxduration, Integer minduration, Pageable pageable
    ){
        Specification<College> coll = CollegeSpecification.getCollege(name,minratings, city, maxHostelFees, minplacements, coursename, maxcoursefees, maxduration, minduration);
        return Collegerepo.findAll(coll,pageable);
    }
    @Transactional
    public List<College> addCollege(List<College> coll) {
        return Collegerepo.saveAll(coll);
    }

    public College updateCollege(Long id, College coll) {
         College exc = Collegerepo.findById(id)
            .orElseThrow(() -> new RuntimeException("College not found with ID: " + id));
        coll.setId(id);
        modelmapper.map(coll, exc);
        return Collegerepo.save(exc);
    }

    @Transactional
    public void deleteCollege(Long id) {
        if(!Collegerepo.existsById(id)){
            throw new RuntimeException("College Not Found!!!");
        }
        Collegerepo.deleteById(id);
    }

    public College getCollegeById(Long id) {
        return Collegerepo.findById(id)
            .orElseThrow(() -> new RuntimeException("College Not Found try another id..."));
    }
    public List<College> getCollegeByIds(Long a, Long b, Long c) {
        List<Long> ids = Stream.of(a, b, c)
                              .filter(Objects::nonNull)
                              .toList();
        return Collegerepo.findAllById(ids);
    }
    public List<College> getRankedCollege(Integer rank,Integer diff, List<String> exam) {
        Specification<College> col = CollegeSpecification.getCollegeByRankAndExam(rank,diff,exam);
        return Collegerepo.findAll(col);
    }
}
