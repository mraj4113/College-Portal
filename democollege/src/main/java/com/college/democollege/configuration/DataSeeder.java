package com.college.democollege.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.college.democollege.model.*;
import com.college.democollege.repository.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final Userrepo userrepo;
    private final collegerepo collegeRepo;
    private final Discussionrepo discussionrepo;

    public DataSeeder(Userrepo userrepo, collegerepo collegeRepo, Discussionrepo discussionrepo) {
        this.userrepo = userrepo;
        this.collegeRepo = collegeRepo;
        this.discussionrepo = discussionrepo;
    }

    @Override
    public void run(String... args) throws Exception {
        if (collegeRepo.count() > 0) return;

        User studentUser = new User();
        studentUser.setName("Ashwani Soni");
        studentUser.setEmail("ashwani@college.edu");
        studentUser.setPassword("password123");
        studentUser = userrepo.save(studentUser);

        User seniorUser = new User();
        seniorUser.setName("Priya Sharma");
        seniorUser.setEmail("priya@college.edu");
        seniorUser.setPassword("password123");
        seniorUser = userrepo.save(seniorUser);

        List<College> colleges = new ArrayList<>();

        College nitp = new College();
        nitp.setName("National Institute of Technology, Patna");
        nitp.setLocation(new Location("Bihar", "Patna", 25.6207, 85.1724, "Ashok Rajpath, Patna"));
        nitp.setRatings(4);
        nitp.setPlacements(92.5);
        nitp.setHostelfees(48000L);
        nitp.setCutoffRank(4200L);
        nitp.setJee(true);
        nitp.setNeet(false);
        nitp.setOverview("Premier institute of national importance located on the banks of Ganges.");
        nitp.setCourse(List.of(
            new Courses("Computer Science & Engineering", 160000L, 4),
            new Courses("Electrical Engineering", 140000L, 4)
        ));
        colleges.add(nitp);
        College iitb = new College();
        iitb.setName("Indian Institute of Technology, Bombay");
        iitb.setLocation(new Location("Maharashtra", "Mumbai", 19.1334, 72.9133, "Powai, Mumbai"));
        iitb.setRatings(5);
        iitb.setPlacements(98.2);
        iitb.setHostelfees(65000L);
        iitb.setCutoffRank(1200L);
        iitb.setJee(true);
        iitb.setNeet(false);
        iitb.setOverview("Ranked among top engineering colleges in India with world-class research.");
        iitb.setCourse(List.of(
            new Courses("Computer Science & Engineering", 220000L, 4),
            new Courses("Data Science & AI", 240000L, 4)
        ));
        colleges.add(iitb);

        College aiims = new College();
        aiims.setName("All India Institute of Medical Sciences, New Delhi");
        aiims.setLocation(new Location("Delhi", "New Delhi", 28.5672, 77.2100, "Ansari Nagar, New Delhi"));
        aiims.setRatings(5);
        aiims.setPlacements(99.0);
        aiims.setHostelfees(12000L);
        aiims.setCutoffRank(800L);
        aiims.setJee(false);
        aiims.setNeet(true);
        aiims.setOverview("India's top medical institute known for excellence in healthcare and education.");
        aiims.setCourse(List.of(
            new Courses("MBBS", 50000L, 5)
        ));
        colleges.add(aiims);

        College bits = new College();
        bits.setName("Birla Institute of Technology and Science, Pilani");
        bits.setLocation(new Location("Rajasthan", "Pilani", 28.3639, 75.5875, "Vidya Vihar, Pilani"));
        bits.setRatings(5);
        bits.setPlacements(94.0);
        bits.setHostelfees(75000L);
        bits.setCutoffRank(3500L);
        bits.setJee(true);
        bits.setNeet(false);
        bits.setOverview("Top tier private deemed university with a strong entrepreneurship culture.");
        bits.setCourse(List.of(
            new Courses("Information Technology", 280000L, 4),
            new Courses("Mechanical Engineering", 260000L, 4)
        ));
        colleges.add(bits);

        collegeRepo.saveAll(colleges);

        Discussion disc1 = new Discussion();
        disc1.setAuthor(studentUser);
        disc1.setTitle("How are the hostel facilities and mess food at NIT Patna?");
        disc1.setContent("I recently got allocated CSE in NIT Patna. Can any senior tell me about hostel allocation for 1st years?");
        disc1.setSlug("how-are-hostel-facilities-at-nit-patna");
        disc1.setCollege(nitp);
        discussionrepo.save(disc1);

        Discussion disc2 = new Discussion();
        disc2.setAuthor(seniorUser);
        disc2.setTitle("Tips for preparing for JEE and choosing between CSE vs ECE");
        disc2.setContent("Feel free to ask any questions about branch selection and placement statistics.");
        disc2.setSlug("tips-for-preparing-jee-branch-selection");
        discussionrepo.save(disc2);

        System.out.println("🌱 Live Database seeded with Colleges, Courses, and Discussions successfully!");
    }
}