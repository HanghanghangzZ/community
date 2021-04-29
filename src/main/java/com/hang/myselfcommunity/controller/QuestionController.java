package com.hang.myselfcommunity.controller;

import com.hang.myselfcommunity.dto.QuestionDTO;
import com.hang.myselfcommunity.mapper.QuestionMapper;
import com.hang.myselfcommunity.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {

    private QuestionService questionService;
    @Autowired
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/question/{id}")
    public String question(@PathVariable Integer id,
                           Model model) {
        QuestionDTO questionDTO = questionService.getById(id);
        model.addAttribute("questionDTO", questionDTO);

        return "question";
    }
}
