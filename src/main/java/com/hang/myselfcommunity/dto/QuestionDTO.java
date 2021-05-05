package com.hang.myselfcommunity.dto;

import com.hang.myselfcommunity.model.Question;
import com.hang.myselfcommunity.model.User;
import lombok.Data;


@Data
public class QuestionDTO {
    private Question question;
    private User user;

    public QuestionDTO() {
    }

    public QuestionDTO(Question question) {
        this.question = question;
    }
}
