package com.college.democollege.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.college.democollege.model.College;
import com.college.democollege.service.collegeserv;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@RestController
@RequestMapping("/api/colleges")
public class collegecont {
    private final collegeserv Collegeserv;
    public collegecont(collegeserv c){
        this.Collegeserv = c;
    }
    @GetMapping
    public ResponseEntity<Page<College>> getMethodName(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer minratings,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Integer maxHostelFees,
            @RequestParam(required = false) Double minplacements,
            @RequestParam(required = false) String coursename,
            @RequestParam(required = false) Long maxcoursefees,
            @RequestParam(required = false) Integer maxduration,
            @RequestParam(required = false) Integer minduration,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir

    ) {

            Sort sort = sortDir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page,size,sort);
            Page<College> coll = Collegeserv.getFilteredColleges(name,minratings, city, maxHostelFees, minplacements, coursename, maxcoursefees, maxduration, minduration,pageable);
            return ResponseEntity.ok(coll);
    }
    @GetMapping("/{id}")
    public ResponseEntity<College> getCollByid(@PathVariable Long id) {
        College c = Collegeserv.getCollegeById(id);
        return ResponseEntity.ok(c);
    }
    @PostMapping
    public ResponseEntity<List<College>> addCollege(@RequestBody List<College> coll) {
        List<College> c = Collegeserv.addCollege(coll);
        return ResponseEntity.status(HttpStatus.CREATED).body(c);
    }
    @PutMapping("/{id}")
    public ResponseEntity<College> updateColl(@PathVariable Long id, @RequestBody College coll) {
        College c = Collegeserv.updateCollege( id,coll);
        return ResponseEntity.ok(c);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColl(@PathVariable Long id) {
        Collegeserv.deleteCollege(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/compare")
    public ResponseEntity<List<College>> comparecollege(
        @RequestParam(defaultValue = "null") Long a,
        @RequestParam(defaultValue = "null") Long b,
        @RequestParam(defaultValue = "null") Long c
    ) 
    {
        List<College> col = Collegeserv.getCollegeByIds(a,b,c);
        return ResponseEntity.ok(col);
    }
    @GetMapping("/collegePredictor")
    public ResponseEntity<?> collegePredictor(@RequestParam(required = false) Integer rank,
        @RequestParam(required = false) List<String> exams,
        @RequestParam(defaultValue = "2000") Integer diff
    ) {
        if(rank==null&&(exams==null||exams.isEmpty())){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                    "status", 400,
                    "error", "Bad Request",
                    "message", "You must provide at least a 'rank', an 'exam', or both parameters."
                ));
    }
        List<College> co = Collegeserv.getRankedCollege(rank,diff,exams);
        return ResponseEntity.ok(co);
    }
    
    
}
