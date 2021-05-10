package com.hang.myselfcommunity.controller;

import com.hang.myselfcommunity.cache.TagCache;
import com.hang.myselfcommunity.dto.QuestionDTO;
import com.hang.myselfcommunity.model.Question;
import com.hang.myselfcommunity.model.User;
import com.hang.myselfcommunity.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    private QuestionService questionService;

    @Autowired
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    /**
     * 修改问题时所用的请求路径，与发布问题的页面与代码复用
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable Long id,
                       Model model) {
        QuestionDTO questionDTO = questionService.getById(id);
        model.addAttribute("title", questionDTO.getQuestion().getTitle());
        model.addAttribute("tag", questionDTO.getQuestion().getTag());
        model.addAttribute("description", questionDTO.getQuestion().getDescription());
        model.addAttribute("id", id);
        model.addAttribute("tags", TagCache.get());

        return "publish";
    }

    /* get渲染页面， post执行请求 */
    @GetMapping("/publish")
    public String publish(Model model) {
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(@RequestParam(value = "title", required = false, defaultValue = "") String title,
                            @RequestParam(value = "tag", required = false, defaultValue = "") String tag,
                            @RequestParam(value = "description", required = false, defaultValue = "") String description,
                            @RequestParam(value = "id", required = false) Long id,
                            HttpServletRequest request,
                            Model model) {

        model.addAttribute("id", id);
        model.addAttribute("title", title);
        model.addAttribute("tag", tag);
        model.addAttribute("description", description);
        model.addAttribute("tags", TagCache.get());

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }

        if (description == null || "".equals(description)) {
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }
        if (title == null || "".equals(title)) {
            model.addAttribute("error", "问题标题不能为空");
            return "publish";
        }
        if (tag == null || "".equals(tag)) {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }

        String invalid = TagCache.filterInvalid(tag);
        if (StringUtils.isNotEmpty(invalid)) {
            model.addAttribute("error", "不能填入非法标签: " + invalid);
            return "publish";
        }

        Question question = new Question();

        question.setTag(tag);
        question.setTitle(title);
        question.setDescription(description);
        question.setCreator(user.getId());

        /* 发布 和 编辑 复用代码的关键 */
        /* 发布时因为id是需要自动生成的，所以是null */
        /* 编辑时因为已经有了记录，所以有id */
        question.setId(id);

        questionService.createOrUpdate(question);

        return "redirect:/";
    }
}
