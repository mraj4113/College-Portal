package com.college.democollege.controller;

import com.college.democollege.dto.UserSignupRequest;
import com.college.democollege.model.User;
import com.college.democollege.service.ForumService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final ForumService forumService;

    public UserController(ForumService forumService) {
        this.forumService = forumService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody UserSignupRequest signupRequest) {
        String result = forumService.signupUser(signupRequest);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(
            @RequestParam String email, 
            @RequestParam String password, 
            HttpSession session) {
        
        User user = forumService.validateUserLogin(email, password);
        session.setAttribute("LOGGED_IN_USER_ID", user.getUser_id());
        return ResponseEntity.ok("Login successful! Welcome, " + user.getName());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully!");
    }
}
