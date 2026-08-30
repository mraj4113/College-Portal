package com.college.democollege.dto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscussionDto {
    private Long id;
    private String title;
    private String content;
    private String authorName;
    private String collegeName;
    private LocalDateTime createdTime;
    List<AnswerDto> answer;
}
