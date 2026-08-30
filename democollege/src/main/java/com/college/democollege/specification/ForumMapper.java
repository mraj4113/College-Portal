package com.college.democollege.specification;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.college.democollege.dto.AnswerDto;
import com.college.democollege.dto.DiscussionDto;
import com.college.democollege.model.Answer;
import com.college.democollege.model.Discussion;

@Mapper(componentModel="spring")
public interface ForumMapper {
    @Mapping(source = "author.name",target="authorName")
    @Mapping(source = "college.name",target = "collegeName")
    @Mapping(target = "answer", ignore = true) 
    DiscussionDto toDiscussionDto(Discussion discussion);
    @Mapping(source="responder.name",target="responderName")
    AnswerDto toAnswerDto(Answer answer);
}
