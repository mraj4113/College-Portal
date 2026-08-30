package com.college.democollege.service;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import jakarta.persistence.criteria.Predicate;
import java.util.regex.Pattern;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.college.democollege.dto.AnswerDto;
import com.college.democollege.dto.DiscussionDto;
import com.college.democollege.dto.UserSignupRequest;
import com.college.democollege.exception.ResourceNotFoundException;
import com.college.democollege.model.Answer;
import com.college.democollege.model.College;
import com.college.democollege.model.Discussion;
import com.college.democollege.model.User;
import com.college.democollege.repository.Answerrepo;
import com.college.democollege.repository.Discussionrepo;
import com.college.democollege.repository.Userrepo;
import com.college.democollege.repository.collegerepo;
import com.college.democollege.specification.ForumMapper;

@Service
public class ForumService {

    private final Answerrepo answerrepo;
    private final collegerepo collegeRepo;
    private final Discussionrepo discussionrepo;
    private final Userrepo userrepo;
    private final ForumMapper forumMapper;

    public ForumService(Answerrepo answerrepo, collegerepo collegeRepo, Discussionrepo discussionrepo, Userrepo userrepo, ForumMapper forumMapper) {
        this.answerrepo = answerrepo;
        this.collegeRepo = collegeRepo;
        this.discussionrepo = discussionrepo;
        this.forumMapper = forumMapper;
        this.userrepo = userrepo;
    }

    public List<DiscussionDto> getAllDiscussion() {
        return discussionrepo
                .findAllByOrderByCreatedTimeDesc()
                .stream()
                .map(disc -> {
                    DiscussionDto dto = forumMapper.toDiscussionDto(disc);
                    truncateTitleForFeed(dto);
                    return dto;
                })
                .toList();
    }

    public List<DiscussionDto> getAllDiscussionByCollegeId(Long id) {
        return discussionrepo
                .findByCollegeIdOrderByCreatedTimeDesc(id)
                .stream()
                .map(disc -> {
                    DiscussionDto dto = forumMapper.toDiscussionDto(disc);
                    truncateTitleForFeed(dto);
                    return dto;
                })
                .toList();
    }

    public List<DiscussionDto> searchDiscussions(String query) {

        if (query == null || query.isBlank()) {
            return List.of();
        }

        String[] words = query.trim().split("\\s+");

        Specification<Discussion> spec = (root,criteriaquery,criteriaBuilder)->{
            List<Predicate> predicates=new ArrayList<>();
            for(String s:words){
                String pattern = "%" + s.toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return discussionrepo
                .findAll(spec)
                .stream()
                .map(disc -> {
                    DiscussionDto dto = forumMapper.toDiscussionDto(disc);
                    truncateTitleForFeed(dto);
                    return dto;
                })
                .toList();
    }

    public DiscussionDto getDiscussionById(Long id) {
        Discussion disc = discussionrepo.findById(id)
                .orElseThrow(() -> new RuntimeException("No Discussion Found with ID: " + id));
        
        DiscussionDto discdto = forumMapper.toDiscussionDto(disc);
        List<Answer> ans = answerrepo.findByDiscussionIdOrderByCreatedTimeAsc(id);
        List<AnswerDto> ansdto = ans.stream().map(forumMapper::toAnswerDto).toList();
        discdto.setAnswer(ansdto);
        return discdto;
    }

    @Transactional
    public DiscussionDto createDiscussion(Long userid, Long collegeid, String title, String content) {
        User u = userrepo.findById(userid).orElseThrow(() -> new RuntimeException("User Not Found"));
        
        Discussion disc = new Discussion();
        disc.setAuthor(u);
        disc.setContent(content);
        disc.setTitle(title);
        
        String baseSlug = generateSlug(title);
        String uniqueSlug = makeSlugUnique(baseSlug);
        disc.setSlug(uniqueSlug);

        if (collegeid != null && collegeid > 0) {
            College col = collegeRepo.findById(collegeid).orElseThrow(() -> new RuntimeException("College Not Found"));
            disc.setCollege(col);
        }
        return forumMapper.toDiscussionDto(discussionrepo.save(disc));
    }

    @Transactional
    public AnswerDto createAnswer(Long userid, Long discussionid, String answer) {
        User u = userrepo.findById(userid).orElseThrow(() -> new RuntimeException("User Not Found"));
        Discussion disc = discussionrepo.findById(discussionid).orElseThrow(() -> new RuntimeException("Discussion Not Found"));
        
        Answer ans = new Answer();
        ans.setAnswer(answer);
        ans.setResponder(u);
        ans.setDiscussion(disc);
        return forumMapper.toAnswerDto(answerrepo.save(ans));
    }


    @Transactional
    public String signupUser(UserSignupRequest request) {
        if (userrepo.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email is already registered!");
        }
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(request.password()); 
        userrepo.save(user);
        return "Registration successful for user: " + user.getName();
    }

    public User validateUserLogin(String email, String password) {
        User user = userrepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password."));
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid email or password.");
        }
        return user;
    }




    private void truncateTitleForFeed(DiscussionDto dto) {
        if (dto.getTitle() != null && dto.getTitle().length() > 60) {
            dto.setTitle(dto.getTitle().substring(0, 57).trim() + "...");
        }
    }

    private String generateSlug(String input) {
        if (input == null || input.isBlank()) return "discussion";
        Pattern nonLatin = Pattern.compile("[^\\w-]");
        Pattern whitespace = Pattern.compile("[\\s]");
        String slug = Normalizer.normalize(input, Normalizer.Form.NFD);
        slug = whitespace.matcher(slug).replaceAll("-");
        slug = nonLatin.matcher(slug).replaceAll("");
        slug = slug.replaceAll("-+", "-").replaceAll("^-|-$", ""); 
        return slug.toLowerCase(Locale.ENGLISH);
    }

    private String makeSlugUnique(String baseSlug) {
        String finalSlug = baseSlug;
        int count = 1;
        while (discussionrepo.existsBySlug(finalSlug)) {
            finalSlug = baseSlug + "-" + count;
            count++;
        }
        return finalSlug;
    }



}
