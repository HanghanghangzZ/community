package com.hang.myselfcommunity.service;

import com.hang.myselfcommunity.dto.PaginationDTO;
import com.hang.myselfcommunity.dto.QuestionDTO;
import com.hang.myselfcommunity.mapper.QuestionMapper;
import com.hang.myselfcommunity.mapper.UserMapper;
import com.hang.myselfcommunity.model.Question;
import com.hang.myselfcommunity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    private QuestionMapper questionMapper;
    @Autowired
    public void setQuestionMapper(QuestionMapper questionMapper) {
        this.questionMapper = questionMapper;
    }

    private UserMapper userMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 从数据库中获取所有的question并将其封装成供实现分页的PaginationDTO
     * @param page
     * @param size
     * @return
     */
    public PaginationDTO list(Integer page, Integer size) {

        /* 容错 */
        Integer totalPage;
        Integer totalCount = questionMapper.count();
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if (page <= 0) {
            page  = 1;
        } else if (page > totalPage) {
            page = totalPage;
        }

        int offset = size * (page - 1);

        List<Question> list = questionMapper.list(offset, size);
        ArrayList<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();
        for (Question question : list) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO(question);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestionDTOList(questionDTOList);

        paginationDTO.setPaginationDTO(totalPage, page, size);

        return paginationDTO;
    }
}
