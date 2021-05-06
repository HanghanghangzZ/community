package com.hang.myselfcommunity.service;

import com.hang.myselfcommunity.dto.PaginationDTO;
import com.hang.myselfcommunity.dto.QuestionDTO;
import com.hang.myselfcommunity.exception.CustomizeErrorCode;
import com.hang.myselfcommunity.exception.CustomizeException;
import com.hang.myselfcommunity.mapper.QuestionExtMapper;
import com.hang.myselfcommunity.mapper.QuestionMapper;
import com.hang.myselfcommunity.mapper.UserMapper;
import com.hang.myselfcommunity.model.Question;
import com.hang.myselfcommunity.model.QuestionExample;
import com.hang.myselfcommunity.model.User;
import com.hang.myselfcommunity.model.UserExample;
import org.apache.ibatis.session.RowBounds;
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

    private QuestionExtMapper questionExtMapper;

    @Autowired
    public void setQuestionExtMapper(QuestionExtMapper questionExtMapper) {
        this.questionExtMapper = questionExtMapper;
    }

    /**
     * 从数据库中获取所有的question并将其封装成供实现分页的PaginationDTO
     *
     * @param page
     * @param size
     * @return
     */
    public PaginationDTO list(Integer page, Integer size) {

        /* 容错 */
        Integer totalPage;
        Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if (page <= 0) {
            page = 1;
        } else if (page > totalPage && totalPage != 0) {
            page = totalPage;
        }

        int offset = size * (page - 1);

        /* Mybatis Generator 的方式实现分页 */
        List<Question> list = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, size));

        ArrayList<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();
        for (Question question : list) {

            UserExample userExample = new UserExample();
            userExample.createCriteria()
                    .andIdEqualTo(question.getCreator());
            List<User> users = userMapper.selectByExample(userExample);

            QuestionDTO questionDTO = new QuestionDTO(question);
            questionDTO.setUser(users.get(0));
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestionDTOList(questionDTOList);

        paginationDTO.setPaginationDTO(totalPage, page);

        return paginationDTO;
    }

    /**
     * 根据userId获取这个用户所发布的问题，并将其封装成QuestionDTO，再封装成分页使用的PaginationDTO
     *
     * @param userId
     * @param page
     * @param size
     * @return
     */
    public PaginationDTO listByUserId(Integer userId, Integer page, Integer size) {
        /* 容错 */
        Integer totalPage;

        /* 获取指定user所发布问题的总数 */
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(questionExample);

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if (page <= 0) {
            page = 1;
        } else if (page > totalPage && totalPage != 0) {
            page = totalPage;
        }

        int offset = size * (page - 1);

        /* 分页查询指定user的Question */
        QuestionExample questionExample1 = new QuestionExample();
        questionExample1.createCriteria()
                .andCreatorEqualTo(userId);
        List<Question> list = questionMapper.selectByExampleWithRowbounds(questionExample1, new RowBounds(offset, size));

        ArrayList<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();

        /* 获取指定user */
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdEqualTo(userId);
        List<User> users = userMapper.selectByExample(userExample);

        for (Question question : list) {
            /* 封装成QuestionDTO */
            QuestionDTO questionDTO = new QuestionDTO(question);
            questionDTO.setUser(users.get(0));
            questionDTOList.add(questionDTO);
        }
        /* 封装成分页使用的PaginationDTO */
        paginationDTO.setQuestionDTOList(questionDTOList);

        paginationDTO.setPaginationDTO(totalPage, page);

        return paginationDTO;
    }

    /**
     * 根据id获取QuestionDTO
     * 其中已经封装好了对应id的question和创建这个question的user
     *
     * @param id
     * @return
     */
    public QuestionDTO getById(Integer id) {

        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }

        QuestionDTO questionDTO = new QuestionDTO(question);

        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdEqualTo(question.getCreator());
        List<User> users = userMapper.selectByExample(userExample);

        questionDTO.setUser(users.get(0));

        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        Integer id = question.getId();

        if (id == null) {
            /* 创建 */
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.insert(question);

        } else {
            /* 更新 */
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setTag(question.getTag());

            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (updated != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void increaseView(Integer id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.increaseView(question);
    }
}
