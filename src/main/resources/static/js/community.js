/**
 * 提交问题回复
 */
function post() {
    /* # 选择标签的id */
    let questionId = $("#question_id").val();
    let commentContent = $("#comment_content").val();
    comment2Target(questionId, 1, commentContent);
}

/**
 * 提交评论回复
 * @param e
 */
function comment(e) {
    let commentID = e.getAttribute("data-id");
    let commentContent = $("#input-" + commentID).val();
    comment2Target(commentID, 2, commentContent);
}

function comment2Target(targetId, type, commentContent) {
    /* js可以直接这样判断内容是否为空 */
    if (!commentContent.trim()) {
        alert("不能回复空的内容~~~~~")
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": targetId,
            "content": commentContent,
            "type": type
        }),
        success: function (response) {  //请求成功的回调函数
            if (response.code == 200) {
                location.reload();
            } else {
                if (response.code == 2003) {    //未登录
                    /**
                     * 在评论的时候如果没有登录是不能评论的，这时如果用户评论，那么就会跳出弹窗询问用户是否登录
                     * confirm() 方法用于显示一个带有指定消息和 OK 及取消按钮的对话框。
                     * 如果用户点击确定按钮，则 confirm() 返回 true。如果点击取消按钮，则 confirm() 返回 false。
                     * 在用户点击确定按钮或取消按钮把对话框关闭之前，它将阻止用户对浏览器的所有输入。
                     * 在调用 confirm() 时，将暂停对 JavaScript 代码的执行，在用户作出响应之前，不会执行下一条语句。
                     */
                    let isAccepted = confirm(response.message);
                    if (isAccepted) {
                        /* open() 方法用于打开一个新的浏览器窗口或查找一个已命名的窗口。 */
                        window.open("https://github.com/login/oauth/authorize?client_id=86996cfecf7f8dcf255a&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        /* localStorage是存储在浏览器上的 */
                        window.localStorage.setItem("closeable", "true");
                    }
                } else {
                    alert(response.message);
                }
            }
            console.log(response);
        },
        dataType: "json"
    });
}

function collapseComments(e) {
    let id = e.getAttribute("data-id");
    let comments = $("#comment-" + id);

    /* 获取一下二级评论的展开状态 */
    let collapse = e.getAttribute("data-collapse");
    if (collapse) {
        /* 折叠二级评论 */
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        /* 获取二级评论部分的标签 */
        let subCommentContainer = $("#comment-" + id);
        /* 查看subCommentContainer拥有几个子元素 */
        if (subCommentContainer.children().length != 1) {       //未拼接
            /* 展开二级评论 */
            comments.addClass("in");
            /* 标记二级评论展开状态 */
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {    //拼接
            $.getJSON("/comment/" + id, function (response) {
                /* index 就是当前遍历列表的索引下标， commentDTO表示这个列表中的元素 */
                $.each(response.data.reverse(), function (index, commentDTO) {
                    /* 将二级评论列表拼接进HTML页面 */
                    let mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": commentDTO.user.avatarUrl
                    }));

                    let mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": commentDTO.user.name
                    })).append($("<div/>", {
                        "html": commentDTO.comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(commentDTO.gmtCreate).format('YYYY-MM-DD HH:mm')
                    })));

                    let mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    let commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    /* 在被选元素的开头（仍位于内部）插入指定内容 */
                    subCommentContainer.prepend(commentElement);
                });
                /* 展开二级评论 */
                comments.addClass("in");
                /* 标记二级评论展开状态 */
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }
}