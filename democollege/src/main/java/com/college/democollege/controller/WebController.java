package com.college.democollege.controller;

import com.college.democollege.dto.DiscussionDto;
import com.college.democollege.dto.UserSignupRequest;
import com.college.democollege.model.College;
import com.college.democollege.model.User;
import com.college.democollege.service.ForumService;
import com.college.democollege.service.collegeserv;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/forum")
public class WebController {

    private final ForumService forumService;
    private final collegeserv collegeServ;
    public WebController(ForumService forumService, collegeserv collegeServ) {
        this.forumService = forumService;
        this.collegeServ = collegeServ;
    }
    @GetMapping({"", "/home"})
    public String viewHomepage(
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
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model, HttpSession session) {
        
        String cleanCity = (city != null && !city.trim().isEmpty()) ? city.trim() : null;
        String cleanCourse = (coursename != null && !coursename.trim().isEmpty()) ? coursename.trim() : null;

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<College> collegePage = collegeServ.getFilteredColleges(name,
                minratings, cleanCity, maxHostelFees, minplacements, cleanCourse, maxcoursefees, maxduration, minduration, pageable
        );
        
        model.addAttribute("colleges", collegePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", collegePage.getTotalPages());
        model.addAttribute("userId", session.getAttribute("LOGGED_IN_USER_ID"));

        return "homepage";
    }

    @GetMapping("/predictor")
    public String viewCollegePredictor(
            @RequestParam(required = false) Integer rank,
            @RequestParam(required = false) List<String> exams,
            @RequestParam(defaultValue = "2000") Integer diff,
            Model model, HttpSession session) {

        if (rank != null || (exams != null && !exams.isEmpty())) {
            List<College> predictedColleges = collegeServ.getRankedCollege(rank, diff, exams);
            model.addAttribute("predictedColleges", predictedColleges);
        }

        model.addAttribute("rank", rank);
        model.addAttribute("exams", exams);
        model.addAttribute("diff", diff);
        model.addAttribute("userId", session.getAttribute("LOGGED_IN_USER_ID"));

        return "predictor";
    }
    
    @GetMapping("/discussions/general")
    public String viewGeneralFeed(@RequestParam(required = false) String query, Model model, HttpSession session) {
        List<DiscussionDto> discussions = (query != null && !query.isBlank())
                ? forumService.searchDiscussions(query)
                : forumService.getAllDiscussion();

        model.addAttribute("discussions", discussions);
        model.addAttribute("searchQuery", query);
        model.addAttribute("feedTitle", "Global Student Community Forum");
        model.addAttribute("isCollegeSpecific", false);
        model.addAttribute("userId", session.getAttribute("LOGGED_IN_USER_ID"));
        return "forum-feed";
    }
    @GetMapping("/colleges/{collegeid}/discussions")
    public String viewCollegeForum(@PathVariable Long collegeid, Model model, HttpSession session) {
        College college = collegeServ.getCollegeById(collegeid);
        List<DiscussionDto> discussions = forumService.getAllDiscussionByCollegeId(collegeid);

        model.addAttribute("discussions", discussions);
        model.addAttribute("college", college);
        model.addAttribute("feedTitle", college.getName() + " - Campus Forum");
        model.addAttribute("isCollegeSpecific", true);
        model.addAttribute("collegeId", collegeid);
        model.addAttribute("userId", session.getAttribute("LOGGED_IN_USER_ID"));
        return "forum-feed";
    }
    @GetMapping("/discussion/{id}/{slug}")
    public String viewSingleDiscussion(@PathVariable Long id, @PathVariable String slug, Model model, HttpSession session) {
        model.addAttribute("discussion", forumService.getDiscussionById(id));
        model.addAttribute("userId", session.getAttribute("LOGGED_IN_USER_ID"));
        return "discussion-detail"; 
    }
    @PostMapping("/discussion/create")
    public String processCreateDiscussion(
            @RequestParam String title, 
            @RequestParam String content, 
            @RequestParam(required = false) Long collegeId, 
            HttpSession session) {
        
        Long userId = (Long) session.getAttribute("LOGGED_IN_USER_ID");
        if (userId == null) return "redirect:/forum/login";

        forumService.createDiscussion(userId, collegeId, title, content);
        if (collegeId != null && collegeId > 0) {
            return "redirect:/forum/colleges/" + collegeId + "/discussions";
        }
        return "redirect:/forum/discussions/general";
    }
    @PostMapping("/discussion/{id}/{slug}/answer")
    public String processPostAnswer(
            @PathVariable Long id, 
            @PathVariable String slug, 
            @RequestParam String answer, 
            HttpSession session) {
        
        Long userId = (Long) session.getAttribute("LOGGED_IN_USER_ID");
        if (userId == null) return "redirect:/forum/login";
        
        forumService.createAnswer(userId, id, answer);
        return "redirect:/forum/discussion/" + id + "/" + slug;
    }
    @GetMapping("/login")
    public String showLoginPage(HttpSession session) {
        return session.getAttribute("LOGGED_IN_USER_ID") != null ? "redirect:/forum/home" : "login";
    }

    @GetMapping("/signup")
    public String showSignupPage(HttpSession session) {
        return session.getAttribute("LOGGED_IN_USER_ID") != null ? "redirect:/forum/home" : "signup";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        try {
            User user = forumService.validateUserLogin(email, password);
            session.setAttribute("LOGGED_IN_USER_ID", user.getUser_id());
            return "redirect:/forum/home";
        } catch (Exception e) {
            model.addAttribute("error", "Invalid email or password. Please try again.");
            return "login";
        }
    }

    @PostMapping("/signup")
    public String processSignup(@RequestParam String name, @RequestParam String email, @RequestParam String password, Model model) {
        try {
            UserSignupRequest request = new UserSignupRequest(name, email, password);
            forumService.signupUser(request);
            return "redirect:/forum/login?success=true";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }

    @GetMapping("/logout")
    public String processLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/forum/login";
    }
}
