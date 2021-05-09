package com.hang.myselfcommunity.controller;

import com.hang.myselfcommunity.dto.CommentDTO;
import com.hang.myselfcommunity.dto.QuestionDTO;
import com.hang.myselfcommunity.enums.CommentTypeEnum;
import com.hang.myselfcommunity.service.CommentService;
import com.hang.myselfcommunity.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    private QuestionService questionService;

    @Autowired
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    private CommentService commentService;

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/question/{id}")
    public String question(@PathVariable Long id,
                           Model model) {

        /* 累加阅读数 */
        questionService.increaseView(id);
        /* 问题内容以及信息 */
        QuestionDTO questionDTO = questionService.getById(id);
        model.addAttribute("questionDTO", questionDTO);

        /* 评论列表 */
        List<CommentDTO> commentDTOS = commentService.listByIdAndType(id, CommentTypeEnum.QUESTION);
        model.addAttribute("commentDTOS", commentDTOS);

        /* 相关问题列表 */
        List<QuestionDTO> relatedQuestionDTOList = questionService.selectRelated(questionDTO);
        model.addAttribute("relatedQuestionDTOList", relatedQuestionDTOList);

        return "question";
    }
}
