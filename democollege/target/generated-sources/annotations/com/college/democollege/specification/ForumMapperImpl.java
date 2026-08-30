package com.college.democollege.specification;

import com.college.democollege.dto.AnswerDto;
import com.college.democollege.dto.DiscussionDto;
import com.college.democollege.model.Answer;
import com.college.democollege.model.College;
import com.college.democollege.model.Discussion;
import com.college.democollege.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-30T17:10:21+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class ForumMapperImpl implements ForumMapper {

    @Override
    public DiscussionDto toDiscussionDto(Discussion discussion) {
        if ( discussion == null ) {
            return null;
        }

        DiscussionDto discussionDto = new DiscussionDto();

        discussionDto.setAuthorName( discussionAuthorName( discussion ) );
        discussionDto.setCollegeName( discussionCollegeName( discussion ) );
        discussionDto.setId( discussion.getId() );
        discussionDto.setTitle( discussion.getTitle() );
        discussionDto.setContent( discussion.getContent() );
        discussionDto.setCreatedTime( discussion.getCreatedTime() );

        return discussionDto;
    }

    @Override
    public AnswerDto toAnswerDto(Answer answer) {
        if ( answer == null ) {
            return null;
        }

        AnswerDto answerDto = new AnswerDto();

        answerDto.setResponderName( answerResponderName( answer ) );
        answerDto.setId( answer.getId() );
        answerDto.setAnswer( answer.getAnswer() );

        return answerDto;
    }

    private String discussionAuthorName(Discussion discussion) {
        if ( discussion == null ) {
            return null;
        }
        User author = discussion.getAuthor();
        if ( author == null ) {
            return null;
        }
        String name = author.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String discussionCollegeName(Discussion discussion) {
        if ( discussion == null ) {
            return null;
        }
        College college = discussion.getCollege();
        if ( college == null ) {
            return null;
        }
        String name = college.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String answerResponderName(Answer answer) {
        if ( answer == null ) {
            return null;
        }
        User responder = answer.getResponder();
        if ( responder == null ) {
            return null;
        }
        String name = responder.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
