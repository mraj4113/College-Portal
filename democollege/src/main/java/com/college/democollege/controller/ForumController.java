package com.college.democollege.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpSession;
import java.util.List;

import com.college.democollege.dto.AnswerDto;
import com.college.democollege.dto.DiscussionDto;
import com.college.democollege.service.ForumService;

@RestController
@RequestMapping("/api/forum")
@CrossOrigin(origins = "*")
public class ForumController {

    private final ForumService forumService;

    public ForumController(ForumService forumService) {
        this.forumService = forumService;
    }

    @GetMapping("/discussions/general")
    public ResponseEntity<List<DiscussionDto>> getGeneralFeed() {
        return ResponseEntity.ok(forumService.getAllDiscussion());
    }

    @GetMapping("/colleges/{collegeid}/discussions")
    public ResponseEntity<List<DiscussionDto>> getCollegeDiscussion(@PathVariable Long collegeid) {
        return ResponseEntity.ok(forumService.getAllDiscussionByCollegeId(collegeid));
    }

    @GetMapping("/discussions/search")
    public ResponseEntity<List<DiscussionDto>> searchDiscussions(@RequestParam String query) {
        return ResponseEntity.ok(forumService.searchDiscussions(query));
    }

    @GetMapping("/discussions/{id}/{slug}")
    public ResponseEntity<DiscussionDto> getSingleDiscussion(
            @PathVariable Long id, 
            @PathVariable String slug) {
        return ResponseEntity.ok(forumService.getDiscussionById(id));
    }

    @PostMapping("/discussions/create")
    public ResponseEntity<DiscussionDto> createPost(
            HttpSession session,
            @RequestParam(required = false) Long collegeid, 
            @RequestParam String title,
            @RequestParam String content) {
        
        Long loggedInUserId = (Long) session.getAttribute("LOGGED_IN_USER_ID");
        if (loggedInUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(forumService.createDiscussion(loggedInUserId, collegeid, title, content));
    }

    @PostMapping("/discussions/{discussionid}/answers")
    public ResponseEntity<AnswerDto> postAnswer(
            HttpSession session,
            @PathVariable Long discussionid,
            @RequestParam String answer) {
        
        Long loggedInUserId = (Long) session.getAttribute("LOGGED_IN_USER_ID");
        if (loggedInUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(forumService.createAnswer(loggedInUserId, discussionid, answer));
    }
}
